/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.entidades.Tipodocumento;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.TipoDocumentoServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class TipoDocumentoServicioImp extends GenericoServicioImpl<Tipodocumento, Integer> implements TipoDocumentoServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private GenericoDao<Tipodocumento, Integer> tipoDocumentoDao;

    public TipoDocumentoServicioImp(GenericoDao<Tipodocumento, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public BusquedaPaginada busquedaPaginada(Tipodocumento entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc) {
        Criterio filtro;
        filtro = Criterio.forClass(Tipodocumento.class);
        //filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (numdoc!= null && !numdoc.equals("")) {
            filtro.add(Restrictions.ilike("abreviatura", '%'+numdoc+'%'));
        }
        busquedaPaginada.setTotalRegistros(tipoDocumentoDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(tipoDocumentoDao.buscarPorCriteriaSinProyecciones(filtro));
        return busquedaPaginada;
    }

    @Override
    public Tipodocumento insertar(Tipodocumento entidad) throws GeneralException{
        Criterio filtro;
        filtro = Criterio.forClass(Tipodocumento.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (entidad.getId()!=null) {
            filtro.add(Restrictions.eq("id", entidad.getId()));
        }
        filtro.add(Restrictions.eq("abreviatura", entidad.getAbreviatura()));
        Tipodocumento u = tipoDocumentoDao.obtenerPorCriteriaSinProyecciones(filtro);
        if (u!=null) {
            throw new GeneralException("Guardar retorno nulo", "Ya existe un documento con igual nombre.", loggerServicio);
        }
        entidad.setEstado(Boolean.TRUE);
        return tipoDocumentoDao.insertar(entidad);
    }
    
    @Override
    public Tipodocumento actualizar(Tipodocumento u) throws GeneralException {
        return tipoDocumentoDao.actualizar(u);
    }
    
}
