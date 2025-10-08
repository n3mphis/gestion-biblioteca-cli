package com.mibiblioteca.gestion_biblioteca_cli.service;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.*;
import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import com.mibiblioteca.gestion_biblioteca_cli.model.Prestamo;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import com.mibiblioteca.gestion_biblioteca_cli.repository.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioService usuarioService;
    private final LibroService libroService;

    public PrestamoService(PrestamoRepository prestamoRepository, UsuarioService usuarioService, LibroService libroService) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioService = usuarioService;
        this.libroService = libroService;
    }

    public Prestamo realizarPrestamo(String isbn, String dni, LocalDate fechaDevolucionEstimado) {
        Libro libro = libroService.buscarPorIsbn(isbn);
        Usuario usuario = usuarioService.buscarPorDni(dni);

        if (!libro.isDisponible()) {
            throw new LibroNoDisponibleExcepcion("El libro "+ libro.getTitulo() + " no esta disponible");
        } else {
            libro.setDisponible(false);
            libroService.guardarLibro(libro);
        }

        Prestamo prestamo = new Prestamo(libro, usuario, fechaDevolucionEstimado);

        return prestamoRepository.save(prestamo);
    }

    public Prestamo devolverPrestamo(String isbn) {
        // Buscar el prestamo por fecha de devolucion
        Prestamo prestamo = prestamoRepository.findByLibroIsbnAndFechaDevolucionRealIsnull(isbn)
                .orElseThrow(() -> new LibroYaDevueltoException("El libro ya fue devuelto"));

        // Establecer cuando fue devuelto realmente
        prestamo.setFechaDevolucionReal(LocalDate.now());

        // Establecer que el libro puede ser prestado otra vez
        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);
        libroService.guardarLibro(libro);

        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> prestamos = prestamoRepository.findByFechaDevolucionRealIsNull();

        if (prestamos.isEmpty()) {
            throw new SinPrestamosActivosExcepcion("No hay prestamos activos en el momento");
        }

        return prestamos;
    }

    public List<Prestamo> obtenerPrestamosEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Prestamo> prestamos = prestamoRepository.findByFechaPrestamoBetween(fechaInicio, fechaFin);

        if (prestamos.isEmpty()) {
            throw new PrestamosNoEncontradosExcepcion("No hay prestamos encontrados entre " + fechaInicio + " y " + fechaFin + ".");
        }

        return prestamos;
    }
}
