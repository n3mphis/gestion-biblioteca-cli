package com.mibiblioteca.gestion_biblioteca_cli.repository;




import com.mibiblioteca.gestion_biblioteca_cli.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
