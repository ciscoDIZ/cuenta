
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
import cuenta_bancaria.obj.model.CuentaBancaria;
import cuenta_bancaria.obj.model.CuentaOnline;
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
        CuentaBancaria cuenta = null;
        int opt;
        boolean salir = false;
        CuentaOnline cuentaOnline = null;
        while (!salir) {
            if (controller.getInterfaz() != null) {
                try {
                    switch (controller.getInterfaz()) {
                        case WEB:
                            if (cuentaOnline == null) {
                                cuentaOnline = controller.menuLOG_REG(cuentaOnline);
                            }else{
                                cuentaOnline = controller.interfazOnline(cuentaOnline);
                            }
                            break;
                        case ESCRITORIO:
                            if (cuenta == null) {
                                cuenta = controller.menuInterfazEscritorio(cuenta, salir);
                            } else {
                                cuenta = controller.interfazCuentaBancaria(cuenta);
                            }
                            break;
                        default:
                            throw new AssertionError();
                    }
                } catch (AssertionError
                        | ExcepcionValidacionDNI
                        | IllegalArgumentException
                        | InputMismatchException
                        | TitularDuplicado
                        | ExcepcionValidacionCCC
                        | ArrayIndexOutOfBoundsException e) {
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
                    } else if (e instanceof ExcepcionValidacionCCC) {
                        System.out.println("ccc no válido");
                    }

                }
            } else {
                System.out.println("1)interfaz web\n2)interfaz de escritorio");
                opt = sc.nextInt();
                sc.nextLine();
                controller.seleccionarInterfaz(opt - 1);
            }
        }
    }
}
