/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import pe.limatambo.entidades.Usuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface UsuarioServicio extends GenericoServicio<Usuario, Integer>{
    public BusquedaPaginada busquedaPaginada(Usuario entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc, String nomusu);
    public Usuario insertar(Usuario entidad) throws GeneralException;

    public Usuario actualizar(Usuario unidad) throws GeneralException;
}
