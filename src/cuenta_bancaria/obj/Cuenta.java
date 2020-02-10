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
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tote
 */
public class Cuenta {

    private static class Movimiento {

        enum TipoMovimiento {
            INGRESO,
            NOMINA,
            RETIRADA,
            PAGO,
        }

        TipoMovimiento tipo;
        double cuantia;
        Calendar fecha;

        Movimiento(TipoMovimiento asunto, double cuantia) {
            this.tipo = asunto;
            this.cuantia = cuantia;
            fecha = Calendar.getInstance();
        }

        Movimiento(TipoMovimiento asunto, double cuantia, Calendar fecha) {
            this.tipo = asunto;
            this.cuantia = cuantia;
            this.fecha = fecha;
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
            String fechaStr;
            String anioStr = "" + fecha.get(Calendar.YEAR);
            String mesStr = ((fecha.get(Calendar.MONTH) + 1) < 10)
                    ? "0" + (fecha.get(Calendar.MONTH) + 1)
                    : "" + (fecha.get(Calendar.MONTH) + 1);
            String diaStr = (fecha.get(Calendar.DATE) < 10)
                    ? "0" + fecha.get(Calendar.DATE)
                    : "" + fecha.get(Calendar.DATE);

            fechaStr = diaStr + "/" + mesStr + "/" + anioStr;
            String asuntoStr = tipo.toString().toLowerCase();
            String cuantiaStr = String.format("%.2f", Double.parseDouble(String
                    .valueOf(cuantia)));
            int cuantiaInt = (int) cuantia;
            String decimalesCuantia = "" + (cuantia - cuantiaInt);
            decimalesCuantia = decimalesCuantia.substring(2, decimalesCuantia
                    .length());
            System.out.println("length de deciimales: " + decimalesCuantia
                    .length());
            if (cuantiaStr.length() == 3 || (cuantiaStr.length() == 4
                    && decimalesCuantia.length() > 1)) {
                movimientosStr += fechaStr + "\t" + asuntoStr + "\t"
                        + cuantiaStr + "\n";
            } else {
                movimientosStr += fechaStr + "\t" + asuntoStr + "\t\t"
                        + cuantiaStr + "\n";
            }
            return movimientosStr;
        }
    }

    public enum Estado {
        INACTIVA,
        ACTIVA,
        RETENIDA,
        BLOQUEADA
    }
    private String titular;
    private HashMap<Integer, ArrayList<Movimiento>> movimientos;
    private double saldo;
    private double disponible;
    private double retenciones;
    private final String IBAN;
    private final int ENTIDAD;
    private final int OFICINA;
    private final byte CONTROL;
    private final long CUENTA;
    private static Movimiento.TipoMovimiento[] tipos;
    private static Estado[] estados;
    private Estado estado;

    public Cuenta(String titular, double saldo, String IBAN, int ENTIDAD,
            int OFICINA, long CUENTA) throws IllegalArgumentException {
        Pattern ibanPatron = Pattern.compile("((ES[0-9]{2})|(ES00))-(([0-9]{4})|(0))-(([0-9]{4})|(0))-(([0-9]{2})|(0))-(([0-9]{10})|(0))");
        String numCuentaString = IBAN + "-" + ENTIDAD + "-" + OFICINA + "-00-" + CUENTA;
        Matcher m = ibanPatron.matcher(numCuentaString);
        if (m.matches()) {
            this.titular = titular;
            this.saldo = saldo;
            disponible = saldo;
            retenciones = 0.0;
            this.IBAN = IBAN.toUpperCase();
            this.ENTIDAD = ENTIDAD;
            this.OFICINA = OFICINA;
            this.CONTROL = 0;
            this.CUENTA = CUENTA;
            movimientos = new HashMap<>();
            tipos = Movimiento.TipoMovimiento.values();
            estados = Estado.values();
            estado = estados[1];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Cuenta(String titular, double saldo) {
        this(titular, saldo, "ES00", 0, 0, 0l);
        estado = estados[0];
    }

    public Cuenta(String titular) {
        this(titular, 0.0, "ES00", 0, 0, 0l);
        estado = estados[0];
    }

    public Cuenta() {
        this("Usuario por defecto", 0.0, "ES00", 0, 0, 0l);
        estado = estados[0];
    }

    public Cuenta(String IBAN, int ENTIDAD, int OFICINA, long CUENTA,
            Cuenta toCopy) {
        this(toCopy.titular, toCopy.saldo, IBAN, ENTIDAD, OFICINA, CUENTA);
        estado = (!titular.equals("Usuario por defecto")) ? estados[1] : estados[0];
    }

    private boolean ingresar(double cuantia, Movimiento.TipoMovimiento asunto,
            Calendar fecha) {
        boolean ret = false;
        if (fecha != null) {
            Movimiento m = new Movimiento(asunto, cuantia, fecha);

            if (movimientos.containsKey(m.getFechaKey())) {
                saldo += cuantia;
                ret = movimientos.get(m.getFechaKey()).add(m);
            } else {
                saldo += cuantia;
                movimientos.put(m.getFechaKey(), new ArrayList<>());
                ret = movimientos.get(m.getFechaKey()).add(m);
            }

        } else {
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
        }
        return ret;
    }

    private boolean retirar(double cuantia, Movimiento.TipoMovimiento opt,
            Calendar fecha) {
        boolean ret = false;
        if (fecha != null) {
            Movimiento m = new Movimiento(opt, cuantia, fecha);

            if (movimientos.containsKey(m.getFechaKey())) {
                saldo -= cuantia;
                ret = movimientos.get(m.getFechaKey()).add(m);
            } else {
                saldo -= cuantia;
                movimientos.put(m.getFechaKey(), new ArrayList<>());
                ret = movimientos.get(m.getFechaKey()).add(m);
            }

        } else {
            Movimiento m = new Movimiento(opt, cuantia);

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
    public boolean setMovimiento(Movimiento.TipoMovimiento tipo, double cuantia,
            Calendar fecha) throws IllegalArgumentException, NumberFormatException {
        boolean ret = false;
        if (estado.equals(Estado.INACTIVA)) {
            throw new IllegalArgumentException("Se debe inicializar la clase Cuenta");
        } else if (cuantia > 0) {
            switch (tipo) {
                case INGRESO:
                case NOMINA:
                    ret = ingresar(cuantia, tipo, fecha);
                    break;
                case RETIRADA:
                case PAGO:
                    ret = retirar(cuantia, tipo, fecha);
                    break;
                default:
                    throw new AssertionError();
            }
        } else {
            throw new NumberFormatException();
        }

        return ret;

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
        return IBAN + "-" + entString + "-" + ofcString + "-" + conString + "-"
                + cueString;
    }

    public String mostrarDatos() {
        String movimientosStr = mostrarMovimientos();

        return titular + "\n" + getNumCuenta() + "\n" + movimientosStr
                + "\nSaldo: " + String.format("%.2f", Double.parseDouble(String
                        .valueOf(saldo)));
    }

    public String mostrarMovimientos() {
        TreeMap<Integer, ArrayList<Movimiento>> movOrdenados;
        movOrdenados = new TreeMap<>(movimientos);
        String movimientosStr = "Fecha\t\tAsunto\t\tCuantia\n";
        for (Map.Entry<Integer, ArrayList<Movimiento>> en
                : this.movimientos.entrySet()) {
            for (Movimiento m : en.getValue()) {
                movimientosStr += m.toString();
            }
        }
        return movimientosStr;
    }

    public boolean validarFecha(String fecha) {
        Pattern p = Pattern
                .compile("(^([1-2]?[0-9]?|3[0-1])[.]([1-9]?|1[0-2])[.]((20[0-9]{2})|(19[0-9]{2})))"
                        + "|(^([1-2]?[0-9]?|3[0-1])[/]([1-9]?|1[0-2])[/]((20[0-9]{2})|(19[0-9]{2})))"
                        + "|(^([1-2]?[0-9]?|3[0-1])[-]([1-9]?|1[0-2])[-]((20[0-9]{2})|(19[0-9]{2})))");
        Matcher m = p.matcher(fecha);
        boolean valido = m.matches();
        return valido;
    }

    public String movimientosPorFecha(String fecha) {
        String[] fechaArray = fecha.split("[.|/|-]");
        String movimientosStr = "Fecha\t\tAsunto\t\tCuantia\n";
        if (validarFecha(fecha)) {
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
                    movimientosStr += movimiento.toString();
                }
            } else {
                movimientosStr += "No existen movimientos para este filtro"
                        + ". Por favor, compruebe que ha introducido "
                        + "correctamente la fecha.";
            }
        } else {
            movimientosStr = "Formato de fecha erróneo";
        }

        return movimientosStr;
    }

    /**
     *
     * @param asunto
     * @return
     */
    @SuppressWarnings("NonPublicExported")
    public String movimientosPorAsunto(Movimiento.TipoMovimiento asunto) {
        String movimientoStr = "";
        ArrayList<Movimiento> movimientosAsunto = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Movimiento>> m : movimientos.entrySet()) {
            for (Movimiento movimiento : m.getValue()) {
                if (movimiento.tipo.equals(asunto)) {
                    movimientosAsunto.add(movimiento);
                }
            }
        }
        movimientoStr += movimientosAsunto.stream()
                .map((m) -> m.toString())
                .reduce(movimientoStr, String::concat);
        return "Fecha\t\tAsunto\t\tCuantia\n"+movimientoStr;
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

    public void setTitular(String titular) {
        this.titular = titular;
        estado = (!getNumCuenta().equals("ES00-0000-0000-00-0000000000")
                && !titular.equals("Usuario por defecto"))
                ? estados[1] : estados[0];
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public Estado getEstado() {
        return estado;
    }

    public static Estado getEstado(int i) {
        return estados[i];
    }

    @SuppressWarnings("NonPublicExported")
    public static Movimiento.TipoMovimiento getTipo(int i) {
        return tipos[i];
    }
}
