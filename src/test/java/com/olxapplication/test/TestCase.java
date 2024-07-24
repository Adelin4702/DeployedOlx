package com.olxapplication.test;

import com.olxapplication.OlxApplication;
import com.olxapplication.dtos.*;
import com.olxapplication.entity.Message;
import com.olxapplication.repository.FavouriteRepository;
import com.olxapplication.repository.UserRepository;
import com.olxapplication.service.*;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {OlxApplication.class})
@AutoConfigureMockMvc
@Transactional
public class TestCase {
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private UserService userService;
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private MessageService messageService;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
        favouriteRepository.deleteAll();
    }

    @Test
    void testAddToFavourite(){
        UserDetailsDTO user1 = UserDetailsDTO.builder()
                .firstName("Andrei")
                .lastName("Pop")
                .email("ap@gmail.com")
                .password("000000")
                .role("user")
                .build();
        UserDetailsDTO user2 = UserDetailsDTO.builder()
                .firstName("Ana")
                .lastName("Moldovan")
                .email("am@gmail.com")
                .password("000000")
                .role("user")
                .build();
        userService.insert(user1);
        userService.insert(user2);
        List<UserDetailsDTO> list = userService.findUsers();
        for(UserDetailsDTO user : list){
            System.out.println(user);
        }
        CategoryDTO cat = CategoryDTO.builder()
                .categoryName("Categorie")
                .build();
        categoryService.insert(cat);
        CategoryDetailsDTO category = categoryService.findCategories().get(0);

        AnnouncementWebDTO announcement = AnnouncementWebDTO.builder()
                .title("Titlu")
                .description("bla bla bla")
                .price(100.0)
                .discount(10.0)
                .user(list.get(0).getId())
                .category(category.getId())
                .imageURL("https://m.media-amazon.com/images/I/81VpUW4zZyL._AC_UF350,350_QL50_.jpg")
                .build();
        announcementService.insert(announcement);
        AnnouncementDetailsDTO announcementDetailsDTO = announcementService.findAnnouncementByUserId(list.get(0).getId()).get(0);

        String response = favouriteService.insertAnnouncement(list.get(1).getId(), announcementDetailsDTO.getId());
        list = userService.findUsers();
        for(UserDetailsDTO user : list){
            System.out.println(user.toString());
        }
        System.out.println(favouriteService.findByUserId(list.get(1).getId()).getId());
        assertThat(response).isEqualTo("Announcement added to favourites successfully");
    }

    @Test
    void testRemoveFromFavourite(){
        UserDetailsDTO user1 = UserDetailsDTO.builder()
                .firstName("Andrei")
                .lastName("Pop")
                .email("ap@gmail.com")
                .password("000000")
                .role("user")
                .build();
        UserDetailsDTO user2 = UserDetailsDTO.builder()
                .firstName("Ana")
                .lastName("Moldovan")
                .email("am@gmail.com")
                .password("000000")
                .role("user")
                .build();
        userService.insert(user1);
        userService.insert(user2);
        List<UserDetailsDTO> list = userService.findUsers();
        for(UserDetailsDTO user : list){
            System.out.println(user);
        }
        CategoryDTO cat = CategoryDTO.builder()
                .categoryName("Categorie")
                .build();
        categoryService.insert(cat);
        CategoryDetailsDTO category = categoryService.findCategories().get(0);

        AnnouncementWebDTO announcement = AnnouncementWebDTO.builder()
                .title("Titlu")
                .description("bla bla bla")
                .price(100.0)
                .discount(10.0)
                .user(list.get(0).getId())
                .category(category.getId())
                .imageURL("https://m.media-amazon.com/images/I/81VpUW4zZyL._AC_UF350,350_QL50_.jpg")
                .build();
        announcementService.insert(announcement);
        AnnouncementDetailsDTO announcementDetailsDTO = announcementService.findAnnouncementByUserId(list.get(0).getId()).get(0);
        favouriteService.insertAnnouncement(list.get(1).getId(), announcementDetailsDTO.getId());
        String response = favouriteService.deleteAnnouncement(list.get(1).getId(), announcementDetailsDTO.getId());

        list = userService.findUsers();
        for(UserDetailsDTO user : list){
            System.out.println(user.toString());
        }
        System.out.println(favouriteService.findByUserId(list.get(1).getId()).getId());
        assertThat(response).isEqualTo("Announcement removed from favourites successfully");
    }

    @Test
    void testSendMessage(){
        UserDetailsDTO user1 = UserDetailsDTO.builder()
                .firstName("Andrei")
                .lastName("Pop")
                .email("ap@gmail.com")
                .password("000000")
                .role("user")
                .build();
        UserDetailsDTO user2 = UserDetailsDTO.builder()
                .firstName("Ana")
                .lastName("Moldovan")
                .email("am@gmail.com")
                .password("000000")
                .role("user")
                .build();
        userService.insert(user1);
        userService.insert(user2);
        List<UserDetailsDTO> list = userService.findUsers();

        MessageWebDTO messageWebDTO = MessageWebDTO.builder()
                .msg("Hello World")
                .sender(list.get(0).getId())
                .receiver(list.get(1).getId())
                .build();



        String response = messageService.insert(messageWebDTO);
        list = userService.findUsers();
        for(UserDetailsDTO user : list){
            System.out.println(user.toString());
        }
        for(Message m : messageService.findChat(list.get(0).getId(), list.get(1).getId())){
            System.out.println(m.toString());
        }
        assertThat(response).isEqualTo("The message was sent successfully");
    }
}
