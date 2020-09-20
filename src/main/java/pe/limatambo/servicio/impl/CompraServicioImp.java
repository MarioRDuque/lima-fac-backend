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
    @Value("${url.doc.cab}")
    private String URL_DOC_CAB;
    @Value("${url.doc.det}")
    private String URL_DOC_DET;

    public CompraServicioImp(GenericoDao<Compra, Long> genericoHibernate) {
        super(genericoHibernate);
    }

    @Override
    public Compra guardar(Compra compra){
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
            compra = compraDao.actualizar(compra);
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
                + compra.getSerie()+"-"+correlativo+ "|"
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
                + detalle.get(i).getValorunitariosinigv()+ "|"
                + detalle.get(i).getDescuentototal()+ "|"
                + detalle.get(i).getIgvitem()+ "|"
                + detalle.get(i).getAfectacionigv() + "|"
                + detalle.get(i).getIscitem() + "|"
                + detalle.get(i).getTiposistemaisc() + "|"
                + detalle.get(i).getPreciototal() + "|" 
                + detalle.get(i).getPreciototalsinigv()+ "|"
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
        switch(compra.getTipooperacion()){
            case "01":
                compra.setSerie("F"+p.getValor());
                break;
            case "03":
                compra.setSerie("B"+p.getValor());
                break;
            case "00":
                compra.setSerie("N"+p.getValor());
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

}
