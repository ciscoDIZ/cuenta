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
public class CuentaOnlineOperador extends CuentaOnline<Operador,CuentaOnlineOperador>{

    public CuentaOnlineOperador(Operador op) {
        super(op);
        account = this;
    }

    @Override
    public void activarCuentaOnline(String nombre, String contra) {
        user.nombreUsuario = nombre;
        user.contra = contra;
        estado = Estado.ACTIVA;
    }

    @Override
    public void login(String nombre, String contra) {
        account.login(nombre, contra);
    }

    @Override
    public void logout() {
        account.logout();
    }
    
}
