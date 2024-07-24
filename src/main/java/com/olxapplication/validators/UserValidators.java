package com.olxapplication.validators;

import com.olxapplication.constants.UserMessages;
import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.exception.PatternNotMathcedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidators {
    public boolean nameValidator(String name) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^[-a-zA-Z ]+$");
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new PatternNotMathcedException(UserMessages.NAME_PATTERN_NOT_MATCHED);
        }
    }

    public boolean emailValidator(String email) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^[-\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);

        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new PatternNotMathcedException(UserMessages.EMAIL_PATTERN_NOT_MATCHED);
        }
    }

    public boolean passwordValidator(String pass) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^\\w{6,20}$");
        Matcher matcher = pattern.matcher(pass);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new PatternNotMathcedException(UserMessages.PASSWORD_PATTERN_NOT_MATCHED);
        }
    }

    public boolean roleValidator(String role) throws PatternNotMathcedException{
        Pattern pattern = Pattern.compile("^(admin|user)$");
        Matcher matcher = pattern.matcher(role);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new PatternNotMathcedException(UserMessages.PASSWORD_PATTERN_NOT_MATCHED);
        }
    }



    public boolean userDtoValidator(UserDetailsDTO userDetailsDTO) throws PatternNotMathcedException{

        if(nameValidator(userDetailsDTO.getFirstName()) && nameValidator(userDetailsDTO.getLastName()) && roleValidator(userDetailsDTO.getRole()) &&
                emailValidator(userDetailsDTO.getEmail()) && passwordValidator(userDetailsDTO.getPassword())){

            return true;

        } else {
            return false;
        }
    }
}
