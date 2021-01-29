package com.lovemesomecoding.utils.validator;

import java.time.LocalDate;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureStartDateValidator implements ConstraintValidator<FutureStartDate, LocalDate> {

	@Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		
        return value.isAfter(LocalDate.now());
	}

}
