/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import pe.limatambo.entidades.Compra;
import pe.limatambo.entidades.Compradet;
import pe.limatambo.servicio.CompraServicio;

/**
 *
 * @author dev-out-03
 */
@Service
@Transactional
public class CompraServicioImp extends GenericoServicioImpl<Compra, Long> implements CompraServicio {

    private final Logger loggerServicio = LoggerFactory.getLogger(getClass());
    @Autowired
    private GenericoDao<Compra, Long> compraDao;
    @Autowired
    private GenericoDao<Compradet, Long> compraDetalleDao;
    @Autowired
    private GenericoDao<Parametro, Long> parametroDao;
    @Autowired
    private GenericoDao<Productomedida, Integer> productomedidaDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private GenericoDao<Producto, Integer> productoDao;
    @Value("${url.doc.cab}")
    private String URL_DOC_CAB;
    @Value("${url.doc.det}")
    private String URL_DOC_DET;

    public CompraServicioImp(GenericoDao<Compra, Long> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public Compra guardar(Compra compra) {
        if (compra != null) {
            List<Compradet> compradelList = compra.getCompradetList();
            compra.setCompradetList(null);
            compra.setEstado(Boolean.TRUE);
            TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
            compra.setFechaemision(new Date());
            obtenerSerie(compra);
            obtenerRuc(compra);
            compra = compraDao.insertar(compra);// cabecera
            for (Compradet detalle : compradelList) {
                Producto p = productoDao.obtener(Producto.class, detalle.getIdproducto().getId());
                if (p.getStockmin() == null) {
                    p.setStockmin(BigDecimal.ZERO);
                }
                p.setStockmin(p.getStockmin().add(new BigDecimal(detalle.getCantidad())));
                productoDao.actualizar(p);
                detalle.setIdcompra(compra.getId());
                compraDetalleDao.insertar(detalle);// detalle
            }
        } else {
            throw new GeneralException("Evento nulo", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
        }
        return compra;
    }

    @Override
    public Compra obtener(Long id) {
        Compra p = obtener(Compra.class, id);
        List<Compradet> dp = obtenerVigentes(id);
        p.setCompradetList(dp);
        for (int i = 0; i < dp.size(); i++) {
            Producto pr = dp.get(i).getIdproducto();
            List<Productomedida> pm = obtenerMedidasPorProducto(pr.getId());
            pr.setProductomedidaList(pm);
        }
        return p;
    }

    @Override
    public void actualizarEstadoDetalle(Long id) throws GeneralException {
        String hql = "UPDATE Compradet SET estado=FALSE WHERE id=:id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public BusquedaPaginada busquedaPaginada(Compra entidadBuscar, BusquedaPaginada busquedaPaginada,
            Integer idCompra, Date desde, Date hasta, String dni, String nombre, String usuario, String seriecorrelativo) {
        Criterio filtro;
        filtro = Criterio.forClass(Compra.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        if (idCompra != null && idCompra > 0) {
            filtro.add(Restrictions.eq("id", idCompra));
        }
        if (seriecorrelativo != null) {
            String serie;
            Integer correlativo;
            int inicio = seriecorrelativo.indexOf("-");
            if (inicio <= 0) {
                throw new GeneralException("Formato erroneo de busqueda", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
            }
            serie = seriecorrelativo.substring(0, inicio);
            correlativo = Integer.parseInt(seriecorrelativo.substring(inicio + 1));
            filtro.add(Restrictions.eq("serie", serie));
            filtro.add(Restrictions.eq("correlativo", correlativo));
        }
        if (dni != null) {
            filtro.add(Restrictions.ilike("docproveedor", '%' + dni + '%'));
        }
        if (nombre != null) {
            filtro.add(Restrictions.ilike("nombreproveedor", '%' + nombre + '%'));
        }
        if (LimatamboUtil.sonNoNulos(desde, hasta)) {
            if (desde.before(hasta)) {
                filtro.add(Restrictions.between("fechaemision", desde, hasta));
            } else {
                throw new GeneralException("Las fechas son insconsistentes", Mensaje.ERROR_GENERAL, loggerServicio);
            }
        }
        busquedaPaginada.setTotalRegistros(compraDao.cantidadPorCriteria(filtro, "id"));
        busquedaPaginada.calcularCantidadDePaginas();
        busquedaPaginada.validarPaginaActual();
        filtro.setProjection(Projections.projectionList()
                .add(Projections.property("id"), "id")
                .add(Projections.property("serie"), "serie")
                .add(Projections.property("correlativo"), "correlativo")
                .add(Projections.property("fechaemision"), "fechaemision")
                .add(Projections.property("docproveedor"), "docproveedor")
                .add(Projections.property("nombreproveedor"), "nombreproveedor")
                .add(Projections.property("usuariosave"), "usuariosave"));
        filtro.calcularDatosParaPaginacion(busquedaPaginada);
        filtro.addOrder(Order.desc("id"));
        busquedaPaginada.setRegistros(compraDao.proyeccionPorCriteria(filtro, Compra.class));
        return busquedaPaginada;
    }

    @Override
    public Compra actualizar(Compra compra) {
        if (compra.getId() > 0) {
            List<Compradet> compradelList = compra.getCompradetList();
            for (Compradet detalle : compradelList) {
                Producto p = productoDao.obtener(Producto.class, detalle.getIdproducto().getId());
                if (p.getStockmin() == null) {
                    p.setStockmin(BigDecimal.ZERO);
                }
                p.setStockmin(p.getStockmin().subtract(new BigDecimal(detalle.getCantidad())));
                if (p.getStockmin().compareTo(BigDecimal.ZERO) < 0) {
                    throw new GeneralException("No hay stock para el producto: " + p.getNombre(), "No hay stock para quitar el producto: " + p.getNombre(), loggerServicio);
                }
                productoDao.actualizar(p);
            }
            compraDao.eliminar(compra);
        } else {
            throw new GeneralException("Compra nulo", Mensaje.CAMPO_OBLIGATORIO_VACIO, loggerServicio);
        }
        return compra;
    }

    private List<Compradet> obtenerVigentes(Long id) {
        Criterio filtro;
        filtro = Criterio.forClass(Compradet.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("idcompra", id));
        return compraDetalleDao.listarFiltroDistinct(filtro);
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
        Compra compra = obtener(id);
        String extension = ".CAB";
        String correlativo = LimatamboUtil.completarNumeroConCeros(8, compra.getCorrelativo());
        String nombrecab = compra.getRucempresa() + "-" + compra.getTipooperacion() + "-" + compra.getSerie() + "-" + correlativo;
        File cabecera = new File(URL_DOC_CAB + "" + nombrecab + extension);
        FileWriter escribir = new FileWriter(cabecera, false);
        escribir.write(
                "01" + "|"
                + compra.getFechaemision() + "|"
                + compra.getDomfiscal() + "|"
                + compra.getIdtipodocumento().getTipo() + "|"
                + compra.getDocproveedor() + "|"
                + compra.getNombreproveedor() + "|"
                + compra.getIdmoneda().getSimbolosunat() + "|"
                + compra.getDescglobal() + "|"
                + compra.getSumcargos() + "|"
                + compra.getTotaldesc() + "|"
                + compra.getTotalsinigv() + "|"
                + compra.getValoropeinaf() + "|"
                + compra.getValopeexo() + "|"
                + compra.getIgv() + "|"
                + compra.getIsc() + "|"
                + compra.getSumaotrostrib() + "|"
                + compra.getImportetotal() + "|"
        );
        escribir.close();
    }

    @Override
    public void generarDocumentoCabNota(Long id, String tipoOld) throws IOException {
        Compra compra = obtener(id);
        String extension = ".NOT";
        String correlativo = LimatamboUtil.completarNumeroConCeros(8, compra.getCorrelativo());
        String nombrecab = compra.getRucempresa() + "-" + compra.getTipooperacion() + "-" + compra.getSerie() + "-" + correlativo;
        File cabecera = new File(URL_DOC_CAB + "" + nombrecab + extension);
        FileWriter escribir = new FileWriter(cabecera, false);
        escribir.write(
                compra.getFechaemision() + "|"
                + "01" + "|"
                + compra.getDescripcion() + "|"
                + tipoOld + "|"
                + compra.getSerie() + "-" + correlativo + "|"
                + compra.getIdtipodocumento().getTipo() + "|"
                + compra.getDocproveedor() + "|"
                + compra.getNombreproveedor() + "|"
                + compra.getIdmoneda().getSimbolosunat() + "|"
                + compra.getSumcargos() + "|"
                + compra.getTotalsinigv() + "|"
                + compra.getValoropeinaf() + "|"
                + compra.getValopeexo() + "|"
                + compra.getIgv() + "|"
                + compra.getIsc() + "|"
                + compra.getSumaotrostrib() + "|"
                + compra.getImportetotal() + "|"
        );
        escribir.close();
    }

    @Override
    public void generarDocumentoDet(Long id) throws IOException {
        Compra compra = obtener(id);
        String correlativo = LimatamboUtil.completarNumeroConCeros(8, compra.getCorrelativo());
        List<Compradet> detalle = compra.getCompradetList();
        String nombredet = compra.getRucempresa() + "-" + compra.getTipooperacion() + "-" + compra.getSerie() + "-" + correlativo;
        File detalle_det = new File(URL_DOC_DET + "" + nombredet + ".DET");
        FileWriter escribir = new FileWriter(detalle_det, false);
        for (int i = 0; i < detalle.size(); i++) {
            escribir.write(
                    "EA" + "|"
                    + detalle.get(i).getCantidad() + "|"
                    + detalle.get(i).getIdproducto().getId() + "|"
                    + detalle.get(i).getCodproductosunat() + "|"
                    + detalle.get(i).getIdproducto().getNombre() + "|"
                    + detalle.get(i).getValorunitariosinigv() + "|"
                    + detalle.get(i).getDescuentototal() + "|"
                    + detalle.get(i).getIgvitem() + "|"
                    + detalle.get(i).getAfectacionigv() + "|"
                    + detalle.get(i).getIscitem() + "|"
                    + detalle.get(i).getTiposistemaisc() + "|"
                    + detalle.get(i).getPreciototal() + "|"
                    + detalle.get(i).getPreciototalsinigv() + "|"
                    + "\n"
            );
        }
        escribir.close();
    }

    private void obtenerSerie(Compra compra) {
        Criterio filtro;
        filtro = Criterio.forClass(Parametro.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("nombre", "SERIE"));
        Parametro p = parametroDao.obtenerPorCriteriaSinProyecciones(filtro);
        switch (compra.getTipooperacion()) {
            case "01":
                compra.setSerie("F" + p.getValor());
                break;
            case "03":
                compra.setSerie("B" + p.getValor());
                break;
            case "00":
                compra.setSerie("N" + p.getValor());
                break;
        }
    }

    private void obtenerRuc(Compra compra) {
        Criterio filtro;
        filtro = Criterio.forClass(Parametro.class);
        filtro.add(Restrictions.eq("estado", Boolean.TRUE));
        filtro.add(Restrictions.eq("nombre", "RUC"));
        Parametro p = parametroDao.obtenerPorCriteriaSinProyecciones(filtro);
        compra.setRucempresa(p.getValor());
    }

    @Override
    public Map<String, Object> exportarCompras(Date desde, Date hasta, String seriecorrelativo, String usuario) throws Exception {
        List<String> listaCabecera = new ArrayList();
        List<String> listaCuerpo = new ArrayList();
        Map<String, Object> respuesta = new HashMap<>();
        try {
            listaCabecera.add("SListado de compras");
            if (desde != null) {
                listaCabecera.add("SDesde: " + desde);
            }
            if (hasta != null) {
                listaCabecera.add("SHasta: " + hasta);
            }
            if (seriecorrelativo != null && !seriecorrelativo.isEmpty()) {
                listaCabecera.add("SCodigo: " + seriecorrelativo);
            }
            listaCabecera.add("SUsuario: " + usuario);
            listaCabecera.add("S");
            listaCuerpo.add("SCódigo de la compra" + "¬" + "SProducto" + "¬" + "SFecha Compra" + "¬" + "STotal" + "¬" + "S-" + "¬" + "SCodigo Producto" + "¬" + "SProducto" + "¬" + "SCantidad" + "¬" + "SPrecio" + "¬" + "SParcial");
            List<Compra> compras = listarCompras(desde, hasta, seriecorrelativo, usuario);
            for (Compra compra : compras) {
                listaCuerpo.add(
                        (compra.getSerie() == null ? "B" : "S" + compra.getSerie() + "-" + compra.getCorrelativo()) + "¬"
                        + (compra.getDocproveedor()== null ? "B" : "S" + compra.getDocproveedor() + " " + compra.getNombreproveedor()) + "¬"
                        + (compra.getFechaemision() == null ? "B" : "S" + compra.getFechaemision()) + "¬"
                        + (compra.getImportetotal() < 0 ? "D0" : "D" + compra.getImportetotal()) + "¬"
                        + "B" + "¬"
                );
                List<Compradet> compradelList = obtenerVigentes(compra.getId());
                for (Compradet detalle : compradelList) {
                    listaCuerpo.add(
                            "B" + "¬"
                            + "B" + "¬"
                            + "B" + "¬"
                            + "B" + "¬"
                            + "B" + "¬"
                            + (detalle.getIdproducto().getId() == null ? "B" : "S" + detalle.getIdproducto().getId()) + "¬"
                            + (detalle.getIdproducto().getNombre() == null ? "B" : "S" + detalle.getIdproducto().getNombre()) + "¬"
                            + "D" + (detalle.getCantidad()) + "¬"
                            + "D" + (detalle.getPreciototal()) + "¬"
                            + "D" + (detalle.getPreciototal()) + "¬"
                    );
                }
                listaCuerpo.add("B" + "¬");
                listaCuerpo.add("B" + "¬");
            }
            respuesta.put("listaCabecera", listaCabecera);
            respuesta.put("listaCuerpo", listaCuerpo);

            return respuesta;
        } catch (Exception e) {
            if (e != null && e.getMessage() != null) {
                listaCuerpo.add("S" + e.getMessage() + "¬");
            } else {
                listaCuerpo.add("SNo se encontraron resultados" + "¬");
            }
            respuesta.put("listaCabecera", listaCabecera);
            respuesta.put("listaCuerpo", listaCuerpo);
            return respuesta;
        }
    }

    public List<Compra> listarCompras(Date desde, Date hasta, String seriecorrelativo, String usuario) {
        Criterio filtro;
        filtro = Criterio.forClass(Compra.class);
        if (seriecorrelativo != null && !seriecorrelativo.isEmpty()) {
            String serie;
            Integer correlativo;
            int inicio = seriecorrelativo.indexOf("-");
            if (inicio <= 0) {
                throw new GeneralException("Formato erroneo de busqueda", "Formato erroneo de busqueda", loggerServicio);
            }
            serie = seriecorrelativo.substring(0, inicio);
            correlativo = Integer.parseInt(seriecorrelativo.substring(inicio + 1));
            filtro.add(Restrictions.eq("serie", serie));
            filtro.add(Restrictions.eq("correlativo", correlativo));
        }
        if (LimatamboUtil.sonNoNulos(desde, hasta)) {
            if (desde.before(hasta)) {
                filtro.add(Restrictions.between("fechaemision", desde, hasta));
            } else {
                throw new GeneralException("Las fechas son insconsistentes", "Las fechas son insconsistentes", loggerServicio);
            }
        }
        filtro.setProjection(Projections.projectionList()
                .add(Projections.property("id"), "id")
                .add(Projections.property("serie"), "serie")
                .add(Projections.property("correlativo"), "correlativo")
                .add(Projections.property("fechaemision"), "fechaemision")
                .add(Projections.property("docproveedor"), "docproveedor")
                .add(Projections.property("importetotal"), "importetotal")
                .add(Projections.property("nombreproveedor"), "nombreproveedor")
                .add(Projections.property("usuariosave"), "usuariosave"));
        filtro.addOrder(Order.desc("id"));
        return compraDao.proyeccionPorCriteria(filtro, Compra.class);
    }

}
