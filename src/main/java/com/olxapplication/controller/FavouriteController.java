package com.olxapplication.controller;

import com.olxapplication.entity.Favourite;
import com.olxapplication.service.FavouriteService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This controller class provides API endpoints for managing favourite announcements within the application.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/favourite")
@Setter
@Getter
@AllArgsConstructor
public class FavouriteController {
    private final FavouriteService favouriteService;

    /**
     * Displays the favourite announcements in user's favourite list.
     * @param userId The ID of the user.
     * @return ModelAndView "Favourites".
     */
    @GetMapping("/get/{id}")
    public ModelAndView getAnnounces(@PathVariable("id") String userId) {
        ModelAndView mav = new ModelAndView("Favourites");
        Favourite favourite = favouriteService.findByUserId(userId);
        mav.addObject("favourite", favourite);
        return mav;
    }

    /**
     * Adds an announcement to the user's favourite list.
     * @param userId The ID of the user.
     * @param announcementId the announcement to be added in favourite list.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView "/favourite/get/{id}".
     */
    @PostMapping("/add/{id}/{announcementId}")
    public ModelAndView addAnnouncementToFavourites(@PathVariable("id") String userId, @PathVariable("announcementId") String announcementId,  RedirectAttributes redirectAttributes) {
        String msg = favouriteService.insertAnnouncement(userId, announcementId);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/favourite/get/{id}");
        return mav;
    }

    /**
     * Delete an announcement from the user's favourite list.
     * @param userId The ID of the user.
     * @param announcementId the announcement to be removed from favourite list.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView "/favourite/get/{id}".
     */
    @PostMapping("/remove/{id}/{announcementId}")
    public ModelAndView removeAnnouncementFromFavourites(@PathVariable("id") String userId,@PathVariable("announcementId") String announcementId,  RedirectAttributes redirectAttributes) {
        String msg = favouriteService.deleteAnnouncement(userId, announcementId);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/favourite/get/{id}");
        return mav;
    }
}
