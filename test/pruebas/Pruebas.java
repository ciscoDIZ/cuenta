/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import java.util.Random;

/**
 *
 * @author tote
 */
public class Pruebas {
    public static void main(String[] args) {
        Random rnd = new Random();
        System.out.println(rnd.nextInt(Integer.MAX_VALUE-1000000000)+1000000000);
    }
}
