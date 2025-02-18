package com.sistemaligafutbol.sistemaligafutbol.custom_validations.anotation;

import com.sistemaligafutbol.sistemaligafutbol.custom_validations.validator.TorneoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TorneoValidator.class)
public @interface ValidTorneo {
    String message() default "Datos del torneo no válidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

