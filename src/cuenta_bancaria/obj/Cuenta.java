/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author tote
 */
public class Cuenta {

    private static class Movimiento {

        enum Asunto {
            INGRESO,
            RETIRADA,
            PAGO,
        }

        Asunto asunto;
        double cuantia;
        Calendar fecha;

        Movimiento(Asunto asunto, double cuantia) {
            this.asunto = asunto;
            this.cuantia = cuantia;
            fecha = Calendar.getInstance();
        }

        int getFechaKey() {
            int ret = fecha.get(Calendar.YEAR);
            if (fecha.get(Calendar.MONTH) > 9) {
                ret *= 100;
                ret += (fecha.get(Calendar.MONTH) + 1);
            } else {
                ret *= 10;
                ret += (fecha.get(Calendar.MONTH) + 1);
            }
            if (fecha.get(Calendar.DATE) > 9) {
                ret *= 100;
                ret += fecha.get(Calendar.DATE);
            } else {
                ret *= 10;
                ret += fecha.get(Calendar.DATE);
            }
            return ret;
        }

        @Override
        public String toString() {
            return "Movimiento{" + "asunto=" + asunto + ", cuantia=" + cuantia + ", fecha=" + fecha + '}';
        }
        
        
    }

    private String titular;
    private HashMap<Integer, ArrayList<Movimiento>> movimientos;
    private double saldo;
    private final String IBAN;
    private final int ENTIDAD;
    private final int OFICINA;
    private final int CONTROL;
    private final long CUENTA;
    private static Movimiento.Asunto[] asuntos;

    public Cuenta(String titular, double saldo, String IBAN, int ENTIDAD,
            int OFICINA, long CUENTA) {
        this.titular = titular;
        this.saldo = saldo;
        this.IBAN = IBAN;
        this.ENTIDAD = ENTIDAD;
        this.OFICINA = OFICINA;
        this.CONTROL = 00;
        this.CUENTA = CUENTA;
        movimientos = new HashMap<>();
        asuntos = Movimiento.Asunto.values();
    }

    public Cuenta(String titular, double saldo) {
        this(titular, saldo, "ES00", 0000, 0000, 0000000000l);
    }

    public Cuenta(String titular) {
        this(titular, 0.0, "ES00", 0000, 0000, 0000000000l);
    }

    public Cuenta() {
        this(null, 0.0, "ES00", 0000, 0000, 0000000000l);
    }

    private boolean ingresar(double cuantia) {
        boolean ret = false;
        Movimiento m = new Movimiento(Movimiento.Asunto.INGRESO, cuantia);
        if (cuantia > 0) {
            if (movimientos.containsKey(m.getFechaKey())) {
                saldo += cuantia;
                ret = movimientos.get(m.getFechaKey()).add(m);
            } else {
                saldo += cuantia;
                movimientos.put(m.getFechaKey(), new ArrayList<>());
                ret = movimientos.get(m.getFechaKey()).add(m);
            }
        }
        return ret;
    }

    private boolean retirar(double cuantia, Movimiento.Asunto opt) {
        boolean ret = false;
        Movimiento m = new Movimiento(opt, cuantia);
        if (cuantia > 0) {
            if (movimientos.containsKey(m.getFechaKey())) {
                saldo -= cuantia;
                ret = movimientos.get(m.getFechaKey()).add(m);
            } else {
                saldo -= cuantia;
                movimientos.put(m.getFechaKey(), new ArrayList<>());
                ret = movimientos.get(m.getFechaKey()).add(m);
            }
        }

        return ret;
    }
    
    @SuppressWarnings("NonPublicExported")
    public boolean setMovimiento(Movimiento.Asunto opt, double cuantia) {
        boolean ret = false;
        switch (opt) {
            case INGRESO:
                ingresar(cuantia);
                break;
            case RETIRADA:
            case PAGO:
                retirar(cuantia, opt);
                break;
            default:
                throw new AssertionError();
        }
        return ret;
    }
    @SuppressWarnings("NonPublicExported")
    public static Movimiento.Asunto getAsunto(int idx) {
        return asuntos[idx];
    }

    public double mostrarSaldo() {
        return saldo;
    }
    
    public String getNumCuenta(){
        return IBAN+"-"+ENTIDAD+"-"+OFICINA+"-"+CONTROL+"-"+CUENTA;
    }
    
    public String mostrarDatos(){
        return titular+"\n"+getNumCuenta()+"\nSaldo: "+saldo;
    }
    
    public String getIBAN() {
        return IBAN;
    }

    public int getENTIDAD() {
        return ENTIDAD;
    }

    public int getOFICINA() {
        return OFICINA;
    }

    public int getCONTROL() {
        return CONTROL;
    }

    public long getCUENTA() {
        return CUENTA;
    }
}
