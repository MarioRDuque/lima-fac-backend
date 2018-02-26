/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import pe.limatambo.entidades.Categoria;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;

/**
 *
 * @author dev-out-03
 */
public interface CategoriaServicio extends GenericoServicio<Categoria, Integer>{
    public BusquedaPaginada busquedaPaginada(Categoria entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc);
    public List<Categoria> listar();
    public Categoria insertar(Categoria entidad) throws GeneralException;

    public Categoria actualizar(Categoria entidad) throws GeneralException;
}
