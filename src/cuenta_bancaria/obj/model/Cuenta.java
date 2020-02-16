/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            PERSONALIZADO
        }

        Asunto asunto;
        String asuntoPers;
        double cuantia;
        Calendar fecha;

        public Movimiento() {
        }

        Movimiento(Asunto asunto, String asuntoPers, double cuantia) {
            this.asunto = asunto;
            if (asunto.equals(Asunto.PERSONALIZADO)) {
                this.asuntoPers = asuntoPers;
            } else {
                this.asuntoPers = asuntoPers;
            }
            this.cuantia = cuantia;
            fecha = Calendar.getInstance();
        }

        Movimiento(Asunto asunto, String asuntoPers, double cuantia, Calendar fecha) {
            this.asunto = asunto;
            if (asunto.equals(Asunto.PERSONALIZADO)) {
                this.asuntoPers = asuntoPers;
            } else {
                this.asuntoPers = asuntoPers;
            }
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
            String asuntoStr = asunto.toString().toLowerCase();
            String cuantiaStr = String.format("%.2f", Double.parseDouble(String
                    .valueOf(cuantia)));

            movimientosStr += fechaStr + "\t" + asuntoStr + "\t\t" + cuantiaStr + "\n";
            return movimientosStr;
        }
    }

    static class CCC {

        private final String IBAN;
        private final int ENTIDAD;
        private final int OFICINA;
        private final byte CONTROL;
        private final long CUENTA;

        public CCC() {

            Random rnd = new Random();
            IBAN = "ES25";
            ENTIDAD = rnd.nextInt(9999 - 1000) + 1000;
            OFICINA = rnd.nextInt(9999 - 1000) + 1000;
            CUENTA = Math.abs(rnd.nextInt());
            CONTROL = 0;
        }

        public CCC(String IBAN, int ENTIDAD, int OFICINA, long CUENTA) {
            this.IBAN = IBAN;
            this.ENTIDAD = ENTIDAD;
            this.OFICINA = OFICINA;
            CONTROL = 0;
            this.CUENTA = CUENTA;
        }
        
        public CCC(int OFICINA) {
            Random rnd = new Random();
            IBAN = "ES25";
            ENTIDAD = 3321;
            this.OFICINA = OFICINA;
            CONTROL = 0;
            CUENTA = Math.abs(rnd.nextInt());
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

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 41 * hash + Objects.hashCode(this.IBAN);
            hash = 41 * hash + this.ENTIDAD;
            hash = 41 * hash + this.OFICINA;
            hash = 41 * hash + this.CONTROL;
            hash = 41 * hash + (int) (this.CUENTA ^ (this.CUENTA >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            boolean retorno=false;
            if(obj instanceof CCC){
                retorno = this.hashCode() == ((CCC)obj).hashCode();
            }
            return retorno;
        }
        
    }

    public enum Estado {
        INACTIVA,
        ACTIVA,
        RETENIDA,
        BLOQUEADA
    }

    private HashMap<Integer, ArrayList<Movimiento>> movimientos;
    private final HashMap<DNI, Usuario> TITULARES;
    private double saldo;
    private double disponible;
    private double retenciones;
    private CCC ccc;
    private static Movimiento.Asunto[] tipos;
    private static Estado[] estados;
    private Estado estado;

    public Cuenta(Set<Usuario> titulares, double saldo) throws IllegalArgumentException {
        TITULARES = new HashMap(titulares.size());
        for (Usuario titular : titulares) {
            TITULARES.put(titular.getDni(), titular);
        }
        this.saldo = saldo;
        disponible = saldo;
        retenciones = 0.0;
        ccc = new CCC();
        movimientos = new HashMap<>();
        tipos = Movimiento.Asunto.values();
        estados = Estado.values();
        estado = estados[1];
    }

    public Cuenta(Set<Usuario> titulares) {
        Random rnd = new Random();
        TITULARES = new HashMap(titulares.size());
        for (Usuario titular : titulares) {

            TITULARES.put(titular.getDni(), titular);
        }
        movimientos = new HashMap<>();
        ccc = new CCC();
        estados = Estado.values();
        estado = estados[0];
    }

    public Cuenta(String IBAN, int ENTIDAD, int OFICINA, long CUENTA,
            Cuenta toCopy) {
        this(toCopy.TITULARES.values().stream().collect(Collectors.toSet()));
        estado = estados[0];
    }

    public Cuenta(Set<Usuario> titulares, Cuenta toCopy) {
        this(titulares, toCopy.saldo);
        estado = estados[0];
    }

    public void vincularCuenta() {
        TITULARES.entrySet().forEach((entry) -> {
            entry.getValue().addCuenta(this);
        });
    }
    
    public static Cuenta.CCC getCCC(String IBAN, int ENTIDAD, int OFICINA, long CUENTA){
        return new CCC(IBAN,  ENTIDAD,  OFICINA,  CUENTA);
    }
    public Cuenta.CCC getCCC(){
        return ccc;
    }
    private boolean ingresar(double cuantia, String asuntoPers, Movimiento.Asunto asunto,
            Calendar fecha) {
        boolean ret = false;
        if (fecha != null) {
            Movimiento m = new Movimiento(asunto, asuntoPers, cuantia, fecha);

            if (movimientos.containsKey(m.getFechaKey())) {
                saldo += cuantia;
                ret = movimientos.get(m.getFechaKey()).add(m);
            } else {
                saldo += cuantia;
                movimientos.put(m.getFechaKey(), new ArrayList<>());
                ret = movimientos.get(m.getFechaKey()).add(m);
            }

        } else {
            Movimiento m = new Movimiento(asunto, asuntoPers, cuantia);
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

    private boolean retirar(double cuantia, String asuntoPers, Movimiento.Asunto asunto,
            Calendar fecha) {
        boolean ret;
        if (fecha != null) {
            Movimiento m = new Movimiento(asunto, asuntoPers, cuantia, fecha);

            if (movimientos.containsKey(m.getFechaKey())) {
                saldo -= cuantia;
                ret = movimientos.get(m.getFechaKey()).add(m);
            } else {
                saldo -= cuantia;
                movimientos.put(m.getFechaKey(), new ArrayList<>());
                ret = movimientos.get(m.getFechaKey()).add(m);
            }

        } else {
            Movimiento m = new Movimiento(asunto, asuntoPers, cuantia);

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
    public boolean setMovimiento(Movimiento.Asunto asunto, String asuntoPers, double cuantia,
            Calendar fecha) throws IllegalArgumentException, NumberFormatException {
        boolean ret = false;
        if (estado.equals(Estado.INACTIVA) || estado.equals(Estado.BLOQUEADA)) {
            throw new IllegalArgumentException("Se debe inicializar la clase Cuenta");
        } else if (cuantia > 0) {
            switch (asunto) {
                case INGRESO:
                case NOMINA:
                    ret = ingresar(cuantia, asuntoPers, asunto, fecha);
                    break;
                case RETIRADA:
                case PAGO:
                    ret = retirar(cuantia, asuntoPers, asunto, fecha);
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

        return ccc.getNumCuenta();
    }

    public String mostrarDatos() {//necesario cambios
        String movimientosStr = mostrarMovimientos();
        String titulares = "";
        for (Usuario usuario : TITULARES.values()) {
            titulares += usuario.getNombreCompleto() + " ";
        }
        return ccc.getNumCuenta() + "\nTitular/es: " + titulares + "\n" + movimientosStr
                + "\nSaldo: " + String.format("%.2f", Double.parseDouble(String
                        .valueOf(saldo)));
    }

    public String mostrarMovimientos() {
        TreeMap<Integer, ArrayList<Movimiento>> movOrdenados;
        movOrdenados = new TreeMap<>(movimientos);
        String movimientosStr = "Fecha\t\tAsunto\t\tCuantia\n";
        for (Map.Entry<Integer, ArrayList<Movimiento>> tm
                : movOrdenados.entrySet()) {
            movimientosStr = tm.getValue().stream().map((m) -> m.toString())
                    .reduce(movimientosStr, String::concat);
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
    public String movimientosPorAsunto(Movimiento.Asunto asunto) {
        String movimientoStr = "";
        ArrayList<Movimiento> movimientosAsunto = new ArrayList<>();
        movimientos.entrySet().stream().forEach((m) -> {
            m.getValue().stream().filter((movimiento) -> (movimiento.asunto
                    .equals(asunto)))
                    .forEachOrdered((movimiento) -> {
                        movimientosAsunto.add(movimiento);
                    });
        });
        movimientoStr += movimientosAsunto.stream()
                .map((m) -> m.toString())
                .reduce(movimientoStr, String::concat);
        return "Fecha\t\tAsunto\t\tCuantia\n" + movimientoStr;
    }

    public String mostrarTitular() {
        String retorno = "";
        for (Map.Entry<DNI, Usuario> entry : TITULARES.entrySet()) {
            retorno += entry.getValue().toString() + "\n";
        }
        return retorno;
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

    public double getDisponible() {
        return disponible;
    }

    public void setDisponible(double disponible) {
        this.disponible = disponible;
    }

    public double getRetenciones() {
        return retenciones;
    }

    public void setRetenciones(double retenciones) {
        this.retenciones = retenciones;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Map<DNI, Usuario> getTITULARES() {
        return TITULARES;
    }

    @SuppressWarnings("NonPublicExported")
    public static Movimiento.Asunto getAsunto(int i) {
        return tipos[i];
    }

    @Override
    public boolean equals(Object obj) {

        return this.hashCode() == ((Cuenta) obj).hashCode();
    }

}
