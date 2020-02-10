/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import java.util.ArrayList;

/**
 *
 * @author tote
 */
public class Usuario {
    public enum Sexo{
        MUJER,
        HOMBRE
    }
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int edad;
    private String dni;
    private Sexo sexo;
    private ArrayList<Cuenta> cuentas;

    public Usuario(String nombre, String apellido1, String apellido2, int edad
            , String dni, Sexo sexo, ArrayList<Cuenta> cuentas) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.dni = dni;
        this.sexo = sexo;
        this.cuentas = cuentas;
    }
    
    
}
