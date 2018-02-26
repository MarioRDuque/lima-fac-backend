/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import pe.limatambo.entidades.Tipousuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface TipoUsuarioServicio extends GenericoServicio<Tipousuario, Integer>{
    public BusquedaPaginada busquedaPaginada(Tipousuario entidadBuscar, BusquedaPaginada busquedaPaginada, String tipo);
    public Tipousuario insertar(Tipousuario entidad) throws GeneralException;
}
