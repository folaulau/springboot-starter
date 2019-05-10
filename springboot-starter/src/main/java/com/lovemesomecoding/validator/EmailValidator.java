package com.lovemesomecoding.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.lovemesomecoding.utils.ValidationUtils;

public class EmailValidator implements ConstraintValidator<Email, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return ValidationUtils.isValidEmail(value);
	}

}
