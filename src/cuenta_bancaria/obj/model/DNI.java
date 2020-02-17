/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import cuenta_bancaria.exc.ExcepcionValidacionDNI;

/**
 *
 * @author tote
 */
public class DNI {

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
            if (validar(dni.substring(8,9).charAt(0)
                    , Integer.parseInt(numDni[0].substring(0, 8)))) {
                letra = dni.substring(8,9).charAt(0);
                num = Integer.parseInt(dni.substring(0, 8));
            }else{
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
        if(obj instanceof DNI){
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
    public int getInt(){
        return num;
    }
    
}
