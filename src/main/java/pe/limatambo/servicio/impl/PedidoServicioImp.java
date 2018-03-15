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
                //DATOS QUE DE ARCHIVO CAB
                String tip_ope="01"; //TIPO DE OPERACIÓN // 01 FACTURA - 03 BOLETA DE VENTA  07 NOTA DE CREDITO 08 NOTA DE CARGO
                Date fecha_emision=pedido.getFechapedido(); //FECHA DE EMISIÓN
                String cod_doc="001"; //Codigo de domicilio fiscal
                String tip_documento="1"; //Tipo de documento de identidad
                String numero_doc=pedido.getIdcliente().getIdpersona().getNumdocumento(); //Número de documento de identidad del adquirente o usuario
                String nombre=pedido.getIdcliente().getIdpersona().getNombrecompleto(); //Apellidos y nombres, denominación o razón social del adquirente o usuario
                String cod_moneda="PEN"; //Tipo de moneda en la cual se emite la factura electrónica
                String dec_glo="0.00"; //Descuentos Globales
                String sum_carg="0.00"; //Sumatoria otros Cargos
                String tot_desc="0.00"; //Total descuentos
                String tot_sin_igv="21.19"; //Total valor de venta – Operaciones gravadas sin IGV
                String tot_val_ope_inaf="0.00"; //Total valor de venta – Operaciones inafectas
                String tot_val_ope_exo="0.00"; //Total valor de venta – Operaciones exoneradas
                String igv="3.81"; //Sumatoria IGV
                String isc="0.00"; //Sumatoria ISC
                String sum_otros_trib="0.00"; //Sumatoria otros tributos
                String importe_total="25.00"; //Importe total de la venta, cesión en uso o del servicio prestado
                //DATOS DEL ARCHIVO PLANO .DET
                String cod_item="EA"; //Código de unidad de medida por ítem
                String cantidad_unid="1.000";// Cantidad de unidades por ítem
                String cod_producto="317"; // codigo de producto
                String cod_prod_sunat=""; // codigo de producto sunat
                String descripcion="DEDUCIBLE DE CONSULTA  MAPFRE SEGUROS"; // Del servicio prestado, bien vendido o cedido en uso, indicando las características.
                String valor_unit="42.3700";// Valor unitario de producto
                String descuento="0.00";//descuentos por el total de la linea
                String igv_item="7.63";//Monto de IGV por ítem
                String afet_igv="10";//Afectación al IGV
                String tip_isc_item="0.00";//Monto de ISC por ítem
                String tip_sist_isc="01";//Tipo de sistema ISC
                String precio_unit="50.0000";//Precio de venta unitario por item    
                String valor_venta="42.37"; //VALOR DE VENTA POR ITEM
                try{
                    //NOMBRE DEL ARCHIVO PLANO 
                    String ruc="20525904424";
                    String tip_doc="03";
                    String serie="B002";
                    String correlativo="00007740";
                    //RUTA DONDE SE GUARDAN LOS ARCHIVOS PLANOS
                    String ruta="src/main/java/archivos_planos/";
                    //NOMBRE ARCHIVO PLANO .CAT
                    String cab=ruc+"-"+tip_doc+"-"+serie+"-"+correlativo;
                    File cabecera=new File(ruta+""+cab+".CAB");
                    //NOMBRE DEL ARCHIVO PLANO .DET
                    String det=ruc+"-"+tip_doc+"-"+serie+"-"+correlativo;
                    File detalle_det=new File(ruta+""+det+".DET");
                    FileWriter escribir=new FileWriter(cabecera,true);
                    escribir.write(tip_ope+"|"+
                                   fecha_emision+"|"+
                                   cod_doc+"|"+
                                   tip_documento+"|"+
                                   numero_doc+"|"+
                                   nombre+"|"+
                                   cod_moneda+"|"+
                                   dec_glo+"|"+
                                   sum_carg+"|"+
                                   tot_desc+"|"+
                                   tot_sin_igv+"|"+
                                   tot_val_ope_inaf+"|"+
                                   tot_val_ope_exo+"|"+
                                   igv+"|"+
                                   isc+"|"+
                                   sum_otros_trib+"|"+
                                   importe_total);
                    //ESCRIBIR EL ARCHIVO PLANO .DET
                    FileWriter escribir_detalle=new FileWriter(detalle_det,true);
                    //Escribimos en el archivo con el metodo write 
                    escribir_detalle.write(cod_item+"|"+
                                           cantidad_unid+"|"+
                                           cod_producto+"|"+
                                           cod_prod_sunat+"|"+
                                           descripcion+"|"+
                                           valor_unit+"|"+
                                           descuento+"|"+
                                           igv_item+"|"+
                                           afet_igv+"|"+
                                           tip_isc_item+"|"+
                                           tip_sist_isc+"|"+
                                           precio_unit+"|"+
                                           valor_venta+"|");
                    escribir_detalle.close();
                } catch(Exception e){
                    System.out.println("Error al escribir");
                }
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
