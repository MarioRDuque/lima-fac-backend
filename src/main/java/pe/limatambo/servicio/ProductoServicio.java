/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import java.util.Map;
import pe.limatambo.entidades.Producto;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface ProductoServicio extends GenericoServicio<Producto, Integer> {

    public BusquedaPaginada busquedaPaginada(Producto entidadBuscar, BusquedaPaginada busquedaPaginada, String idProducto, int idCategoria);

    public Producto insertar(Producto entidad) throws GeneralException;

    public Producto actualizar(Producto producto) throws GeneralException;

    public Producto obtener(Integer id) throws GeneralException;

    public Map<String, Object> exportarReporteProducto(List<Producto> productos, String usuario) throws Exception;
}
