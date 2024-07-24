package com.olxapplication.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.olxapplication.config.RabbitMQSender;
import com.olxapplication.dtos.*;
import com.olxapplication.service.UserService;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This controller class provides API endpoints for managing users within the application.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/user")
@Setter
@Getter
@AllArgsConstructor
public class UserController {
    private static final String URL = "http://localhost:8081/microservice/receiver";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private final RabbitMQSender rabbitMQSender;

    private final UserService userService;

    /**
     * Get all users.
     * @return ModelAndView "AdminGetUsers".
     */
    @GetMapping("/get")
    public ModelAndView getUsers(){
        List<UserDetailsDTO> users = userService.findUsers();
        ModelAndView mav = new ModelAndView("AdminGetUsers");
        mav.addObject("users", users);
        return mav;
    }

    /**
     * Delete a user by its ID.
     * @param userId The ID of the user.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView for redirection.
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String userId, RedirectAttributes redirectAttributes) {
        String msg = userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        return mav;
    }

    /**
     * Insert a new user and synchronously sends an email to its address.
     * @param userDetailsDTO The user to be inserted.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/user/get".
     * */
    @PostMapping("/insert")
    public ModelAndView insertUser(@ModelAttribute("user") UserDetailsDTO userDetailsDTO, RedirectAttributes redirectAttributes) {
        String msg = userService.insert(userDetailsDTO);

        // Crearea unui nou UserDto
        UserMailDTO userMailDTO = new UserMailDTO(userDetailsDTO.getId()
                , userDetailsDTO.getFirstName()
                , userDetailsDTO.getLastName()
                , userDetailsDTO.getEmail()
                , "insert", "");

        // Crearea HttpHeaders și setarea token-ului
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(userMailDTO.getId() + userMailDTO.getEmail()); // presupunem că token-ul este disponibil

        // Crearea NotificationRequestDto și HttpEntity
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(userMailDTO.getId(), userMailDTO.getFirstName() + " " + userMailDTO.getLastName(), userMailDTO.getEmail(), userMailDTO.getAction(), userMailDTO.getFilePath()); // completați cu datele necesare
        HttpEntity<NotificationRequestDto> entity = new HttpEntity<>(notificationRequestDto, headers);

        // Apelarea metodei restTemplate.exchange
        ResponseMessageDto response = restTemplate.exchange(URL, HttpMethod.POST, entity, ResponseMessageDto.class).getBody();
        //System.out.println("!!!!!!!!------------>" + response + "<------------!!!!!!!!");

        ModelAndView mav = new ModelAndView("redirect:/user/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    /**
     * Update the user with the specified id and asynchronously sends an email to its address.
     * @param userId the id of the user.
     * @param userDetailsDTO The user with the updated values.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/user/get".
     * */
    @PostMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") String userId, @ModelAttribute("user") UserDetailsDTO userDetailsDTO, RedirectAttributes redirectAttributes) {
        String msg = userService.updateUserById(userId, userDetailsDTO);

        UserMailDTO userDTO = new UserMailDTO(userDetailsDTO.getId()
                , userDetailsDTO.getFirstName()
                , userDetailsDTO.getLastName()
                , userDetailsDTO.getEmail()
                , "update"
                ,"");
        rabbitMQSender.send(userDTO);

        ModelAndView mav = new ModelAndView("redirect:/user/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }


}

