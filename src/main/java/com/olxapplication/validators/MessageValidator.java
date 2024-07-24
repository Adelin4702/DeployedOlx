package com.olxapplication.validators;

import com.olxapplication.constants.AnnouncementMessages;
import com.olxapplication.constants.MessageMessages;
import com.olxapplication.dtos.AnnouncementWebDTO;
import com.olxapplication.dtos.MessageWebDTO;
import com.olxapplication.exception.PatternNotMathcedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageValidator {
    public boolean msgValidator(String description) throws PatternNotMathcedException {
        Pattern pattern = Pattern.compile("^.{1,255}$");
        Matcher matcher = pattern.matcher(description);

        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            throw new PatternNotMathcedException(MessageMessages.MESSAGE_PATTERN_NOT_MATCHED);
        }
    }

    public boolean idValidator(String id) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^[-a-zA-Z0-9]{5,250}$");
        Matcher matcher = pattern.matcher(id);

        if(matcher.find()){
            matcher.reset();
            return true;
        } else {
            throw new PatternNotMathcedException(MessageMessages.ID_PATTERN_NOT_MATCHED);
        }
    }

    public boolean messageValidator(MessageWebDTO messageWebDTO) throws PatternNotMathcedException{
        if(msgValidator(messageWebDTO.getMsg())&&idValidator(messageWebDTO.getSender())&&
                msgValidator(messageWebDTO.getReceiver())) {
            return true;
        }else{
            return false;
        }
    }
}
