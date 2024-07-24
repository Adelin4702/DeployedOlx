package com.olxapplication.entity;

import jakarta.persistence.*;
        import lombok.*;
        import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favourites_table")
public class Favourite {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy =  "uuid2")
    private String id;

    @OneToOne(mappedBy = "favouriteList", cascade = CascadeType.ALL)
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "favourite_announcement",
            joinColumns = @JoinColumn(name = "favourite_id"),
            inverseJoinColumns = @JoinColumn(name = "announcement_id"))
    private List<Announcement> favouriteAnnouncements;

    private Double total;
}
