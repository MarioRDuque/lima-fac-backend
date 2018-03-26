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
import pe.limatambo.entidades.Parametro;
import pe.limatambo.entidades.Producto;
import pe.limatambo.entidades.Productomedida;
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
    private GenericoDao<Parametro, Long> parametroDao;
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
    public Venta guardar(Venta venta){
        if (venta != null) {
            List<Ventadet> ventadelList = venta.getVentadetList();
            venta.setVentadetList(null);
            venta.setEstado(Boolean.TRUE);
            TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
            venta.setFechaemision(new Date());
            obtenerSerie(venta);
            obtenerRuc(venta);
            venta = ventaDao.insertar(venta);// cabecera
            for (Ventadet detalle : ventadelList) {
                detalle.setIdventa(venta.getId());
                ventaDetalleDao.insertar(detalle);// detalle
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
            Integer idVenta, Date desde, Date hasta, String dni, String nombre, String usuario, String seriecorrelativo) {
        Criterio filtro;
        filtro = Criterio.forClass(Venta.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (idVenta != null && idVenta > 0) {
            filtro.add(Restrictions.eq("id", idVenta));
        }
        if(seriecorrelativo!=null){
            String serie;
            Integer correlativo;
            int inicio = seriecorrelativo.indexOf("-");
            if (inicio <= 0) {
                throw new GeneralException("Formato erroneo de busqueda", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
            }
            serie = seriecorrelativo.substring(0, inicio);
            correlativo = Integer.parseInt(seriecorrelativo.substring(inicio+1));
            filtro.add(Restrictions.eq("serie", serie));
            filtro.add(Restrictions.eq("correlativo", correlativo));
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

    @Override
    public void generarDocumentoCab(Long id) throws IOException {
        Venta venta = obtener(id);
        String correlativo = LimatamboUtil.completarNumeroConCeros(8, venta.getCorrelativo());
        String nombrecab = venta.getRucempresa() + "-" + venta.getTipooperacion() + "-" + venta.getSerie() + "-" + correlativo;
        File cabecera = new File(URL_DOC_CAB + "" + nombrecab + ".CAB");
        FileWriter escribir = new FileWriter(cabecera, false);
        escribir.write(
                "01" + "|"
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
                + venta.getImportetotal() + "|"
        );
        escribir.close();
    }

    @Override
    public void generarDocumentoDet(Long id) throws IOException {
        Venta venta = obtener(id);
        String correlativo = LimatamboUtil.completarNumeroConCeros(8, venta.getCorrelativo());
        List<Ventadet> detalle = venta.getVentadetList();
        String nombredet = venta.getRucempresa() + "-" + venta.getTipooperacion() + "-" + venta.getSerie() + "-" + correlativo;
        File detalle_det = new File(URL_DOC_DET + "" + nombredet + ".DET");
        FileWriter escribir = new FileWriter(detalle_det, false);
        for (int i = 0; i < detalle.size(); i++) {
            escribir.write(
                "EA" + "|"
                + detalle.get(i).getCantidad() + "|"
                + detalle.get(i).getIdproducto().getId() + "|"
                + detalle.get(i).getCodproductosunat() + "|"
                + detalle.get(i).getIdproducto().getNombre() + "|"
                + detalle.get(i).getValorunitariosinigv()+ "|"
                + detalle.get(i).getDescuentounitario() + "|"
                + detalle.get(i).getIgvitem()+ "|"
                + detalle.get(i).getAfectacionigv() + "|"
                + detalle.get(i).getIscitem() + "|"
                + detalle.get(i).getTiposistemaisc() + "|"
                + detalle.get(i).getPreciototalsinigv()+ "|"
                + detalle.get(i).getPreciototal() + "|" 
                + "\n"
            );
        }
        escribir.close();
    }

    private void obtenerSerie(Venta venta) {
        Criterio filtro;
        filtro = Criterio.forClass(Parametro.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("nombre", "SERIE"));
        Parametro p = parametroDao.obtenerPorCriteriaSinProyecciones(filtro);
        switch(venta.getTipooperacion()){
            case "01":
                venta.setSerie("F"+p.getValor());
                break;
            case "03":
                venta.setSerie("B"+p.getValor());
                break;
            case "00":
                venta.setSerie("N"+p.getValor());
                break;    
        }
    }

    private void obtenerRuc(Venta venta) {
        Criterio filtro;
        filtro = Criterio.forClass(Parametro.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("nombre", "RUC"));
        Parametro p = parametroDao.obtenerPorCriteriaSinProyecciones(filtro);
        venta.setRucempresa(p.getValor());
    }

}
