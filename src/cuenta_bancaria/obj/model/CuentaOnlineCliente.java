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
        account = user.getCoc();
        cuentasBancarias = new HashSet<>(user.getCuentas());
    }

    public String listarCuentas() {
        String lista = "";
        int i = 1;
        if (login != null) {
            for (CuentaBancaria cuentasBancaria : user.getCuentas()) {
                lista += (i++)+cuentasBancaria.getNumCuenta()+"\n";
            }
                    
        }
        return lista;
    }
    
    public CuentaBancaria accederCBN(String nCuenta){
        return cuentasBancarias.stream()
                .filter(c -> String.valueOf(c.getCCC().getCUENTA()).equals(nCuenta))
                .findFirst().get();
    }
    public void crearCBN(){
        Sucursal.darAltaCuenta(user.getDni());
    }
    @Override
    public void activarCuentaOnline(String nombre, String contra) {
        user.nombreUsuario = nombre;
        user.contra = contra;
        user.getCuentas().forEach((cuenta) -> {
            cuentasBancarias.add(cuenta);
        });
        user.getCoc().estado = Estado.ACTIVA;
    }
    
    @Override
    public void login(String nombre, String contra) throws Error{
        if (user.nombre.equals(nombre) 
                && user.contra.equals(contra)) {
            login = CuentaOnline.getLoginInstace(nombre, contra);
        }else{
            throw new Error();
        }

    }

    @Override
    public void logout() throws Error{
        if (login != null) {
            login = null;
        }else{
            throw new Error();
        }
    }

}
