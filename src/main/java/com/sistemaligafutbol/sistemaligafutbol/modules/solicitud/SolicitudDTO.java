package com.sistemaligafutbol.sistemaligafutbol.modules.solicitud;

public class SolicitudDTO {
    private Long id;
    private Long idEquipo;
    private String nombreEquipo; // Para mostrar solo lo indispensable
    private Long idTorneo;
    private String nombreTorneo;
    private Boolean inscripcionEstatus;
    private Boolean resolucion;
    private Boolean pendiente;

    //Constructor


    public SolicitudDTO() {
    }

    public SolicitudDTO(Long id, Long idEquipo, String nombreEquipo, Long idTorneo, String nombreTorneo, Boolean inscripcionEstatus, Boolean resolucion, Boolean pendiente) {
        this.id = id;
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.idTorneo = idTorneo;
        this.nombreTorneo = nombreTorneo;
        this.inscripcionEstatus = inscripcionEstatus;
        this.resolucion = resolucion;
        this.pendiente = pendiente;
    }

    //Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public Boolean getInscripcionEstatus() {
        return inscripcionEstatus;
    }

    public void setInscripcionEstatus(Boolean inscripcionEstatus) {
        this.inscripcionEstatus = inscripcionEstatus;
    }

    public Boolean getResolucion() {
        return resolucion;
    }

    public void setResolucion(Boolean resolucion) {
        this.resolucion = resolucion;
    }

    public Boolean getPendiente() {
        return pendiente;
    }

    public void setPendiente(Boolean pendiente) {
        this.pendiente = pendiente;
    }
}

