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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author tote
 */
public class Sucursal {

    public static enum UsuarioTipo {
        OPERADOR,
        CLIENTE
    }
    private static HashMap<Usuario, ArrayList<CuentaBancaria>> usuarios = new HashMap<>();

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

        CuentaBancaria c = new CuentaBancaria(titulares, "1234");
        c.vincularCuenta();
        for (Object dni1 : dni) {
            usuarios.get(buscarCliente((Cliente.DNI) dni1)).add(c);
        }
        return retorno;
    }

    public Usuario accederUsuario() {
        return null;
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
        for (Map.Entry<Usuario, ArrayList<CuentaBancaria>> entry : usuarios.entrySet()) {
            retorno = entry.getValue().stream()
                    .filter((c) -> c.getNumCuenta().equals(nCuenta))
                    .findFirst()
                    .get().mostrarDatos();
        }
        return retorno;
    }

    /*public static CuentaBancaria accederCuentaCliente(Object dni, int pin){
        
    }*/
    public static String consultCuenta(Object dni) {
        String retorno = "";

        for (Map.Entry<Usuario, ArrayList<CuentaBancaria>> entry : usuarios.entrySet()) {
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

    public static CuentaBancaria accederCuenta(Object usuario, Object ccc) {
        CuentaBancaria c = null;
        for (Map.Entry<Usuario, ArrayList<CuentaBancaria>> entry : usuarios.entrySet()) {
            for (CuentaBancaria cuenta : entry.getValue()) {

                if (((Cliente) entry.getKey()).getDni().equals(usuario)
                        && ((CuentaBancaria) cuenta).getCCC()
                                .equals(((CuentaBancaria.CCC) ccc))) {
                    c = ((CuentaBancaria) cuenta);
                }
            }

        }
        return c;
    }

    public static CuentaBancaria accederCuenta(Object ccc) {
        CuentaBancaria c = null;
        for (Map.Entry<Usuario, ArrayList<CuentaBancaria>> entry : usuarios.entrySet()) {
            for (CuentaBancaria cuenta : entry.getValue()) {
                if (cuenta instanceof CuentaBancaria) {
                    if (((CuentaBancaria) cuenta).getCCC().equals((CuentaBancaria.CCC) ccc)) {
                        c = (CuentaBancaria) cuenta;
                    }
                }
            }
        }
        return c;

    }

    public static CuentaOnline accederCuentaOnline(String nombre, String contra) {

        CuentaOnline co = null;
        for (Usuario usuario : usuarios.keySet()) {
            if (usuario instanceof Cliente
                    && ((Cliente) usuario).getNombreUsuario().equals(nombre)) {
                co = ((Cliente) usuario).getCl();

            }

        }
        if (co != null) {
            co.login(nombre, contra);
        }
        return co;
    }

    public static CuentaBancaria cambiarTitularCuenta(Object antiguo, Object nuevo, Object ccc) {
        //TODO
        CuentaBancaria c = accederCuenta(antiguo, ccc);
        return c;
    }

    public static CuentaBancaria cambiarCodigoCuenta(Object ccc) {
        CuentaBancaria c = null;
        //TODO
        return c;
    }

    public static boolean transfererirFondos(Cliente.DNI titOrigen, CuentaBancaria.CCC origen, Object destino, double cuantia) {
        boolean retorno = false;
        CuentaBancaria cOrigen = accederCuenta(titOrigen, ((CuentaBancaria.CCC) origen));
        CuentaBancaria cDestino = accederCuenta((CuentaBancaria.CCC) destino);
        cOrigen.setMovimiento(CuentaBancaria.getAsunto(2), "transferencia", cuantia, null);
        cDestino.setMovimiento(CuentaBancaria.getAsunto(0), "transferencia", cuantia, null);
        return retorno;
    }

    public boolean delCuenta(String nCuenta) {
        return true;
    }

    public static HashMap<Usuario, ArrayList<CuentaBancaria>> getClientes() {
        return usuarios;
    }

    public static void setClientes(HashMap<Usuario, ArrayList<CuentaBancaria>> clientes) {
        Sucursal.usuarios = clientes;
    }

}
