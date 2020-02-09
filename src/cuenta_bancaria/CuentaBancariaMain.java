/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria;

import cuenta_bancaria.obj.Cuenta;
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

    public static Cuenta __ini__() throws InputMismatchException {

        Scanner sc = new Scanner(System.in);
        Cuenta c = new Cuenta();
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
                c = new Cuenta(iban, entidad, oficina, cuenta, c);
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
        Scanner sc = new Scanner(System.in);
        Cuenta c = null;
        String titular = "";
        double saldo = 0;
        String iban = "";
        int entidad = 0;
        int oficina = 0;
        long cuenta = 0l;
        try {
            switch (args.length) {
                case 6:
                    titular = args[0];
                    saldo = Double.parseDouble(args[1]);
                    iban = args[2];
                    entidad = Integer.parseInt(args[3]);
                    oficina = Integer.parseInt(args[4]);
                    cuenta = Long.parseLong(args[5]);
                    c = new Cuenta(titular, saldo, iban, entidad, oficina, cuenta);
                    break;
                case 2:
                    titular = args[0];
                    saldo = Double.parseDouble(args[1]);
                    c = new Cuenta(titular, saldo);
                    break;
                case 1:
                    c = new Cuenta(args[0]);
                    break;
                default:
                    c = new Cuenta();
                    break;
            }
            if (c.getTitular() == null || c.getIBAN() == null || c.getENTIDAD() == 0.0
                    || c.getOFICINA() == 0.0 || c.getCUENTA() == 0l) {
                System.out.println("Hemos detectado que falta por introducir datos "
                        + "importantes en su cuenta. ¿Desea introducirlos ahora?\nS\\n");
                String opt = sc.nextLine().toLowerCase();
                if (opt.equals("") || opt.equals("s")) {
                    c = __ini__();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Se ha equivocado en algun campo, ¿desea volver a intentarlo? S\\n");
            String respuesta = sc.nextLine().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("")) {
                c = __ini__();
            } else {
                System.out.println("disfrute");
            }
        }
        System.out.println(c.mostrarDatos());
        //TODO
        String mensajeMenu = "";
        int opt = 0;
        boolean salir = false;
        while (!salir) {
            try {
                if (c.getTitular().equals("Usuario por defecto")
                        && c.getNumCuenta().equals(
                                "ES00-0000-0000-00-0000000000")) {
                    mensajeMenu = "1)Agregar usuario\n2)Agregar número de cuenta\n"
                            + "3)realizar ingreso\n4)realizar retirada\n5)salir\n";
                    System.out.println(mensajeMenu);
                    opt = sc.nextInt();
                    sc.nextLine();
                    switch (opt) {
                        case 1:
                            System.out.println("intruducir titular");
                            c.setTitular(sc.nextLine());
                            break;
                        case 2:
                            System.out.println("introducir numero de cuenta válido");
                            if (!ibanPatron.matcher(iban).matches()) {
                                System.out.println("introducir IBAN");
                                if (ibanPatron.matcher(iban = sc.nextLine().toUpperCase())
                                        .matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            if (!entidadOficinaPatron.matcher(String.valueOf(
                                    entidad)).matches()) {
                                System.out.println("introducir entidad");
                                if (entidadOficinaPatron.matcher(String.valueOf(
                                        entidad = sc.nextInt())).matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            if (!entidadOficinaPatron.matcher(String.valueOf(
                                    oficina)).matches()) {
                                System.out.println("introducir oficina");
                                if (entidadOficinaPatron.matcher(String.valueOf(oficina
                                        = sc.nextInt())).matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            if (!cuentaPatron.matcher(String.valueOf(cuenta))
                                    .matches()) {
                                System.out.println("introducir cuenta");
                                if (cuentaPatron.matcher(String.valueOf(cuenta
                                        = sc.nextLong())).matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            c = new Cuenta(iban, entidad, oficina, cuenta, c);
                            break;
                        case 3:
                            c.setMovimiento(Cuenta.getTipo(1), 0, null);
                            break;
                        case 4:
                            c.setMovimiento(Cuenta.getTipo(3), 0, null);
                            break;
                        case 5:
                            salir = true;
                            break;
                        default:
                            throw new AssertionError();

                    }

                } else if (c.getTitular().equals("Usuario por defecto")) {
                    mensajeMenu = "1)Agregar usuario\n2)realizar ingreso"
                            + "\n3)realizar retirada\n4)salir";
                    System.out.println(mensajeMenu);
                    switch (opt) {
                        case 1:
                            System.out.println("intruducir titular");
                            c.setTitular(sc.nextLine());
                            break;
                        case 2:
                            c.setMovimiento(Cuenta.getTipo(1), 0, null);
                            break;
                        case 3:
                            c.setMovimiento(Cuenta.getTipo(3), 0, null);
                            break;
                        case 4:
                            salir = true;
                            break;
                        default:
                            throw new AssertionError();

                    }
                    opt = sc.nextInt();
                } else if (c.getNumCuenta().equals("ES00-0000-0000-00-0000000000")) {
                    mensajeMenu = "1)Agregar número de cuenta\n2)realizar ingreso"
                            + "\n3)realizar retirada\n4)salir";
                    System.out.println(mensajeMenu);
                    opt = sc.nextInt();
                    sc.nextLine();
                    switch (opt) {
                        case 1:
                            System.out.println("introducir numero de cuenta válido");
                            if (!ibanPatron.matcher(iban).matches()) {
                                System.out.println("introducir IBAN");
                                if (ibanPatron.matcher(iban = sc.nextLine().toUpperCase())
                                        .matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            if (!entidadOficinaPatron.matcher(String.valueOf(
                                    entidad)).matches()) {
                                System.out.println("introducir entidad");
                                if (entidadOficinaPatron.matcher(String.valueOf(
                                        entidad = sc.nextInt())).matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            if (!entidadOficinaPatron.matcher(String.valueOf(
                                    oficina)).matches()) {
                                System.out.println("introducir oficina");
                                if (entidadOficinaPatron.matcher(String.valueOf(oficina
                                        = sc.nextInt())).matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            if (!cuentaPatron.matcher(String.valueOf(cuenta))
                                    .matches()) {
                                System.out.println("introducir cuenta");
                                if (cuentaPatron.matcher(String.valueOf(cuenta
                                        = sc.nextLong())).matches()) {
                                    System.out.println("OK");
                                } else {
                                    //TODO informar usuario
                                    break;
                                }
                            }
                            c = new Cuenta(iban, entidad, oficina, cuenta, c);
                            break;
                        case 2:
                            c.setMovimiento(Cuenta.getTipo(1), 0, null);
                            break;
                        case 3:
                            c.setMovimiento(Cuenta.getTipo(3), 0, null);
                            break;
                        case 4:
                            salir = true;
                            break;
                        default:
                            throw new AssertionError();
                    }
                } else {
                    System.out.println("");
                    switch (opt) {
                        case 1:
                            c.setMovimiento(Cuenta.getTipo(1), 0, null);
                            break;
                        case 2:
                            c.setMovimiento(Cuenta.getTipo(3), 0, null);
                            break;
                        case 5:
                            salir = true;
                            break;
                        default:
                            throw new AssertionError();

                    }
                }

            } catch (NumberFormatException nf) {
                System.out.println("no permitidos infresos negativos o valor 0");
            } catch (IllegalArgumentException ia) {
                String mensaje = "Antes de realizar un movimiento en una cuenta"
                        + ", esta debe estar activada.\nRequisitos para sactivacion:\n";
                if (c.getNumCuenta().equals("ES00-0000-0000-00-0000000000")) {
                    mensaje += "\t-establecer un numero de cuenta.\n";
                }
                if (c.getTitular().equals("Usuario por defecto")) {
                    mensaje += "\t-establecer usuario";
                }
                System.out.println(mensaje);
            }

            System.out.println(c.getEstado());
        }

    }
}
