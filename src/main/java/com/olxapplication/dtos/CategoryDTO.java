package com.olxapplication.dtos;

import com.olxapplication.entity.Announcement;
import lombok.*;

import java.util.List;

/**
 * This Data Transfer Object (DTO) encapsulates detailed information about a category.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private String id;
    private String categoryName;
    private List<Announcement> announces;
}

