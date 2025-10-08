package com.mibiblioteca.gestion_biblioteca_cli.service;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.AutorNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.LibroNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.LibroYaRegistradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.NoHayLibrosDeEseAutorException;
import com.mibiblioteca.gestion_biblioteca_cli.model.Autor;
import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import com.mibiblioteca.gestion_biblioteca_cli.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibroService {


    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro registrarLibro(Libro nuevoLibro) {
        if (libroRepository.findByIsbn(nuevoLibro.getIsbn()).isPresent()) {
            throw new LibroYaRegistradoException("Ya existe un libro con este ISBN");
        }
        return libroRepository.save(nuevoLibro);
    }

    public Libro buscarPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe."));
    }

    public List<Libro> buscarPorAutor(Autor autor) {
        List<Libro> libros = libroRepository.findByAutor(autor);

        if (libros.isEmpty()) {
            throw new NoHayLibrosDeEseAutorException("No se encontraron libros del autor " + autor.getNombre() + " " + autor.getApellido());
        }
        return libros;
    }
}
