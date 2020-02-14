/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.controller;

import cuenta_bancaria.exc.ExcepcionValidacionDNI;
import cuenta_bancaria.obj.model.Cuenta;
import cuenta_bancaria.obj.model.DNI;
import cuenta_bancaria.obj.model.Usuario;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class Controller {

    /**
     * <h1>CLASE CUENTA</h1>
     * Clase encargada de controlar todo lo que tenga que ver con el volcado de
     * datos en terminal. Adicionalmente, en determinados casos, tambien crea
     * instancias de la clase Cuenta. La forma de usarla es instanciandola en el
     * metodo main de dicha clase y ejecutar cada método en en momento que se
     * precise.
     */
    private static final Pattern IBAN_PATRON = Pattern.compile("(ES[0-9]{2})");
    private static final Pattern ENTOF_PATRON = Pattern.compile("[0-9]{4}");
    private static final Pattern CUENTA_PATRON = Pattern.compile("[0-9]{10}");
    private static final Pattern NOMBRE_PATRON = Pattern.compile("([A-Z]{1})([a-z]{1,100})");
    private static final Pattern EDAD_PATRON = Pattern.compile("[0-9]{1,2}");
    private final Scanner sc;
    private String iban;
    private int entidad;
    private int oficina;
    private long cuenta;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int edad;
    private DNI dni;
    private Usuario.Sexo sexo;

    /**
     * constructor unico y sin parámetros
     */
    public Controller() {
        sc = new Scanner(System.in);
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
     *
     * @return
     * @throws IllegalArgumentException en caso de introducir un campo
     * incorrecto
     */
    public Object[] menuIniCuenta() throws IllegalArgumentException {
        Object[] retorno = new Object[5];
        ArrayList<Usuario> titulares = new ArrayList<>();
        retorno[0] = titulares;
        if (!IBAN_PATRON.matcher(iban).matches()) {
            System.out.println("introducir IBAN");
            if (IBAN_PATRON.matcher(iban = sc.nextLine().toUpperCase())
                    .matches()) {
                System.out.println("OK");
                retorno[1] = (String) iban;
            } else {
                throw new IllegalArgumentException("IBAN incorrecto");
            }
        }

        if (!ENTOF_PATRON.matcher(String.valueOf(entidad))
                .matches()) {
            System.out.println("introducir entidad");
            if (ENTOF_PATRON.matcher(String.valueOf(entidad
                    = sc.nextInt())).matches()) {
                System.out.println("OK");
                retorno[2] = (Integer) entidad;
            } else {
                System.out.println("entidad incorrecta");

                throw new IllegalArgumentException("entidad incorrecta");
            }
        }
        if (!ENTOF_PATRON.matcher(String.valueOf(oficina)).matches()) {
            System.out.println("introducir oficina");
            if (ENTOF_PATRON.matcher(String.valueOf(oficina
                    = sc.nextInt())).matches()) {
                System.out.println("OK");
                retorno[3] = (Integer) oficina;
            } else {
                System.out.println("oficina incorrecta");

                throw new IllegalArgumentException("oficina incorrecta");
            }

        }
        if (!CUENTA_PATRON.matcher(String.valueOf(cuenta)).matches()) {
            System.out.println("introducir cuenta");
            if (CUENTA_PATRON.matcher(String.valueOf(cuenta = sc.nextLong()))
                    .matches()) {
                System.out.println("OK");
                retorno[4] = (Long) cuenta;
            } else {
                System.out.println("cuenta incorrecta");
                throw new IllegalArgumentException("cuenta erronea");
            }
        }
        return retorno;
    }

    public ArrayList<Usuario> menuIniTitulares() throws AssertionError,
            ExcepcionValidacionDNI {
        Pattern p = Pattern.compile("([X|Z]?[0-9]{8}[A-Z])"
                + "|([X|Z]?[0-9]{8} [A-Z])"
                + "|([X|Z]?[0-9]{8}-[A-Z])");

        sc.nextLine();
        ArrayList<Usuario> retorno = new ArrayList<>();
        boolean salir = false;
        while (!salir) {
            System.out.println("introducir nombre");
            nombre = sc.nextLine();
            System.out.println("introducir primer apellido");
            apellido1 = sc.nextLine();
            System.out.println("introducir segundo apellido");
            apellido2 = sc.nextLine();
            System.out.println("introducir edad");
            edad = sc.nextInt();
            sc.nextLine();
            System.out.println("introducir dni");
            String dniStr = sc.nextLine();
            Matcher m = p.matcher(dniStr);
            while (m.find()) {
                dniStr = m.group();
            }
            this.dni = new DNI(dniStr);

            System.out.println("1)Mujer\n2)Hombre");
            int opt = sc.nextInt();
            sc.nextLine();
            switch (opt) {
                case 1:
                    sexo = Usuario.Sexo.MUJER;
                    break;
                case 2:
                    sexo = Usuario.Sexo.HOMBRE;
                    break;
                default:
                    throw new AssertionError();
            }
            retorno.add(new Usuario(nombre, apellido1, apellido2, edad, dni, sexo, new ArrayList<>()));
            System.out.println("¿Desea agregar otro titular?\nS/n");
            String respuesta = sc.nextLine();
            salir = !(respuesta.equals("s") || respuesta.equals(""));
        }
        return retorno;
    }

    
}
