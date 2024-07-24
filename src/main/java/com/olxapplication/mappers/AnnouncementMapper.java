package com.olxapplication.mappers;

import com.olxapplication.dtos.AnnouncementDTO;
import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.entity.Announcement;

/**
 * This class provides utility methods for mapping between Announcement entities and their corresponding DTO representations.
 */
public class AnnouncementMapper {

    /**
     * Converts an Announcement entity into a comprehensive AnnouncementDetailsDTO object.
     * This DTO includes details about the announcement's category and user.
     *
     * @param announcement The Announcement entity to be converted.
     * @return A new AnnouncementDetailsDTO object containing comprehensive announcement details.
     */
    public static AnnouncementDetailsDTO toAnnouncementDetailsDTO(Announcement announcement){
        return AnnouncementDetailsDTO.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .description(announcement.getDescription())
                .price(announcement.getPrice())
                .category(CategoryMapper.toCategoryDetailsDTO(announcement.getCategory()))
                .user(UserMapper.toUserDetailsDTO(announcement.getUser()))
                .date(announcement.getDate())
                .discount(announcement.getDiscount())
                .newPrice(announcement.getNewPrice())
                .imageURL(announcement.getImageURL())
                .build();
    }


    /**
     * Converts an AnnouncementDTO object into a corresponding Announcement entity.
     *
     * @param announcementDTO The AnnouncementDTO object to be converted.
     * @return A new Announcement entity populated with data from the DTO.
     */
    public static Announcement toEntity(AnnouncementDTO announcementDTO) {
        return Announcement.builder()
                .id(announcementDTO.getId())
                .title(announcementDTO.getTitle())
                .description(announcementDTO.getDescription())
                .price(announcementDTO.getPrice())
                .category(announcementDTO.getCategory())
                .user(announcementDTO.getUser())
                .date(announcementDTO.getDate())
                .discount(announcementDTO.getDiscount())
                .newPrice(announcementDTO.getNewPrice())
                .imageURL(announcementDTO.getImageURL())
                .build();
    }

    /**
     * Converts an AnnouncementDetailsDTO object into a corresponding Announcement entity.
     * This method assumes that the category and user details within the DTO have already been converted to their respective entities.
     *
     * @param announcementDTO The AnnouncementDetailsDTO object to be converted.
     * @return A new Announcement entity populated with data from the DTO.
     */
    public static Announcement toEntity(AnnouncementDetailsDTO announcementDTO) {
        return Announcement.builder()
                .id(announcementDTO.getId())
                .title(announcementDTO.getTitle())
                .description(announcementDTO.getDescription())
                .price(announcementDTO.getPrice())
                .category(CategoryMapper.toEntity(announcementDTO.getCategory()))
                .user(UserMapper.toEntity(announcementDTO.getUser()))
                .date(announcementDTO.getDate())
                .discount(announcementDTO.getDiscount())
                .newPrice(announcementDTO.getNewPrice())
                .imageURL(announcementDTO.getImageURL())
                .build();
    }
}
