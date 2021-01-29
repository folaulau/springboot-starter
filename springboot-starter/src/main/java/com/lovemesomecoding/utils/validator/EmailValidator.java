package com.lovemesomecoding.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.lovemesomecoding.utils.ValidationUtils;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() <= 0) {
            return false;
        }
        return ValidationUtils.isValidEmailFormat(value);
    }

}
