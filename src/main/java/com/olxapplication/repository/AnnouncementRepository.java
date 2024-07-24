package com.olxapplication.repository;

import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface extends JPA's JpaRepository, providing access to Announcement entities within the persistence layer.
 * It offers basic CRUD (Create, Read, Update, Delete) operations for Announcement entities identified by their unique Strings.
 */
public interface AnnouncementRepository extends JpaRepository<Announcement, String> {
    List<Announcement> findAnnouncementsByCategoryCategoryNameContainsIgnoreCaseAndUserIdNot(String category, String userId);
    List<Announcement> findAnnouncementsByUser_Id(String category_id);

}
