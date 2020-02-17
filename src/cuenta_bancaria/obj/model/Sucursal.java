/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import cuenta_bancaria.exc.TitularDuplicado;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author tote
 */
public class Sucursal {
    
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
    public static String consultCuenta(DNI dni){
        String retorno="";
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : clientes.entrySet()) {
            if(entry.getKey().getDni().equals(dni)){
                retorno = entry.getKey().getNombreCompleto()+"\n";
                for (Cuenta cuenta : entry.getValue()) {
                    retorno += cuenta.getNumCuenta()+"\n";
                }
            }
        }
        return retorno;
    }
    public static Cuenta accederCuenta(DNI dni, Object ccc){
        Cuenta c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : clientes.entrySet()) {
            for (Cuenta cuenta : entry.getValue()) {
                if (entry.getKey().getDni().equals(dni) && cuenta.getCCC().equals(((Cuenta.CCC)ccc))){
                    c =cuenta;
                }
            }
        }
        return c;
    }
    private static Cuenta accederCuenta(Cuenta.CCC ccc){
        Cuenta c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : clientes.entrySet()) {
            for (Cuenta cuenta : entry.getValue()) {
                if (cuenta.getCCC().equals(ccc)){
                    c =cuenta;
                }
            }
        }
        return c;

    }
    public static Cuenta cambiarTitularCuenta(DNI antiguo,DNI nuevo, Cuenta.CCC ccc){
        //TODO
        Cuenta c = accederCuenta(antiguo, ccc);
        return c;
    }
    public static Cuenta cambiarCodigoCuenta(Cuenta.CCC ccc){
        Cuenta c = null;
        //TODO
        return c;
    }
    public static boolean transfererirFondos(DNI titOrigen, Cuenta.CCC origen
            , Object destino, int cuantia){
        boolean retorno = false;
        Cuenta cOrigen = accederCuenta(titOrigen, origen);
        Cuenta cDestino = accederCuenta((Cuenta.CCC)destino);
        cOrigen.setMovimiento(Cuenta.getAsunto(2), "transferencia", cuantia, null);
        cDestino.setMovimiento(Cuenta.getAsunto(0), "transferencia", cuantia, null);
        return retorno;
    }
    public boolean delCuenta(String nCuenta){
        return true;
    }

    public static HashMap<Usuario, ArrayList<Cuenta>> getClientes() {
        return clientes;
    }

    public static void setClientes(HashMap<Usuario, ArrayList<Cuenta>> clientes) {
        Sucursal.clientes = clientes;
    }
    
}
