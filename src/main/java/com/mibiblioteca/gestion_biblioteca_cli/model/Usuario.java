package com.mibiblioteca.gestion_biblioteca_cli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@Getter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String dni;

    public Usuario(String nombre, String apellido, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public String getDniFormateado() {
        if (dni == null || dni.length() < 7 ||dni.length() > 8) {
            return dni; // Devuelve el valor original si es invalido o nulo
        }

        // Uso stringbuilder para construir la cadena de forma eficiente
        StringBuilder sb = new StringBuilder(dni);


        // Se inserta 3 caracteres antes del final
        sb.insert(sb.length() - 3, '.');

        // Se inserta 6 o 5 caracteres antes del final (3 + el punto ya insertado)
        sb.insert(sb.length() - 7, '.');

        return sb.toString();
    }
}
