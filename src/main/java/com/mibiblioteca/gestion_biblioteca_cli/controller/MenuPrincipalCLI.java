package com.mibiblioteca.gestion_biblioteca_cli.controller;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.*;
import com.mibiblioteca.gestion_biblioteca_cli.model.Autor;
import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import com.mibiblioteca.gestion_biblioteca_cli.service.AutorService;
import com.mibiblioteca.gestion_biblioteca_cli.service.LibroService;
import com.mibiblioteca.gestion_biblioteca_cli.service.PrestamoService;
import com.mibiblioteca.gestion_biblioteca_cli.service.UsuarioService;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipalCLI {
    private final AutorService autorService;
    private final LibroService libroService;
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private Scanner sc = new Scanner(System.in);
    private boolean volver = true;
    private boolean salir = false;

    public MenuPrincipalCLI(AutorService autorService, LibroService libroService, PrestamoService prestamoService, UsuarioService usuarioService) {
        this.autorService = autorService;
        this.libroService = libroService;
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
    }

    private void iniciarMenu() {
        while (!salir) {
            mostrarMenu();
        }
    }

    private void mostrarMenu() {
        while (!salir) {
            System.out.println("""
                \uD83D\uDCD6 ¡Bienvenido al Gestor De Biblioteca!
                Las opciones son:
                
                1_ Registro y Gestión
                2_ Préstamos
                3_ Devoluciones
                4_ Reportes y Estadísticas
                
                5_ SALIR
                """);

            System.out.print("-> ");
            String seleccion = sc.nextLine();

            switch (seleccion) {
                case "1":
                    registroYGestion();
                    break;
                case "2":
                    prestamos();
                    break;
                case "3":
                    devoluciones();
                    break;
                case "4":
                    reportesYEstadisticas();
                    break;
                case "5":
                    salir = true;
                    break;
                default:
                    System.out.println("No es una elección válida");
            }
        }
    }

    //-----------------------------------
    // Maneja toda la sección de Registro y Gestión
    private void registroYGestion() {
        while (!salir) {
            System.out.println("""
                1. Registrar Nuevo Usuario
                2. Registrar Nuevo Autor
                3. Registrar Nuevo Libro
                4. Buscar Usuario por DNI
                5. Buscar Libro por ISBN
                6. Volver al menu principal
                """);
            System.out.println("Elija su opción: ");
            System.out.print("-> ");
            String seleccion = sc.nextLine();

            switch (seleccion) {
                case "1":
                    registrarUsuario();
                    break;
                case "2":
                    registrarAutor();
                    break;
                case "3":
                    registrarNuevoLibro();
                    break;
                case "4":
                    buscarUsuarioPorDni();
                    break;
                case "5":
                    buscarLibroPorIsbn();
                    break;
                case "6":
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

            usuarioService.registrarUsuario(nombre, apellido, dni);

            System.out.println("\n✅ Usuario" + nombre + " "+ apellido + "registrado con éxito!");
//            registroYGestion();
        } catch (UsuarioYaRegistradoException e) {
            System.out.println("❌ Error de registro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: No se pudo completar el registro: " + e.getMessage());
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
            System.out.println("\n✅ Autor: " + nombre + apellido + " registrado con exito!");
        } catch (AutorYaRegistradoException e) {
            System.out.println("❌ Error de registro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: No se pudo completar el registro: " + e.getMessage());
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

            System.out.println("\n✅ Éxito! El libro: " + libro.getTitulo() + " fue registrado exitosamente!");
        } catch (InputMismatchException e) {
            System.out.println("❌ Error: Por favor, ingrese un numero en año de publicación. " + e.getMessage());
            sc.nextLine(); // limpiar buffer para que no haya un loop infinito
        } catch (AutorNoEncontradoException e) {
            System.out.println("❌ Error: No se encontró el autor. " + e.getMessage());
        }
        catch (LibroYaRegistradoException e) {
            System.out.println("❌ Error: Este libro ya fue registrado");
        } catch (Exception e) {
            System.out.println("❌ Error en el registro del libro: " + e.getMessage());
        }
    }

    private void buscarUsuarioPorDni() {
        System.out.println("\n--- BUSCAR USUARIO POR DNI ---");

        try {
            System.out.println("Ingrese el DNI del usuario:");
            System.out.print("-> ");
            String dni = sc.nextLine().trim();

            Usuario usuarioEncontrado = usuarioService.buscarPorDni(dni);

            System.out.println("\n✅ Usuario encontrado: ");
            System.out.println(usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellido());
        } catch (UsuarioNoEncontradoException e) {
            System.out.println("❌" + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error! " + e.getMessage());
        }
    }

    private void buscarLibroPorIsbn() {
        System.out.println("\n--- BUSCAR LIBRO POR ISBN ---");

        try {
            System.out.println("Ingrese el ISBN del libro:");
            System.out.print("-> ");
            String isbn = sc.nextLine().trim();

            Libro libroEncontrado = libroService.buscarPorIsbn(isbn);

            System.out.println("\n✅ Libro Encontrado!");
            System.out.println("    Título:    " + libroEncontrado.getTitulo());
            System.out.println("    Autor:   " + libroEncontrado.getAutor().getNombre() + " " + libroEncontrado.getAutor().getApellido());
            System.out.println("    Año de publicación:    " +libroEncontrado.getAñoPublicacion());

            String estado = libroEncontrado.isDisponible() ? "\uD83D\uDFE2 DISPONIBLE" : "\uD83D\uDD34 PRESTADO";
            System.out.println("    Estado:    " + estado);
        } catch (LibroNoEncontradoException e) {
            System.out.println("❌" + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    //---------------------------------

    //---------------------------------
    // Maneja toda la sección de Préstamos
    private void prestamos() {
        while (!salir) {
            System.out.println("""
                    1. Realizar Préstamo
                    2. Devolver Préstamo
                    3. Obtener Prestamos Activos
                    4. Obtener Prestamos entre fechas
                    5. Volver al menu principal
                    """);

            System.out.println("Elija su opción:");
            System.out.print("-> ");
            String elección = sc.nextLine().trim();

            switch (elección) {
                case "1":
                    realizarPrestamo();
                    break;
                case "2":
                    devolverPrestamo();
                    break;
                case "3":
                    obtenerPrestamosActivos();
                    break;
                case "4":
                    obtenerPrestamosEntreFechas();
                    break;
                case "5":
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

            LocalDate fechaEstimada = LocalDate.now().plusDays(7);

            prestamoService.realizarPrestamo(isbn, dni, fechaEstimada);

            System.out.println("✅ Préstamo hecho con éxito!");
            System.out.println("    Libro (ISBN):    " + isbn + " prestado al usuario con DNI " + dni);
            System.out.println("    Fecha de devolución estimada:    " + fechaEstimada);
        } catch (LibroNoEncontradoException | UsuarioNoEncontradoException | LibroNoDisponibleExcepcion e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error inesperado al realizar el préstamo: " + e.getMessage());
        }
    }
}
