package com.olxapplication.dtos;

import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteDTO {
    private String id;
    private User user;
    private List<Announcement> favouriteAnnouncements;
}
