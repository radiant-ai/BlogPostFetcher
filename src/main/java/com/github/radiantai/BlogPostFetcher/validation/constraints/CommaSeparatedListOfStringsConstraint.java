package com.github.radiantai.BlogPostFetcher.validation.constraints;

import com.github.radiantai.BlogPostFetcher.validation.validators.CommaSeparatedListOfStringsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = CommaSeparatedListOfStringsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CommaSeparatedListOfStringsConstraint {
    String message() default "Invalid list of tags";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}