/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.entidades.Cliente;
import pe.limatambo.entidades.Menu;
import pe.limatambo.entidades.Menutipousuario;
import pe.limatambo.entidades.MenutipousuarioPK;
import pe.limatambo.entidades.Persona;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.MenuServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
import pe.limatambo.util.Mensaje;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class MenuServicioImp extends GenericoServicioImpl<Menu, Integer> implements MenuServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private GenericoDao<Cliente, Integer> clienteDao;
    @Autowired
    private GenericoDao<Persona, Integer> personaDao;
    @Autowired
    private GenericoDao<Menutipousuario, MenutipousuarioPK> menuTipousuarioDao;

    public MenuServicioImp(GenericoDao<Menu, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public List<Menu> listarPorTipoDeUsuario(Integer tipousuario) throws GeneralException {
        List<Menu> menus = new ArrayList<>();
        Criterio filtro;
        filtro = Criterio.forClass(Menutipousuario.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.createAlias("menu", "menu");
        filtro.add(Restrictions.eq("menu.estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("menutipousuarioPK.idtipousuario", tipousuario));
        List<Menutipousuario> menustipos = menuTipousuarioDao.buscarPorCriteriaSinProyecciones(filtro);
        for (int i = 0; i < menustipos.size(); i++) {
            menus.add(menustipos.get(i).getMenu());
        }
        return menus;
    }
    
}
