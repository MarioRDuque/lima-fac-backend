package pe.limatambo.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pe.limatambo.enums.TipoFormatoExportacion;
import pe.limatambo.excepcion.GeneralException;

public class LimatamboUtil {
    
    private final Logger loggerConstantes = LoggerFactory.getLogger(getClass());
    
    public static final Character ESPACIO = ' ';
    public static final Character COMA = ',';
    public static final Character PUNTO = '.';

    public static String obtenerUsuarioSesion() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession(true);
        String nick = null;
        if (session != null) {
            nick = (String) session.getAttribute("SESSION_USUARIO");
        }
        return nick;
    }
    
    public static String obtenerMimeTypeDeFormatoExportacion(String formatoExportacion) {
        String mimeType = null;
        if (formatoExportacion.equals(TipoFormatoExportacion.PDF.getValue())) {
            mimeType = "application/pdf";
        } else if (formatoExportacion.equals(TipoFormatoExportacion.EXCEL.getValue())) {
            mimeType = "application/xls";
        }
        return mimeType;
    }

    public static Timestamp obtenerFechaHoraActual() {
        java.util.Date date = new java.util.Date();
        Timestamp timeStampTransaction = new Timestamp(date.getTime());
        return timeStampTransaction;
    }

    public static Timestamp obtenerFechaActual() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return new Timestamp(date.getTime().getTime());
    }

    public static Timestamp agregarDias(Timestamp date, int days) {
        Timestamp temporalDate = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(temporalDate);
        // if the days are negative then it can substract the number of days.
        cal.add(Calendar.DAY_OF_WEEK, days);
        Timestamp finalDate = new Timestamp(cal.getTime().getTime());
        return finalDate;
    }

    public static Date agregarDias(Date date, int days) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(date.getTime()));

        cal.add(Calendar.DAY_OF_WEEK, days);
        Timestamp finalDate = new Timestamp(cal.getTime().getTime());
        return new Date(finalDate.getTime());
    }

    public static String cadenasSeparadasPorComas(String[] cadenas) {
        StringBuilder result = new StringBuilder();
        String glue = ",";
        for (String cadena : cadenas) {
            result.append(glue).append(cadena);
        }
        if (result.length() > 0) {
            result.delete(0, glue.length());
        }
        return result.toString();
    }

    public static String cadenasSeparadasPorComas(List<String> cadenas) {
        StringBuilder result = new StringBuilder();
        String glue = ",";
        cadenas.stream().forEach((cadena) -> {
            result.append(glue).append(cadena);
        });
        if (result.length() > 0) {
            result.delete(0, glue.length());
        }
        return result.toString();
    }

    public static String completarNumeroConCeros(Integer len, Integer value) {
        return String.format("%0" + len + "d", value);
    }

    public static String getDiasTranscurridos(Date desde, Date hasta) {
        return ((hasta.getTime()) - desde.getTime()) / (24 * 60 * 60 * 1000) + "";
    }

    public static Long getDiasTranscurridos(Timestamp desde, Timestamp hasta) {
        Timestamp d1, d2;
        d1 = LimatamboUtil.obtenerInicioDia(desde);
        d2 = LimatamboUtil.obtenerInicioDia(hasta);
        return ((d2.getTime()) - d1.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static String diferenciaFechas(Timestamp desde, Timestamp hasta) {
        String salida;
        long milis1 = desde.getTime();
        long milis2 = hasta.getTime();
        long diff = milis2 - milis1;
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        salida = diffDays + " dia(s) y " + (diffHours % 24) + " hora(s)";
        return salida;
    }

    public String obtenerFormatoFecha(Timestamp Fecha) {
        String fecha = null;
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").format(Fecha);
        } catch (Exception e) {
            throw new GeneralException(Mensaje.CAMPO_NO_FECHA, Mensaje.CAMPO_NO_FECHA, loggerConstantes);
        }
        return fecha;
    }

    public String obtenerFormatoHora24(Timestamp Fecha) {
        String fecha = null;
        try {
            fecha = new SimpleDateFormat("HH:mm").format(Fecha);
        } catch (Exception e) {
            throw new GeneralException(Mensaje.CAMPO_NO_FECHA, Mensaje.CAMPO_NO_FECHA, loggerConstantes);
        }
        return fecha;
    }

    public String obtenerFormatoHoraActual24() {
        Timestamp Fecha = obtenerFechaHoraActual();
        String fecha = null;
        try {
            fecha = new SimpleDateFormat("HH:mm").format(Fecha);
        } catch (Exception e) {
            throw new GeneralException(Mensaje.CAMPO_NO_FECHA, Mensaje.CAMPO_NO_FECHA, loggerConstantes);
        }
        return fecha;
    }

    public static Date getFechaMesAtras() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static boolean isNumber(String cadena) {
        boolean isDecimal = false;
        if (cadena == null || cadena.isEmpty()) {
            return false;
        }
        int i = 0;
        if (cadena.charAt(0) == '-') {
            if (cadena.length() > 1) {
                i++;
            } else {
                return false;
            }
        }
        for (; i < cadena.length(); i++) {
            if (!Character.isDigit(cadena.charAt(i))) {
                if (!isDecimal && (cadena.charAt(i) != '.' || cadena.charAt(i) != ',')) {
                    isDecimal = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public static boolean isFechaValida(Date fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(formatoFecha.format(fecha));
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static Date convertirStringToDate(String fechaCadena) throws ParseException, IllegalArgumentException {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
            date = simpleDateFormat.parse(fechaCadena);
        } catch (ParseException | IllegalArgumentException e) {
            throw e;
        }
        return date;
    }

    public static Timestamp obtenerInicioDia(Timestamp fecha) {
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp obtenerInicioMes(Timestamp fecha) {
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Integer obtenerAnioActual() {
        Calendar calendar;
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static Integer obtenerMesActual() {
        Calendar calendar;
        calendar = Calendar.getInstance();
        return (calendar.get(Calendar.MONTH) + 1);
    }

    public static String obtenerExtensionDeNombreDeArchivo(String nombreDeArchivo) {
        String extension;
        int indice;
        indice = nombreDeArchivo.lastIndexOf(".");
        if (indice >= 0 && !nombreDeArchivo.isEmpty()) {
            extension = nombreDeArchivo.substring(indice, nombreDeArchivo.length());
        } else {
            extension = "";
        }
        return extension;
    }

    public static String obtenerNombreDeArchivoSinExtension(String nombreDeArchivo) {
        String nombreArchivo;
        int indicePunto;
        indicePunto = nombreDeArchivo.lastIndexOf(".");
        if (!nombreDeArchivo.isEmpty() && indicePunto >= 0) {
            nombreArchivo = nombreDeArchivo.substring(0, indicePunto);
        } else {
            nombreArchivo = nombreDeArchivo;
        }
        return nombreArchivo;
    }

    public static String obtenerFechaHoraActualFormato() {
        Timestamp ahora = obtenerFechaHoraActual();
        SimpleDateFormat format;
        format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(new Date(ahora.getTime()));
    }

    public static String obtenerAnioDeFecha(Timestamp fecha) {
        Calendar c;
        c = Calendar.getInstance();
        c.setTimeInMillis(fecha.getTime());
        return c.get(Calendar.YEAR) + "";
    }

    public Timestamp convertirStringTimestamp(String fechaHoraCadena) {
        Timestamp fechaHora = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(fechaHoraCadena);
            fechaHora = new java.sql.Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            throw new GeneralException(Mensaje.CAMPO_NO_FECHA, Mensaje.CAMPO_NO_FECHA, loggerConstantes);
        }
        return fechaHora;
    }

    public static Timestamp addTimeToTimestamp(Timestamp timestamp, Time time) {
        Calendar horas, fechas;
        horas = Calendar.getInstance();
        fechas = Calendar.getInstance();

        horas.setTimeInMillis(time.getTime());
        fechas.setTimeInMillis(timestamp.getTime());

        fechas.add(Calendar.HOUR_OF_DAY, horas.get(Calendar.HOUR_OF_DAY));
        fechas.add(Calendar.MINUTE, horas.get(Calendar.MINUTE));
        fechas.add(Calendar.SECOND, horas.get(Calendar.SECOND));
        fechas.add(Calendar.MILLISECOND, horas.get(Calendar.MILLISECOND));

        return new Timestamp(fechas.getTimeInMillis());
    }

    public static Integer obtenerDiaDeFecha(Timestamp fecha) {
        Calendar c;
        c = Calendar.getInstance();
        c.setTimeInMillis(fecha.getTime());
        return c.get(Calendar.DATE);
    }

    public static Integer obtenerMesDeFecha(Timestamp fecha) {
        Calendar c;
        c = Calendar.getInstance();
        c.setTimeInMillis(fecha.getTime());
        return c.get(Calendar.MONTH) + 1;
    }

    public static Integer obtenerAnioIntDeFecha(Timestamp fecha) {
        Calendar c;
        c = Calendar.getInstance();
        c.setTimeInMillis(fecha.getTime());
        return c.get(Calendar.YEAR);
    }

    public static BigDecimal redondearBigDecimal(BigDecimal valor, Integer precision) {
        return valor.setScale(precision, BigDecimal.ROUND_HALF_UP);
    }

    public static Boolean esCadenaValida(String cadena) {
        return (cadena != null && !cadena.isEmpty());
    }

    public static Boolean esNoNulo(Object o) {
        return o != null;
    }

    public static Boolean esNulo(Object o) {
        return o == null;
    }

    public static Boolean esNoNuloYVacio(String cadena) {
        return esNoNulo(cadena) && !cadena.isEmpty();
    }

    public static Boolean esNoNuloYCadenaNoVacia(Object o, String cadena) {
        return esNoNulo(o) && esNoNuloYVacio(cadena);
    }

    public static Boolean sonNoNulos(Object... obs) {
        for (Object o : obs) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }

    public static Boolean listaEstaVacia(List lista) {
        return !esNoNulo(lista) || lista.isEmpty();
    }

    public static Boolean numeroEsValidoParaBusquedas(Number numero) {
        return esNoNulo(numero) && numero.doubleValue() > 0;
    }

    public static String formatTimestamp(Timestamp date) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(date);
    }
    
    public static String formatMoneda(BigDecimal moneda) {
        String respuesta = "";
        if (moneda != null) {
            DecimalFormat twoPlaces = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
            respuesta = twoPlaces.format(moneda);
        }
        return respuesta;
    }

    public static Timestamp obtenerFechaDesdeMesAnio(String mes, String anio) {
        Calendar calendar;
        Integer mesInt, anioInt;
        calendar = Calendar.getInstance();
        mesInt = Integer.valueOf(mes) - 1;
        anioInt = Integer.valueOf(anio);
        calendar.set(Calendar.YEAR, anioInt);
        calendar.set(Calendar.MONTH, mesInt);
        return obtenerInicioDia(new Timestamp(calendar.getTimeInMillis()));
    }

    public static Timestamp agregarMeses(Timestamp fecha, Integer meses) {
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, meses);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String castString(Object o) {
        String r = null;
        if (LimatamboUtil.sonNoNulos(o)) {
            try {
                r = String.valueOf(o);
            } catch (ClassCastException ex) {
                r = o + "";
            }
        }
        return r;
    }

    public static Long castLong(Object o) {
        Long r = null;
        String s = castString(o);
        if (LimatamboUtil.esNoNulo(s)) {
            try {
                r = Long.valueOf(s);
            } catch (NumberFormatException ex) {
                r = null;
            }
        }
        return r;
    }

    public static Time obtenerHoraParaGuardar(String hora) {
        Time r = null;
        if (esNoNulo(hora)) {
            try {
                r = Time.valueOf(hora + ":00");
            } catch (NumberFormatException ex) {
                r = null;
            }

        }
        return r;
    }

    public static String obtenerFiltroComoString(Map<String, Object> buscar, String nombrePropiedad) {
        String f;
        Object objetoObtenido = buscar.get(nombrePropiedad);
        f = null;
        if (LimatamboUtil.sonNoNulos(buscar, objetoObtenido)) {
            try {
                f = String.valueOf(objetoObtenido);
            } catch (ClassCastException ex) {
                f = objetoObtenido + "";
            }
        }
        return f;
    }

    public static Long obtenerFiltroComoLong(Map<String, Object> buscar, String nombrePropiedad) {
        String f;
        Long filtro;
        filtro = null;
        f = obtenerFiltroComoString(buscar, nombrePropiedad);
        if (LimatamboUtil.esNoNulo(f)) {
            try {
                filtro = Long.valueOf(f);
            } catch (NumberFormatException ex) {
                filtro = null;
            }
        }
        return filtro;
    }

    public static Integer obtenerFiltroComoInteger(Map<String, Object> buscar, String nombrePropiedad) {
        String f;
        Integer filtro;
        filtro = null;
        f = obtenerFiltroComoString(buscar, nombrePropiedad);
        if (LimatamboUtil.esNoNulo(f)) {
            try {
                filtro = Integer.valueOf(f);
            } catch (NumberFormatException ex) {
                filtro = null;
            }
        }
        return filtro;
    }

    public static Timestamp obtenerFiltroComoTimestamp(Map<String, Object> buscar, String nombrePropiedad) {
        Timestamp timestamp;
        Long milis;
        timestamp = null;
        try {
            milis = obtenerFiltroComoLong(buscar, nombrePropiedad);
            timestamp = new Timestamp(milis);
        } catch (Exception exception) {
            timestamp = null;
        }
        return timestamp;
    }

    public static Date obtenerFiltroComoDate(Map<String, Object> buscar, String nombrePropiedad) {
        Date date = null;
        String fechaCadena;
        try {
            fechaCadena = obtenerFiltroComoString(buscar, nombrePropiedad);
            date = fechaCadena!= null ? LimatamboUtil.convertirStringToDate(fechaCadena) : null;
        } catch (ParseException | IllegalArgumentException e) {
            date = null;
        }
        return date;
    }

    public static Boolean obtenerFiltroComoBoolean(Map<String, Object> buscar, String nombrePropiedad) {
        String f;
        Boolean filtro;
        filtro = null;
        f = obtenerFiltroComoString(buscar, nombrePropiedad);
        if (LimatamboUtil.esNoNulo(f)) {
            try {
                filtro = Boolean.valueOf(f);
            } catch (NumberFormatException ex) {
                filtro = null;
            }
        }
        return filtro;
    }

    public static BigDecimal obtenerFiltroComoBigDecimal(Map<String, Object> buscar, String nombrePropiedad) {
        String f;
        BigDecimal filtro;
        filtro = null;
        f = obtenerFiltroComoString(buscar, nombrePropiedad);
        if (LimatamboUtil.esNoNulo(f)) {
            try {
                filtro = new BigDecimal(f);
            } catch (NumberFormatException ex) {
                filtro = null;
            }
        }
        return filtro;
    }
    
    public static String obtenerHoraFormatHHMMSS(Time hora){
    	String horaStr = hora.toString();
    	return horaStr.replace(":","");
    }

    public static Boolean esNuloFalso(Boolean obj){
    	return esNulo(obj) || !obj;
    }
}
