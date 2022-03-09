package com.github.radiantai.BlogPostFetcher.validation.constraints;

import com.github.radiantai.BlogPostFetcher.validation.validators.DirectionSpecifierValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DirectionSpecifierValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectionSpecifierConstraint {
    String message() default "direction parameter is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}