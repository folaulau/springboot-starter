package com.lovemesomecoding.utils.validator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotInvalidAmountValidator implements ConstraintValidator<NotInvalidAmount, Double> {

	@Override
	public boolean isValid(Double amount, ConstraintValidatorContext context) {

		if (amount == null) {
			return false;
		}
		
		return true;
	}

}
