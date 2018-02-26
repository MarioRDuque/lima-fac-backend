package pe.limatambo.dao.impl;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.util.Criterio;

@Repository
public class GenericoDaoImpl<Entidad extends Serializable, TipoLlave extends Serializable> implements GenericoDao<Entidad, TipoLlave> {

    @Autowired
    protected SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public Entidad obtener(Class<Entidad> claseEntidad, TipoLlave id) {
        return (Entidad) this.sessionFactory.getCurrentSession().get(claseEntidad.getName(), id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entidad insertar(Entidad entidad) {
        this.sessionFactory.getCurrentSession().save(entidad);
        return entidad;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Entidad actualizar(Entidad object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
        return object;
    }

    @Override
    public void eliminar(Entidad entidad) {
        this.sessionFactory.getCurrentSession().delete(entidad);
    }
    
    @Override
    public List<Entidad> listarConFiltro(Criterio filtro, Projection proyeccion) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setProjection(proyeccion);
        if(proyeccion == null){
            busqueda.setResultTransformer(Criteria.ROOT_ENTITY);
        }else{
        }
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (List<Entidad>) busqueda.list();
    } 

    @SuppressWarnings({"rawtypes"})
    @Override
    public List proyeccionPorCriteria(Criterio filtro, Class resultado) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        busqueda.setResultTransformer(new AliasToBeanResultTransformer(resultado));
        return busqueda.list();
    }
    
    @Override
    public List<Entidad> listarPorCriteriaProyeccion(Criterio filtro) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (List<Entidad>) busqueda.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Long cantidadPorCriteria(Criterio filtro, String distinctProperty) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        busqueda.setProjection(Projections.projectionList()
                .add(Projections.countDistinct(distinctProperty)));
        List<Long> a = (List<Long>) busqueda.list();
        return a.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entidad> listarTodosVigentes(Class<Entidad> claseEntidad, String nameColum, String valor) {
        Criterio filtro = Criterio.forClass(claseEntidad);
        filtro.add(Restrictions.eq(nameColum, valor));
        return listarPorCriteria(filtro);
    }
    
    @SuppressWarnings("unchecked")
    public List<Entidad> listarPorCriteria(Criterio filtro) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setProjection(null);
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (List<Entidad>) busqueda.list();
    }
    
    @Override
    public List<Entidad> buscarPorCriteriaSinProyecciones(Criterio filtro) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setProjection(null);
        busqueda.setResultTransformer(Criteria.ROOT_ENTITY);
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (List<Entidad>) busqueda.list();
    }
    
    @Override
    public List<Entidad> buscarPorCriteriaConProyecciones(Criterio filtro) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setResultTransformer(Criteria.ROOT_ENTITY);
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (List<Entidad>) busqueda.list();
    }
    
    @Override
    public List<Entidad> listarFiltroDistinct(Criterio filtro) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setProjection(null);
        busqueda.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (List<Entidad>) busqueda.list();
    }

    @Override
    public Entidad obtenerPorCriteriaSinProyecciones(Criterio filtro) {
        Criteria busqueda = filtro.getExecutableCriteria(this.sessionFactory.getCurrentSession());
        busqueda.setProjection(null);
        busqueda.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        busqueda.setFirstResult(((Criterio) filtro).getFirstResult());
        return (Entidad) busqueda.uniqueResult();
    }
    
}
