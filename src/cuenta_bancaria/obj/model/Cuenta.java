/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

/**
 *
 * @author tote
 */
public abstract class Cuenta {
    
    protected Usuario u;
    protected String contra;

    public Cuenta(String contra) {
        this.contra = contra;
    }
    
}
