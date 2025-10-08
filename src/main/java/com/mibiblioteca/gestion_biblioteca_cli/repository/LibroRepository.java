package com.mibiblioteca.gestion_biblioteca_cli.repository;

import com.mibiblioteca.gestion_biblioteca_cli.model.Autor;
import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);
    List<Libro> findByAutor(Autor autor);
}
