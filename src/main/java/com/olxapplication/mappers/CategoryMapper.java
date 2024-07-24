package com.olxapplication.mappers;

import com.olxapplication.dtos.CategoryDTO;
import com.olxapplication.dtos.CategoryDetailsDTO;
import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides utility methods for mapping between Category entities and their corresponding DTO representations.
 */
public class CategoryMapper {

    /**
     * Converts a Category entity into a lightweight CategoryDetailsDTO object.
     * This DTO only includes the category ID, name, and a list of Strings representing the associated announcements.
     *
     * @param category The Category entity to be converted.
     * @return A new CategoryDetailsDTO object containing concise category information and announcement Strings.
     */
    public static CategoryDetailsDTO toCategoryDetailsDTO(Category category){
        List<String> StringList =category.getAnnounces().stream()
                .map(Announcement::getId)
                .collect(Collectors.toList());

        return CategoryDetailsDTO.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .announces(StringList)
                .build();
    }

    /**
     * Converts a CategoryDTO object into a corresponding Category entity.
     *
     * @param categoryDTO The CategoryDTO object to be converted.
     * @return A new Category entity populated with data from the DTO, including a set of announcements.
     */
    public static Category toEntity(CategoryDTO categoryDTO){
        return Category.builder()
                .id(categoryDTO.getId())
                .categoryName(categoryDTO.getCategoryName())
                .announces(new ArrayList<Announcement>())
                .build();
    }

    /**
     * Converts a CategoryDetailsDTO object into a corresponding Category entity.
     * This method initializes an empty set of announcements, as the CategoryDetailsDTO only provides announcement Strings.
     *
     * @param categoryDTO The CategoryDetailsDTO object to be converted.
     * @return A new Category entity populated with basic category information, but without associated announcements.
     */
    public static Category toEntity(CategoryDetailsDTO categoryDTO){
        return Category.builder()
                .id(categoryDTO.getId())
                .categoryName(categoryDTO.getCategoryName())
                .announces((new ArrayList<Announcement>()).stream().collect(Collectors.toList()))
                .build();
    }

}
