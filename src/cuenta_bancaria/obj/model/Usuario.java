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
public abstract class Usuario<T> {

    public static enum Sexo {
        MUJER,
        HOMBRE;
    }
    protected String nombre;
    protected String apellido1;
    protected String apellido2;
    protected int edad;
    protected Sexo sexo;
    protected String nombreUsuario;
    protected String contra;
    
    
    public Usuario(String nombre, String apellido1, String apellido2, int edad,
             Sexo sexo) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.edad = edad;
        this.sexo = sexo;
     
    }
    public abstract void accedercuenta(String n, String c);
    public abstract void salirCuenta();
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
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
        return retorno;
    }

}
