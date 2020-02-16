/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import cuenta_bancaria.exc.TitularDuplicado;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 *
 * @author tote
 */
public class Banco {
    
    private static HashMap<Usuario,ArrayList<Cuenta>> clientes = new HashMap<>();
    
    
    public static boolean darAltaCliente(Usuario c) throws TitularDuplicado{
        boolean retorno = false;
        if(!clientes.containsKey(c)){
            clientes.put(c, new ArrayList<>());
            retorno= true;
        }else{
            throw new TitularDuplicado("titular duplicado");
        }
        return retorno;
    }
    public static boolean darAltaCuenta( DNI... dni){
        boolean retorno = false;
        HashSet<Usuario> titulares = new HashSet<>();
        for (DNI dni1 : dni) {
            if(clientes.containsKey(buscarCliente(dni1))){
                for (Usuario usuario : clientes.keySet()) {
                    titulares.add(usuario);
                }
            }
        }
        Cuenta c = new Cuenta(titulares);
        c.vincularCuenta();
        for (DNI dni1 : dni) {
            clientes.get(buscarCliente(dni1)).add(c);
        }
        return retorno;
    }
    public static Usuario buscarCliente(DNI dni){
       Usuario u = null;
       for (Usuario usuario : clientes.keySet()) {
            if(usuario.getDni().equals(dni)){
                u = usuario;
            }
        }
       return u;
    }
    public static String consultCuenta(String nCuenta){
        String retorno="";
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : clientes.entrySet()) {
            retorno = entry.getValue().stream()
                    .filter((c) -> c.getNumCuenta().equals(nCuenta))
                    .findFirst()
                    .get().mostrarDatos();
        }
        return retorno;
    }
    public static Cuenta accederCuenta(DNI dni, Cuenta.CCC ccc){
        Cuenta c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : clientes.entrySet()) {
            for (Cuenta cuenta : entry.getValue()) {
                if (entry.getKey().getDni().equals(dni) && cuenta.getCCC().equals(ccc)){
                    c =cuenta;
                }
            }
        }
        return c;
    }
    public boolean delCuenta(String nCuenta){
        return true;
    }

    public static HashMap<Usuario, ArrayList<Cuenta>> getClientes() {
        return clientes;
    }

    public static void setClientes(HashMap<Usuario, ArrayList<Cuenta>> clientes) {
        Banco.clientes = clientes;
    }
    
}
