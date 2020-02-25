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
public abstract class Cuenta<T> {
    
    protected T user;
    protected String contra;

    public Cuenta(String contra) {
        this.contra = contra;
    }

    
    
    
    public T getU() {
        return user;
    }

    public void setU(T u) {
        this.user = u;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }
    
}
