/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import cuenta_bancaria.exc.ExcepcionValidacionDNI;
import cuenta_bancaria.obj.model.Cliente.DNI;
import java.util.HashSet;

/**
 *
 * @author tote
 */
public class Cliente extends Usuario<DNI> {

    

    static class DNI {

        private char letra;
        private int num;
        private static final char[] LETRAS = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F',
            'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

        public DNI(char letra, int num) throws ExcepcionValidacionDNI {
            if (validar(letra, num)) {
                this.letra = letra;
                this.num = num;
            } else {
                throw new ExcepcionValidacionDNI();
            }
        }

        public DNI(String dni) throws ExcepcionValidacionDNI {
            String[] numDni = dni.split(" |-");
            if (numDni.length < 2) {
                if (validar(dni.substring(8, 9).charAt(0),
                         Integer.parseInt(numDni[0].substring(0, 8)))) {
                    letra = dni.substring(8, 9).charAt(0);
                    num = Integer.parseInt(dni.substring(0, 8));
                } else {
                    throw new ExcepcionValidacionDNI();
                }
            } else {
                if (validar(numDni[1].toUpperCase().charAt(0),
                        Integer.parseInt(numDni[0]))) {
                    letra = numDni[1].toUpperCase().charAt(0);
                    num = Integer.parseInt(numDni[0]);
                } else {
                    throw new ExcepcionValidacionDNI();
                }
            }

        }
 
        private boolean validar(char letra, int num) {
            return LETRAS[num % LETRAS.length] == letra;
        }

        @Override
        public String toString() {
            return num + "" + letra;
        }

        @Override
        public boolean equals(Object obj) {
            boolean retorno = false;
            if (obj instanceof DNI) {
                retorno = this.hashCode() == obj.hashCode();
            }
            return retorno;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + this.letra;
            hash = 97 * hash + this.num;
            return hash;
        }

        public int getInt() {
            return num;
        }

    }
    private HashSet<CuentaCliente> cuentas;
    private DNI dni;
    private int codAcceso;
    private final String NOMBRE_USUARIO;

    public Cliente(String nombre, String apellido1, String apellido2, int edad, Object dni, Sexo sexo, int pin) {
        super(nombre, apellido1, apellido2, edad, sexo, pin);
        this.cuentas = new HashSet<>();
        this.dni = (Cliente.DNI)dni;
        NOMBRE_USUARIO = this.dni.toString();
    }
    @Override
    public CuentaCliente accederCuenta(DNI numbreUsuario, int pin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @SuppressWarnings("NonPublicExported")
    public Cliente.DNI getDni(){
        return dni;
    }
    @SuppressWarnings("NonPublicExported")
    public static Cliente.DNI getDninstance(String dni) throws ExcepcionValidacionDNI{
        return new Cliente.DNI(dni);
    }
    public boolean addCuenta(CuentaCliente c) {
        return cuentas.add(c);
    }

    public void setCuentas(HashSet<CuentaCliente> cuentas) {
        this.cuentas = cuentas;
    }

    public HashSet<CuentaCliente> getCuentas() {
        return cuentas;
    }

    public int getCodAcceso() {
        return codAcceso;
    }

    public void setCodAcceso(int codAcceso) {
        this.codAcceso = codAcceso;
    }

    public String getNOMBRE_USUARIO() {
        return NOMBRE_USUARIO;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.dni.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retrno = false;
        if (obj instanceof Usuario) {
            retrno = this.hashCode() == ((Usuario) obj).hashCode();
        }
        return retrno;
    }
    
    @Override
    public String toString() {
        String nCuentas = "";
        nCuentas = cuentas.stream()
                .map((cuenta) -> cuenta.getNumCuenta() + "\n")
                .reduce(nCuentas, String::concat);
        return nombre + " " + apellido1 + " " + apellido2 + "\ndni: " + dni + "\ncuentas:" + nCuentas;
    }
}
