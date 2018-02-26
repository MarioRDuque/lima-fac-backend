package pe.limatambo.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class BusquedaPaginada implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalPaginas;
    private Long paginaActual;
    private Long totalRegistros;
    private Long cantidadPorPagina;
    private List<?> registros;
    private Map<String, Object> buscar;

    public BusquedaPaginada() {
    }
    
    public static BusquedaPaginada crearConPaginacionInicial(Long paginaActual,Long cantidadPorPagina){
    	return new BusquedaPaginada(paginaActual,cantidadPorPagina);
    }
    
    public BusquedaPaginada(Long paginaActual,Long cantidadPorPagina){
    	this.cantidadPorPagina = cantidadPorPagina;
    	this.paginaActual = paginaActual;
    }

    public BusquedaPaginada(Map<String, Object> filtroBusqueda) {
        this.buscar = filtroBusqueda;
    }

    public Map<String, Object> getBuscar() {
        return buscar;
    }

    public void setBuscar(Map<String, Object> filtroBusqueda) {
        this.buscar = filtroBusqueda;
    }

    public void setBuscar(String buscar) {
        this.buscar = JsonMapper.parseToMap(buscar);
    }

    public List<?> getRegistros() {
        return registros;
    }

    public void setRegistros(List<?> registros) {
        this.registros = registros;
    }

    public void calcularCantidadDePaginas() {
        this.totalPaginas = this.totalRegistros / this.cantidadPorPagina;
        if (this.totalRegistros % this.cantidadPorPagina != 0) {
            this.totalPaginas = this.totalPaginas + 1;
        }
    }

    public void validarPaginaActual() {
        if (this.paginaActual > this.totalPaginas) {
            this.paginaActual = this.totalPaginas;
        } else if (this.paginaActual <= 0) {
            this.paginaActual = 1L;
        }
    }

    public String obtenerFiltroComoString(String nombrePropiedad) {
        return LimatamboUtil.obtenerFiltroComoString(buscar, nombrePropiedad);
    }

    public Long obtenerFiltroComoLong(String nombrePropiedad) {
        return LimatamboUtil.obtenerFiltroComoLong(buscar, nombrePropiedad);
    }

    public Integer obtenerFiltroComoInteger(String nombrePropiedad) {
        return LimatamboUtil.obtenerFiltroComoInteger(buscar, nombrePropiedad);
    }

    public Timestamp obtenerFiltroComoTimestamp(String nombrePropiedad) {
        return LimatamboUtil.obtenerFiltroComoTimestamp(buscar, nombrePropiedad);
    }

    public Date obtenerFiltroComoDate(String nombrePropiedad) {
        return LimatamboUtil.obtenerFiltroComoDate(buscar, nombrePropiedad);
    }

    public Boolean obtenerFiltroComoBoolean(String nombrePropiedad) {
        return LimatamboUtil.obtenerFiltroComoBoolean(buscar, nombrePropiedad);
    }

    public BigDecimal obtenerFiltroComoBigDecimal(String nombrePropiedad) {
        return LimatamboUtil.obtenerFiltroComoBigDecimal(buscar, nombrePropiedad);
    }
    
    public List<Long> obtenerFiltroComoListLong(String nombrePropiedad){
    	List<Integer> list = (List<Integer>)buscar.get(nombrePropiedad);
    	return list.stream().map(num -> new Long(num)).collect(Collectors.toList());
    }

}
