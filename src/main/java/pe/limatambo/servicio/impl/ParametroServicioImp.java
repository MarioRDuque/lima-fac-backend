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
import pe.limatambo.entidades.Parametro;
import pe.limatambo.servicio.ParametroServicio;
import pe.limatambo.util.Criterio;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class ParametroServicioImp extends GenericoServicioImpl<Parametro, Long> implements ParametroServicio {

    @Autowired
    private GenericoDao<Parametro, Long> parametroDao;

    public ParametroServicioImp(GenericoDao<Parametro, Long> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public List<Parametro> listar() {
        Criterio filtro;
        filtro = Criterio.forClass(Parametro.class);
        filtro.add(Restrictions.eq("estado", true));
        filtro.add(Restrictions.eq("nombre", "IGV"));
        return parametroDao.buscarPorCriteriaSinProyecciones(filtro);
    }
    
}