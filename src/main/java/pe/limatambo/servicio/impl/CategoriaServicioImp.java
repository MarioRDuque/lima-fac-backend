/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import java.util.List;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.entidades.Categoria;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.CategoriaServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class CategoriaServicioImp extends GenericoServicioImpl<Categoria, Integer> implements CategoriaServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    @Autowired
    private GenericoDao<Categoria, Integer> categoriaDao;

    public CategoriaServicioImp(GenericoDao<Categoria, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public List<Categoria> listar() {
        Criterio filtro;
        filtro = Criterio.forClass(Categoria.class);
        filtro.add(Restrictions.eq("estado", true));
        filtro.addOrder(Order.asc("descripcion"));
        return categoriaDao.buscarPorCriteriaSinProyecciones(filtro);
    }

    @Override
    public Categoria insertar(Categoria entidad) throws GeneralException{
        entidad.setEstado(Boolean.TRUE);
        return categoriaDao.insertar(entidad);
    }
    
    @Override
    public Categoria actualizar(Categoria u) throws GeneralException {
        return categoriaDao.actualizar(u);
    }
    
    @Override
    public BusquedaPaginada busquedaPaginada(Categoria entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc) {
        Criterio filtro;
        filtro = Criterio.forClass(Categoria.class);
        if (numdoc!= null && !numdoc.equals("")) {
            filtro.add(Restrictions.ilike("descripcion", '%'+numdoc+'%'));
        }
        busquedaPaginada.setTotalRegistros(categoriaDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.asc("descripcion"));
        busquedaPaginada.setRegistros(categoriaDao.buscarPorCriteriaSinProyecciones(filtro));
        return busquedaPaginada;
    }
    
}
