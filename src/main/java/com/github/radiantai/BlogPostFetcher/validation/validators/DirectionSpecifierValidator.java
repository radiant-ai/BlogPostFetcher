package com.github.radiantai.BlogPostFetcher.validation.validators;

import com.github.radiantai.BlogPostFetcher.validation.constraints.DirectionSpecifierConstraint;
import org.jetbrains.annotations.NotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;

public class DirectionSpecifierValidator implements
        ConstraintValidator<DirectionSpecifierConstraint, String> {
    @Override
    public boolean isValid(@NotNull String direction, ConstraintValidatorContext constraintValidatorContext) {
        String lowerCase = direction.toLowerCase(Locale.ROOT);
        return lowerCase.equals("asc") || lowerCase.equals("desc");
    }
}
