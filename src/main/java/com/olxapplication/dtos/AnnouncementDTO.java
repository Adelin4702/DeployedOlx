package com.olxapplication.dtos;

import com.olxapplication.entity.Category;
import com.olxapplication.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * This Data Transfer Object (DTO) encapsulates detailed information about an announcement.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDTO {
    private String id;
    private String title;
    private String description;
    private Double price;
    private User user;
    private Category category;
    private LocalDateTime date;
    private Double discount;
    private Double newPrice;
    private String imageURL;
}
