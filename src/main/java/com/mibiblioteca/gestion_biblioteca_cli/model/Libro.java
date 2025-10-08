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
    private String autor;
    private String ISBN;
    private int añoPublicacion;

    @Setter
    private boolean disponible;

    public Libro(String titulo, String autor,int añoPublicacion, String ISBN) {
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.ISBN = ISBN;
        this.disponible = true;
    }
}
