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
public class CuentaInactiva extends Exception{
    /**
     * Excepcion que se produce al detectar que se esta intntando acceder al 
     * sistema con una cuenta inactiva
     */
    public CuentaInactiva() {
        super();
    }
    public CuentaInactiva(String message) {
        super(message);
    }
}
