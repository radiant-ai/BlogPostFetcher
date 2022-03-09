package com.github.radiantai.BlogPostFetcher.validation.validators;

import com.github.radiantai.BlogPostFetcher.validation.constraints.CommaSeparatedListOfStringsConstraint;
import org.jetbrains.annotations.NotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommaSeparatedListOfStringsValidator implements
        ConstraintValidator<CommaSeparatedListOfStringsConstraint, String> {

    @Override
    public boolean isValid(@NotNull String tagList, ConstraintValidatorContext constraintValidatorContext) {
        return (tagList.matches("^(\\w+)(,\\w+)*$"));
    }
}
