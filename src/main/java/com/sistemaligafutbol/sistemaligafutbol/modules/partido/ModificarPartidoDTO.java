package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class ModificarPartidoDTO {

    @NotNull(message = "La fecha del partido no puede ser nula.")
    @Future(message = "La fecha debe ser futura.")
    private LocalDate nuevaFecha;

    @NotNull(message = "La hora del partido no puede ser nula.")
    private LocalTime nuevaHora;

    @NotNull(message = "El ID de la cancha no puede ser nulo.")
    private Long idCancha;

    @NotNull(message = "El ID del árbitro no puede ser nulo.")
    private Long idArbitro;

    // Getters and Setters
    public LocalDate getNuevaFecha() {
        return nuevaFecha;
    }

    public void setNuevaFecha(LocalDate nuevaFecha) {
        this.nuevaFecha = nuevaFecha;
    }

    public LocalTime getNuevaHora() {
        return nuevaHora;
    }

    public void setNuevaHora(LocalTime nuevaHora) {
        this.nuevaHora = nuevaHora;
    }

    public Long getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(Long idCancha) {
        this.idCancha = idCancha;
    }

    public Long getIdArbitro() {
        return idArbitro;
    }

    public void setIdArbitro(Long idArbitro) {
        this.idArbitro = idArbitro;
    }
}

