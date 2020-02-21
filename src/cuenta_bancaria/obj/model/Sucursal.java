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
        CuentaCliente c = new CuentaCliente(titulares,"1234");
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
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            if (entry.getKey() instanceof Cliente) {
                List<Cuenta> list = entry.getValue();
                ArrayList<CuentaCliente> al = new ArrayList<>();
                entry.getValue().stream()
                        .filter((cuenta) -> (cuenta instanceof CuentaCliente))
                        .forEach((cuenta) -> {
                            al.add((CuentaCliente) cuenta);
                        });
                retorno = al.stream()
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
                    ArrayList<CuentaCliente> al = new ArrayList<>();
                    for (Cuenta cuenta : entry.getValue()) {
                        if (cuenta instanceof CuentaCliente) {
                            al.add((CuentaCliente) cuenta);
                        }
                    }
                    retorno = al.stream()
                            .map((cuenta) -> cuenta
                            .getNumCuenta() + "\n")
                            .reduce(retorno, String::concat);
                }
            }

        }
        return retorno;
    }

    public static CuentaCliente accederCuenta(Object dni, Object ccc) {
        CuentaCliente c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            for (Cuenta cuenta : entry.getValue()) {
                if (entry.getKey() instanceof Cliente && cuenta instanceof CuentaCliente) {
                    if (((Cliente) entry.getKey()).getDni().equals(dni) && ((CuentaCliente) cuenta).getCCC()
                            .equals(((CuentaCliente.CCC) ccc))) {
                        c = ((CuentaCliente) cuenta);
                    }
                }

            }
        }
        return c;
    }

    public static CuentaCliente accederCuenta(Object ccc) {
        CuentaCliente c = null;
        for (Map.Entry<Usuario, ArrayList<Cuenta>> entry : usuarios.entrySet()) {
            for (Cuenta cuenta : entry.getValue()) {
                if (cuenta instanceof CuentaCliente) {
                    if (((CuentaCliente)cuenta).getCCC().equals((CuentaCliente.CCC) ccc)) {
                        c = (CuentaCliente)cuenta;
                    }
                }
            }
        }
        return c;

    }

    public static CuentaCliente cambiarTitularCuenta(Object antiguo, Object nuevo, Object ccc) {
        //TODO
        CuentaCliente c = accederCuenta(antiguo, ccc);
        return c;
    }

    public static CuentaCliente cambiarCodigoCuenta(Object ccc) {
        CuentaCliente c = null;
        //TODO
        return c;
    }

    public static boolean transfererirFondos(Cliente.DNI titOrigen, CuentaCliente.CCC origen, Object destino, double cuantia) {
        boolean retorno = false;
        CuentaCliente cOrigen = accederCuenta(titOrigen, ((CuentaCliente.CCC) origen));
        CuentaCliente cDestino = accederCuenta((CuentaCliente.CCC) destino);
        cOrigen.setMovimiento(CuentaCliente.getAsunto(2), "transferencia", cuantia, null);
        cDestino.setMovimiento(CuentaCliente.getAsunto(0), "transferencia", cuantia, null);
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
