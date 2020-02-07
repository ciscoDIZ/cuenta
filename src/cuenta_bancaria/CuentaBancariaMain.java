/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria;

import cuenta_bancaria.obj.Cuenta;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author tote
 */
public class CuentaBancariaMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Calendar fecha = new GregorianCalendar();
        System.out.println(fecha.get(Calendar.YEAR)+" "+fecha.get(Calendar.MONTH)+" "+ fecha.get(Calendar.DATE));
        Cuenta c = new Cuenta();
        c.setMovimiento(Cuenta.getAsunto(0), 10);
        System.out.println(c.mostrarDatos());
    }
    
}
