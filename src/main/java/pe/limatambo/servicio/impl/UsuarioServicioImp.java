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
import pe.limatambo.entidades.Usuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.UsuarioServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class UsuarioServicioImp extends GenericoServicioImpl<Usuario, Integer> implements UsuarioServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private GenericoDao<Usuario, Integer> usuarioDao;

    public UsuarioServicioImp(GenericoDao<Usuario, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public BusquedaPaginada busquedaPaginada(Usuario entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc, String nomusu) {
        Criterio filtro;
        filtro = Criterio.forClass(Usuario.class);
        //filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (numdoc!= null && !numdoc.equals("")) {
            filtro.add(Restrictions.ilike("dni", '%'+numdoc+'%'));
        }
        busquedaPaginada.setTotalRegistros(usuarioDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(usuarioDao.buscarPorCriteriaSinProyecciones(filtro));
        return busquedaPaginada;
    }

    @Override
    public Usuario insertar(Usuario entidad) throws GeneralException{
        Criterio filtro;
        filtro = Criterio.forClass(Usuario.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (entidad.getId()!=null) {
            filtro.add(Restrictions.eq("id", entidad.getId()));
        }
        filtro.add(Restrictions.eq("userId", entidad.getUserId()));
        Usuario u = usuarioDao.obtenerPorCriteriaSinProyecciones(filtro);
        if (u!=null) {
            throw new GeneralException("Guardar retorno nulo", "Ya existe un usuario con igual nombre.", loggerServicio);
        }
        entidad.setEstado(Boolean.TRUE);
        entidad.setCambiarclave(Boolean.TRUE);
        return usuarioDao.insertar(entidad);
    }
    
    @Override
    public Usuario actualizar(Usuario u) throws GeneralException {
        return usuarioDao.actualizar(u);
    }
    
}
