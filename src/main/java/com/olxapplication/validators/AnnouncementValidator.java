package com.olxapplication.validators;

import com.olxapplication.constants.AnnouncementMessages;
import com.olxapplication.dtos.AnnouncementWebDTO;
import com.olxapplication.exception.PatternNotMathcedException;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AnnouncementValidator {
    public boolean titleValidator(String title) throws PatternNotMathcedException {
        Pattern pattern = Pattern.compile("^[-a-zA-Z,; ]{3,30}$");
        Matcher matcher = pattern.matcher(title);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.TITLE_PATTERN_NOT_MATCHED);
        }
    }

    public boolean descriptionValidator(String description) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^.{5,200}$");
        Matcher matcher = pattern.matcher(description);

        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.DESCRIPTION_PATTERN_NOT_MATCHED);
        }
    }

    public boolean priceValidator(Double price) throws PatternNotMathcedException{

        if(price >= 0){

            return true;
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.PRICE_PATTERN_NOT_MATCHED);
        }
    }

    public boolean discountValidator(Double discount) throws PatternNotMathcedException{

        if(discount >= 0 && discount <= 100){

            return true;
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.DISCOUNT_PATTERN_NOT_MATCHED);
        }
    }

    public boolean imageURLValidator(String url) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$");
        Matcher matcher = pattern.matcher(url);

        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.IMAGE_URL_NOT_MATCHED);
        }
    }

    public boolean idValidator(String id) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^[-a-zA-Z0-9]{5,250}$");
        Matcher matcher = pattern.matcher(id);

        if(matcher.find()){
            matcher.reset();
            return true;
        } else {
            throw new PatternNotMathcedException(AnnouncementMessages.ID_PATTERN_NOT_MATCHED);
        }
    }


    public boolean announcementWebDtoValidator(AnnouncementWebDTO announcementWebDTO) throws PatternNotMathcedException{
        if(titleValidator(announcementWebDTO.getTitle())&&descriptionValidator(announcementWebDTO.getDescription())
                && discountValidator(announcementWebDTO.getDiscount()) && priceValidator(announcementWebDTO.getPrice())
                &&idValidator(announcementWebDTO.getUser())&&idValidator(announcementWebDTO.getCategory())
                &&imageURLValidator(announcementWebDTO.getImageURL()) ){
            return true;
        }else{
            return false;
        }
    }
}
