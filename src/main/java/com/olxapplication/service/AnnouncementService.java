package com.olxapplication.service;

import com.olxapplication.constants.AnnouncementMessages;
import com.olxapplication.constants.CategoryMessages;
import com.olxapplication.constants.UserMessages;
import com.olxapplication.dtos.*;
import com.olxapplication.entity.Category;
import com.olxapplication.entity.Favourite;
import com.olxapplication.entity.User;
import com.olxapplication.exception.PatternNotMathcedException;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.AnnouncementMapper;
import com.olxapplication.entity.Announcement;
import com.olxapplication.mappers.CategoryMapper;
import com.olxapplication.mappers.UserMapper;
import com.olxapplication.repository.AnnouncementRepository;
import com.olxapplication.repository.CategoryRepository;
import com.olxapplication.repository.FavouriteRepository;
import com.olxapplication.repository.UserRepository;
import com.olxapplication.validators.AnnouncementValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service layer class provides business logic operations for managing announcements within the application.
 * It interacts with the AnnouncementRepository, AnnouncementValidator, UserRepository and CategoryRepository.
 */
@Service
@AllArgsConstructor
public class AnnouncementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementService.class);
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementValidator announcementValidator = new AnnouncementValidator();
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final FavouriteRepository favouriteRepository;

    /**
     * Finds all announcements.
     * @return a list of AnnouncementDetailsDTO objects.
     */
    public List<AnnouncementDetailsDTO> findAnnounces(){
        List<Announcement> announcementList = announcementRepository.findAll();
        return announcementList.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds all announcements excluding those made by a specific user.
     * @param id the id of the user whose announcements are to be excluded.
     * @return a list of AnnouncementDetailsDTO objects.
     */
    public List<AnnouncementDetailsDTO> findOtherAnnounces(String id){
        List<Announcement> announcementList = announcementRepository.findAll();
        for(int i = announcementList.size()-1; i >= 0; i--){
            Announcement a = announcementList.get(i);
            if(a.getUser().getId().equals(id)){
                announcementList.remove(announcementList.get(i));
            }
        }
        return announcementList.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds an announcement by its id.
     * @param id the id of the announcement to find.
     * @return the AnnouncementDetailsDTO object of the found announcement.
     */
    public AnnouncementDetailsDTO findAnnouncementById(String id){
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);
        if (!announcementOptional.isPresent()){
            LOGGER.debug("Announcement with id {" + id + "} was not found in db");
            throw new ResourceNotFoundException(Announcement.class.getSimpleName() + "with id: " + id);
        }
        return AnnouncementMapper.toAnnouncementDetailsDTO(announcementOptional.get());
    }

    /**
     * Finds all announcements in a category with the specified substring in name.
     * @param userId the id of the user that request.
     * @param categoryName the id of the category.
     * @return a list of AnnouncementDetailsDTO objects.
     */
    public List<AnnouncementDetailsDTO> findAnnouncementByCategoryNameAndNotUser(String categoryName, String userId){

        List<Announcement> announces = announcementRepository.findAnnouncementsByCategoryCategoryNameContainsIgnoreCaseAndUserIdNot(categoryName, userId);

        return announces.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .sorted(Comparator.comparing(AnnouncementDetailsDTO::getNewPrice))
                .collect(Collectors.toList());
    }

    /**
     * Finds all announcements made by a specific user.
     * @param user_id the id of the user.
     * @return a list of AnnouncementDetailsDTO objects.
     */
    public List<AnnouncementDetailsDTO> findAnnouncementByUserId(String user_id){
        List<Announcement> announces = announcementRepository.findAnnouncementsByUser_Id(user_id);

        return announces.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Inserts a new announcement.
     * @param announcementDTO the AnnouncementDetailsDTO object of the announcement to insert.
     * @return the id of the inserted announcement.
     */
    public String insert(AnnouncementDetailsDTO announcementDTO) {
        Announcement announcement = AnnouncementMapper.toEntity(announcementDTO);
        announcement.setNewPrice(announcement.getPrice()*(1-(announcement.getDiscount()/100.0)));
        announcement = announcementRepository.save(announcement);
        LOGGER.debug("Announcement with id {} was inserted in db", announcement.getId());
        return announcement.getId();
    }

    /**
     * Inserts a new announcement after validating the input and checking the existence of the user and category.
     * @param announcementWebDTO the AnnouncementWebDTO object of the announcement to insert.
     * @return a string message indicating the result of the operation.
     */
    public String insert(AnnouncementWebDTO announcementWebDTO) {
        try{
            announcementValidator.announcementWebDtoValidator(announcementWebDTO);
            Optional<User> user = userRepository.findById(announcementWebDTO.getUser());
            Optional<Category> category = categoryRepository.findById(announcementWebDTO.getCategory());
            if(user.isPresent()) {
                if(category.isPresent()) {
                    AnnouncementDetailsDTO ann = AnnouncementDetailsDTO.builder()
                            .title(announcementWebDTO.getTitle())
                            .description(announcementWebDTO.getDescription())
                            .price(announcementWebDTO.getPrice())
                            .user(UserMapper.toUserDetailsDTO(user.get()))
                            .category(CategoryMapper.toCategoryDetailsDTO(category.get()))
                            .date(LocalDateTime.now())
                            .discount(announcementWebDTO.getDiscount())
                            .newPrice(Double.valueOf(decimalFormat.format(announcementWebDTO.getPrice()*(1-(announcementWebDTO.getDiscount()/100.0)))))
                            .imageURL(announcementWebDTO.getImageURL())
                            .build();
                    Announcement announcement = AnnouncementMapper.toEntity(ann);
                    announcement = announcementRepository.save(announcement);
                    LOGGER.debug(AnnouncementMessages.ANNOUNCEMENT_INSERTED_SUCCESSFULLY + announcement.getId() +  announcementWebDTO.getUser());
                    return AnnouncementMessages.ANNOUNCEMENT_INSERTED_SUCCESSFULLY + announcement.getId() + announcementWebDTO.getUser();
                } else {
                    LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + CategoryMessages.CATEGORY_NOT_FOUND + announcementWebDTO.getCategory());
                    return AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + CategoryMessages.CATEGORY_NOT_FOUND  + announcementWebDTO.getCategory();
                }
            } else {
                LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + UserMessages.USER_NOT_FOUND );
                return AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + UserMessages.USER_NOT_FOUND;
            }
        } catch (PatternNotMathcedException | ResourceNotFoundException e){
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + e.getMessage());
            return AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + e.getMessage();
        }
    }

    /**
     * Deletes an announcement by its id.
     * @param id the id of the announcement to delete.
     * @return a string message indicating the result of the operation.
     */
    public String deleteAnnouncementById(String id) {
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);
        if (announcementOptional.isEmpty()) {
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id);
            return AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id;
        } else {
            List<Favourite> favouriteList = (List<Favourite>) favouriteRepository.findAll();
            for (Favourite favourite : favouriteList) {
                if (favourite.getFavouriteAnnouncements().contains(announcementOptional.get())) {
                    favourite.getFavouriteAnnouncements().remove(announcementOptional.get());
                    favouriteRepository.save(favourite);
                }
            }

            announcementRepository.delete(announcementOptional.get());
            LOGGER.debug(AnnouncementMessages.ANNOUNCEMENT_DELETED_SUCCESSFULLY + id);
            return AnnouncementMessages.ANNOUNCEMENT_DELETED_SUCCESSFULLY + id;
        }
    }

    /**
     * Updates an announcement by its id with the provided details after validating the input.
     * @param id the id of the announcement to update.
     * @param announcementWebDTO the AnnouncementWebDTO object containing the new details of the announcement.
     * @return a string message indicating the result of the operation.
     */
    public String updateAnnouncementById(String id, AnnouncementWebDTO announcementWebDTO) {
        try {
            announcementValidator.announcementWebDtoValidator(announcementWebDTO);
            Optional<Announcement> announcementOptional = announcementRepository.findById(id);

            if (!announcementOptional.isPresent()) {
                LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id);
                return AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id;
            } else {
                Announcement toBeUpdated = announcementOptional.get();
                toBeUpdated.setTitle(announcementWebDTO.getTitle());
                toBeUpdated.setDescription(announcementWebDTO.getDescription());
                toBeUpdated.setPrice(announcementWebDTO.getPrice());
                toBeUpdated.setUser(userRepository.findById(announcementWebDTO.getUser()).get());
                toBeUpdated.setCategory(categoryRepository.findById(announcementWebDTO.getCategory()).get());
                toBeUpdated.setDate(LocalDateTime.now());
                toBeUpdated.setDiscount(announcementWebDTO.getDiscount());
                toBeUpdated.setNewPrice(Double.valueOf(decimalFormat.format(announcementWebDTO.getPrice()*(1-(announcementWebDTO.getDiscount()/100.0)))));
                toBeUpdated.setImageURL(announcementWebDTO.getImageURL());
                announcementRepository.save(toBeUpdated);
                LOGGER.debug(AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + id);
                return AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + id;
            }
        } catch (PatternNotMathcedException e){
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_UPDATED + e.getMessage());
            return AnnouncementMessages.ANNOUNCEMENT_NOT_UPDATED + e.getMessage();
        }
    }
}
