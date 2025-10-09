package com.mibiblioteca.gestion_biblioteca_cli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "libros")
// Getter para todas las variables de campo
@Getter
@NoArgsConstructor // requerido por JPA
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    @ManyToOne
    private Autor autor;
    private String Isbn;
    private int añoPublicacion;

    private boolean disponible;

    public Libro(String titulo, Autor autor,int añoPublicacion, String Isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.Isbn = Isbn;
        this.disponible = true;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
