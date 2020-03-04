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
public class Operador extends Usuario<CuentaOperador>{
    private final int IDENTIFICACION;
    private CuentaOperador co;
    
    public Operador(String nombre, String apellido1, String apellido2, int edad, Sexo sexo, String contra, int ID, String nombreUsuario) {
        super(nombre, apellido1, apellido2, edad, sexo, nombreUsuario, contra);
        this.IDENTIFICACION = ID;
        co = new CuentaOperador(this);
        
    }

    @Override
    public CuentaOperador accedercuenta(String n, String c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
