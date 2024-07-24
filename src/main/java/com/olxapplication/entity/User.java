package com.olxapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;

/**
 * This entity class represents a user within the application's data model.
 * It maps to the corresponding "users_table" in the database.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_table")
public class User {
    /**
     * The unique identifier of the user, generated using a String strategy.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy =  "uuid2")
    private String id;

    /**
     * The user's first name, forming part of their personal identification.
     */
    @Column(name = "firstName", nullable = false)
    private String firstName;

    /**
     * The user's last name, completing their personal identification.
     */
    @Column(name = "lastName", nullable = false)
    private String lastName;

    /**
     * The user's email address, which serves as their unique login credential.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * The user's role, which serves as their role in the application.
     */
    @Column(name = "role", nullable = false)
    private String role;

    /**
     * The user's password, securely stored to authenticate their account access.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The set of announcements created by this user, establishing a one-to-many relationship.
     * CascadeType.ALL ensures that changes to a user are propagated to their associated announcements.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Announcement> announces;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> receivedMessages;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "favourite_id")
    private Favourite favouriteList;



}