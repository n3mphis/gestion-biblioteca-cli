package com.mibiblioteca.gestion_biblioteca_cli.service;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.AutorNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.AutorYaRegistradoException;
import com.mibiblioteca.gestion_biblioteca_cli.model.Autor;
import com.mibiblioteca.gestion_biblioteca_cli.repository.AutorRepository;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Autor registrarAutor(String nombre, String apellido) {
        if (autorRepository.findByNombreAndApellido(nombre, apellido).isPresent()) {
            throw new AutorYaRegistradoException("El autor ya está registrado");
        }

        Autor nuevoAutor = new Autor(nombre, apellido);
        return autorRepository.save(nuevoAutor);
    }

    public Autor buscarPorNombreYApellido(String nombre, String apellido) {
        return autorRepository.findByNombreAndApellido(nombre, apellido)
                .orElseThrow(() -> new AutorNoEncontradoException("El autor " + nombre + " " + apellido + " no existe."));
    }
}
