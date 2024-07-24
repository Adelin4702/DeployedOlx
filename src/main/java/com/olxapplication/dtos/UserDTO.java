package com.olxapplication.dtos;

import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Favourite;
import com.olxapplication.entity.Message;
import lombok.*;

import java.util.List;


/**
 * This Data Transfer Object (DTO) encapsulates detailed information about an user.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private List<Announcement> announces;
    private List<Message> sentMessages;
    private List<Message> receivedMessages;
    private Favourite favouriteList;
}
