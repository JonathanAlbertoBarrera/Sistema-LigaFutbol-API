package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import java.time.LocalDate;
import java.time.LocalTime;

public class PartidoModificarDTO {
    private LocalDate fechaPartido;
    private LocalTime hora;
    private Long idCancha;
    private Long idArbitro;

    // Getters y Setters
    public LocalDate getFechaPartido() { return fechaPartido; }
    public void setFechaPartido(LocalDate fechaPartido) { this.fechaPartido = fechaPartido; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public Long getIdCancha() { return idCancha; }
    public void setIdCancha(Long idCancha) { this.idCancha = idCancha; }

    public Long getIdArbitro() { return idArbitro; }
    public void setIdArbitro(Long idArbitro) { this.idArbitro = idArbitro; }
}
