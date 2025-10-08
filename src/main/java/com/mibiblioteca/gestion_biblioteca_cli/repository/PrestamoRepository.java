package com.mibiblioteca.gestion_biblioteca_cli.repository;

import com.mibiblioteca.gestion_biblioteca_cli.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Optional<Prestamo> findByLibroIsbnAndFechaDevolucionRealIsnull(String isbn);

    List<Prestamo> findByFechaDevolucionRealIsNull();
    List<Prestamo> findByFechaPrestamoBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
