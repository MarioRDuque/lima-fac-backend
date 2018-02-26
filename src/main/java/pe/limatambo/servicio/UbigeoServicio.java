/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import pe.limatambo.entidades.Ubigeo;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface UbigeoServicio extends GenericoServicio<Ubigeo, Integer>{
    public BusquedaPaginada busquedaPaginada(Ubigeo entidadBuscar, BusquedaPaginada busquedaPaginada, String nombre);
    public List<Ubigeo> listar(String padre);
    public Ubigeo insertar(Ubigeo entidad) throws GeneralException;
}
