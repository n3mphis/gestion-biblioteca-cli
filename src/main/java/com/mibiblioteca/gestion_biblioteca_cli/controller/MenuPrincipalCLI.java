package com.mibiblioteca.gestion_biblioteca_cli.controller;

import com.mibiblioteca.gestion_biblioteca_cli.exceptions.AutorNoEncontradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.AutorYaRegistradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.LibroYaRegistradoException;
import com.mibiblioteca.gestion_biblioteca_cli.exceptions.UsuarioYaRegistradoException;
import com.mibiblioteca.gestion_biblioteca_cli.model.Autor;
import com.mibiblioteca.gestion_biblioteca_cli.model.Libro;
import com.mibiblioteca.gestion_biblioteca_cli.model.Usuario;
import com.mibiblioteca.gestion_biblioteca_cli.service.AutorService;
import com.mibiblioteca.gestion_biblioteca_cli.service.LibroService;
import com.mibiblioteca.gestion_biblioteca_cli.service.PrestamoService;
import com.mibiblioteca.gestion_biblioteca_cli.service.UsuarioService;

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

    private void registroYGestion() {
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
                mostrarMenu();
                break;
            default:
                System.out.println("No es una opción válida");
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


}
