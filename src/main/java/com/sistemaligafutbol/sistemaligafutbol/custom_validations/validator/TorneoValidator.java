package com.sistemaligafutbol.sistemaligafutbol.custom_validations.validator;

import com.sistemaligafutbol.sistemaligafutbol.custom_validations.anotation.ValidTorneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.DayOfWeek;

public class TorneoValidator implements ConstraintValidator<ValidTorneo, TorneoDTO> {

    @Override
    public boolean isValid(TorneoDTO torneo, ConstraintValidatorContext context) {
        boolean valid = true;

        // Verificar que la fecha de inicio sea un domingo
        if (torneo.getFechaInicio() != null && torneo.getFechaInicio().getDayOfWeek() != DayOfWeek.SUNDAY) {
            context.buildConstraintViolationWithTemplate("La fecha de inicio del torneo debe ser un domingo")
                    .addPropertyNode("fechaInicio")
                    .addConstraintViolation();
            valid = false;
        }

        // Verificar que maxEquipos es mayor que minEquipos
        if (torneo.getMaxEquipos() < torneo.getMinEquipos()) {
            context.buildConstraintViolationWithTemplate("El número máximo de equipos debe ser mayor al mínimo")
                    .addPropertyNode("maxEquipos")
                    .addConstraintViolation();
            valid = false;
        }

        // Verificar que maxEquipos, minEquipos y equiposLiguilla sean pares
        if (torneo.getMaxEquipos() % 2 != 0) {
            context.buildConstraintViolationWithTemplate("El número máximo de equipos debe ser par")
                    .addPropertyNode("maxEquipos")
                    .addConstraintViolation();
            valid = false;
        }
        if (torneo.getMinEquipos() % 2 != 0) {
            context.buildConstraintViolationWithTemplate("El número mínimo de equipos debe ser par")
                    .addPropertyNode("minEquipos")
                    .addConstraintViolation();
            valid = false;
        }
        if (torneo.getEquiposLiguilla() % 2 != 0) {
            context.buildConstraintViolationWithTemplate("El número de equipos en liguilla debe ser par")
                    .addPropertyNode("equiposLiguilla")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}

