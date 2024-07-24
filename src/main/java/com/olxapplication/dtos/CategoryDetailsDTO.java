package com.olxapplication.dtos;

import com.olxapplication.entity.Announcement;
import lombok.*;

import java.util.List;

/**
 * This Data Transfer Object (DTO) encapsulates detailed information about a category.
 * It's primarily used for data exchange between application layers and APIs.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailsDTO {
    private String id;
    private String categoryName;
    private List<String> announces;
}

