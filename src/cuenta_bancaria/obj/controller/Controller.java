/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.controller;

import cuenta_bancaria.obj.model.Cuenta;
import cuenta_bancaria.obj.model.Usuario;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class Controller {
    /**
     * <h1>CLASE CUENTA</h1>
     * Clase encargada de controlar todo lo  que tenga que ver con el volcado de
     * datos en terminal. Adicionalmente, en determinados casos, tambien crea 
     * instancias de la clase Cuenta. 
     * La forma de usarla es instanciandola en el metodo main de dicha clase y 
     * ejecutar cada método en en momento que se precise.
     */
    static Pattern ibanPatron = Pattern.compile("(ES[0-9]{2})");
    static Pattern entidadOficinaPatron = Pattern.compile("[0-9]{4}");
    static Pattern cuentaPatron = Pattern.compile("[0-9]{10}");
    private Scanner sc;
    private String titular;
    private String iban;
    private int entidad;
    private int oficina;
    private long cuenta;
    /**
     * constructor unico y sin parámetros
     */
    public Controller() {
        sc = new Scanner(System.in);
        titular = "";
        iban = "";
        entidad = 0;
        oficina = 0;
        cuenta = 0l;
    }
    /**
     * Método encargado e la inicializacion de la cuenenta y por motivos de 
     * seguridad para evitar posibles cambios en el número de cuenta, la clase 
     * Cuetnta obliga a instanciar un nuevo objeto para poder efectuar cambios
     * en los atributos vinculados con dicho número. Por ese motivo, debe crear 
     * una instancia si el usuario no da parametros en el inicio de la 
     * aplicacion.
     * @param c instancia de lase Cuenta inicializada con el parametro titular 
     * en blanco.
     * @return 
     * @throws IllegalArgumentException 
     */
    public Cuenta menuIniCuenta(Cuenta c) throws IllegalArgumentException {
        ArrayList<Usuario> titulares = new ArrayList<>();
        if (c.getTitular().equals("")) {
            
            /*if (titular.equals("")) {
                System.out.println("introducir titular");
                titular = sc.nextLine();
            }*/
        }
        if (!ibanPatron.matcher(iban).matches()) {
            System.out.println("introducir IBAN");
            if (ibanPatron.matcher(iban = sc.nextLine().toUpperCase())
                    .matches()) {
                System.out.println("OK");
                c.setTitular(titular);
            } else {
                throw new IllegalArgumentException("IBAN incorrecto");
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

                throw new IllegalArgumentException("entidad incorrecta");
            }
        }
        if (!entidadOficinaPatron.matcher(String.valueOf(oficina)).matches()) {
            System.out.println("introducir oficina");
            if (entidadOficinaPatron.matcher(String.valueOf(oficina
                    = sc.nextInt())).matches()) {
                System.out.println("OK");
            } else {
                System.out.println("oficina incorrecta");

                throw new IllegalArgumentException("oficina incorrecta");
            }

        }
        if (!cuentaPatron.matcher(String.valueOf(cuenta)).matches()) {
            System.out.println("introducir cuenta");
            if (cuentaPatron.matcher(String.valueOf(cuenta = sc.nextLong()))
                    .matches()) {
                System.out.println("OK");
            } else {
                System.out.println("cuenta incorrecta");
                throw new IllegalArgumentException("cuenta erronea");
            }
        }
        return new Cuenta(titulares, 0.0, iban, entidad, oficina, cuenta);
    }
}
