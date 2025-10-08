package com.mibiblioteca.gestion_biblioteca_cli.service;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.DniNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.UsuarioNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.UsuarioYaRegistradoException;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import com.mibiblioteca.gestion_biblioteca_cli.repository.UsuarioRepository;

import java.util.List;

public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario nuevoUsuario) {
        if (usuarioRepository.findByDni(nuevoUsuario.getDni()).isPresent()) {
            throw new UsuarioYaRegistradoException("El usuario ya está registrado");
        }
        return usuarioRepository.save(nuevoUsuario);
    }

    public Usuario buscarPorDni(String dni) {
        return usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new DniNoEncontradoException("El usuario con DNI " + dni + " no existe."));
    }

    public List<Usuario> buscarPorNombreYApellido(String nombre, String apellido) {
        List<Usuario> usuarios = usuarioRepository.findByNombreAndApellido(nombre, apellido);

        if (usuarios.isEmpty()) {
            throw new UsuarioNoEncontradoException("No se encontró el usuario " + nombre + " " + apellido + ".");
        }

        return usuarios;
    }
}
