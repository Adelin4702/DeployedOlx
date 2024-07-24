package com.olxapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "announces_table")
public class Announcement {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy =  "uuid2")
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private LocalDateTime date;

    @Column
    private Double discount;

    @Column
    private Double newPrice;

    @ManyToMany(mappedBy = "favouriteAnnouncements", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    List<Favourite> favourites;

    @Column
    private String imageURL;

    @PreRemove
    private void removeAnnouncementFromFavourites() {
        for (Favourite favourite : favourites) {
            favourite.getFavouriteAnnouncements().remove(this);
        }
    }
}

