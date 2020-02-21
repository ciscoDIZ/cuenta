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
public class Operador extends Usuario<Integer>{
    private final int ID;
    
    public Operador(String nombre, String apellido1, String apellido2, int edad, Sexo sexo,int pin, int ID) {
        super(nombre, apellido1, apellido2, edad, sexo, pin);
        this.ID = ID;
    }

    @Override
    public CuentaCliente accederCuenta(Integer numbreUsuario, int pin) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
