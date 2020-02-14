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
    public static enum Sexo{
        MUJER,
        HOMBRE
    }
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int edad;
    private DNI dni;
    private Sexo sexo;
    private ArrayList<Cuenta> cuentas;

    public Usuario(String nombre, String apellido1, String apellido2, int edad
            , DNI dni, Sexo sexo) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.dni = dni;
        this.sexo = sexo;
        this.cuentas = new ArrayList<>();
        
    }
    
    public boolean addCuenta(Cuenta c){
        return cuentas.add(c);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public DNI getDni() {
        return dni;
    }

    public void setDni(DNI dni) {
        this.dni = dni;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(ArrayList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    @Override
    public String toString() {
        String nCuentas = "";
        for (Cuenta cuenta : cuentas) {
            nCuentas += cuenta.getNumCuenta()+"\n";
        }
        return nombre + " " + apellido1 + " " + apellido2 + "\ndni: " + dni +"\ncuentas:"+nCuentas;
    }
     
    
}
