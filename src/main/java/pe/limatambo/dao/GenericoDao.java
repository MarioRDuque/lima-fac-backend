/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.limatambo.dao;

import java.util.List;
import org.hibernate.criterion.Projection;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.util.Criterio;

/**
 *
 * @author dev-out-04
 * @param <Entidad>
 * @param <TipoLlave>
 */
@Transactional
public interface GenericoDao <Entidad, TipoLlave> {
    List<Entidad> listarTodosVigentes(Class<Entidad> claseEntidad, String nameColum, String valor);
    Entidad insertar(Entidad entidad);
    Entidad actualizar(Entidad entidad);
    void eliminar(Entidad entidad);
    Long cantidadPorCriteria(Criterio filtro, String distinctProperty);
    List proyeccionPorCriteria(Criterio filtro, Class resultado);
    List<Entidad> listarConFiltro(Criterio filtro, Projection proyeccion);
    Entidad obtener(Class<Entidad> claseEntidad, TipoLlave id);
    List<Entidad> buscarPorCriteriaSinProyecciones(Criterio filtro);
    Entidad obtenerPorCriteriaSinProyecciones(Criterio filtro);
    public List<Entidad> listarPorCriteriaProyeccion(Criterio filtro);
    List<Entidad> listarFiltroDistinct(Criterio filtro);
    List<Entidad> buscarPorCriteriaConProyecciones(Criterio filtro);
}
