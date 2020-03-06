/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

import cuenta_bancaria.exc.ExcepcionValidacionCCC;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase CuentaBancaria genera almacena y hace consultas sobre una estructuta de
 datos
 *
 * @version 1.0
 * @author Francisco A Domínguez Iceta
 */
public class CuentaBancaria {

    private static class Movimiento {

        protected static enum Asunto {
            INGRESO,
            NOMINA,
            RETIRADA,
            PAGO,
            PERSONALIZADO,
            TRANSFERENCIA
        }

        protected Asunto asunto;
        protected String asuntoPers;
        protected double cuantia;
        protected Calendar fecha;

        public Movimiento() {
        }

        Movimiento(Asunto asunto, String asuntoPers, double cuantia) {
            this.asunto = asunto;
            this.asuntoPers = asuntoPers;
            this.cuantia = cuantia;
            fecha = Calendar.getInstance();
           
        }

        Movimiento(Asunto asunto, String asuntoPers, double cuantia, Calendar fecha) {
            this.asunto = asunto;
            this.asuntoPers = asuntoPers;
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
            String asuntoStr = (asuntoPers != null) ? asuntoPers : asunto
                    .toString()
                    .toLowerCase();
            String cuantiaStr = String.format("%.2f", Double.parseDouble(String
                    .valueOf(cuantia)));

            movimientosStr += fechaStr + "\t" + asuntoStr + "\t\t" + cuantiaStr
                    + "\n";
            return movimientosStr;
        }
    }

    private static class Trasnferencia extends Movimiento {

        CCC origen;
        CCC destino;

        public Trasnferencia(CCC origen, CCC destino) {
            this.origen = origen;
            this.destino = destino;
        }

        public Trasnferencia(CCC origen, CCC destino, Movimiento.Asunto asunto, String asuntoPers, double cuantia) {
            super(asunto, asuntoPers, cuantia);
            this.origen = origen;
            this.destino = destino;
        }

        public Trasnferencia(CCC origen, CCC destino, Movimiento.Asunto asunto, String asuntoPers, double cuantia, Calendar fecha) {
            super(asunto, asuntoPers, cuantia, fecha);
            this.origen = origen;
            this.destino = destino;
        }

        @Override
        public String toString() {
            return super.toString(); //To change body of generated methods, choose Tools | Templates.
        }

        
    }

    static class CCC {

        private final String IBAN;
        private final int ENTIDAD;
        private final int OFICINA;
        private final int CUENTA;
        private final byte DC;
        private static final byte[] PRODUCTOS = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};

        public CCC() {

            Random rnd = new Random();
            IBAN = "ES25";
            ENTIDAD = 3321;
            OFICINA = 2020;
            CUENTA = rnd.nextInt(Integer.MAX_VALUE - 1000000000) + 1000000000;
            DC = genDC(ENTIDAD, OFICINA, CUENTA);
        }

        public CCC(String IBAN, int ENTIDAD, int OFICINA, byte DC, int CUENTA) throws ExcepcionValidacionCCC {
            if (validarDC(ENTIDAD, OFICINA, DC, CUENTA)) {
                this.IBAN = IBAN;
                this.ENTIDAD = ENTIDAD;
                this.OFICINA = OFICINA;
                this.CUENTA = CUENTA;
                this.DC = genDC(ENTIDAD, OFICINA, CUENTA);
            } else {
                throw new ExcepcionValidacionCCC();
            }
        }

        public CCC(int OFICINA) {
            Random rnd = new Random();
            IBAN = "ES25";
            ENTIDAD = 3321;
            this.OFICINA = OFICINA;
            CUENTA = rnd.nextInt(Integer.MAX_VALUE - 1000000000) + 1000000000;
            DC = genDC(ENTIDAD, this.OFICINA, CUENTA);
        }

        public String getNumCuenta() {
            String entString, ofcString, conString, cueString;
            entString = (ENTIDAD != 0) ? "" + ENTIDAD : "0000";
            ofcString = (OFICINA != 0) ? "" + OFICINA : "0000";
            conString = (DC != 0) ? "" + DC : "00";
            cueString = (CUENTA != 0) ? "" + CUENTA : "0000000000";
            return IBAN + "-" + entString + "-" + ofcString + "-" + conString + "-"
                    + cueString;
        }

        private boolean validarDC(int ENTIDAD, int OFICINA, byte DC, int CUENTA) {
            byte dc = genDC(ENTIDAD, OFICINA, CUENTA);
            return dc == DC;
        }

        private byte genDC(int ENTIDAD, int OFICINA, int CUENTA) {
            byte dc;
            int sumatorio = 0;
            int resto;
            String regA = "00" + ENTIDAD + "" + OFICINA;
            String regB = "" + CUENTA;
            for (int i = 0; i < regA.length(); i++) {
                sumatorio += ((regA.charAt(i) - '0') * PRODUCTOS[i]);
            }
            resto = 11 - (sumatorio % 11);
            switch (resto) {
                case 10:
                    dc = 1;
                    dc *= 10;
                    break;
                case 11:
                    dc = 0;
                    break;
                default:
                    dc = (byte) resto;
                    dc *= 10;
                    break;
            }
            sumatorio = 0;
            for (int i = 0; i < regB.length(); i++) {
                sumatorio += ((regB.charAt(i) - '0') * PRODUCTOS[i]);
            }
            resto = 11 - (sumatorio % 11);
            switch (resto) {
                case 10:
                    dc += 1;
                    break;
                case 11:
                    dc += 0;
                    break;
                default:
                    dc += (byte) resto;
                    break;
            }
            return dc;
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
            return DC;
        }

        public long getCUENTA() {
            return CUENTA;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 79 * hash + Objects.hashCode(this.IBAN);
            hash = 79 * hash + this.ENTIDAD;
            hash = 79 * hash + this.OFICINA;
            hash = 79 * hash + this.DC;
            hash = 79 * hash + this.CUENTA;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            boolean retorno = false;
            if (obj instanceof CCC) {
                retorno = this.hashCode() == ((CCC) obj).hashCode();
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
    private final HashSet<Cliente> TITULARES;
    private double saldo;
    private double disponible;
    private double retenciones;
    private CCC ccc;
    private static Movimiento.Asunto[] tipos;
    private static Estado[] estados = Estado.values();
    private Estado estado;
    public CuentaBancaria(Set<Cliente> titulares, double saldo, String contra) throws IllegalArgumentException {
        TITULARES = new HashSet<>(titulares);
        this.saldo = saldo;
        disponible = saldo;
        retenciones = 0.0;
        ccc = new CCC();
        movimientos = new HashMap<>();
        tipos = Movimiento.Asunto.values();
        estados = Estado.values();
        estado = estados[1];
    }

    public CuentaBancaria(Set<Cliente> titulares, String contra) {
        TITULARES = new HashSet(titulares);
        movimientos = new HashMap<>();
        ccc = new CCC();
        estados = Estado.values();
        tipos = Movimiento.Asunto.values();
        estado = estados[0];
    }

    public CuentaBancaria(String IBAN, int ENTIDAD, int OFICINA, byte DC, int CUENTA,
            CuentaBancaria toCopy) throws ExcepcionValidacionCCC {
        TITULARES = new HashSet(toCopy.TITULARES);
        ccc = new CCC(IBAN, ENTIDAD, OFICINA, DC, CUENTA);
        estados = Estado.values();
        tipos = Movimiento.Asunto.values();
        estado = estados[0];
    }

    public CuentaBancaria(Set<Cliente> titulares, CuentaBancaria toCopy)
            throws ExcepcionValidacionCCC {
        TITULARES = new HashSet<>(titulares);
        ccc = new CCC(toCopy.ccc.IBAN, toCopy.ccc.ENTIDAD, toCopy.ccc.OFICINA, toCopy.ccc.DC, toCopy.ccc.OFICINA);
        estados = Estado.values();
        tipos = Movimiento.Asunto.values();
        estado = estados[0];
    }
    
    public void vincularCuenta() {
        TITULARES.forEach((titular) -> {
            titular.addCuenta(this);
        });
    }

    @SuppressWarnings("NonPublicExported")
    public static CuentaBancaria.CCC getCCC(String IBAN, int ENTIDAD, int OFICINA,
            byte DC, int CUENTA) throws ExcepcionValidacionCCC {
        return new CCC(IBAN, ENTIDAD, OFICINA, DC, CUENTA);
    }

    @SuppressWarnings("NonPublicExported")
    public CuentaBancaria.CCC getCCC() {
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

    private boolean transferirFondos(){
        return false;
    }
    
    @SuppressWarnings("NonPublicExported")
    public boolean setMovimiento(Movimiento.Asunto asunto, String asuntoPers
            , double cuantia, Object ccc) throws IllegalArgumentException
            , NumberFormatException
    {    
        boolean ret = false;
        if (estado.equals(Estado.INACTIVA) || estado.equals(Estado.BLOQUEADA)) {
            throw new IllegalArgumentException("Se debe inicializar la clase Cuenta");
        } else if (cuantia > 0) {
            switch (asunto) {
                case INGRESO:
                case NOMINA:
                    ret = ingresar(cuantia, asuntoPers, asunto, Calendar.getInstance());
                    break;
                case RETIRADA:
                case PAGO:
                    ret = retirar(cuantia, asuntoPers, asunto, Calendar.getInstance());
                    break;
                case TRANSFERENCIA:
                    
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

    public String mostrarDatos() {
        String movimientosStr = mostrarMovimientos();
        String titulares = "";
        titulares = TITULARES.stream()
                .map((usuario) -> usuario.getNombreCompleto() + " ")
                .reduce(titulares, String::concat);
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
            movimientosStr = tm.getValue().stream()
                    .map((m) -> m.toString())
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
            m.getValue().stream()
                    .filter((movimiento) -> (movimiento.asunto
                    .equals(asunto)))
                    .forEach((movimiento) -> {
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
        retorno = TITULARES.stream()
                .map((titular) -> titular.toString() + "\n")
                .reduce(retorno, String::concat);
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

    public HashSet<Cliente> getTITULARES() {
        return TITULARES;
    }

    @SuppressWarnings("NonPublicExported")
    public static Movimiento.Asunto getAsunto(int i) {
        return tipos[i];
    }

    @Override
    public boolean equals(Object obj) {
        boolean retorno = false;
        if (obj instanceof CuentaBancaria) {
            retorno = this.hashCode() == ((CuentaBancaria) obj).hashCode();
        }
        return retorno;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.ccc);
        return hash;
    }

}
