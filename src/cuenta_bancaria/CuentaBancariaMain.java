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

/**
 *
 * @author tote
 */
public class CuentaBancariaMain {

    public static Cuenta __ini__() throws InputMismatchException {
        return __ini__(null, 0);
    }

    public static Cuenta __ini__(String titular, double saldo) throws InputMismatchException {
        Cuenta c = new Cuenta(titular, saldo);
        Scanner sc = new Scanner(System.in);
        String iban;
        int entidad;
        int oficina;
        long cuenta;
        if (c.getTitular() == null) {
            System.out.println("Aun no ha establecido un titular\n¿Desea hacerlo ahora? S\\n");
            String opt = sc.nextLine().toLowerCase();
            if (opt.equals("") || opt.equals("s")) {
                System.out.println("introducir titular");
                c.setTitular(sc.nextLine());
            }
        }
        System.out.println("Aun no se ha establecido un número de cuenta."
                + "\n¿Desea hacerlo ahora? S\\n");
        String in = sc.nextLine().toLowerCase();
        if (in.equals("") || in.equals("s")) {
            System.out.println("introducir IBAN");
            iban = sc.nextLine();
            System.out.println("introducir entidad");
            entidad = sc.nextInt();
            System.out.println("introducir oficina");
            oficina = sc.nextInt();
            System.out.println("introducir cuenta");
            cuenta = sc.nextLong();
            c = new Cuenta(iban, entidad, oficina, cuenta, c);
        } else {
            c = new Cuenta();
        }
        return c;
    }

    public static Cuenta __ini__(String titular) throws InputMismatchException {
        return CuentaBancariaMain.__ini__(titular, 0);
    }

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("null")
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cuenta c = null;
        String titular;
        double saldo;
        String iban;
        int entidad;
        int oficina;
        long cuenta;
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
                    c = CuentaBancariaMain.__ini__(args[0], Double.parseDouble(args[1]));
                    break;
                case 1:
                    c = CuentaBancariaMain.__ini__(args[0]);
                    break;
                default:

                    c = CuentaBancariaMain.__ini__();
                    break;
            }
            if (c.getTitular() == null || c.getIBAN() == null || c.getENTIDAD() == 0.0
                    || c.getOFICINA() == 0.0 || c.getCUENTA() == 0l) {
                System.out.println("Hemos detectado que falta por introducir datos "
                        + "importantes en su cuenta. ¿Desea introducirlos ahora?\nS\\n");
                String opt = sc.nextLine().toLowerCase();
                if (opt.equals("") || opt.equals("s")) {
                    if (c.getTitular() == null) {
                        System.out.println("introducir titular");
                        c.setTitular(sc.nextLine());
                    }
                    if (c.getIBAN() == null || c.getENTIDAD() == 0.0
                            || c.getOFICINA() == 0.0 || c.getCUENTA() == 0l) {
                        System.out.println("introducir IBAN");
                        iban = sc.nextLine();
                        System.out.println("Introducir entidad");
                        entidad = sc.nextInt();
                        System.out.println("introducir oficina");
                        oficina = sc.nextInt();
                        System.out.println("introducir cuenta");
                        cuenta = sc.nextLong();
                        c = new Cuenta(iban, entidad, oficina, cuenta, c);
                        System.out.println("Su cuenta ya esta operativa");
                    } else {
                        c = new Cuenta();
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Se ha equivocado en algun campo, ¿desea volver a intentarlo? S\\n");
            String respuesta = sc.nextLine().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("")) {
                __ini__();
            } else {
                System.out.println("ERROR critico");
            }
        }
        System.out.println(c.mostrarDatos());
        //TODO
    }
}
