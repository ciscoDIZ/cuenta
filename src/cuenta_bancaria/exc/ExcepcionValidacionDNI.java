/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.exc;

/**
 *
 * @author tote
 */
public class ExcepcionValidacionDNI extends Exception{
    public ExcepcionValidacionDNI() {
        super();
    }
    public ExcepcionValidacionDNI(String message) {
        super(message);
    }
}
