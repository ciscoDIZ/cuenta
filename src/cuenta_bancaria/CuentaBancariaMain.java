/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria;

import cuenta_bancaria.exc.ExcepcionValidacionCCC;
import cuenta_bancaria.exc.ExcepcionValidacionDNI;
import cuenta_bancaria.exc.TitularDuplicado;
import cuenta_bancaria.obj.controller.Controller;
import cuenta_bancaria.obj.model.Cliente;
import cuenta_bancaria.obj.model.Sucursal;
import cuenta_bancaria.obj.model.Cuenta;
import cuenta_bancaria.obj.model.Usuario;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class CuentaBancariaMain {

    static Pattern ibanPatron = Pattern.compile("(ES[0-9]{2})");
    static Pattern entidadOficinaPatron = Pattern.compile("[0-9]{4}");
    static Pattern cuentaPatron = Pattern.compile("[0-9]{10}");

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings({"null", "UnusedAssignment"})
    public static void main(String[] args) {
        Controller controller = new Controller();
        Scanner sc = new Scanner(System.in);
        Cuenta cuenta = null;
        String iban;
        int entidad;
        int oficina;
        int nCuenta;
        byte dc=0;
        Calendar fecha;
        Object[] data;
        int opt;
        boolean salir = false;
        String mensajeBienvenida = null;
        String dniStr;
        String nCuentaStr;
        Object dni;
        String[] cccArray = new String[0];
        while (!salir) {
            try {
                if (cuenta == null) {
                    System.out.println("1)dar de alta cliente\n2)dar de alta cuenta"
                            + "\n3)consultar cuenta\n4)gestionar cuenta\n5)acceder cuenta\n6)salir");
                    opt = sc.nextInt();
                    switch (opt) {
                        case 1:
                            Object[] datos = controller.menuIniTitular();
                            Usuario u = new Cliente((String) datos[0], (String) datos[1],
                                    (String) datos[2], (int) datos[3],  datos[4],
                                    (Usuario.Sexo) datos[5]);
                            Sucursal.darAltaCliente((Cliente)u);
                            break;
                        case 2:
                            sc.nextLine();
                            System.out.println("introducir dni");
                            dniStr = sc.nextLine();
                            dni = Cliente.getDninstance(dniStr);
                            Sucursal.darAltaCuenta(dni);
                            break;
                        case 3:
                            sc.nextLine();
                            System.out.println("introducir dni");
                            dniStr = sc.nextLine();
                            dni = Cliente.getDninstance(dniStr);
                            System.out.println(Sucursal.consultCuenta(dni));

                            break;
                        case 4:
                            sc.nextLine();
                            System.out.println("introducir dni");
                            dniStr = sc.nextLine();
                            iban = "ES25";
                            entidad = 3321;
                            oficina = 2020;
                            System.out.println("intoducir digito de control");
                            System.out.print(iban+"-"+entidad+"-"+oficina+"-");
                            dc = sc.nextByte();
                            sc.nextLine();
                            System.out.println("intoducir nº cuenta");
                            System.out.print(iban+"-"+entidad+"-"+oficina+"-"+dc+"-");
                            nCuentaStr = sc.nextLine();
                            System.out.println("");
                            nCuenta = Integer.parseInt(nCuentaStr);
                            cuenta = Sucursal.accederCuenta(Cliente.getDninstance(dniStr),
                                    Cuenta.getCCC(iban, entidad, oficina,dc, nCuenta));
                            if (cuenta.getEstado().equals(Cuenta.Estado.ACTIVA)) {
                                System.out.println("1)desactivar cuenta\n2)salir");
                                opt = sc.nextInt();
                                switch (opt) {
                                    case 1:
                                        cuenta.setEstado(Cuenta.Estado.INACTIVA);
                                        break;
                                    case 2:
                                        cuenta = null;
                                        break;
                                    default:
                                        throw new AssertionError();
                                }
                            } else if (cuenta.getEstado().equals(Cuenta.Estado.INACTIVA)) {
                                System.out.println("1)activar cuenta\n2)bloquear cuenta\n3)salir");
                                opt = sc.nextInt();
                                switch (opt) {
                                    case 1:
                                        cuenta.setEstado(Cuenta.Estado.ACTIVA);
                                        break;
                                    case 2:
                                        cuenta.setEstado(Cuenta.Estado.BLOQUEADA);
                                        break;
                                    case 3:

                                        cuenta = null;
                                        break;
                                    default:
                                        throw new AssertionError();
                                }
                            }
                            cuenta = null;
                            break;
                        case 5:
                            sc.nextLine();
                            System.out.println("introducir dni");
                            dniStr = sc.nextLine();

                            iban = "ES25";
                            entidad = 3321;
                            oficina = 2020;
                            System.out.println("intoducir nº cuenta");
                            System.out.print(iban+"-"+entidad+"-"+oficina+"-00-");
                            nCuentaStr = sc.nextLine();
                            System.out.println("");
                            nCuenta = Integer.parseInt(nCuentaStr);
                            cuenta = Sucursal.accederCuenta(Cliente.getDninstance(dniStr), Cuenta.getCCC(iban, entidad, oficina,dc, nCuenta));
                            break;
                        case 6:
                            salir = true;
                            break;
                        default:
                            throw new AssertionError();
                    }
                } else if ((cuenta != null)) {
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
                                            cuenta.setMovimiento(Cuenta.getAsunto(opt), null, cuantia, fecha);
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
                                            cuenta.setMovimiento(Cuenta.getAsunto(opt), null, cuantia, fecha);
                                            break;
                                        default:
                                            throw new AssertionError();
                                    }
                                    break;
                                case 3:
                                    sc.nextLine();
                                    System.out.println("introducir dni");
                                    dniStr = sc.nextLine();
                                    
                                    
                                    System.out.println("introducir CCC destinatario");
                                    nCuentaStr = sc.nextLine();
                                    cccArray = nCuentaStr.split("-");
                                    Object ccc = Cuenta.getCCC(cccArray[0],
                                            Integer.parseInt(cccArray[1]),
                                            Integer.parseInt(cccArray[2]),
                                            Byte.parseByte(cccArray[3]),
                                            Integer.parseInt(cccArray[4]));
                                    System.out.println("introducir cuantia");
                                    int cuantia = sc.nextInt();
                                    Sucursal.transfererirFondos(Cliente.getDninstance(dniStr), cuenta
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
                                    switch (movimiento) {
                                        case 0:
                                        case 1:
                                        case 2:
                                        case 3:
                                            System.out.println(cuenta.movimientosPorAsunto(
                                                    Cuenta.getAsunto(movimiento)));
                                            break;
                                        default:
                                            throw new AssertionError();
                                    }

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
                            switch (opt) {
                                case 1:
                                    try {
                                        cuenta = new Cuenta((Set) controller.menuIniTitulares(cuenta), cuenta);
                                    } catch (ExcepcionValidacionDNI | AssertionError e) {
                                    }

                                    break;
                                case 2:

                                    data = controller.menuIniCuenta();
                                    iban = (String) data[1];
                                    entidad = (Integer) data[2];
                                    oficina = (Integer) data[3];
                                    dc = (Byte)data[4];
                                    nCuenta = (Integer) data[5];
                                    cuenta = new Cuenta(iban, entidad, oficina
                                            , dc, nCuenta, cuenta);
                                    cuenta.vincularCuenta();

                                    break;
                                case 3:
                                    cuenta = null;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case BLOQUEADA:
                            int cod = 1234;
                            System.out.println("Su cuenta esta bloqueada, para desbloquearla ponga el codigo de deabloqueo");
                            System.out.print(":");
                            opt = sc.nextInt();
                            if (cod == opt) {
                                cuenta.setEstado(Cuenta.Estado.ACTIVA);
                                System.out.println("Cuenta activa");
                            }
                        default:
                            break;
                    }

                } else {
                    System.out.println("Hemos detectado que falta por introducir datos "
                            + "importantes en su cuenta.");

                }

            } catch (AssertionError | ExcepcionValidacionDNI
                    | IllegalArgumentException | InputMismatchException 
                    | TitularDuplicado | ExcepcionValidacionCCC e) {
                if (e instanceof AssertionError) {
                    System.out.println("opcion incorrecta");
                } else if (e instanceof ExcepcionValidacionDNI) {
                    System.out.println("DNI incorrecto");
                } else if (e instanceof IllegalArgumentException) {
                    System.out.println("Campo incorrecto");
                } else if (e instanceof InputMismatchException) {
                    System.out.println("tipo incorrecto");
                    sc.nextLine();
                } else if (e instanceof TitularDuplicado) {
                    System.out.println("este titular ya existe");
                } else if(e instanceof ExcepcionValidacionCCC){
                    System.out.println("ccc no válido");
                }

            }
        }
    }

}
