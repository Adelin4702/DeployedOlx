package com.olxapplication.service;

import com.olxapplication.constants.MessageMessages;
import com.olxapplication.dtos.MessageWebDTO;
import com.olxapplication.entity.Message;
import com.olxapplication.entity.User;
import com.olxapplication.exception.PatternNotMathcedException;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.repository.MessageRepository;
import com.olxapplication.repository.UserRepository;
import com.olxapplication.validators.MessageValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for managing messages in the OLX application.
 */
@Service
@AllArgsConstructor
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageValidator messageValidator = new MessageValidator();


    /**
     * Inserts a new message into the repository after validating the input.
     * @param messageWebDTO The object containing message details.
     * @return A success message if the message is sent, otherwise an error message.
     */
    public String insert(MessageWebDTO messageWebDTO){
        try {
            messageValidator.messageValidator(messageWebDTO);
            new Message();
            Message msg = Message.builder()
                    .msg(messageWebDTO.getMsg())
                    .sender(userRepository.findById(messageWebDTO.getSender()).get())
                    .receiver(userRepository.findById(messageWebDTO.getReceiver()).get())
                    .date(LocalDateTime.now())
                    .build();
            messageRepository.save(msg);
            LOGGER.debug(MessageMessages.MESSAGE_SENT_SUCCESSFULLY);
            return MessageMessages.MESSAGE_SENT_SUCCESSFULLY;
        } catch (PatternNotMathcedException | ResourceNotFoundException e) {
            LOGGER.error(MessageMessages.MESSAGE_NOT_SENT + e.getMessage());
            return MessageMessages.MESSAGE_NOT_SENT + e.getMessage();
        }
    }


    /**
     * Finds all correspondents for a specific user.
     * @param id The ID of the user.
     * @return A list of users who have corresponded with the specified user.
     */
    public List<User> findCorespondents(String id){
        List<User> users = new java.util.ArrayList<>(messageRepository.findAll().stream().map(Message::getSender).toList());
        users.addAll(messageRepository.findAll().stream().map(Message::getReceiver).toList());
        List<User> distinctUsers = new java.util.ArrayList<>(users.stream().distinct().toList());
        for(int i = distinctUsers.size() - 1; i >= 0; i--){
            if(distinctUsers.get(i).getId().equals(id)){
                distinctUsers.remove(i);
            }
        }
        return distinctUsers;
    }

    /**
     * Finds the chat between two users.
     * @param sender The ID of the sender.
     * @param receiver The ID of the receiver.
     * @return A sorted list of messages forming the chat history between the two users.
     */
    public List<Message> findChat(String sender, String receiver){
        List<Message> messages = messageRepository.findBySenderIdAndReceiverId(sender, receiver);
        messages.addAll(messageRepository.findBySenderIdAndReceiverId(receiver, sender));
        messages.sort(Comparator.comparing(Message::getDate));
        return messages;
    }
}
