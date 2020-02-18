/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import java.util.HashSet;

/**
 *
 * @author tote
 */
public class Cliente extends Usuario {

    private HashSet<Cuenta> cuentas;

    public Cliente(String nombre, String apellido1, String apellido2, int edad, DNI dni, Sexo sexo) {
        super(nombre, apellido1, apellido2, edad, dni, sexo);
        this.cuentas = new HashSet<>();
    }

    public boolean addCuenta(Cuenta c) {
        return cuentas.add(c);
    }

    public void setCuentas(HashSet<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public HashSet<Cuenta> getCuentas() {
        return cuentas;
    }

    @Override
    public String toString() {
        String nCuentas = "";
        nCuentas = cuentas.stream()
                .map((cuenta) -> cuenta.getNumCuenta() + "\n")
                .reduce(nCuentas, String::concat);
        return nombre + " " + apellido1 + " " + apellido2 + "\ndni: " + dni + "\ncuentas:" + nCuentas;
    }
}
