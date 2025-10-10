package com.mibiblioteca.gestion_biblioteca_cli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
@Getter
@NoArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEstimada;

    @Setter
    private LocalDate fechaDevolucionReal;

    @ManyToOne
    private Libro libro;

    @ManyToOne
    private Usuario usuario;

    public Prestamo(Libro libro, Usuario usuario, LocalDate fechaDevolucionEstimada) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;

        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucionReal = null;
    }
}
