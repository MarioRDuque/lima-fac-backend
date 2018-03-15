/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.entidades.Producto;
import pe.limatambo.entidades.Productomedida;
import pe.limatambo.entidades.Usuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.Criterio;
import pe.limatambo.util.LimatamboUtil;
import pe.limatambo.util.Mensaje;

//crear archivo plano
import pe.limatambo.entidades.Venta;
import pe.limatambo.entidades.Ventadet;
import pe.limatambo.servicio.VentaServicio;

/**
 *
 * @author dev-out-03
 */
@Service
@Transactional
public class VentaServicioImp extends GenericoServicioImpl<Venta, Long> implements VentaServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    @Autowired
    private GenericoDao<Venta, Long> ventaDao;
    @Autowired
    private GenericoDao<Ventadet, Long> ventaDetalleDao;
    @Autowired
    private GenericoDao<Productomedida, Integer> productomedidaDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Value("${url.doc.cab}")
    private String URL_DOC_CAB;
    @Value("${url.doc.det}")
    private String URL_DOC_DET;

    public VentaServicioImp(GenericoDao<Venta, Long> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public Venta guardar(Venta venta) throws IOException {
        if (venta != null) {
            List<Ventadet> ventadelList = venta.getVentadetList();
            venta.setVentadetList(null);
            venta.setEstado(Boolean.TRUE);
            TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
            venta.setFechaemision(new Date());
            venta.setRucempresa("10480470848");
            venta.setSerie("E001");
            venta.setCorrelativo("00000001");
            venta = ventaDao.insertar(venta);// cabecera
            this.generarDocumentoCab(venta);
            for (Ventadet detalle : ventadelList) {
                detalle.setIdventa(venta.getId());
                ventaDetalleDao.insertar(detalle);// detalle  
                this.generarDocumentoDet(venta, detalle);
            }
        } else {
            throw new GeneralException("Evento nulo", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
        }
        return venta;
    }

    @Override
    public Venta obtener(Long id) {
        Venta p = obtener(Venta.class, id);
        List<Ventadet> dp = obtenerVigentes(id);
        p.setVentadetList(dp);
        for (int i = 0; i < dp.size(); i++) {
            Producto pr = dp.get(i).getIdproducto();
            List<Productomedida> pm = obtenerMedidasPorProducto(pr.getId());
            pr.setProductomedidaList(pm);
        }
        return p;
    }

    @Override
    public void actualizarEstadoDetalle(Long id) throws GeneralException {
        String hql = "UPDATE Ventadet SET estado=FALSE WHERE id=:id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public BusquedaPaginada busquedaPaginada(Venta entidadBuscar, BusquedaPaginada busquedaPaginada,
            Integer idVenta, Date desde, Date hasta, String dni, String nombre, String usuario) {
        Criterio filtro;
        filtro = Criterio.forClass(Venta.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (idVenta != null && idVenta > 0) {
            filtro.add(Restrictions.eq("id", idVenta));
        }
        if (dni != null) {
            filtro.add(Restrictions.ilike("doccliente", '%' + dni + '%'));
        }
        if (nombre != null) {
            filtro.add(Restrictions.ilike("nombrecliente", '%' + nombre + '%'));
        }
        if (LimatamboUtil.sonNoNulos(desde, hasta)) {
            if (desde.before(hasta)) {
                filtro.add(Restrictions.between("fechaemision", desde, hasta));
            } else {
                throw new GeneralException("Las fechas son insconsistentes", Mensaje.ERROR_GENERAL, loggerServicio);
            }
        }
        busquedaPaginada.setTotalRegistros(ventaDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.setProjection(Projections.projectionList()
                .add(Projections.property("id"), "id")
                .add(Projections.property("serie"), "serie")
                .add(Projections.property("correlativo"), "correlativo")
                .add(Projections.property("fechaemision"), "fechaemision")
                .add(Projections.property("doccliente"), "doccliente")
                .add(Projections.property("nombrecliente"), "nombrecliente")
                .add(Projections.property("usuariosave"), "usuariosave"));
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(ventaDao.proyeccionPorCriteria(filtro, Venta.class));
        return busquedaPaginada;
    }

    @Override
    public Venta actualizar(Venta venta) {
        if (venta.getId() > 0) {
            venta = ventaDao.actualizar(venta);
        } else {
            throw new GeneralException("Venta nulo", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
        }
        return venta;
    }

    private List<Ventadet> obtenerVigentes(Long id) {
        Criterio filtro;
        filtro = Criterio.forClass(Ventadet.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("idventa", id));
        return ventaDetalleDao.listarFiltroDistinct(filtro);
    }

    private List<Productomedida> obtenerMedidasPorProducto(Integer id) {
        Criterio filtro;
        filtro = Criterio.forClass(Productomedida.class);
        filtro.add(Restrictions.eq("idproducto", id));
        filtro.add(Restrictions.eq("estado", true));
        return productomedidaDao.buscarPorCriteriaSinProyecciones(filtro);
    }

    private void generarDocumentoCab(Venta venta) throws IOException {
        String nombrecab = venta.getRucempresa() + "-" + venta.getIdtipodocumento().getTipo() + "-" + venta.getSerie() + "-" + venta.getCorrelativo();
        File cabecera = new File(URL_DOC_CAB + "" + nombrecab + ".CAB");
        FileWriter escribir = new FileWriter(cabecera, true);
        escribir.write(
                venta.getTipooperacion() + "|"
                + venta.getFechaemision() + "|"
                + venta.getDomfiscal() + "|"
                + venta.getIdtipodocumento().getTipo() + "|"
                + venta.getDoccliente() + "|"
                + venta.getNombrecliente() + "|"
                + venta.getIdmoneda().getSimbolosunat() + "|"
                + venta.getDescglobal() + "|"
                + venta.getSumcargos() + "|"
                + venta.getTotaldesc() + "|"
                + venta.getTotalsinigv() + "|"
                + venta.getValoropeinaf() + "|"
                + venta.getValopeexo() + "|"
                + venta.getIgv() + "|"
                + venta.getIsc() + "|"
                + venta.getSumaotrostrib() + "|"
                + venta.getImportetotal()
        );
        escribir.close();
    }

    private void generarDocumentoDet(Venta venta, Ventadet detalle) throws IOException {
        String nombredet = venta.getRucempresa() + "-" + venta.getIdtipodocumento().getTipo() + "-" + venta.getSerie() + "-" + venta.getCorrelativo();
        File detalle_det = new File(URL_DOC_DET + "" + nombredet + ".DET");
        FileWriter escribir = new FileWriter(detalle_det, true);
        escribir.write(
                detalle.getIdunidadmedida().getAbreviatura() + "|"
                + detalle.getCantidad() + "|"
                + detalle.getIdproducto() + "|"
                + detalle.getCodproductosunat() + "|"
                + detalle.getIdproducto().getNombre() + "|"
                + detalle.getValorunitariosinigv()+ "|"
                + detalle.getDescuentounitario() + "|"
                + detalle.getIgvitem() + "|"
                + detalle.getAfectacionigv() + "|"
                + detalle.getIscitem() + "|"
                + detalle.getTiposistemaisc() + "|"
                + detalle.getPreciounitario() + "|"
                + detalle.getPreciototal() + "|"
        );
        escribir.close();
    }

}
