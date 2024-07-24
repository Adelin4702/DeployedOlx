package com.olxapplication.service;

import com.olxapplication.constants.UserMessages;
import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Favourite;
import com.olxapplication.exception.PatternNotMathcedException;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.UserMapper;
import com.olxapplication.entity.User;
import com.olxapplication.repository.FavouriteRepository;
import com.olxapplication.repository.UserRepository;
import com.olxapplication.validators.UserValidators;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service layer class provides business logic operations for managing users within the application.
 * It interacts with the UserRepository to perform CRUD operations and leverages mappers for DTO conversions.
 */
@Service
@AllArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    private final UserValidators userValidators = new UserValidators();

    /**
     * Fetches all users from the repository and maps them to UserDetailsDTO objects.
     * @return a list of UserDetailsDTO objects.
     */
    public List<UserDetailsDTO> findUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toUserDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Inserts a new user into the repository after validating the inputs.
     * @param userDTO the UserDetailsDTO object of the user to insert.
     * @return a message indicating the result of the operation.
     */
    public String insert(UserDetailsDTO userDTO) {
        try {
            userValidators.userDtoValidator(userDTO);
            if(userRepository.existsByEmailIgnoreCase(userDTO.getEmail())){
                LOGGER.error(UserMessages.USER_NOT_INSERTED + UserMessages.EMAIL_ALREADY_EXISTS);
                return UserMessages.USER_NOT_INSERTED + UserMessages.EMAIL_ALREADY_EXISTS;
            }
            User user = UserMapper.toEntity(userDTO);
            Favourite favourite = new Favourite(null, user, new ArrayList<>(), 0.0);
            user = userRepository.save(user);
            favourite = favouriteRepository.save(favourite);
            user.setFavouriteList(favourite);
            user = userRepository.save(user);
            LOGGER.debug(UserMessages.USER_INSERTED_SUCCESSFULLY);
            return UserMessages.USER_INSERTED_SUCCESSFULLY;
        }catch (PatternNotMathcedException e){
            LOGGER.error(UserMessages.USER_NOT_INSERTED + e.getMessage());
            return UserMessages.USER_NOT_INSERTED + e.getMessage();
        }
    }

    /**
     * Deletes a user by ID from the repository.
     * @param id the ID of the user to delete.
     * @return a message indicating the result of the operation.
     */
    public String deleteUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error(UserMessages.USER_NOT_FOUND + id);
            return UserMessages.USER_NOT_FOUND + id;
        } else {
            if(userOptional.get().getId().equals("22b95bc0-2123-42d3-8234-69a8dd91c1bf")){
                return UserMessages.ADMIN_DELETE;
            }

            userRepository.delete(userOptional.get());
            LOGGER.debug(UserMessages.USER_DELETED_SUCCESSFULLY);
            return UserMessages.USER_DELETED_SUCCESSFULLY + id;
        }
    }

    /**
     * Updates a user by ID in the repository after validating the input.
     * @param id the ID of the user to update.
     * @param userDTO the UserDetailsDTO object containing the updated user data.
     * @return a message indicating the result of the operation.
     */
    public String updateUserById(String id, UserDetailsDTO userDTO) {
        try {
            userValidators.userDtoValidator(userDTO);
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isEmpty()) {
                LOGGER.error(UserMessages.USER_NOT_FOUND + id);
                return UserMessages.USER_NOT_FOUND + id;
            } else {
                User toBeUpdated = userOptional.get();
                toBeUpdated.setFirstName(userDTO.getFirstName());
                toBeUpdated.setLastName(userDTO.getLastName());
                toBeUpdated.setEmail(userDTO.getEmail());
                toBeUpdated.setPassword(userDTO.getPassword());
                userRepository.save(toBeUpdated);
                LOGGER.info(UserMessages.USER_UPDATED_SUCCESSFULLY + id);
            }
            return UserMessages.USER_UPDATED_SUCCESSFULLY + id;
        }catch (PatternNotMathcedException e){
            LOGGER.error(UserMessages.USER_NOT_UPDATED + e.getMessage());
            return UserMessages.USER_NOT_UPDATED + e.getMessage();
        }
    }

    /**
     * Checks if there exist a user with the specified email and if the password match.
     * @param email the email of the user.
     * @param password the password inserted.
     * @return a message indicating the result of the operation or the user role.
     */
    public String checkUser(String email, String password){
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        if (userOptional.isEmpty()){
            LOGGER.error(UserMessages.INVALID_EMAIL);
            return UserMessages.INVALID_EMAIL;
        } else {
            if(userOptional.get().getPassword().equals(password)){
                return userOptional.get().getRole();
            } else {
                LOGGER.error(UserMessages.INVALID_PASSWORD);
                return UserMessages.INVALID_PASSWORD;
            }
        }
    }

    /**
     * Return the user with the specified email
     * @param email the ID of the user to update.
     * @return the user if exists or null.
     */
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmailIgnoreCase(email);
    }

}
