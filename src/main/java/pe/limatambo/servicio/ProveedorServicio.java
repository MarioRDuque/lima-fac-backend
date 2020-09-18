/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import pe.limatambo.dto.ProveedorDTO;
import pe.limatambo.entidades.Proveedor;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface ProveedorServicio extends GenericoServicio<Proveedor, Integer>{
    public BusquedaPaginada busquedaPaginada(Proveedor entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc, String nombre, String idubigeo);
    public Proveedor insertar(Proveedor entidad) throws GeneralException;
    public Proveedor actualizar(Proveedor entidad) throws GeneralException;

    public ProveedorDTO obtenerProveedor(String id) throws GeneralException;
}
