package com.olxapplication.repository;

import com.olxapplication.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface extends JPA's JpaRepository, providing access to Category entities within the persistence layer.
 * It offers basic CRUD (Create, Read, Update, Delete) operations for Announcement entities identified by their unique Strings.
 */
public interface CategoryRepository extends JpaRepository<Category, String> {
}
