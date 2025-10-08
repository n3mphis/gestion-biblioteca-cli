package com.mibiblioteca.gestion_biblioteca_cli.repository;

import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDni(String dni);
    List<Usuario> findByNombreAndApellido(String nombre, String apellido);
}
