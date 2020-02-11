/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria;

import cuenta_bancaria.exc.CuentaInactiva;
import cuenta_bancaria.obj.controller.Controller;
import cuenta_bancaria.obj.model.Cuenta;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class CuentaBancariaMain {

    static Pattern ibanPatron = Pattern.compile("(ES[0-9]{2})");
    static Pattern entidadOficinaPatron = Pattern.compile("[0-9]{4}");
    static Pattern cuentaPatron = Pattern.compile("[0-9]{10}");

    public static Cuenta __ini__(Cuenta c) throws InputMismatchException {

        Scanner sc = new Scanner(System.in);
        String titular = "";
        String iban;
        int entidad;
        int oficina;
        long cuenta;

        if (c.getTitular().equals("Usuario por defecto")) {
            System.out.println("Aun no ha establecido un titular\n¿Desea hacerlo ahora? S\\n");
            String opt = sc.nextLine().toLowerCase();
            if (opt.equals("") || opt.equals("s")) {
                System.out.println("introducir titular");
                c.setTitular(sc.nextLine());

            }
        }
        if (c.getNumCuenta().equals("ES00-0000-0000-00-0000000000")) {
            System.out.println("Aun no se ha establecido un número de cuenta."
                    + "\n¿Desea hacerlo ahora? S\\n");
            String in = sc.nextLine().toLowerCase();
            if (in.equals("") || in.equals("s")) {

                System.out.println("introducir IBAN");
                if (ibanPatron.matcher(iban = sc.nextLine().toUpperCase())
                        .matches()) {
                    System.out.println("OK");
                } else {
                    throw new InputMismatchException("IBAN incorrecto");
                }

                System.out.println("introducir entidad");

                if (entidadOficinaPatron.matcher(String.valueOf(entidad
                        = sc.nextInt())).matches()) {
                    System.out.println("OK");
                } else {
                    throw new InputMismatchException("entidad incorrecta");
                }
                System.out.println("introducir oficina");

                if (entidadOficinaPatron.matcher(String.valueOf(oficina
                        = sc.nextInt())).matches()) {
                    System.out.println("OK");
                } else {
                    throw new InputMismatchException("oficina incorrecta");
                }
                System.out.println("introducir cuenta");
                if (cuentaPatron.matcher(String.valueOf(cuenta = sc.nextLong()))
                        .matches()) {
                    System.out.println("OK");
                } else {
                    throw new InputMismatchException("cuenta erronea");
                }
                c = new Cuenta(titular, 0.0, iban, entidad, oficina, cuenta);
            } else {
                throw new InputMismatchException();
            }

        }
        return c;
    }

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("null")
    public static void main(String[] args) {
        Controller controller = new Controller();
        Scanner sc = new Scanner(System.in);
        Cuenta cuenta = null;
        String titular;
        double saldo;
        String iban = "";
        int entidad = 0;
        int oficina = 0;
        long nCuenta = 0l;
        Calendar fecha;
        // try {
        switch (args.length) {
            case 6:
                titular = args[0];
                saldo = Double.parseDouble(args[1]);
                iban = args[2];
                entidad = Integer.parseInt(args[3]);
                oficina = Integer.parseInt(args[4]);
                nCuenta = Long.parseLong(args[5]);
                cuenta = new Cuenta(titular, saldo, iban, entidad, oficina, nCuenta);
                break;
            case 2:
                titular = args[0];
                saldo = Double.parseDouble(args[1]);
                cuenta = new Cuenta(titular, saldo);
                break;
            case 1:
                cuenta = new Cuenta(args[0]);
                break;
            default:
                cuenta = new Cuenta("");

        }
        if (cuenta.getTitular().equals("")) {
            System.out.println("Hemos detectado que falta por introducir datos "
                    + "importantes en su cuenta. ¿Desea introducirlos ahora?\nS\\n");
            String opt = sc.nextLine().toLowerCase();
            if (opt.equals("") || opt.equals("s")) {
                while (cuenta.getEstado().equals(Cuenta.getEstado(0))) {
                    try {
                        cuenta = controller.menuIniCuenta(cuenta);
                    } catch (CuentaInactiva e) {
                    }

                }

            }
        }
        
        System.out.println(cuenta.mostrarDatos());
        //TODO
        String mensajeMenu;
        int opt = 0;
        boolean salir = false;
        String mensajeBienvenida = null;
        while (!salir) {

            String opciones = "1)realizar ingreso\n2)realizar retirada"
                    + "\n3)mostrar datos\n4)búsqueda por fecha"
                    + "\n5) búsqueda por asunto\n6)mostrar opciones"
                    + "\n7)salir";
            if (mensajeBienvenida == null) {
                mensajeBienvenida = "Bienvenido la cuenta de "
                        + cuenta.getTitular() + " estado: " + cuenta.getEstado().toString()
                        .toLowerCase();
                System.out.println(mensajeBienvenida);
                System.out.println(opciones);
            }

            opt = sc.nextInt();
            switch (opt) {
                case 1:

                    System.out.println("1)ingreso manual\n2)ingreso automatico");
                    opt = sc.nextInt();
                    switch (opt) {
                        case 2:
                            fecha = null;
                            break;
                        case 1:
                            System.out.println("introducir dia");
                            int dia = sc.nextInt();
                            System.out.println("introducir mes");
                            int mes = sc.nextInt();
                            System.out.println("año");
                            int anio = sc.nextInt();
                            fecha = Calendar.getInstance();
                            fecha.set(anio, mes - 1, dia);
                            break;
                        default:
                            fecha = null;
                    }
                    System.out.println("seleccionar asunto");
                    System.out.println("0)ingreso\n1)nomina");
                    opt = sc.nextInt();
                    switch (opt) {
                        case 0:
                        case 1:
                            System.out.println("introducir cuantia");
                            double cuantia = sc.nextDouble();
                            cuenta.setMovimiento(Cuenta.getTipo(opt), null, cuantia, fecha);
                            break;
                        default:
                            throw new AssertionError();
                    }
                    break;
                case 2:
                    System.out.println("1)retirada manual\n2)retirada automatico");
                    opt = sc.nextInt();
                    switch (opt) {
                        case 2:
                            fecha = null;
                            break;
                        case 1:
                            System.out.println("introducir dia");
                            int dia = sc.nextInt();
                            System.out.println("introducir mes");
                            int mes = sc.nextInt();
                            System.out.println("año");
                            int anio = sc.nextInt();
                            fecha = Calendar.getInstance();
                            fecha.set(anio, mes - 1, dia);
                            break;
                        default:
                            fecha = null;
                    }
                    System.out.println("seleccionar asunto");
                    System.out.println("2)retirada\n1)pago");
                    opt = sc.nextInt();
                    switch (opt) {
                        case 2:
                        case 3:
                            System.out.println("introducir cuantia");
                            double cuantia = sc.nextDouble();
                            cuenta.setMovimiento(Cuenta.getTipo(opt), null, cuantia, fecha);
                            break;
                        default:
                            throw new AssertionError();
                    }
                    break;
                case 3:
                    System.out.println(cuenta.mostrarDatos());
                    break;
                case 4:
                    System.out.println("Introducir fecha");
                    String fechaStr = sc.nextLine();
                    System.out.println(cuenta.movimientosPorFecha(fechaStr));
                    break;
                case 5:
                    System.out.println("seleccionar asunto");
                    System.out.println("0)ingreso\n1)nomina\n2)retirada\n3)pago");
                    int movimiento = sc.nextInt();
                    switch (movimiento) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            System.out.println(cuenta.movimientosPorAsunto(
                                    Cuenta.getTipo(movimiento)));
                            break;
                        default:
                            throw new AssertionError();
                    }

                    break;
                case 6:
                    System.out.println(opciones);
                    break;
                case 7:
                    salir = true;
                    break;
                default:
                    throw new AssertionError();

            }
        }

        System.out.println(cuenta.getEstado());
    }

}

