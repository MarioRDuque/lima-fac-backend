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
import pe.limatambo.entidades.Unidadmedida;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.UnidadMedidaServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class UnidadMedidaServicioImp extends GenericoServicioImpl<Unidadmedida, Integer> implements UnidadMedidaServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    @Autowired
    private GenericoDao<Unidadmedida, Integer> unidadMedidaDao;

    public UnidadMedidaServicioImp(GenericoDao<Unidadmedida, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public List<Unidadmedida> listar() {
        Criterio filtro;
        filtro = Criterio.forClass(Unidadmedida.class);
        filtro.add(Restrictions.eq("estado", true));
        filtro.addOrder(Order.asc("descripcion"));
        return unidadMedidaDao.buscarPorCriteriaSinProyecciones(filtro);
    }

    @Override
    public Unidadmedida insertar(Unidadmedida entidad) throws GeneralException{
        entidad.setEstado(Boolean.TRUE);
        return unidadMedidaDao.insertar(entidad);
    }
    
    @Override
    public Unidadmedida actualizar(Unidadmedida u) throws GeneralException {
        return unidadMedidaDao.actualizar(u);
    }
    
    @Override
    public BusquedaPaginada busquedaPaginada(Unidadmedida entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc) {
        Criterio filtro;
        filtro = Criterio.forClass(Unidadmedida.class);
        if (numdoc!= null && !numdoc.equals("")) {
            filtro.add(Restrictions.ilike("descripcion", '%'+numdoc+'%'));
        }
        busquedaPaginada.setTotalRegistros(unidadMedidaDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.asc("descripcion"));
        busquedaPaginada.setRegistros(unidadMedidaDao.buscarPorCriteriaSinProyecciones(filtro));
        return busquedaPaginada;
    }

}
