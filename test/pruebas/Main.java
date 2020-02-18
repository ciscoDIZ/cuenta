/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import cuenta_bancaria.exc.ExcepcionValidacionCCC;
import cuenta_bancaria.exc.ExcepcionValidacionDNI;
import cuenta_bancaria.exc.TitularDuplicado;
import cuenta_bancaria.obj.model.Cliente;
import cuenta_bancaria.obj.model.Sucursal;
import cuenta_bancaria.obj.model.Cuenta;
import cuenta_bancaria.obj.model.DNI;
import cuenta_bancaria.obj.model.Usuario;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

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
            HashSet<Cliente> titulares = new HashSet();
            titulares.add(new Cliente("Juanito", "de los palotes", "AAAAAAAA", 12, dni2, Usuario.Sexo.HOMBRE));
            titulares.add(new Cliente("Pepito", "de los palotes", "AAAAAAAA", 12, dni1, Usuario.Sexo.HOMBRE));
            Cuenta c = new Cuenta(titulares);
            Cuenta c1 = new Cuenta(titulares, 0);
            
            //System.out.println("salida de Sucursal.accederCuenta: "+Sucursal.accederCuenta(dni2,Cuenta.getCCC("ES23",1234,1234,123457890)));
            Sucursal.darAltaCliente(new Cliente("Pepito", "de los palotes", "AAAAAAAA", 12, dni2, Usuario.Sexo.HOMBRE));
            Sucursal.darAltaCliente(new Cliente("Juanito", "de los palotes", "AAAAAAAA", 12, dni1, Usuario.Sexo.HOMBRE));
            //DNI[] array = {dni1,dni2};
            Sucursal.darAltaCuenta(dni1);
            Sucursal.darAltaCuenta(dni2);
            
            System.out.println(Sucursal.buscarCliente(dni2));
            System.out.println(Sucursal.buscarCliente(dni1));
            Scanner sc = new Scanner(System.in);
            System.out.println("intro num cuenta");
            
            String nCuenta = sc.nextLine();
            String[] ncArray = nCuenta.split("-");
            System.out.println("intro dni");
            String dniStr = sc.nextLine();
            Cuenta cuenta = Sucursal.accederCuenta(new DNI(dniStr), Cuenta.getCCC(ncArray[0]
                    , Integer.parseInt(ncArray[1]), Integer.parseInt(ncArray[2])
                    ,Byte.parseByte(ncArray[3]), Integer.parseInt(ncArray[4])));
            cuenta.setEstado(Cuenta.Estado.ACTIVA);
            System.out.println(cuenta.mostrarDatos());
            cuenta.setMovimiento(Cuenta.getAsunto(0), null, 100, null);
            System.out.println("volcado cuenta: "+cuenta.mostrarMovimientos());
            
            System.out.println("volcado de ccc.soloNumString: "+cuenta.getCCC());
            Cuenta cuenta1 = Sucursal.accederCuenta(new DNI(dniStr), Cuenta.getCCC(ncArray[0]
                    , Integer.parseInt(ncArray[1]), Integer.parseInt(ncArray[2])
                    ,Byte.parseByte(ncArray[3]), Integer.parseInt(ncArray[4])));
            
            System.out.println("volcado cuenta1: "+cuenta1.mostrarMovimientos());
            System.out.println("volcado Banco.consulCuenta(DNI dni): "+Sucursal.consultCuenta(dni2));
            //System.out.println(Sucursal.consultCuenta(nCuenta));
        } catch (ExcepcionValidacionDNI | TitularDuplicado | IllegalArgumentException | ExcepcionValidacionCCC e ) {
            
        }
        
        
        Random rnd = new Random();
        System.out.println(rnd.nextInt(9999-1000)+1000);
        System.out.println();
        
    }
}
