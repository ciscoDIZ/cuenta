/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.controller;

import cuenta_bancaria.exc.CuentaInactiva;
import cuenta_bancaria.obj.model.Cuenta;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class Controller {

    static Pattern ibanPatron = Pattern.compile("(ES[0-9]{2})");
    static Pattern entidadOficinaPatron = Pattern.compile("[0-9]{4}");
    static Pattern cuentaPatron = Pattern.compile("[0-9]{10}");
    private Scanner sc;
    private String titular;
    private String iban;
    private int entidad;
    private int oficina;
    private long cuenta;

    public Controller() {
        sc = new Scanner(System.in);
        titular = "";
        iban = "";
        entidad = 0;
        oficina = 0;
        cuenta = 0l;
    }

    public Cuenta menuIniCuenta(Cuenta c) throws CuentaInactiva {
        if (c.getTitular().equals("Usuario por defecto")) {
            if (titular.equals("")) {
                System.out.println("introducir titular");
                titular = sc.nextLine();
            }
        }
        if (!ibanPatron.matcher(iban).matches()) {
            System.out.println("introducir IBAN");
            if (ibanPatron.matcher(iban = sc.nextLine().toUpperCase())
                    .matches()) {
                System.out.println("OK");
                c.setTitular(titular);
            } else {
                throw new CuentaInactiva("IBAN incorrecto");
            }
        }

        if (!entidadOficinaPatron.matcher(String.valueOf(entidad))
                .matches()) {
            System.out.println("introducir entidad");
            if (entidadOficinaPatron.matcher(String.valueOf(entidad
                    = sc.nextInt())).matches()) {
                System.out.println("OK");
            } else {
                System.out.println("entidad incorrecta");

                throw new CuentaInactiva("entidad incorrecta");
            }
        }
        if (!entidadOficinaPatron.matcher(String.valueOf(oficina)).matches()) {
            System.out.println("introducir oficina");
            if (entidadOficinaPatron.matcher(String.valueOf(oficina
                    = sc.nextInt())).matches()) {
                System.out.println("OK");
            } else {
                System.out.println("oficina incorrecta");

                throw new CuentaInactiva("oficina incorrecta");
            }

        }
        if (!cuentaPatron.matcher(String.valueOf(cuenta)).matches()) {
            System.out.println("introducir cuenta");
            if (cuentaPatron.matcher(String.valueOf(cuenta = sc.nextLong()))
                    .matches()) {
                System.out.println("OK");
            } else {
                System.out.println("cuenta incorrecta");
                throw new CuentaInactiva("cuenta erronea");
            }
        }
        return new Cuenta(titular, 0.0, iban, entidad, oficina, cuenta);
    }
}
