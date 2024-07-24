package com.olxapplication.controller;

import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.service.UserService;
import com.olxapplication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


/**
 * Controller for handling requests to the index page.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/index")
@Setter
@Getter
@AllArgsConstructor
public class IndexController {
    private final UserService userService;


    /**
     * Handles GET requests to the HomePage -- The login page.
     * @return ModelAndView "HomePage".
     */
    @GetMapping("/HomePage")
    public ModelAndView logIn(){
        List<UserDetailsDTO> users = userService.findUsers();
        ModelAndView mav = new ModelAndView("HomePage");
        mav.addObject("users", users);
        return mav;
    }

    /**
     * Redirect the user based on their role.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirects to a specific url.
     */
    @PostMapping("/redirectPage")
    public ModelAndView redirectBasedOnRole(@ModelAttribute("userEmail") String email, @ModelAttribute("userPassword") String password, RedirectAttributes redirectAttributes, RedirectAttributes redirectAttributesNume){
        ModelAndView mav = new ModelAndView();
        System.out.println("a ajuns in redirectBasedOnRole");
        if(userService.checkUser(email, password).equals("admin")) {
            mav.setViewName("redirect:/user/get");
        } else {
            if(userService.checkUser(email, password).equals("user")) {
                Optional<User> user = userService.findUserByEmail(email);
                redirectAttributesNume.addFlashAttribute("userMessage", "Hello, " + user.get().getFirstName() + " " + user.get().getLastName());
                mav.setViewName("redirect:/announcement/getOthers/" + user.get().getId());
            } else {
                redirectAttributes.addFlashAttribute("message", userService.checkUser(email, password));
                mav.setViewName("redirect:/index/HomePage");
            }
        }
        return mav;
    }

    /**
     * Redirect the admin to another page.
     * @param newLink The new link to redirect to.
     * @return ModelAndView object with the view name set to the new link.
     */
    @PostMapping("/admin")
    public ModelAndView adminRedirect(@ModelAttribute("newLink") String newLink){
        ModelAndView mav = new ModelAndView(newLink);
        return mav;
    }

    /**
     * Redirect the user to another page.
     * @param id The ID of the user.
     * @param newLink The new link to redirect to.
     * @return ModelAndView object with the view name set to the new link.
     */
    @PostMapping("/user/{id}")
    public ModelAndView userRedirect(@PathVariable("id") String id, @ModelAttribute("newLink") String newLink){
        ModelAndView mav = new ModelAndView(newLink);
        return mav;
    }

}
