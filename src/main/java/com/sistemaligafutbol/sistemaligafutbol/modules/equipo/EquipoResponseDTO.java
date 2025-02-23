package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

public class EquipoResponseDTO {
    private Long id;
    private String nombreEquipo;
    private String logoEquipo;
    private Long idDueno;
    private String correoDueno;
    private String nombreDueno;
    private String imagenDueno;
    private Long idCampo;
    private String nombreCampo;
    private String direccionCampo;
    private String latitudCampo;
    private String longitudCampo;

    public EquipoResponseDTO(Equipo equipo) {
        this.id = equipo.getId();
        this.nombreEquipo = equipo.getNombreEquipo();
        this.logoEquipo = equipo.getLogo();

        // Extraemos datos del dueño
        if (equipo.getDueno() != null) {
            this.idDueno = equipo.getDueno().getId();
            this.nombreDueno = equipo.getDueno().getNombreCompleto();
            this.imagenDueno = equipo.getDueno().getImagenUrl();
            if (equipo.getDueno().getUsuario() != null) {
                this.correoDueno = equipo.getDueno().getUsuario().getEmail();
            }
        }

        // Extraemos datos del campo
        if (equipo.getCampo() != null) {
            this.idCampo = equipo.getCampo().getId();
            this.nombreCampo = equipo.getCampo().getNombre();
            this.direccionCampo = equipo.getCampo().getDireccion();
            this.latitudCampo = String.valueOf(equipo.getCampo().getLatitud());
            this.longitudCampo = String.valueOf(equipo.getCampo().getLongitud());
        }
    }

    // Getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getLogoEquipo() {
        return logoEquipo;
    }

    public void setLogoEquipo(String logoEquipo) {
        this.logoEquipo = logoEquipo;
    }

    public Long getIdDueno() {
        return idDueno;
    }

    public void setIdDueno(Long idDueno) {
        this.idDueno = idDueno;
    }

    public String getCorreoDueno() {
        return correoDueno;
    }

    public void setCorreoDueno(String correoDueno) {
        this.correoDueno = correoDueno;
    }

    public String getNombreDueno() {
        return nombreDueno;
    }

    public void setNombreDueno(String nombreDueno) {
        this.nombreDueno = nombreDueno;
    }

    public String getImagenDueno() {
        return imagenDueno;
    }

    public void setImagenDueno(String imagenDueno) {
        this.imagenDueno = imagenDueno;
    }

    public Long getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(Long idCampo) {
        this.idCampo = idCampo;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public String getDireccionCampo() {
        return direccionCampo;
    }

    public void setDireccionCampo(String direccionCampo) {
        this.direccionCampo = direccionCampo;
    }

    public String getLatitudCampo() {
        return latitudCampo;
    }

    public void setLatitudCampo(String latitudCampo) {
        this.latitudCampo = latitudCampo;
    }

    public String getLongitudCampo() {
        return longitudCampo;
    }

    public void setLongitudCampo(String longitudCampo) {
        this.longitudCampo = longitudCampo;
    }
}

