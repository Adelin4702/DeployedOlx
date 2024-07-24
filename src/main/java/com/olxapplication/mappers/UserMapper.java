package com.olxapplication.mappers;

import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Message;
import com.olxapplication.entity.User;
import com.olxapplication.dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class provides utility methods for mapping between User entities and their corresponding DTO representations.
 */
public class UserMapper {

    /**
     * Converts a User entity into a lightweight UserDetailsDTO object.
     * This DTO includes only the user ID, name, email, password, and a list of Strings representing the associated announcements.
     *
     * @param user The User entity to be converted.
     * @return A new UserDetailsDTO object containing concise user information and announcement Strings.
     */
    public static UserDetailsDTO toUserDetailsDTO(User user) {
        List<String> stringList =user.getAnnounces().stream()
                .map(Announcement::getId)
                .toList();

        return UserDetailsDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .announces(stringList)
                .sentMessages(user.getReceivedMessages())
                .receivedMessages(user.getReceivedMessages())
                .favouriteList(user.getFavouriteList())
                .build();
    }

    /**
     * Converts a UserDTO object into a corresponding User entity.
     *
     * @param userDTO The UserDTO object to be converted.
     * @return A new User entity populated with data from the DTO, including a set of announcements.
     */
    public static User toEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .announces(userDTO.getAnnounces().stream().collect(Collectors.toList()))
                .sentMessages(userDTO.getSentMessages().stream().collect(Collectors.toList()))
                .receivedMessages(userDTO.getReceivedMessages().stream().collect(Collectors.toList()))
                .favouriteList(userDTO.getFavouriteList())
                .build();
    }

    /**
     * Converts a UserDetailsDTO object into a corresponding User entity.
     * This method initializes an empty set of announcements, as the UserDetailsDTO only provides announcement Strings.
     *
     * @param userDTO The UserDetailsDTO object to be converted.
     * @return A new User entity populated with basic user information, but without associated announcements.
     */
    public static User toEntity(UserDetailsDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .announces((new ArrayList<Announcement>()).stream().collect(Collectors.toList()))
                .sentMessages((new ArrayList<Message>()).stream().collect(Collectors.toList()))
                .receivedMessages((new ArrayList<Message>()).stream().collect(Collectors.toList()))
                .favouriteList(userDTO.getFavouriteList())
                .build();
    }

}
