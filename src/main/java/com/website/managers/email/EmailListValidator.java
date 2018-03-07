package com.website.managers.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

public class EmailListValidator implements ConstraintValidator<EmailList, String> {

	@SuppressWarnings("unused")
	private String value;

	@Override
	public void initialize(final EmailList constraintAnnotation) {
		this.value = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(final String object, final ConstraintValidatorContext constraintContext) {
		if (object == null) {
			return true;
		}

		final EmailValidator emailValidator = new EmailValidator();
		for (final String email : object.split(";")) {

			if (!emailValidator.isValid(email, constraintContext)) {
				return false;
			}
		}
		return true;
	}
}
