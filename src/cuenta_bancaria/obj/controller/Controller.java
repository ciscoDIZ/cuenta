/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.controller;

import cuenta_bancaria.exc.ExcepcionValidacionCCC;
import cuenta_bancaria.exc.ExcepcionValidacionDNI;
import cuenta_bancaria.exc.TitularDuplicado;
import cuenta_bancaria.obj.model.Cliente;
import cuenta_bancaria.obj.model.CuentaBancaria;
import cuenta_bancaria.obj.model.Sucursal;
import cuenta_bancaria.obj.model.Usuario;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class Controller {

    /**
     * <h1>CLASE CONTROLLER</h1>
     * Clase encargada de controlar todo lo que tenga que ver con el volcado de
     * datos en terminal. Adicionalmente, en determinados casos, tambien crea
     * instancias de la clase CuentaBancaria. La forma de usarla es
     * instanciandola en el metodo main de dicha clase y ejecutar cada método en
     * en momento que se precise.
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
    private int cuenta;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int edad;
    int opt;
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
        cuenta = 0;
        System.out.println("");
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
        if (!DC_PATRON.matcher(String.valueOf(dc)).matches()) {
            System.out.println("introducir dc");
            if (DC_PATRON.matcher(String.valueOf(dc = sc.nextByte())).matches()) {
                System.out.println("OK");
                retorno[4] = (Byte) dc;
            } else {
                System.out.println("dc incorrecto");

                throw new IllegalArgumentException("dc incorrecto");
            }
        }
        if (!CUENTA_PATRON.matcher(String.valueOf(cuenta)).matches()) {
            System.out.println("introducir cuenta");
            if (CUENTA_PATRON.matcher(String.valueOf(cuenta = sc.nextInt()))
                    .matches()) {
                System.out.println("OK");
                retorno[5] = cuenta;
            } else {
                System.out.println("cuenta incorrecta");
                throw new IllegalArgumentException("cuenta erronea");
            }
        }
        return retorno;
    }

    public HashSet<Usuario> menuIniTitulares(CuentaBancaria c) throws AssertionError,
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
            this.dni = Cliente.getDnInstance(dniStr);

            System.out.println("1)Mujer\n2)Hombre");
            opt = sc.nextInt();
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
            retorno.add(new Cliente(nombre, apellido1, apellido2, edad, dni, sexo));
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
        this.dni = Cliente.getDnInstance(dniStr);
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

    public CuentaBancaria accederCuentaBancaria() throws ExcepcionValidacionCCC,
            ExcepcionValidacionDNI, NumberFormatException {
        String dniStr = null;
        String nCuentaStr = null;
        int nCuenta;
        CuentaBancaria cbn = null;
        System.out.println("introducir dni");
        dniStr = sc.nextLine();
        iban = "ES25";
        entidad = 3321;
        oficina = 2020;
        System.out.println("intoducir digito de control");
        System.out.print(iban + "-" + entidad + "-" + oficina + "-");
        dc = sc.nextByte();
        sc.nextLine();
        System.out.println("intoducir nº cuenta");
        System.out.print(iban + "-" + entidad + "-" + oficina + "-" + dc + "-");
        nCuentaStr = sc.nextLine();
        System.out.println("");
        nCuenta = Integer.parseInt(nCuentaStr);
        return Sucursal.accederCuenta(Cliente.getDnInstance(dniStr),
                CuentaBancaria.getCCC(iban, entidad, oficina, dc, nCuenta));
    }

    public void menuCuentaBloqueada(CuentaBancaria cuenta) {
        int cod = 1234;
        System.out.println("Su cuenta esta bloqueada, para desbloquearla ponga el codigo de deabloqueo");
        System.out.print(":");
        opt = sc.nextInt();
        if (cod == opt) {
            cuenta.setEstado(CuentaBancaria.Estado.ACTIVA);
            System.out.println("Cuenta activa");
        }
    }

    public CuentaBancaria menuCambiarNumCuenta(CuentaBancaria cuenta)
            throws IllegalArgumentException, ExcepcionValidacionCCC {
        Object[] data;
        int nCuenta;
        data = menuIniCuenta();
        iban = (String) data[1];
        entidad = (Integer) data[2];
        oficina = (Integer) data[3];
        dc = (Byte) data[4];
        nCuenta = (Integer) data[5];
        cuenta = new CuentaBancaria(iban, entidad, oficina,
                dc, nCuenta, cuenta);
        cuenta.vincularCuenta();
        return cuenta;
    }

    public CuentaBancaria menuInterfazEscritorio(CuentaBancaria cuenta, boolean salir)
            throws TitularDuplicado, ExcepcionValidacionCCC, ExcepcionValidacionDNI,
            AssertionError, NumberFormatException {
        String dniStr;
        System.out.println("1)dar de alta cliente\n2)dar de alta cuenta"
                + "\n3)consultar cuenta\n4)gestionar cuenta\n5)acceder cuenta\n6)salir");
        opt = sc.nextInt();
        sc.nextLine();
        switch (opt) {
            case 1:
                Object[] datos = menuIniTitular();
                Usuario u = new Cliente((String) datos[0], (String) datos[1],
                        (String) datos[2], (int) datos[3], datos[4],
                        (Usuario.Sexo) datos[5]);
                Sucursal.darAltaUsuario((Cliente) u);
                break;
            case 2:

                System.out.println("introducir dni");
                dniStr = sc.nextLine();
                dni = Cliente.getDnInstance(dniStr);
                Sucursal.darAltaCuenta(dni);
                break;
            case 3:

                System.out.println("introducir dni");
                dniStr = sc.nextLine();
                dni = Cliente.getDnInstance(dniStr);
                System.out.println(Sucursal.consultCuenta(dni));

                break;
            case 4:
                cuenta = accederCuentaBancaria();
                cuenta = gesrionarCuenta(cuenta);
                //cuenta = null;
                break;
            case 5:

                cuenta = accederCuentaBancaria();
                break;
            case 6:
                salir = true;
                break;
            default:
                throw new AssertionError();
        }
        return cuenta;
    }

    private CuentaBancaria gesrionarCuenta(CuentaBancaria cuenta) throws AssertionError {
        if (cuenta.getEstado().equals(CuentaBancaria.Estado.ACTIVA)) {
            System.out.println("1)desactivar cuenta\n2)salir");
            opt = sc.nextInt();
            gestionarCuentaActiva(cuenta);
        } else if (cuenta.getEstado().equals(CuentaBancaria.Estado.INACTIVA)) {
            System.out.println("1)activar cuenta\n2)bloquear cuenta\n3)salir");
            opt = sc.nextInt();
            cuenta = gestionarCuentaInactiva(cuenta);
        }
        return cuenta;
    }

    private CuentaBancaria gestionarCuentaInactiva(CuentaBancaria cuenta) throws AssertionError {
        switch (opt) {
            case 1:
                cuenta.setEstado(CuentaBancaria.Estado.ACTIVA);
                cuenta = null;
                break;
            case 2:
                cuenta.setEstado(CuentaBancaria.Estado.BLOQUEADA);
                cuenta = null;
                break;
            case 3:
                cuenta = null;
                break;
            default:
                throw new AssertionError();
        }
        return cuenta;
    }

    private void gestionarCuentaActiva(CuentaBancaria cuenta) throws AssertionError {
        switch (opt) {
            case 1:
                cuenta.setEstado(CuentaBancaria.Estado.INACTIVA);
                break;
            case 2:
                //cuenta = null;
                break;
            default:
                throw new AssertionError();
        }
    }

    public CuentaBancaria interfazCuentaBancaria(CuentaBancaria cuenta)
            throws AssertionError, ExcepcionValidacionCCC, ExcepcionValidacionDNI,
            IllegalArgumentException {
        String mensajeBienvenida = null;
        String dniStr;
        String nCuentaStr;
        Object dni;
        Calendar fecha;
        String[] cccArray;
         double cuantia;
        switch (cuenta.getEstado()) {
            case ACTIVA:
                String opciones = "1)realizar ingreso\n2)realizar retirada\n3)transferencias"
                        + "\n4)mostrar datos\n5)búsqueda por fecha"
                        + "\n6)búsqueda por asunto\n7)mostrar titular/es\n8)mostrar opciones"
                        + "\n9)salir";
                if (mensajeBienvenida == null) {
                    mensajeBienvenida = "Bienvenido la cuenta de "
                            + " estado: " + cuenta.getEstado().toString()
                                    .toLowerCase();
                    System.out.println(mensajeBienvenida);
                    System.out.println(opciones);
                } else {
                    System.out.println("8)mostrar opciones");
                }
                opt = sc.nextInt();
                switch (opt) {
                    case 1:
                         System.out.println("introducir cuantia");
                              cuantia  = sc.nextDouble();
                         cuenta.setMovimiento(CuentaBancaria.getAsunto(opt), null, cuantia, null);
                        break;
                    case 2:
                           System.out.println("introducir cuantia");
                                cuantia = sc.nextDouble();
                                cuenta.setMovimiento(CuentaBancaria.getAsunto(2), null, cuantia, null);
                        break;
                    case 3:
                        sc.nextLine();
                        System.out.println("introducir dni");
                        dniStr = sc.nextLine();
                        System.out.println("introducir CCC destinatario");
                        nCuentaStr = sc.nextLine();
                        cccArray = nCuentaStr.split("-");
                        Object ccc = CuentaBancaria.getCCC(cccArray[0],
                                Integer.parseInt(cccArray[1]),
                                Integer.parseInt(cccArray[2]),
                                Byte.parseByte(cccArray[3]),
                                Integer.parseInt(cccArray[4]));
                        System.out.println("introducir cuantia");
                        cuantia = sc.nextInt();
                        Sucursal.transfererirFondos(Cliente.getDnInstance(dniStr), cuenta
                                .getCCC(), ccc, cuantia);
                        break;
                    case 4:
                        System.out.println(cuenta.mostrarDatos());
                        break;
                    case 5:
                        sc.nextLine();
                        System.out.println("Introducir fecha");
                        String fechaStr = sc.nextLine();
                        System.out.println(cuenta.movimientosPorFecha(fechaStr));
                        break;
                    case 6:
                        System.out.println("seleccionar asunto");
                        System.out.println("0)ingreso\n1)nomina\n2)retirada\n3)pago");
                        int movimiento = sc.nextInt();
                        seleccionarMovimiento(movimiento, cuenta);
                        break;
                    case 7:
                        System.out.println(cuenta.mostrarTitular());
                        break;
                    case 8:
                        System.out.println(opciones);
                        break;
                    case 9:
                        cuenta = null;
                        break;
                    default:
                        throw new AssertionError();
                }

                break;
            case INACTIVA:
                System.out.println("1)cambiar titular/es\n2)cambiar número cuenta\n3)salir");
                opt = sc.nextInt();
                cuenta = menuCuentaInactiva(cuenta);
                break;
            case BLOQUEADA:
                menuCuentaBloqueada(cuenta);
            default:
                break;
        }
        return cuenta;
    }

    private void seleccionarMovimiento(int movimiento, CuentaBancaria cuenta) throws AssertionError {
        switch (movimiento) {
            case 0:
            case 1:
            case 2:
            case 3:
                System.out.println(cuenta.movimientosPorAsunto(CuentaBancaria.getAsunto(movimiento)));
                break;
            default:
                throw new AssertionError();
        }
    }

    private CuentaBancaria menuCuentaInactiva(CuentaBancaria cuenta) throws ExcepcionValidacionCCC, ExcepcionValidacionDNI, IllegalArgumentException, AssertionError {
        switch (opt) {
            case 1:
                cuenta = new CuentaBancaria((Set) menuIniTitulares(cuenta), cuenta);
                break;
            case 2:
                cuenta = menuCambiarNumCuenta(cuenta);
                break;
            case 3:
                cuenta = null;
                break;
            default:
                break;
        }
        return cuenta;
    }
}
