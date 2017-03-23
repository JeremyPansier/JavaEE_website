package com.aiconoa.trainings.jsf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

public class EmailListValidator implements ConstraintValidator<EmailList, String> {

    private String value;

    @Override
    public void initialize(EmailList constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {

        if (object == null) {
            return true;
        }

        EmailValidator emailValidator = new EmailValidator();
        for (String email : object.split(";")) {
            
            if (!emailValidator.isValid(email, constraintContext)){
              
                return false;
            }
        }
        return true;

    }
}