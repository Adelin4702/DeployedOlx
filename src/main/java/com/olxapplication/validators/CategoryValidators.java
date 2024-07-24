package com.olxapplication.validators;

import com.olxapplication.constants.CategoryMessages;
import com.olxapplication.dtos.CategoryDTO;
import com.olxapplication.dtos.CategoryDetailsDTO;
import com.olxapplication.exception.PatternNotMathcedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoryValidators {
    public boolean nameValidator(String name) throws PatternNotMathcedException {
        Pattern pattern = Pattern.compile("^[-a-zA-Z ]+$");
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            throw new PatternNotMathcedException(CategoryMessages.NAME_PATTERN_NOT_MATCHED);
        }
    }



    public boolean categoryDtoValidator(CategoryDTO categoryDTO) throws PatternNotMathcedException{
        if(nameValidator(categoryDTO.getCategoryName())){
            return true;
        }else{
            return false;
        }
    }
    public boolean categoryDetailsDtoValidator(CategoryDetailsDTO categoryDTO) throws PatternNotMathcedException{

        if(nameValidator(categoryDTO.getCategoryName())){
            return true;
        }else{
            return false;
        }
    }

}
