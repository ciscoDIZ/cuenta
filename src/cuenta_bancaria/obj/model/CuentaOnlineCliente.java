/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author tote
 */
public class CuentaOnlineCliente extends CuentaOnline<Cliente, CuentaOnlineCliente> {

    private Set<CuentaBancaria> cuentasBancarias;

    public CuentaOnlineCliente(Cliente user) {
        super(user);
        account = this.user.getCl();
        cuentasBancarias = new HashSet<>(user.getCuentas());
    }

    public String listarCuentas() {
        String lista = "";
        int i = 1;
        if (account.login != null) {
            lista = cuentasBancarias.stream()
                    .map(c -> c.getNumCuenta())
                    .reduce((i++) + lista + "\n", String::concat);
        }
        return lista;
    }
    
    public CuentaBancaria accederCBN(String nCuenta){
        return cuentasBancarias.stream()
                .filter(c -> c.getNumCuenta().equals(nCuenta))
                .findFirst().get();
    }

    @Override
    public void activarCuentaOnline(String nombre, String contra) {
        user.nombreUsuario = nombre;
        user.contra = contra;
        
    }
    
    @Override
    public void login(String nombre, String contra) throws Error{
        if (account.user.nombre.equals(nombre) 
                && account.user.contra.equals(contra)) {
            account.login(nombre, contra);
        }else{
            throw new Error();
        }

    }

    @Override
    public void logout() throws Error{
        if (account.login != null) {
            account.logout();
        }else{
            throw new Error();
        }
    }

}
