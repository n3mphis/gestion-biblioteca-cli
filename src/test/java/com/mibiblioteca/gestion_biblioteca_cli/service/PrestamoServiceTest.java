package com.mibiblioteca.gestion_biblioteca_cli.service;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.LibroNoDisponibleExcepcion;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.LibroYaDevueltoException;
import com.mibiblioteca.gestion_biblioteca_cli.model.Autor;
import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import com.mibiblioteca.gestion_biblioteca_cli.model.Prestamo;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import com.mibiblioteca.gestion_biblioteca_cli.repository.PrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @InjectMocks
    private PrestamoService prestamoService;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private LibroService libroService;

    @Mock
    private UsuarioService usuarioService;

    private Autor autorPrueba;
    private Libro libroDisponible;
    private Libro libroNoDisponible;
    private Usuario usuarioValido;
    private LocalDate fechaDevolucionEstimada;

    @BeforeEach
    void setUp() {
        // Inicializacion de datos
        autorPrueba = new Autor("Gabriel", "García Márquez");

        libroDisponible = new Libro("Cien Años de SOledad", autorPrueba, 1967, "978-3161484100");
        libroDisponible.setDisponible(true);

        libroNoDisponible = new Libro("Clean Code", autorPrueba, 2001, "988-57815454");
        libroNoDisponible.setDisponible(false);

        usuarioValido = new Usuario("Ana", "Gomez", "45123456");

        fechaDevolucionEstimada = LocalDate.now().plusDays(7);
    }

    //-------------------------------------------------------------------------------
    // TEST 1: Prestamo Exitoso
    //------------------------------------------------------------------------------

    @Test
    @DisplayName("Debe_RealizarPrestamo_Y_ActualizarDisponibilidad_Cuando_LibroDisponible")
    void debeRealizarPrestamoYActualizarDisponibilidadCuandoLibroEstaDisponible() {
        // ARRANGE
        when(libroService.buscarPorIsbn(libroDisponible.getIsbn())).thenReturn(libroDisponible);
        when(usuarioService.buscarPorDni(usuarioValido.getDni())).thenReturn(usuarioValido);

        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Prestamo resultado = prestamoService.realizarPrestamo(
                libroDisponible.getIsbn(),
                usuarioValido.getDni(),
                fechaDevolucionEstimada
        );

        // ASSERT
        assertFalse(libroDisponible.isDisponible(), "El libro debe estar marcado como NO disponible despues del prestamo");

        verify(libroService, times(1)).guardarLibro(libroDisponible);
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(libroDisponible, resultado.getLibro(), "El prestamo debe estar asociado al libro correcto.");
        assertEquals(usuarioValido, resultado.getUsuario(), "El prestamo debe estar asociado al usuario correcto.");
        assertEquals(fechaDevolucionEstimada, resultado.getFechaDevolucionEstimada(), "La fecha estimada debe ser la definida");
        assertNull(resultado.getFechaDevolucionReal(), "La fecha de devolucion REAL debe ser nula al inicio.");
        assertEquals(LocalDate.now(), resultado.getFechaPrestamo(), "La fecha de prestamo debe ser asignada (LocalDate.now()).");
    }

    //----------------------------------------------------------------
    // TEST 2: Fallo de Prestamo (Libro no disponible
    //---------------------------------------------------------------
    @Test
    @DisplayName("Debe_LanzarExcepcion_Cuando_LibroNoDisponible")
    void debeLanzarExcepcionCuandoLibroNoDisponible() {
        // ARRANGE
        when(libroService.buscarPorIsbn(libroNoDisponible.getIsbn())).thenReturn(libroNoDisponible);

        // Simular el usuario para evitar NPE (NullPointerException)
        when(usuarioService.buscarPorDni(anyString())).thenReturn(usuarioValido);

        // ACT & ASSERT
        assertThrows(LibroNoDisponibleExcepcion.class, () ->
        prestamoService.realizarPrestamo(
            libroNoDisponible.getIsbn(),
            usuarioValido.getDni(),
            fechaDevolucionEstimada
    ));

        // Verify: Nada debe ser guardado
        verify(libroService, never()).guardarLibro(any(Libro.class));
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    //---------------------------------------------------------------
    // TEST 3: Devolucion Exitosa
    //--------------------------------------------------------------
    @Test
    @DisplayName("Debe_DevolverLibro_Y_MarcarComoDisponible_Cuando_PrestamoActivo")
    void debeDevolverLibroYActualizarEstadoCuandoPrestamoActivoExsite() {
        // ARRANGE
        Libro libroPrestado = new Libro("Clean Code", autorPrueba, 2001, "999-78452698");
        libroPrestado.setDisponible(false);

        Prestamo prestamoActivo = new Prestamo(libroPrestado, usuarioValido, fechaDevolucionEstimada);

        // 1. Simular la búsqueda: Cuando se busque el préstamo activo por ISBN, devolverlo.
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionRealIsnull(libroPrestado.getIsbn()))
                .thenReturn(Optional.of(prestamoActivo));

        // 2. Simular el guardado: Devolver el objeto que Mockito recibió (el ya modificado)
        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // ACT (Ejecución del metodo)
        Prestamo resultado = prestamoService.devolverPrestamo(libroPrestado.getIsbn());


        // ASSERT (Verificación de resultados y llamadas)

        // 1. Verificar el Libro: El libro debe estar disponible.
        assertTrue(libroPrestado.isDisponible(), "El libro debe estar marcado como DISPONIBLE después de la devolución.");

        // 2. Verificar el Préstamo: La fecha de devolución REAL debe estar seteada.
        assertNotNull(resultado.getFechaDevolucionReal(), "La fecha de devolución REAL no debe ser nula.");
        assertEquals(LocalDate.now(), resultado.getFechaDevolucionReal(), "La fecha de devolución REAL debe ser la fecha actual.");

        // 3. Verificación de Llamadas Transaccionales
        // Verificar que el libro actualizado fue guardado
        verify(libroService, times(1)).guardarLibro(libroPrestado);

        // Verificar que el registro de préstamo actualizado fue guardado
        verify(prestamoRepository, times(1)).save(prestamoActivo);
    }

    @Test
    @DisplayName("Debe_LanzarLibroYaDevueltoException_Cuando_NoHayPrestamoActivo")
    void debeLanzarLibroYaDevueltoExceptionCuandoNoHayPrestamoActivo() {
        String isbnSinPrestamoActivo = "999-9999999999999";

        // 1. Simular la busqueda: El repositorio devuelve un Optional vacío (no hay prestamo activo)
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionRealIsnull(isbnSinPrestamoActivo))
                .thenReturn(Optional.empty()); // <-- Simular el fallo de busqueda

        // ACT & ASSERT

        // Verifico que se lanza la excepcion correcta al intentar devolver el libro
        assertThrows(LibroYaDevueltoException.class, () ->
                prestamoService.devolverPrestamo(isbnSinPrestamoActivo));

        // VERIFICACION DE LLAMADAS (Asegurar que NADA fue modificado)

        // 1. El LibroService NO debe ser llamada para guardar/actualizar
        verify(libroService, never()).guardarLibro(any(Libro.class));

        // 2. El PrestamoRepository NO debe ser llamado para guardar/actualizar
        verify(prestamoRepository, never()).save(any(Prestamo.class));

        // 3. El metodo de busqueda DEBE haber sido llamado
        verify(prestamoRepository, times(1)).findByLibroIsbnAndFechaDevolucionRealIsnull(isbnSinPrestamoActivo);
    }
}
