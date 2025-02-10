package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Arbitro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombreCompleto;

    private String imagenUrl;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    //Constructores

    public Arbitro() {
    }

    public Arbitro(Long id, String nombreCompleto, String imagenUrl, Usuario usuario) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.imagenUrl = imagenUrl;
        this.usuario = usuario;
    }

    // Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(@NotBlank String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
