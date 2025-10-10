package com.mibiblioteca.gestion_biblioteca_cli.repository;

import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import com.mibiblioteca.gestion_biblioteca_cli.model.Prestamo;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Optional<Prestamo> findByLibroIsbnAndFechaDevolucionRealIsNull(String isbn);

    List<Prestamo> findByFechaDevolucionRealIsNull();
    List<Prestamo> findByFechaPrestamoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT p FROM Prestamo p WHERE p.fechaDevolucionReal IS NULL AND p.fechaDevolucionEstimada < CURRENT_DATE")
    List<Prestamo> findPrestamosVencidos();

    @Query("SELECT p.libro FROM Prestamo p GROUP BY p.libro ORDER BY COUNT(p) DESC LIMIT 1")
    Optional<Libro> findLibroMasPrestado();

    @Query("SELECT p.usuario FROM Prestamo p GROUP BY p.usuario ORDER BY COUNT(p) DESC LIMIT 1")
    Optional<Usuario> findUsuariosConMasPrestamos();
}
