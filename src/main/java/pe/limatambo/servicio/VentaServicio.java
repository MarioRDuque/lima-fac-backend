/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.io.IOException;
import java.util.Date;
import pe.limatambo.entidades.Venta;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface VentaServicio extends GenericoServicio<Venta, Long>{
    BusquedaPaginada busquedaPaginada(Venta entidadBuscar, BusquedaPaginada busquedaPaginada, Integer idPedido,
            Date desde, Date hasta, String dni, String nombre, String usuario, String seriecorrelativo);
    Venta actualizar(Venta entidad);
    Venta guardar(Venta entidad);
    public Venta obtener(Long id);
    public void generarDocumentoCab(Long id) throws IOException;
    public void actualizarEstadoDetalle(Long id) throws GeneralException;
    public void generarDocumentoDet(Long id) throws IOException;
    public void generarDocumentoCabNota(Long id, String tipoOld) throws IOException;
}
