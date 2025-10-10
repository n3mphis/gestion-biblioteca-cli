package com.mibiblioteca.gestion_biblioteca_cli.service;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.DniNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.FormatoDniInvalidoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.UsuarioNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.UsuarioYaRegistradoException;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import com.mibiblioteca.gestion_biblioteca_cli.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(String nombre, String apellido, String dni) {
        String dniLimpio = dni.replaceAll("[.\\-,]", "");
        validadorDni(dni);
        if (usuarioRepository.findByDni(dni).isPresent()) {
            throw new UsuarioYaRegistradoException("El usuario ya está registrado\n");
        }
        Usuario nuevoUsuario = new Usuario(nombre, apellido, dniLimpio);
        return usuarioRepository.save(nuevoUsuario);
    }

    public Usuario buscarPorDni(String dni) {
        validadorDni(dni);
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

    private void validadorDni(String dni) {
        String dniLimpio = dni.replaceAll("[.\\-,]", "");
        String DNI_REGEX = "^\\d{7,8}$";

        if (!dniLimpio.matches(DNI_REGEX)){
            throw new FormatoDniInvalidoException(
                    "El formato del DNI es incorrecto. Debe contener solo 7 u 8 dígitos numéricos"
            );
        }
    }
}
