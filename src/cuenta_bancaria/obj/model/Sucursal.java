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

    public static enum UsuarioTipo {
        OPERADOR,
        CLIENTE
    }
    private static HashMap<Usuario, ArrayList<Cuenta>> usuarios = new HashMap<>();

    private static final UsuarioTipo[] TIPOS = UsuarioTipo.values();
    private UsuarioTipo tipo;

    public static boolean darAltaUsuario(Usuario c) throws TitularDuplicado {
        boolean retorno = false;
        if (!usuarios.containsKey(c)) {
            usuarios.put(c, new ArrayList<>());
            retorno = true;
        } else {
            throw new TitularDuplicado("titular duplicado");
        }
        return retorno;
    }

   

    public static boolean darAltaCuenta(Object... dni) {
        boolean retorno = false;
        HashSet<Cliente> titulares = new HashSet<>();
        for (Object dni1 : dni) {
            if (usuarios.containsKey(buscarCliente((Cliente.DNI) dni1))) {
                usuarios.keySet().forEach((usuario) -> {
                    titulares.add((Cliente) usuario);
                });
            }
        }
        Cuenta c = new Cuenta(titulares);
        c.vincularCuenta();
        for (Object dni1 : dni) {
            usuarios.get(buscarCliente((Cliente.DNI) dni1)).add(c);
        }
        return retorno;
    }

    public static Cliente accederComoCliente(String nUsu, int pin) {
        Cliente c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            if(entry.getKey() instanceof Cliente){
                if(((Cliente)entry.getKey()).getNOMBRE_USUARIO().equals(nUsu)){  
                    if(((Cliente)entry.getKey()).getCodAcceso() == pin){
                        c = ((Cliente)entry.getKey());
                    }
                }
            }
        }
        return c;
    }

    public static Operador accederComoOperador(){
        Operador c = null;
        return new Operador(null, null, null, 0, null,0,0);
    }
    
    public static Cliente buscarCliente(Object dni) {
        Cliente u = null;
        for (Usuario usuario : usuarios.keySet()) {
            if (usuario instanceof Cliente) {
                if (((Cliente) usuario).getDni().equals((Cliente.DNI) dni)) {
                    u = (Cliente) usuario;
                }
            }
        }
        return u;
    }

    public static String consultCuenta(String nCuenta) {
        String retorno = "";
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            if (entry.getKey() instanceof Cliente) {
                retorno = entry.getValue().stream()
                        .filter((c) -> c.getNumCuenta().equals(nCuenta))
                        .findFirst()
                        .get().mostrarDatos();
            }

        }
        return retorno;
    }

    public static String consultCuenta(Object dni) {
        String retorno = "";
        Cliente cliente = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            if (entry.getKey() instanceof Cliente) {
                if (((Cliente) entry.getKey()).getDni().equals(dni)) {
                    retorno = entry.getKey().getNombreCompleto() + "\n";
                    retorno = entry.getValue().stream()
                            .map((cuenta) -> cuenta
                            .getNumCuenta() + "\n")
                            .reduce(retorno, String::concat);
                }
            }

        }
        return retorno;
    }

    public static Cuenta accederCuenta(Object dni, Object ccc) {
        Cuenta c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            for (Cuenta cuenta : entry.getValue()) {
                if (entry.getKey() instanceof Cliente) {
                    if (((Cliente) entry.getKey()).getDni().equals(dni) && cuenta.getCCC()
                            .equals(((Cuenta.CCC) ccc))) {
                        c = cuenta;
                    }
                }

            }
        }
        return c;
    }

    private static Cuenta accederCuenta(Cuenta.CCC ccc) {
        Cuenta c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            for (Cuenta cuenta : entry.getValue()) {
                if (cuenta.getCCC().equals(ccc)) {
                    c = cuenta;
                }
            }
        }
        return c;

    }

    public static Cuenta cambiarTitularCuenta(Object antiguo, Object nuevo, Object ccc) {
        //TODO
        Cuenta c = accederCuenta(antiguo, ccc);
        return c;
    }

    public static Cuenta cambiarCodigoCuenta(Object ccc) {
        Cuenta c = null;
        //TODO
        return c;
    }

    public static boolean transfererirFondos(Cliente.DNI titOrigen, Cuenta.CCC origen, Object destino, double cuantia) {
        boolean retorno = false;
        Cuenta cOrigen = accederCuenta(titOrigen, ((Cuenta.CCC) origen));
        Cuenta cDestino = accederCuenta((Cuenta.CCC) destino);
        cOrigen.setMovimiento(Cuenta.getAsunto(2), "transferencia", cuantia, null);
        cDestino.setMovimiento(Cuenta.getAsunto(0), "transferencia", cuantia, null);
        return retorno;
    }

    public boolean delCuenta(String nCuenta) {
        return true;
    }

    public static HashMap<Usuario, ArrayList<Cuenta>> getClientes() {
        return usuarios;
    }

    public static void setClientes(HashMap<Usuario, ArrayList<Cuenta>> clientes) {
        Sucursal.usuarios = clientes;
    }

}
