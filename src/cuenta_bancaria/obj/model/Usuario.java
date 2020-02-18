/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;


import java.util.Objects;

/**
 *
 * @author tote
 */
public abstract class Usuario {

    public static enum Sexo {
        MUJER,
        HOMBRE;
    }
    protected String nombre;
    protected String apellido1;
    protected String apellido2;
    protected int edad;
    protected Sexo sexo;
    protected int pin;
    public Usuario(String nombre, String apellido1, String apellido2, int edad,
             Sexo sexo, int pin) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.sexo = sexo;
        this.pin = pin;
    }

    public String getNombreCompleto() {
        return getNombre() + " " + getApellido1() + " " + getApellido2();
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

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.nombre);
        hash = 97 * hash + Objects.hashCode(this.apellido1);
        hash = 97 * hash + Objects.hashCode(this.apellido2);
        hash = 97 * hash + this.edad;
        hash = 97 * hash + Objects.hashCode(this.sexo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retorno = false;
        if(obj instanceof Usuario){
            retorno = ((Usuario)obj).hashCode() == this.hashCode();
        }
        return true;
    }

}
