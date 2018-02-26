/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.Date;
import pe.limatambo.entidades.Pedido;
import pe.limatambo.entidades.Usuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface PedidoServicio extends GenericoServicio<Pedido, Integer>{
    BusquedaPaginada busquedaPaginada(Pedido entidadBuscar, BusquedaPaginada busquedaPaginada, Integer idPedido,
            Date desde, Date hasta, String dni, String nombre, String idubigeo, String usuario, Integer tipoUsuario);
    Pedido actualizar(Pedido entidad, Usuario usuario);
    Pedido guardar(Pedido entidad);
    public Pedido obtener(Integer id);
    public void actualizarEstadoDetalle(Integer id) throws GeneralException;
    public void terminarVenta(int id) throws GeneralException;
}
