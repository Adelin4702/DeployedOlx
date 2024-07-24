package com.olxapplication.repository;

import com.olxapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * This interface extends JPA's JpaRepository, providing access to User entities within the persistence layer.
 * It offers basic CRUD (Create, Read, Update, Delete) operations for Announcement entities identified by their unique Strings.
 */
public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByEmailIgnoreCase(String email);
    Boolean existsByEmailIgnoreCase(String email);
    User findByRole(String role);
}