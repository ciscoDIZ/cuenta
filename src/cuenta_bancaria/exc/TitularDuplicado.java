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
public class TitularDuplicado extends Exception{

    public TitularDuplicado() {
    }
    
    public TitularDuplicado(String message) {
        super(message);
    }
    
}
