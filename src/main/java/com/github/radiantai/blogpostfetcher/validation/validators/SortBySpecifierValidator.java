package com.github.radiantai.blogpostfetcher.validation.validators;

import com.github.radiantai.blogpostfetcher.validation.constraints.SortBySpecifierConstraint;
import org.jetbrains.annotations.NotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;

public class SortBySpecifierValidator implements
        ConstraintValidator<SortBySpecifierConstraint, String> {

    @Override
    public boolean isValid(@NotNull String sortBy, ConstraintValidatorContext constraintValidatorContext) {
        return switch (sortBy.toLowerCase(Locale.ROOT)) {
            case "id", "reads", "likes", "popularity":
                yield true;
            default:
                yield false;
        };
    }
}
