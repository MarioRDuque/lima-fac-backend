/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import pe.limatambo.entidades.Tipodocumento;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface TipoDocumentoServicio extends GenericoServicio<Tipodocumento, Integer>{
    public BusquedaPaginada busquedaPaginada(Tipodocumento entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc);
    public Tipodocumento insertar(Tipodocumento entidad) throws GeneralException;

    public Tipodocumento actualizar(Tipodocumento unidad) throws GeneralException;
}
