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

    public Autor registrarAutor(Autor autor) {
        if (autorRepository.findByNombreAndApellido(autor.getNombre(), autor.getApellido()).isPresent()) {
            throw new AutorYaRegistradoException("El autor ya está registrado");
        }

        return autorRepository.save(autor);
    }

    public Autor buscarPorNombreYApellido(String nombre, String apellido) {
        return autorRepository.findByNombreAndApellido(nombre, apellido)
                .orElseThrow(() -> new AutorNoEncontradoException("El autor " + nombre + " " + apellido + " no existe."));
    }
}
