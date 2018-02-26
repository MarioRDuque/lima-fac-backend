/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import pe.limatambo.entidades.Cliente;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface ClienteServicio extends GenericoServicio<Cliente, Integer>{
    public BusquedaPaginada busquedaPaginada(Cliente entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc, String nombre, String idubigeo);
    public Cliente insertar(Cliente entidad) throws GeneralException;
    public Cliente actualizar(Cliente entidad) throws GeneralException;
}
