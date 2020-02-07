/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tote
 */
public class Cuenta {

    private static class Movimiento {

        enum Asunto {
            INGRESO,
            NOMINA,
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
            String movimientosStr = "";
            String fechaStr = String.valueOf(getFechaKey());
            String anioStr, mesStr, diaStr;
            if (fechaStr.length() == 8) {
                anioStr = fechaStr.substring(0, 4);
                mesStr = fechaStr.substring(4, 6);
                diaStr = fechaStr.substring(6, 8);
            } else {
                anioStr = fechaStr.substring(0, 4);
                mesStr = fechaStr.substring(4, 5);
                mesStr = "0" + mesStr;
                diaStr = fechaStr.substring(5, 6);
                diaStr = "0" + diaStr;
            }
            fechaStr = diaStr + "/" + mesStr + "/" + anioStr;
            String asuntoStr = asunto.toString().toLowerCase();
            String cuantiaStr = String.valueOf(cuantia);
            int cuantiaInt = (int) cuantia;
            String decimalesCuantia = "" + (cuantia - cuantiaInt);
            decimalesCuantia = decimalesCuantia.substring(2, decimalesCuantia.length());
            System.out.println("length de deciimales: " + decimalesCuantia.length());
            if (cuantiaStr.length() == 3 || (cuantiaStr.length() == 4 && decimalesCuantia.length() > 1)) {
                movimientosStr += fechaStr + "\t" + asuntoStr + "\t" + cuantiaStr + "\n";
            } else {
                movimientosStr += fechaStr + "\t" + asuntoStr + "\t\t" + cuantiaStr + "\n";
            }
            return movimientosStr;
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
        this.CONTROL = 0;
        this.CUENTA = CUENTA;
        movimientos = new HashMap<>();
        asuntos = Movimiento.Asunto.values();
    }

    public Cuenta(String titular, double saldo) {
        this(titular, saldo, "ES00", 0, 0, 0l);
    }

    public Cuenta(String titular) {
        this(titular, 0.0, "ES00", 0, 0, 0l);
    }

    public Cuenta() {
        this(null, 0.0, "ES00", 0, 0, 0l);
    }

    private boolean ingresar(double cuantia, Movimiento.Asunto asunto) {
        boolean ret = false;
        Movimiento m = new Movimiento(asunto, cuantia);
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
            case NOMINA:
                ingresar(cuantia, opt);
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

    public String getNumCuenta() {
        String entString, ofcString, conString, cueString;
        entString = (ENTIDAD != 0) ? "" + ENTIDAD : "0000";
        ofcString = (OFICINA != 0) ? "" + OFICINA : "0000";
        conString = (CONTROL != 0) ? "" + CONTROL : "00";
        cueString = (CUENTA != 0) ? "" + CUENTA : "0000000000";
        return IBAN + "-" + entString + "-" + ofcString + "-" + conString + "-" + cueString;
    }

    public String mostrarDatos() {
        String movimientosStr = mostrarMovimientos();
        return titular + "\n" + getNumCuenta() + "\n" + movimientosStr + "\nSaldo: " + saldo;
    }

    public String mostrarMovimientos() {
        String movimientosStr = "Fecha\t\tAsunto\t\tCuantia\n";
        for (Map.Entry<Integer, ArrayList<Movimiento>> en
                : this.movimientos.entrySet()) {
            for (Movimiento m : en.getValue()) {
                movimientosStr += m;
            }
        }
        return movimientosStr;
    }

    public String movimientosPorFecha(String fecha) {
        String[] fechaArray = fecha.split(" |/|-");
        /**
         * <TODO> implementar exclusion de fechas imposibles
         */
        String movimientosStr = "Fecha\t\tAsunto\t\tCuantia\n";
        if (fechaArray.length == 3) {
            int fechaInt = Integer.parseInt(fechaArray[2]);
            for (int i = fechaArray.length - 2; i >= 0; i--) {
                int fechaHelper = Integer.parseInt(fechaArray[i]);
                if (fechaHelper < 10) {
                    fechaInt *= 10;
                    fechaInt += Integer.parseInt(fechaArray[i]);
                } else {
                    fechaInt *= 100;
                    fechaInt += Integer.parseInt(fechaArray[i]);
                }
            }
            if (movimientos.containsKey(fechaInt)) {
                for (Movimiento movimiento : movimientos.get(fechaInt)) {
                    movimientosStr += movimiento;
                }
            }else{
                movimientosStr+="No existen movimientos para este filtro. Pr favor, compruebe que ha introducido correctamente la fecha.";
            }
        } else {
            movimientosStr = "Formato de fecha erróneo";
        }

        return movimientosStr;
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
