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
         'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K'};

    public DNI(char letra, int num) throws ExcepcionValidacionDNI {
        if (validar(letra, num)) {
            this.letra = letra;
            this.num = num;
        } else {
            throw new ExcepcionValidacionDNI();
        }
    }
    public DNI(String dni){
        String[] numDni = dni.split(" ");
        letra = numDni[1].toUpperCase().charAt(0);
        num = Integer.parseInt(numDni[0]);
    }
    private boolean validar(char letra, int num) {
        return LETRAS[num % LETRAS.length] == letra;
    }

    @Override
    public String toString() {
        return num +""+ letra;
    }
    
}
