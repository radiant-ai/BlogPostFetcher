package com.github.radiantai.blogpostfetcher.validation.validators;

import com.github.radiantai.blogpostfetcher.validation.constraints.CommaSeparatedListOfStringsConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommaSeparatedListOfStringsValidator implements
        ConstraintValidator<CommaSeparatedListOfStringsConstraint, String> {

    @Override
    public boolean isValid(String tagList, ConstraintValidatorContext constraintValidatorContext) {
        return tagList == null || tagList.matches("^([\\w]+)(,[\\w]+)*$");
    }
}
