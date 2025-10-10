package com.mibiblioteca.gestion_biblioteca_cli.controller;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.*;
import com.mibiblioteca.gestion_biblioteca_cli.model.Autor;
import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import com.mibiblioteca.gestion_biblioteca_cli.model.Prestamo;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import com.mibiblioteca.gestion_biblioteca_cli.service.AutorService;
import com.mibiblioteca.gestion_biblioteca_cli.service.LibroService;
import com.mibiblioteca.gestion_biblioteca_cli.service.PrestamoService;
import com.mibiblioteca.gestion_biblioteca_cli.service.UsuarioService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Controller
public class MenuPrincipalCLI {
    private final AutorService autorService;
    private final LibroService libroService;
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private final Scanner sc = new Scanner(System.in);
    //private boolean salir = false;

    public MenuPrincipalCLI(AutorService autorService, LibroService libroService, PrestamoService prestamoService, UsuarioService usuarioService) {
        this.autorService = autorService;
        this.libroService = libroService;
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
    }

    public void iniciarMenu() {
        while (true) {
            System.out.println("""
                ----------------------------------------------------\n
                \uD83D\uDCD6 ¡Bienvenido al Gestor De Biblioteca!
                Las opciones son:
                
                1_ Registro y Gestión
                2_ Préstamos
                3_ Reportes y Estadísticas
                
                4_ SALIR
                """);

            System.out.print("-> ");
            int seleccion = sc.nextInt();
            sc.nextLine();

            switch (seleccion) {
                case 1:
                    registroYGestion();
                    break;
                case 2:
                    prestamos();
                    break;
                case 3:
                    reportesYEstadisticas();
                    break;
                case 4:
                    System.out.println("Hasta la próxima! \uD83D\uDC4B");
                    System.exit(0);
                    break;
                default:
                    System.out.println("No es una elección válida");
            }
        }
    }

    //-----------------------------------
    // Maneja toda la sección de Registro y Gestión
    private void registroYGestion() {
        System.out.println("""
                \n----------------------------------------------------
                --- REGISTRO Y GESTIÓN ---
                """);
        while (true) {
            System.out.println("""
                1. Registrar Nuevo Usuario
                2. Buscar Usuario por Nombre y Apellido
                3. Registrar Nuevo Autor
                4. Registrar Nuevo Libro
                5. Buscar Usuario por DNI
                6. Buscar Libro por ISBN
                
                7. Volver al menu principal
                """);
            System.out.println("Elija su opción: ");
            System.out.print("-> ");
            int seleccion = sc.nextInt();
            sc.nextLine();

            switch (seleccion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    buscarUsuarioPorNombreYApellido();
                    break;
                case 3:
                    registrarAutor();
                    break;
                case 4:
                    registrarNuevoLibro();
                    break;
                case 5:
                    buscarUsuarioPorDni();
                    break;
                case 6:
                    buscarLibroPorIsbn();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("No es una opción válida");
            }
        }

    }

    private void registrarUsuario() {
        System.out.println("\n--- REGISTRAR NUEVO USUARIO ---");
        try {
            System.out.println("Ingrese su nombre:");
            System.out.print("-> ");
            String nombre = sc.nextLine().trim();

            System.out.println("Ingrese su apellido:");
            System.out.print("-> ");
            String apellido = sc.nextLine().trim();

            System.out.println("Ingrese su DNI:");
            System.out.print("-> ");
            String dni = sc.nextLine().trim();
            String dniLimpio = dni.replaceAll("[.\\-,]", "");

            usuarioService.registrarUsuario(nombre, apellido, dniLimpio);

            System.out.println("\n✅ Usuario " + nombre + " "+ apellido + " registrado con éxito!\n");
//            registroYGestion();
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("❌ Error de registro: " + e.getMessage());
        } catch (FormatoDniInvalidoException e) {
            System.out.println("❌ Error de formato: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: No se pudo completar el registro: " + e.getMessage());
        }
    }

    private void buscarUsuarioPorNombreYApellido() {
        System.out.println("\n--- BUSCAR USUARIO POR NOMBRE Y APELLIDO ---");
        try {
            System.out.println("Ingrese el nombre del usuario:");
            System.out.print("-> ");
            String nombreUsuario = sc.nextLine().trim();

            System.out.println("Ingrese el apellido del usuario:");
            System.out.print("-> ");
            String apellidoUsuario = sc.nextLine().trim();

            List<Usuario> usuariosNombreYApellido = usuarioService.buscarPorNombreYApellido(nombreUsuario, apellidoUsuario);

            for (Usuario usuario : usuariosNombreYApellido) {
                System.out.println("\n    Nombre:    " + usuario.getNombre());
                System.out.println("    Apellido:    " + usuario.getApellido());
                System.out.println("    DNI:    " + usuario.getDniFormateado() + "\n");
            }
        } catch (UsuarioNoEncontradoException e) {
            System.out.println("❌ Error! " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }

    }

    private void  registrarAutor() {
        System.out.println("\n--- REGISTRAR NUEVO AUTOR ---");
        try {
            System.out.println("Ingrese el nombre del autor:");
            System.out.print("-> ");
            String nombre = sc.nextLine().trim();

            System.out.println("Ingrese el apellido del autor:");
            System.out.print("-> ");
            String apellido = sc.nextLine().trim();

            autorService.registrarAutor(nombre, apellido);
            System.out.println("\n✅ Autor: " + nombre + " " + apellido + " registrado con exito!\n");
        } catch (AutorYaRegistradoException e) {
            System.out.println("❌ Error! " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }
    }

    private void registrarNuevoLibro() {
        System.out.println("\n--- REGISTRO DE NUEVO LIBRO ---");

        try {
            System.out.println("Ingrese el título del Libro:");
            System.out.print("-> ");
            String nombreTitulo = sc.nextLine().trim();

            System.out.println("Ingrese el nombre del Autor:");
            System.out.print("-> ");
            String nombreAutor = sc.nextLine().trim();

            System.out.println("Ingrese el apellido del Autor:");
            System.out.print("-> ");
            String apellidoAutor = sc.nextLine().trim();

            System.out.println("Ingrese el año de publicación del libro:");
            System.out.print("-> ");
            int añoPublicacion = sc.nextInt();
            sc.nextLine();

            System.out.println("Ingrese ISBN del libro:");
            System.out.print("-> ");
            String isbn = sc.nextLine().trim();

            Autor autor = autorService.buscarPorNombreYApellido(nombreAutor, apellidoAutor);
            Libro libro = new Libro(nombreTitulo, autor, añoPublicacion, isbn);

            libroService.registrarLibro(libro);

            System.out.println("\n✅ Éxito! El libro: " + libro.getTitulo() + " fue registrado exitosamente!\n");
        } catch (InputMismatchException e) {
            System.out.println("❌ Error: Por favor, ingrese un numero en año de publicación. " + e.getMessage() + "\n");
            sc.nextLine(); // limpiar buffer para que no haya un loop infinito
        } catch (AutorNoEncontradoException e) {
            System.out.println("❌ Error: No se encontró el autor. " + e.getMessage() + "\n");
        }
        catch (LibroYaRegistradoException e) {
            System.out.println("❌ Error!" + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }
    }

    private void buscarUsuarioPorDni() {
        System.out.println("\n--- BUSCAR USUARIO POR DNI ---");

        try {
            System.out.println("Ingrese el DNI del usuario:");
            System.out.print("-> ");
            String dni = sc.nextLine().trim();
            String dniLimpio = dni.replaceAll("[.\\-,]", "");

            Usuario usuarioEncontrado = usuarioService.buscarPorDni(dniLimpio);

            System.out.println("\n✅ Usuario encontrado: ");
            System.out.println("    Nombre:    " + usuarioEncontrado.getNombre());
            System.out.println("    Apellido:    " + usuarioEncontrado.getApellido());
            System.out.println("    DNI:    " + usuarioEncontrado.getDniFormateado() + "\n");

        } catch (UsuarioNoEncontradoException e) {
            System.out.println("❌" + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error! " + e.getMessage() + "\n");
        }
    }

    private void buscarLibroPorIsbn() {
        System.out.println("\n--- BUSCAR LIBRO POR ISBN ---");

        try {
            System.out.println("Ingrese el ISBN del libro:");
            System.out.print("-> ");
            String isbn = sc.nextLine().trim();

            Libro libroEncontrado = libroService.buscarPorIsbn(isbn);

            System.out.println("✅ Libro Encontrado!");
            System.out.println("\n    Título:    " + libroEncontrado.getTitulo());
            System.out.println("    Autor:   " + libroEncontrado.getAutor().getNombre() + " " + libroEncontrado.getAutor().getApellido());
            System.out.println("    Año de publicación:    " +libroEncontrado.getAñoPublicacion());

            String estado = libroEncontrado.isDisponible() ? "\uD83D\uDFE2 DISPONIBLE" : "\uD83D\uDD34 PRESTADO";
            System.out.println("    Estado:    " + estado + "\n");
        } catch (LibroNoEncontradoException e) {
            System.out.println("❌" + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() +"\n");
        }
    }
    //---------------------------------

    //---------------------------------
    // Maneja toda la sección de Préstamos
    private void prestamos() {
        System.out.println("""
                \n----------------------------------------------------
                --- PRESTAMOS ---
                """);
        while (true) {
            System.out.println("""
                    1. Realizar Préstamo
                    2. Devolver Préstamo
                    3. Obtener Prestamos Activos
                    4. Obtener Prestamos entre fechas
                    5. Volver al menu principal
                    """);

            System.out.println("Elija su opción:");
            System.out.print("-> ");
            int elección = sc.nextInt();
            sc.nextLine();

            switch (elección) {
                case 1:
                    realizarPrestamo();
                    break;
                case 2:
                    devolverPrestamo();
                    break;
                case 3:
                    obtenerPrestamosActivos();
                    break;
                case 4:
                    obtenerPrestamosEntreFechas();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opción no válida. Elija otra opción!");
                    break;
            }
        }
    }

    private void realizarPrestamo() {
        System.out.println("\n--- REALIZAR PRESTAMO ---");
        try {
            System.out.println("Ingrese ISBN del libro");
            System.out.print("-> ");
            String isbn = sc.nextLine().trim();

            System.out.println("Ingrese su DNI");
            System.out.print("-> ");
            String dni = sc.nextLine().trim();
            String dniLimpio = dni.replaceAll("[.\\-,]", "");

            LocalDate fechaEstimada = LocalDate.now().plusDays(7);

            Libro libro = libroService.buscarPorIsbn(isbn);

            prestamoService.realizarPrestamo(isbn, dniLimpio, fechaEstimada);

            System.out.println("✅ Préstamo hecho con éxito!");
            System.out.println("\n    Libro (ISBN):    " + isbn + "\n    Título:    " + libro.getTitulo()  + "\n    Prestado al usuario con DNI:    " + dni);
            System.out.println("    Fecha de devolución estimada:    " + fechaEstimada + "\n");
        } catch (LibroNoEncontradoException | UsuarioNoEncontradoException | LibroNoDisponibleExcepcion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error inesperado al realizar el préstamo: " + e.getMessage() + "\n");
        }
    }

    private void devolverPrestamo() {
        System.out.println("\n--- DEVOLVER LIBRO PRESTADO ---");
        try {
            System.out.println("Ingrese el ISBN del libro prestado:");
            System.out.print("-> ");
            String isbn = sc.nextLine();

            Prestamo prestamoActualizado = prestamoService.devolverPrestamo(isbn);
            Libro libroDevuelto = libroService.buscarPorIsbn(isbn);

            System.out.println("✅ Devolución hecha con éxito");
            System.out.println("    Libro (ISBN):    " + isbn);
            System.out.println("    Título:    " + libroDevuelto.getTitulo());
            System.out.println("    Fecha de devolucion:    " + prestamoActualizado.getFechaDevolucionReal() + "\n");
        } catch (LibroNoEncontradoException | LibroYaDevueltoException e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error al realizar la devolución: " + e.getMessage() + "\n");
        }
    }

    private void obtenerPrestamosActivos() {
        System.out.println("\n--- OBTENER PRESTAMOS ACTIVOS ---");

        try {
            List<Prestamo> prestamos = prestamoService.obtenerPrestamosActivos();

            for (Prestamo prestamo : prestamos) {
                System.out.println("Libro: " + prestamo.getLibro().getTitulo() + "\nPrestado al usuario: " + prestamo.getUsuario().getNombre() + " " + prestamo.getUsuario().getApellido() + "\nPrestado en la fecha: " + prestamo.getFechaPrestamo() + "\nFecha de devolución estimada: " + prestamo.getFechaDevolucionEstimada() +
                 "\n");
            }
        } catch (SinPrestamosActivosExcepcion e) {
            System.out.println("\n❌ Error! " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\n❌ Error: " + e.getMessage() + "\n");
        }
    }

    private void obtenerPrestamosEntreFechas() {
        System.out.println("\n--- OBTENER PRESTAMOS ENTRE FECHAS ---");

        try {
            System.out.println("Ingrese la primer fecha límite (AAA-MM-DD):");
            System.out.print("-> ");
            String fechaInicioString = sc.nextLine().trim();

            System.out.println("Ingrese la segunda fecha límite (AAAA-MM-DD):");
            System.out.print("-> ");
            String fechaFinString = sc.nextLine().trim();

            LocalDate fechaInicio = LocalDate.parse(fechaInicioString);
            LocalDate fechaFin = LocalDate.parse(fechaFinString);

            List<Prestamo> prestamos = prestamoService.obtenerPrestamosEntreFechas(fechaInicio, fechaFin);

            for (Prestamo prestamo : prestamos) {
                System.out.println("Fecha de prestamo: " + prestamo.getFechaPrestamo() + "\nFecha de devolución: " + prestamo.getFechaDevolucionReal() + "\n");
            }
        } catch (DateTimeParseException e) {
            System.out.println("❌ Error al ingresar la fecha. Intente el formato adecuado (AAAA-MM-DD)\n");
        } catch (PrestamosNoEncontradosExcepcion e) {
            System.out.println("❌\n Error! " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }
    }
    //----------------------------------

    //----------------------------------
    // Maneja toda la sección de Reportes y Estadísticas
    private void reportesYEstadisticas() {
        System.out.println("""
                \n----------------------------------------------------
                --- REPORTES Y ESTADÍSTICAS ---
                """);
        while (true) {
            System.out.println("""
                    1. Mostrar libros más prestados
                    2. Mostrar usuarios con más libros prestados
                    3. Prestamos vencidos
                    
                    4. Volver al menú principal
                    """);
            System.out.println("Elija su opción:");
            System.out.print("-> ");
            int eleccion = sc.nextInt();
            sc.nextLine();

            switch (eleccion) {
                case 1:
                    mostrarLibrosMasPrestados();
                    break;
                case 2:
                    mostrarUsuariosConMasPrestamos();
                    break;
                case 3:
                    mostrarPrestamosVencidos();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opción no válida, vuelva a elegir!\n");
                    break;
            }
        }
    }

    private void mostrarLibrosMasPrestados() {
        System.out.println("\n--- LIBRO MÁS PRESTADO ---");
        try {
            Libro libro = prestamoService.obtenerLibroMasPrestado();

            System.out.println("✅ Libro con más prestamos:");
            System.out.println("    Título:    " + libro.getTitulo());
            System.out.println("    Autor:    " + libro.getAutor().getNombre() + " " + libro.getAutor().getApellido());
            System.out.println("    ISBN:    " + libro.getIsbn() + "\n");
        } catch (LibroNoEncontradoException e) {
            System.out.println("❌ Error! " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }
    }

    private void mostrarUsuariosConMasPrestamos() {
        System.out.println("\n--- USUARIO CON MÁS PRESTAMOS ---");
        try {
            Usuario usuario = prestamoService.obtenerUsuarioConMasPrestamos();

            System.out.println("    Nombre de usuario:    " + usuario.getNombre() + " " + usuario.getApellido());
            System.out.println("    DNI:    " + usuario.getDni() + "\n");
        } catch (SinPrestamosActivosExcepcion e) {
            System.out.println("❌ Error!" +  e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }
    }

    private void mostrarPrestamosVencidos() {
        System.out.println("\n--- PRESTAMOS VENCIDOS ---");
        try {
            List<Prestamo> prestamos = prestamoService.obtenerPrestamosVencidos();

            for (Prestamo prestamo : prestamos) {
                System.out.println("-------------------------------");
                System.out.println("    Nombre y Apellido:    " + prestamo.getUsuario().getNombre() + " " + prestamo.getUsuario().getApellido());
                System.out.println("    Libro:    " + prestamo.getLibro());
                System.out.println("    Fecha de préstamo:    " + prestamo.getFechaPrestamo());
                System.out.println("    Fecha de devolución estimada:    " + prestamo.getFechaDevolucionEstimada());
                System.out.println("Fecha actual:    " + LocalDate.now());
            }
        } catch (SinPrestamosActivosExcepcion e) {
            System.out.println("❌ Error! " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }
    }
}
