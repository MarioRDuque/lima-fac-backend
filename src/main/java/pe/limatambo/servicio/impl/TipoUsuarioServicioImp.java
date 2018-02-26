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
import pe.limatambo.entidades.Tipousuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.TipoUsuarioServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class TipoUsuarioServicioImp extends GenericoServicioImpl<Tipousuario, Integer> implements TipoUsuarioServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private GenericoDao<Tipousuario, Integer> tipoUsuarioDao;

    public TipoUsuarioServicioImp(GenericoDao<Tipousuario, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public BusquedaPaginada busquedaPaginada(Tipousuario entidadBuscar, BusquedaPaginada busquedaPaginada, String tipo) {
        Criterio filtro;
        filtro = Criterio.forClass(Tipousuario.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (tipo!= null && !tipo.equals("")) {
            filtro.add(Restrictions.eq("nombre", tipo));
        }
        busquedaPaginada.setTotalRegistros(tipoUsuarioDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(tipoUsuarioDao.buscarPorCriteriaSinProyecciones(filtro));
        return busquedaPaginada;
    }

    @Override
    public Tipousuario insertar(Tipousuario entidad) throws GeneralException{
        Criterio filtro;
        filtro = Criterio.forClass(Tipousuario.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (entidad.getId()!=null) {
            filtro.add(Restrictions.eq("id", entidad.getId()));
        }
        filtro.add(Restrictions.eq("nombre", entidad.getNombre()));
        Tipousuario u = tipoUsuarioDao.obtenerPorCriteriaSinProyecciones(filtro);
        if (u!=null) {
            throw new GeneralException("Guardar retorno nulo", "Ya existe un tipo de usuario con igual nombre.", loggerServicio);
        }
        entidad.setEstado(Boolean.TRUE);
        return tipoUsuarioDao.insertar(entidad);
    }
    
}
