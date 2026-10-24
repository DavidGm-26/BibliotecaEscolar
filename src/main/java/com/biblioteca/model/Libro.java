package com.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código admite hasta 20 caracteres")
    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título admite hasta 150 caracteres")
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 100, message = "El autor admite hasta 100 caracteres")
    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @Size(max = 50, message = "La categoría admite hasta 50 caracteres")
    @Column(name = "categoria", length = 50)
    private String categoria;

    @NotNull(message = "La cantidad de ejemplares es obligatoria")
    @Min(value = 1, message = "Debe existir al menos un ejemplar")
    @Column(name = "ejemplares", nullable = false)
    private Integer ejemplares;

    @Column(name = "ejemplares_disponibles", nullable = false)
    private int ejemplaresDisponibles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares) {
        this.ejemplares = ejemplares;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void setEjemplaresDisponibles(int ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }
}
