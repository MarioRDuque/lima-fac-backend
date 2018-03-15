/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.entidades.Moneda;
import pe.limatambo.servicio.MonedaServicio;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class MonedaServicioImp extends GenericoServicioImpl<Moneda, Integer> implements MonedaServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    @Autowired
    private GenericoDao<Moneda, Integer> categoriaDao;

    public MonedaServicioImp(GenericoDao<Moneda, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public List<Moneda> listar() {
        Criterio filtro;
        filtro = Criterio.forClass(Moneda.class);
        filtro.add(Restrictions.eq("estado", true));
        return categoriaDao.buscarPorCriteriaSinProyecciones(filtro);
    }
    
}