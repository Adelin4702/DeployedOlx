package com.olxapplication.dtos;

import com.olxapplication.entity.Favourite;
import com.olxapplication.entity.Message;
import lombok.*;

import java.util.List;

/**
 * This Data Transfer Object (DTO) encapsulates detailed information about an user.
 * It's primarily used for data exchange between application layers and APIs.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private List<String> announces;
    private List<Message> sentMessages;
    private List<Message> receivedMessages;
    private Favourite favouriteList;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", favouriteList=" + favouriteList.getId() +
                '}';
    }
}
