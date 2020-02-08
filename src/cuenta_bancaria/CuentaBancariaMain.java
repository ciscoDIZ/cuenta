/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria;


import cuenta_bancaria.obj.Cuenta;
import java.util.Scanner;

/**
 *
 * @author tote
 */
public class CuentaBancariaMain {

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

        switch (args.length) {
            case 6:
                try {
                    titular = args[0];
                    saldo = Double.parseDouble(args[1]);
                    iban = args[2];
                    entidad = Integer.parseInt(args[3]);
                    oficina = Integer.parseInt(args[4]);
                    cuenta = Long.parseLong(args[5]);
                    c = new Cuenta(titular, saldo, iban, entidad, oficina, cuenta);
                } catch (NumberFormatException e) {
                    System.out.println("Alguno de los parámetros no esta en el debido"
                            + "orden: {titular, saldo, iban, entidad, oficina, cuenta}");
                }
                break;
            case 2:
                try {
                    c = CuentaBancariaMain.__ini__(args[0], Double.parseDouble(args[1]));
                } catch (NumberFormatException e) {
                    System.out.println("Alguno de los parámetros no esta en el debido"
                            + "orden: {titular, saldo, iban, entidad, oficina, cuenta}");
                }
                break;
            case 1:
                try {
                    c = CuentaBancariaMain.__ini__(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Alguno de los parámetros no esta en el debido"
                            + "orden: {titular, saldo, iban, entidad, oficina, cuenta}");
                }
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
                }else{
                    c = new Cuenta();
                }
            }
        }
        System.out.println(c.mostrarDatos());
    }

    public static Cuenta __ini__() throws NumberFormatException {
        return __ini__(null, 0);
    }

    public static Cuenta __ini__(String titular, double saldo) throws NumberFormatException {
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
    public static Cuenta __ini__(String titular) throws NumberFormatException {
        return CuentaBancariaMain.__ini__(titular, 0);
    }
   
}
/*Cuenta c1 = new Cuenta();
        Calendar ca = Calendar.getInstance();
        ca.set(1984, 6, 23);
        System.out.println("volcado de ca: "+ca.get(Calendar.MONTH));
        c.setMovimiento(Cuenta.getAsunto(0), 10.55, null);
        c.setMovimiento(Cuenta.getAsunto(0), 30.55, null);
        c.setMovimiento(Cuenta.getAsunto(2), 5.55, ca);
        c.setMovimiento(Cuenta.getAsunto(0), 89.00, ca);
        c.setMovimiento(Cuenta.getAsunto(1), 800.55, null);
        System.out.println("datos c:\n"+c.mostrarDatos());
        System.out.println("ddatos c1\n"+c1.mostrarDatos());
        String str1 = c.movimientosPorFecha("23/7/2020");
        String str = c.movimientosPorFecha("8/2/2020");
        System.out.println(c.mostrarDatos());
        System.out.println(str1);
        System.out.println(str);
        System.out.println(c.movimientosPorAsunto(Cuenta.getAsunto(0)));
        System.out.println(c.validarFecha("23/7/2020"));*/
