/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.dto.ClienteDTO;
import pe.limatambo.entidades.Cliente;
import pe.limatambo.entidades.Persona;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.ClienteServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
import pe.limatambo.util.Mensaje;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class ClienteServicioImp extends GenericoServicioImpl<Cliente, Integer> implements ClienteServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private GenericoDao<Cliente, Integer> clienteDao;
    @Autowired
    private GenericoDao<Persona, Integer> personaDao;

    public ClienteServicioImp(GenericoDao<Cliente, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public BusquedaPaginada busquedaPaginada(Cliente entidadBuscar, BusquedaPaginada busquedaPaginada, String numdoc, String nombre, String idubigeo) {
        Criterio filtro;
        filtro = Criterio.forClass(Cliente.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.createAlias("idpersona", "persona", JoinType.LEFT_OUTER_JOIN);
        if (numdoc!= null && !numdoc.equals("")) {
            filtro.add(Restrictions.ilike("persona.numdocumento", '%'+numdoc+'%'));
        }
        if (idubigeo!= null && !idubigeo.equals("")) {
            filtro.add(Restrictions.eq("persona.idubigeo.id", Integer.parseInt(idubigeo)));
        }
        if (nombre!= null && !nombre.equals("")) {
            filtro.add(Restrictions.ilike("persona.nombrecompleto", '%'+nombre+'%'));
        }
        busquedaPaginada.setTotalRegistros(clienteDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(clienteDao.buscarPorCriteriaSinProyecciones(filtro));
        return busquedaPaginada;
    }

    @Override
    public Cliente insertar(Cliente entidad) throws GeneralException{
        if (entidad.getIdpersona()==null) {
            throw new GeneralException(Mensaje.NO_EXISTEN_DATOS, "Guardar retorno nulo", loggerServicio);
        }
        Criterio filtro;
        filtro = Criterio.forClass(Persona.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("numdocumento", entidad.getIdpersona().getNumdocumento()));
        Persona persona = personaDao.obtenerPorCriteriaSinProyecciones(filtro);
        if (persona!=null) {
            throw new GeneralException("Guardar retorno nulo", "Ya existe persona.", loggerServicio);
        }
        entidad.getIdpersona().setNombrecompleto(entidad.getIdpersona().getNombre() + " " + entidad.getIdpersona().getApellidos());
        entidad.getIdpersona().setEstado(Boolean.TRUE);
        persona = personaDao.insertar(entidad.getIdpersona());
        entidad.setEstado(Boolean.TRUE);
        entidad.setIdpersona(persona);
        return clienteDao.insertar(entidad);
    }

    @Override
    public ClienteDTO obtenerCliente(String id) throws GeneralException {
        Criterio filtro;
        filtro = Criterio.forClass(Persona.class);
        filtro.add(Restrictions.eq("numdocumento", id));
        Persona persona = personaDao.obtenerPorCriteriaSinProyecciones(filtro);
        ClienteDTO o = new ClienteDTO();
        if(persona!=null){
            o.setDocumento(id);
            o.setNombre(persona.getNombrecompleto());
        }
        return o;
    }
    
    @Override
    public Cliente actualizar(Cliente entidad) throws GeneralException{
        if (entidad.getIdpersona()==null) {
            throw new GeneralException(Mensaje.NO_EXISTEN_DATOS, "Guardar retorno nulo", loggerServicio);
        }
        Criterio filtro;
        filtro = Criterio.forClass(Persona.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.ne("id", entidad.getIdpersona().getId()));
        filtro.add(Restrictions.eq("numdocumento", entidad.getIdpersona().getNumdocumento()));
        Persona persona = personaDao.obtenerPorCriteriaSinProyecciones(filtro);
        if (persona!=null) {
            throw new GeneralException("Guardar retorno nulo", "Ya existe persona.", loggerServicio);
        }
        entidad.getIdpersona().setNombrecompleto(entidad.getIdpersona().getNombre() + " " + entidad.getIdpersona().getApellidos());
        entidad.getIdpersona().setEstado(Boolean.TRUE);
        personaDao.actualizar(entidad.getIdpersona());
        entidad.setEstado(Boolean.TRUE);
        entidad.setIdpersona(entidad.getIdpersona());
        return clienteDao.actualizar(entidad);
    }
    
}
