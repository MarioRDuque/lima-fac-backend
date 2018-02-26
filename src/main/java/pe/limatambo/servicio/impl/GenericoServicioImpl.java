/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.limatambo.servicio.impl;

import java.lang.reflect.ParameterizedType;
import javax.transaction.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.servicio.GenericoServicio;

/**
 *
 * @author dev-out-04
 * @param <Entidad>
 * @param <TipoLlave>
 */
@Transactional
public abstract class GenericoServicioImpl<Entidad, TipoLlave> implements GenericoServicio<Entidad, TipoLlave>{
    
    private final GenericoDao<Entidad, TipoLlave> genericoDao;
    
    @SuppressWarnings("unused")
    private Class<Entidad> domainClass = null;

    @SuppressWarnings("unchecked")
    protected GenericoServicioImpl(GenericoDao<Entidad, TipoLlave> genericoHibernate) {
        this.genericoDao = genericoHibernate;
        this.domainClass = (Class<Entidad>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    @Override
    public Entidad obtener(Class<Entidad> aClass, TipoLlave id) {
        return genericoDao.obtener(aClass, id);
    }
    
}
