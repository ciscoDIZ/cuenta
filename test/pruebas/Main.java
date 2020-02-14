/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import cuenta_bancaria.obj.model.Cuenta;
import cuenta_bancaria.obj.model.DNI;
import cuenta_bancaria.obj.model.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 *
 * @author tote
 */
public class Main {

    public static void main(String[] args) {

        HashMap<DNI, Usuario> mapa = new HashMap<>();
        try {
            DNI dni1 = new DNI("12345678Z");
            DNI dni2 = new DNI("78716585M");
            HashSet<Usuario> titulares = new HashSet();
            titulares.add(new Usuario("juanito", "de los palotes", "AAAAAAAA", 12, dni2, Usuario.Sexo.HOMBRE));
            titulares.add(new Usuario("juanito", "de los palotes", "AAAAAAAA", 12, dni1, Usuario.Sexo.HOMBRE));
            Cuenta c = new Cuenta(titulares);
           
            //System.out.println(c.mostrarTitular());
            c = new Cuenta("ES23", 1234, 1234, 1234567890, c);
            /* for (Usuario titulare : titulares) {
                titulare.addCuenta(c);
            }*/
            c.vincularCuenta();
            System.out.println(c.mostrarTitular());
            System.out.println(dni1.equals(dni2));
        } catch (Exception e) {
        }
        
    }
}
