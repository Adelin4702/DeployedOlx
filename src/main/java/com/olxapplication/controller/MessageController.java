package com.olxapplication.controller;

import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.dtos.AnnouncementWebDTO;
import com.olxapplication.dtos.MessageWebDTO;
import com.olxapplication.entity.Message;
import com.olxapplication.entity.User;
import com.olxapplication.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * This controller class provides API endpoints for managing messages within the application.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/message")
@Setter
@Getter
@AllArgsConstructor
@Validated
@Slf4j
public class MessageController {
    private final MessageService messageService;

    /**
     * Send a message from sender to receiver.
     * @param messageWebDTO The message DTO containing the sender, the receiver and the actual message.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to the chat between the two users.
     * */
    @PostMapping("/send")
    public ModelAndView sendMessage(@ModelAttribute("msg") MessageWebDTO messageWebDTO, RedirectAttributes redirectAttributes) {
        String msg = messageService.insert(messageWebDTO);
        ModelAndView mav = new ModelAndView("redirect:/message/chat/" + messageWebDTO.getSender() + "/" + messageWebDTO.getReceiver());
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    /**
     * Displays the correspondents of the user with the specified id.
     * @param id The id of the user.
     * @return ModelAndView "Corespondents".
     * */
    @GetMapping("/messages/{id}")
    public ModelAndView getCorespondents(@PathVariable("id") String id){
        List<User> users = messageService.findCorespondents(id);
        ModelAndView mav = new ModelAndView("Corespondents");
        mav.addObject("users", users);
        return mav;
    }

    /**
     * Displays the chat between a user and its specified correspondent.
     * @param id The id of the user.
     * @param corespondentId The id of the correspondent.
     * @return ModelAndView "ChatNou".
     * */
    @GetMapping("/chat/{id}/{corespondentId}")
    public ModelAndView getChat(@PathVariable("id") String id, @PathVariable("corespondentId") String corespondentId){
        List<Message> messages = messageService.findChat(id, corespondentId);
        ModelAndView mav = new ModelAndView("ChatNou");
        mav.addObject("msgs", messages);
        return mav;
    }
}
