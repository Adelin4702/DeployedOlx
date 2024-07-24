package com.olxapplication.controller;

import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.dtos.AnnouncementWebDTO;
import com.olxapplication.service.AnnouncementService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * This controller class provides API endpoints for managing announcements within the application.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/announcement")
@Setter
@Getter
@AllArgsConstructor
@Validated
@Slf4j
public class AnnouncementController {
    private final AnnouncementService announcementService;

    /**
     * Retrieves all announcements.
     * @return ModelAndView containing all announcements.
     */
    @GetMapping("/get")
    public ModelAndView getAnnounces(){
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnounces();
        ModelAndView mav = new ModelAndView("AdminGetAnnounces");
        mav.addObject("announces", dtos);
        return mav;
    }



//    /**
//     * Retrieves all announcements by user ID.
//     * @param user_id The ID of the user.
//     * @return ResponseEntity containing a list of announcements and HTTP status.
//     */
//    @GetMapping("/byUser/{user_id}")
//    public ResponseEntity<List<AnnouncementDetailsDTO>> getAnnouncesByUserId(@PathVariable("user_id") String user_id){
//        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByUserId(user_id);
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }

//    /**
//     * Retrieves all announcements by title.
//     * @param title The title of the announcement.
//     * @return ResponseEntity containing a list of announcements and HTTP status.
//     */
//    @GetMapping("/byTitle/{title}")
//    public ResponseEntity<List<AnnouncementDetailsDTO>> getAnnouncesByTitle(@PathVariable("title") String title){
//        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByTitle(title);
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }

    /**
     * Inserts a new announcement.
     * @param announcementDTO The announcement to be inserted.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/announcement/get".
     */
    @PostMapping("/insert")
    public ModelAndView insertAnnouncement(@ModelAttribute("announcement") AnnouncementWebDTO announcementDTO,RedirectAttributes redirectAttributes) {

        String msg = announcementService.insert(announcementDTO);
        ModelAndView mav = new ModelAndView("redirect:/announcement/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    /**
     * Inserts a new announcement by a specific user.
     * @param id The ID of the user.
     * @param announcementDTO The announcement to be inserted.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/announcement/getMine/{id}".
     */
    @PostMapping("/insertMine/{id}")
    public ModelAndView insertMyAnnouncement(@PathVariable("id") String id, @ModelAttribute("announcement") AnnouncementWebDTO announcementDTO, RedirectAttributes redirectAttributes) {
        String msg = announcementService.insert(announcementDTO);
        ModelAndView mav = new ModelAndView("redirect:/announcement/getMine/" + id);
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

//    /**
//     * Retrieves a specific announcement by ID.
//     * @param announcementId The ID of the announcement.
//     * @return ResponseEntity containing the announcement details and HTTP status.
//     */
//    @GetMapping("/get/{id}")
//    public ResponseEntity<AnnouncementDetailsDTO> getAnnouncement(@PathVariable("id") String announcementId) {
//        AnnouncementDetailsDTO announcementDetailsDTO = announcementService.findAnnouncementById(announcementId);
//        return new ResponseEntity<>(announcementDetailsDTO, HttpStatus.OK);
//    }

    /**
     * Retrieves all announcements by a specific user.
     * @param userId The ID of the user.
     * @return ModelAndView containing the user's announcements.
     */
    @GetMapping("/getMine/{id}")
    public ModelAndView getMyAnnouncements(@PathVariable("id") String userId) {
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByUserId(userId);
        ModelAndView mav = new ModelAndView("UserGetMyAnnounces");
        mav.addObject("announces", dtos);
        return mav;
    }

//    /**
//     * Retrieves all announcements by category ID.
//     * @param category_id The ID of the category.
//     * @return ResponseEntity containing a list of announcements and HTTP status.
//     */
//    @GetMapping("/getMine/byCategory//{category_id}")
//    public ModelAndView getAnnouncesByCategoryName(@PathVariable("category_id") String category_id){
//        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByCategoryId(category_id);
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }

    /**
     * Retrieves all announcements except those by a specific user.
     * @param userId The ID of the user.
     * @param categoryName The string that match category names to filter the announcements by their category .
     * @return ModelAndView containing the other users' announcements.
     */
    @GetMapping("/getOthers/{id}")
    public ModelAndView getOtherAnnouncements(@PathVariable("id") String userId, @ModelAttribute("categoryName") String categoryName) {
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByCategoryNameAndNotUser(categoryName, userId);
        List<AnnouncementDetailsDTO> reversedDtos = dtos.reversed();
        ModelAndView mav = new ModelAndView("UserGetOtherAnnounces");
        mav.addObject("announces", dtos);
        mav.addObject("reversed", reversedDtos);
        return mav;
    }

    /**
     * Displays the announcements that are not posted by the user with the specified ID, ordered descending by their newPrice.
     * @param userId The ID of the user.
     * @param categoryName The string that match category names to filter the announcements by their category .
     * @return ModelAndView "UserGetOtherAnnounces".
     */
    @GetMapping("/getOthers/0/{id}")
    public ModelAndView getOtherAnnouncementsAsc(@PathVariable("id") String userId, @ModelAttribute("categoryName") String categoryName) {
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByCategoryNameAndNotUser(categoryName, userId);
        ModelAndView mav = new ModelAndView("UserGetOtherAnnounces");
        mav.addObject("announces", dtos);
        return mav;
    }

    /**
     * Displays the announcements that are not posted by the user with the specified ID, ordered ascending by their newPrice.
     * @param userId The ID of the user.
     * @param categoryName The string that match category names to filter the announcements by their category .
     * @return ModelAndView "UserGetOtherAnnounces".
     */
    @GetMapping("/getOthers/1/{id}")
    public ModelAndView getOtherAnnouncementsDesc(@PathVariable("id") String userId, @ModelAttribute("categoryName") String categoryName) {
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByCategoryNameAndNotUser(categoryName, userId);
        List<AnnouncementDetailsDTO> reversedDtos = dtos.reversed();
        ModelAndView mav = new ModelAndView("UserGetOtherAnnounces");
        mav.addObject("announces", reversedDtos);
        return mav;
    }


    /**
     * Deletes a specific announcement by ID.
     * @param announcementId The ID of the announcement.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/announcement/get".
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteAnnouncement(@PathVariable("id") String announcementId, RedirectAttributes redirectAttributes) {
        String msg = announcementService.deleteAnnouncementById(announcementId);
        ModelAndView mav = new ModelAndView("redirect:/announcement/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    /**
     * Deletes a specific announcement by ID for a specific user.
     * @param announcementId The ID of the announcement.
     * @param userId The ID of the user.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/announcement/getMine/{userId}".
     */
    @PostMapping("/deleteMine/{idUser}/{id}")
    public ModelAndView deleteMyAnnouncement(@PathVariable("id") String announcementId, @PathVariable("idUser") String userId, RedirectAttributes redirectAttributes) {

        String msg = announcementService.deleteAnnouncementById(announcementId);
        ModelAndView mav = new ModelAndView("redirect:/announcement/getMine/" + userId);
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    /**
     * Updates a specific announcement by ID.
     * @param announcementId The ID of the announcement.
     * @param announcementDTO The updated announcement details.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/announcement/get".
     */
    @PostMapping("/update/{id}")
    public ModelAndView updateAnnouncement(@PathVariable("id") String announcementId, @ModelAttribute("announcement") AnnouncementWebDTO announcementDTO, RedirectAttributes redirectAttributes) {
        String msg = announcementService.updateAnnouncementById(announcementId, announcementDTO);
        ModelAndView mav = new ModelAndView("redirect:/announcement/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    /**
     * Updates a specific announcement by ID for a specific user.
     * @param announcementId The ID of the announcement.
     * @param announcementDTO The updated announcement details.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/announcement/getMine/{announcementDTO.getUser()}".
     */
    @PostMapping("/updateMine/{id}")
    public ModelAndView updateMyAnnouncement(@PathVariable("id") String announcementId, @ModelAttribute("announcement") AnnouncementWebDTO announcementDTO, RedirectAttributes redirectAttributes) {
        String msg = announcementService.updateAnnouncementById(announcementId, announcementDTO);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/announcement/getMine/" + announcementDTO.getUser());
        return mav;
    }

}
