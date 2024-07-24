package com.olxapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;

/**
 * This entity class represents a category of product or service in the announcement.
 * It maps to the corresponding "categories_table" in the database.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories_table")
public class Category {
    /**
     * The unique identifier of the category, generated using a String strategy.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy =  "uuid2")
    private String id;

    /**
     * The descriptive name of the category, which categorizes announcements.
     */
    @Column(name = "categoryName", nullable = false)
    private String categoryName;

    /**
     * The set of announcements that belong to this category, establishing a one-to-many relationship.
     * CascadeType.ALL ensures that changes to a category are propagated to its associated announcements.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Announcement> announces;

}
