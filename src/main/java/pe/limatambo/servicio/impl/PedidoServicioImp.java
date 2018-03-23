/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.dto.PedidoDTO;
import pe.limatambo.entidades.Detallepedido;
import pe.limatambo.entidades.Pedido;
import pe.limatambo.entidades.Producto;
import pe.limatambo.entidades.Productomedida;
import pe.limatambo.entidades.Usuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.PedidoServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
import pe.limatambo.util.LimatamboUtil;
import pe.limatambo.util.Mensaje;

//crear archivo plano
import java.io.File;
import java.io.FileWriter;
/**
 *
 * @author dev-out-03
 */

@Service
@Transactional
public class PedidoServicioImp extends GenericoServicioImpl<Pedido, Integer> implements PedidoServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    @Autowired
    private GenericoDao<Pedido, Integer> pedidoDao;
    @Autowired
    private GenericoDao<Detallepedido, Integer> pedidoDetalleDao;
    @Autowired
    private GenericoDao<Usuario, Integer> usuarioDao;
    @Autowired
    private GenericoDao<Productomedida, Integer> productomedidaDao;
    @Autowired
    private SessionFactory sessionFactory;

    public PedidoServicioImp(GenericoDao<Pedido, Integer> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        if(pedido != null) 
        {
            pedido.setEstado(Boolean.TRUE);
            TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
            pedido.setFechapedido(new Date());
            pedido = pedidoDao.insertar(pedido);// cabecera
            for (Detallepedido detalle : pedido.getDetallePedidoList()) 
            {
                detalle.setIdpedido(pedido.getId());
                pedidoDetalleDao.insertar(detalle);// detalle       
            }
        } else {
            throw new GeneralException("Evento nulo", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
        }
        return pedido;
    }

    @Override
    public Pedido obtener(Integer id) {
        Pedido p = obtener(Pedido.class, id);
        List<Detallepedido> dp = obtenerVigentes(id);
        p.setDetallePedidoList(dp);
        for (int i = 0; i < dp.size(); i++) {
            Producto pr = dp.get(i).getIdproducto();
            List<Productomedida> pm = obtenerMedidasPorProducto(pr.getId());
            pr.setProductomedidaList(pm);
        }
        return p;
    }

    @Override
    public void actualizarEstadoDetalle(Integer id) throws GeneralException {
        String hql = "UPDATE Detallepedido SET estado=FALSE WHERE id=:id";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("id", id);
            query.executeUpdate();
    }

    @Override
    public void terminarVenta(int id) throws GeneralException{
        String hql = "UPDATE Pedido SET fechaentrega=:fecha WHERE id=:id";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("fecha", new Date());
            query.executeUpdate();
    }
    
    @Override
    public BusquedaPaginada busquedaPaginada(Pedido entidadBuscar, BusquedaPaginada busquedaPaginada, 
            Integer idPedido, Date desde, Date hasta, String dni, String nombre, String idubigeo,
            String usuario, Integer tipoUsuario) {
        Criterio filtro;
        filtro = Criterio.forClass(Pedido.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.createAlias("idcliente", "cliente", JoinType.LEFT_OUTER_JOIN);
        filtro.createAlias("cliente.idpersona", "persona", JoinType.LEFT_OUTER_JOIN);
        if (idPedido!= null && idPedido>0) {
            filtro.add(Restrictions.eq("id", idPedido));
        }
        if (tipoUsuario!= null && tipoUsuario>1) {
            String[] admins = obtenerAdmins(usuario);
            filtro.add(Restrictions.in("usuariosave", admins));
        }
        if (dni!= null) {
            filtro.add(Restrictions.ilike("persona.numdocumento", '%'+dni+'%'));
        }
        if (idubigeo!= null && !idubigeo.equals("")) {
            filtro.add(Restrictions.eq("persona.idubigeo.id", Integer.parseInt(idubigeo)));
        }
        if (nombre!= null) {
            filtro.add(Restrictions.ilike("persona.nombrecompleto", '%'+nombre+'%'));
        }
        if (LimatamboUtil.sonNoNulos(desde, hasta)) {
            if (desde.before(hasta)) {
                filtro.add(Restrictions.between("fechapedido", desde, hasta));
            }else{
                throw new GeneralException("Las fechas son insconsistentes", Mensaje.ERROR_GENERAL, loggerServicio);
            }
        }
        busquedaPaginada.setTotalRegistros(pedidoDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.setProjection(Projections.projectionList()
                .add(Projections.property("id"), "codigo")
                .add(Projections.property("descripcion"), "descripcion")
                .add(Projections.property("fechapedido"), "fechaPedido")
                .add(Projections.property("fechalimite"), "fechaLimite")
                .add(Projections.property("fechaentrega"), "fechaEntrega")
                .add(Projections.property("direccion"), "direccion")
                .add(Projections.property("usuariosave"), "usuariosave")
                .add(Projections.property("persona.nombrecompleto"), "cliente"));
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(pedidoDao.proyeccionPorCriteria(filtro, PedidoDTO.class));
        return busquedaPaginada;
    }

    @Override
    public Pedido actualizar(Pedido pedido, Usuario usuario) {
        if(pedido.getId()> 0){
            pedido = pedidoDao.actualizar(pedido);
        } else {
            throw new GeneralException("Pedido nulo", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
        }
        return pedido;
    }

    private List<Detallepedido> obtenerVigentes(Integer id) {
        Criterio filtro;
        filtro = Criterio.forClass(Detallepedido.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("idpedido", id));
        return pedidoDetalleDao.listarFiltroDistinct(filtro);
    }

    private List<Productomedida> obtenerMedidasPorProducto(Integer id) {
        Criterio filtro;
        filtro = Criterio.forClass(Productomedida.class);
        filtro.add(Restrictions.eq("idproducto", id));
        filtro.add(Restrictions.eq("estado", true));
        return productomedidaDao.buscarPorCriteriaSinProyecciones(filtro);
    }

    private String[] obtenerAdmins(String usuario) {
        Criterio filtro;
        filtro = Criterio.forClass(Usuario.class);
        filtro.add(Restrictions.eq("tipousuario.id", 1));
        List<Usuario> admins = usuarioDao.buscarPorCriteriaSinProyecciones(filtro);
        int tam = admins.size();
        String[] adminArray = new String[tam+1];
        for (int i = 0; i < admins.size(); i++) {
            adminArray[i] = admins.get(i).getUserId();
        }
        adminArray[tam]=usuario;
        return adminArray;
    }
    
}
