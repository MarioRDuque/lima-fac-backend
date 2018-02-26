/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import pe.limatambo.entidades.Unidadmedida;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface UnidadMedidaServicio extends GenericoServicio<Unidadmedida, Integer>{
    public BusquedaPaginada busquedaPaginada(Unidadmedida entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc);
    public List<Unidadmedida> listar();
    public Unidadmedida insertar(Unidadmedida entidad) throws GeneralException;

    public Unidadmedida actualizar(Unidadmedida entidad) throws GeneralException;
}
