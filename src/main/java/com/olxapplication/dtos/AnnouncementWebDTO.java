package com.olxapplication.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementWebDTO {
    private String id;
    private String title;
    private String description;
    private Double price;
    private String user;
    private String category;
    private Double discount;
    private Double newPrice;
    private String imageURL;
}
