/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria;

import cuenta_bancaria.exc.ExcepcionValidacionDNI;
import cuenta_bancaria.obj.controller.Controller;
import cuenta_bancaria.obj.model.Cuenta;
import cuenta_bancaria.obj.model.Usuario;
import java.util.ArrayList;
import java.util.Calendar;
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

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("null")
    public static void main(String[] args) {
        Controller controller = new Controller();
        Scanner sc = new Scanner(System.in);
        Cuenta cuenta = null;
        ArrayList<Usuario> titular;
        Double saldo;
        String iban;
        int entidad;
        int oficina;
        long nCuenta;
        Calendar fecha;
        Object[] data = null;
        //TODO
        String mensajeMenu;
        int opt;
        boolean salir = false;
        String mensajeBienvenida = null;
        while (!salir) {
            if ((cuenta != null)) {
                switch (cuenta.getEstado()) {
                    case ACTIVA:
                        String opciones = "1)realizar ingreso\n2)realizar retirada"
                                + "\n3)mostrar datos\n4)búsqueda por fecha"
                                + "\n5)búsqueda por asunto\n6)mostrar opciones"
                                + "\n7)desactivar cuenta\n8)bloqueat cuenta\n9)salir";
                        if (mensajeBienvenida == null) {
                            mensajeBienvenida = "Bienvenido la cuenta de "
                                    + " estado: " + cuenta.getEstado().toString()
                                            .toLowerCase();
                            System.out.println(mensajeBienvenida);
                            System.out.println(opciones);
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
                                System.out.println(cuenta.mostrarDatos());
                                break;
                            case 4:
                                sc.nextLine();
                                System.out.println("Introducir fecha");
                                String fechaStr = sc.nextLine();
                                System.out.println(cuenta.movimientosPorFecha(fechaStr));
                                break;
                            case 5:
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
                            case 6:
                                System.out.println(opciones);
                                break;
                            case 7:
                                cuenta.setEstado(Cuenta.Estado.INACTIVA);
                                break;
                            case 8:
                                cuenta.setEstado(Cuenta.Estado.BLOQUEADA);
                                break;
                            case 9:
                                salir = true;
                                break;
                            default:
                                throw new AssertionError();

                        }
                        if (mensajeBienvenida != null && opt != 6) {
                            System.out.println("6)mostrar opciones");
                        }
                        break;
                    case INACTIVA:
                        System.out.println("1)activar cuenta\n2)cambiar titular/es\n3)cambiar número cuenta");
                        opt = sc.nextInt();
                        switch (opt) {
                            case 1:
                                cuenta.setEstado(Cuenta.Estado.ACTIVA);
                                System.out.println("Cuenta activa");
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
                while (cuenta == null) {
                    try {
                        if (data == null) {
                            data = controller.menuIniCuenta();
                        }
                        titular = controller.menuIniTitulares();
                        iban = (String) data[1];
                        entidad = (Integer) data[2];
                        oficina = (Integer) data[3];
                        nCuenta = (Long) data[4];
                        cuenta = new Cuenta(titular, 0.0, iban, entidad, oficina,
                                nCuenta);
                        cuenta.getTITULARES()[0].addCuenta(cuenta);
                    } catch (IllegalArgumentException | ExcepcionValidacionDNI e) {
                        if (e instanceof IllegalArgumentException) {
                            System.out.println("Campo erroneo");
                        } else if (e instanceof ExcepcionValidacionDNI) {
                            System.out.println("DNI icorrecto");
                        }
                    }
                }

            }

        }

        System.out.println(cuenta.getEstado());
    }

}
