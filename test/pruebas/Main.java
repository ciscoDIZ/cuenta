/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import cuenta_bancaria.exc.CuentaInactiva;
import cuenta_bancaria.obj.controller.Controller;
import cuenta_bancaria.obj.model.Cuenta;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
       try {
            Cuenta c = new Cuenta(new ArrayList<>());
            System.out.println(c.validarFecha("23/7/2084"));
            System.out.println(c.mostrarDatos());
            Cuenta c1 = new Cuenta(new ArrayList<>());
            Calendar ca = Calendar.getInstance();
            ca.set(1984, 6, 23);
            System.out.println("volcado de ca: " + ca.get(Calendar.MONTH));
            System.out.println("intro cuantida");
            c.setMovimiento(Cuenta.getAsunto(0), null, sc.nextDouble(), null);
            c.setMovimiento(Cuenta.getAsunto(0), null, 30.555, null);
            c.setMovimiento(Cuenta.getAsunto(2), null, 5.552, ca);
            c.setMovimiento(Cuenta.getAsunto(0), null, 89.002, ca);
            c.setMovimiento(Cuenta.getAsunto(1), null, 800.555, null);
            System.out.println("datos c:\n" + c.mostrarDatos());
            System.out.println("ddatos c1\n" + c1.mostrarDatos());
            String str1 = c.movimientosPorFecha("23/7/1984");
            String str = c.movimientosPorFecha("8.2.2020");
            System.out.println(c.mostrarDatos());
            c.setMovimiento(Cuenta.getAsunto(1), null, 100, ca);
            System.out.println(str1);
            System.out.println(str);
            System.out.println(c.movimientosPorAsunto(Cuenta.getAsunto(0)));
            System.out.println(c.validarFecha("23/7/1984"));
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
          /*Cuenta.Movimiento movimiento = new Cuenta.Movimiento();
        movimiento.cuantia = 10;
        Cuenta.Movimiento movimiento1 = new Cuenta.Movimiento();
        System.out.println(movimiento1.cuantia);
         Pattern munCuenta = Pattern.compile("((ES[0-9]{2})|(ES00))-(([0-9]{4})|(0))-(([0-9]{4})|(0))-(([0-9]{2})|(0))-(([0-9]{10})|(0))");
        Matcher m = munCuenta.matcher("ES00-0-0-0-0");
        System.out.println(m.matches());
        /*Controller controller = new Controller();
        Cuenta cuenta = new Cuenta("nombre");
        boolean salir = false;
        while (!salir) {
            try {
                controller.menuIniCuenta(cuenta);
            } catch (IllegalArgumentException e) {
                System.out.println("desea salir?");
                String respuesta = sc.nextLine();
                if(respuesta.equals("s")){
                    salir=true;
                }
            }
        }*/

    }
}
