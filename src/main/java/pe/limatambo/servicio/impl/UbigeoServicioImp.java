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
import pe.limatambo.entidades.Ubigeo;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.UbigeoServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class UbigeoServicioImp extends GenericoServicioImpl<Ubigeo, Integer> implements UbigeoServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    @Autowired
    private GenericoDao<Ubigeo, Integer> ubigeoDao;

    public UbigeoServicioImp(GenericoDao<Ubigeo, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public List<Ubigeo> listar(String padre) {
        Criterio filtro;
        filtro = Criterio.forClass(Ubigeo.class);
        if (padre!=null) {
            filtro.add(Restrictions.eq("codubigeo", padre));
        }
        else {
           filtro.add(Restrictions.isNull("codubigeo")); 
        }
        return ubigeoDao.buscarPorCriteriaSinProyecciones(filtro);
    }

    @Override
    public Ubigeo insertar(Ubigeo entidad) throws GeneralException{
        return ubigeoDao.insertar(entidad);
    }
    
    @Override
    public BusquedaPaginada busquedaPaginada(Ubigeo entidadBuscar, BusquedaPaginada busquedaPaginada, String nombre) {
        Criterio filtro;
        filtro = Criterio.forClass(Ubigeo.class);
        if (nombre!= null && !nombre.equals("")) {
            filtro.add(Restrictions.like("nombre", nombre));
        }
        busquedaPaginada.setTotalRegistros(ubigeoDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(ubigeoDao.buscarPorCriteriaSinProyecciones(filtro));
        return busquedaPaginada;
    }
    
}
