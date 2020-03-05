/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import java.util.HashMap;

/**
 *
 * @author tote
 */
public class Operador extends Usuario<CuentaOnlineOperador>{
    private final int IDENTIFICACION;
    
    private CuentaOnlineOperador co;
    
    public Operador(String nombre, String apellido1, String apellido2, int edad, Sexo sexo, String contra, int ID) {
        super(nombre, apellido1, apellido2, edad, sexo);
        this.IDENTIFICACION = ID;
        co = new CuentaOnlineOperador(this);
        
    }

    public int getIDENTIFICACION() {
        return IDENTIFICACION;
    }

    public CuentaOnlineOperador getCo() {
        return co;
    }

    @Override
    public void accedercuenta(String n, String c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void salirCuenta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
