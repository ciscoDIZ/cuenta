/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.controller;

import cuenta_bancaria.exc.ExcepcionValidacionDNI;
import cuenta_bancaria.obj.model.Cliente;
import cuenta_bancaria.obj.model.CuentaCliente;
import cuenta_bancaria.obj.model.Usuario;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class Controller {

    /**
     * <h1>CLASE CONTROLLER</h1>
 Clase encargada de controlar todo lo que tenga que ver con el volcado de
 datos en terminal. Adicionalmente, en determinados casos, tambien crea
 instancias de la clase CuentaCliente. La forma de usarla es instanciandola en el
 metodo main de dicha clase y ejecutar cada método en en momento que se
 precise.
     */
    private static final Pattern IBAN_PATRON = Pattern.compile("(ES[0-9]{2})");
    private static final Pattern ENTOF_PATRON = Pattern.compile("[0-9]{4}");
    private static final Pattern DC_PATRON = Pattern.compile("[0-9]{2}");
    private static final Pattern CUENTA_PATRON = Pattern.compile("[0-9]{10}");
    private static final Pattern NOMBRE_PATRON = Pattern.compile("([A-Z]{1})([a-z]{1,100})");
    private static final Pattern EDAD_PATRON = Pattern.compile("[0-9]{1,2}");
    private final Scanner sc;
    private String iban;
    private int entidad;
    private int oficina;
    private byte dc;
    private long cuenta;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int edad;
    private Object dni;
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
        HashSet<Usuario> titulares = new HashSet<>();
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
        if(!DC_PATRON.matcher(String.valueOf(dc)).matches()){
            System.out.println("introducir dc");
            if(DC_PATRON.matcher(String.valueOf(dc = sc.nextByte())).matches()){
                System.out.println("OK");
                retorno[4] = (Byte)dc;
            }else{
                System.out.println("dc incorrecto");

                throw new IllegalArgumentException("dc incorrecto");
            }
        }
        if (!CUENTA_PATRON.matcher(String.valueOf(cuenta)).matches()) {
            System.out.println("introducir cuenta");
            if (CUENTA_PATRON.matcher(String.valueOf(cuenta = sc.nextLong()))
                    .matches()) {
                System.out.println("OK");
                retorno[5] = (Long) cuenta;
            } else {
                System.out.println("cuenta incorrecta");
                throw new IllegalArgumentException("cuenta erronea");
            }
        }
        return retorno;
    }

    public HashSet<Usuario> menuIniTitulares(CuentaCliente c) throws AssertionError,
            ExcepcionValidacionDNI {
        Pattern p = Pattern.compile("([X|Z]?[0-9]{8}[A-Z])"
                + "|([X|Z]?[0-9]{8} [A-Z])"
                + "|([X|Z]?[0-9]{8}-[A-Z])");

        sc.nextLine();
        HashSet<Usuario> retorno = new HashSet<>();
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
            this.dni = Cliente.getDninstance(dniStr);

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
            retorno.add(new Cliente(nombre, apellido1, apellido2, edad, dni, sexo,0));
            System.out.println("¿Desea agregar otro titular?\nS/n");
            String respuesta = sc.nextLine();
            salir = !(respuesta.equals("s") || respuesta.equals(""));
        }
        return retorno;
    }

    public Object[] menuIniTitular() throws AssertionError,
            ExcepcionValidacionDNI {
        Pattern p = Pattern.compile("([X|Z]?[0-9]{8}[A-Z])"
                + "|([X|Z]?[0-9]{8} [A-Z])"
                + "|([X|Z]?[0-9]{8}-[A-Z])");
        Object[] retorno = new Object[6];
        
       
            System.out.println("introducir nombre");
            nombre = sc.nextLine();
            retorno[0] = nombre;
            System.out.println("introducir primer apellido");
            apellido1 = sc.nextLine();
            retorno[1] = apellido1;
            System.out.println("introducir segundo apellido");
            apellido2 = sc.nextLine();
            retorno[2] = apellido2;
            System.out.println("introducir edad");
            edad = sc.nextInt();
            retorno[3] = edad;
            sc.nextLine();
            System.out.println("introducir dni");
            String dniStr = sc.nextLine();
            
            Matcher m = p.matcher(dniStr);
            while (m.find()) {
                dniStr = m.group();
            }
            this.dni = Cliente.getDninstance(dniStr);
            retorno[4] = dni;
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
            retorno[5] = sexo;
        
        return retorno;
    }

}
