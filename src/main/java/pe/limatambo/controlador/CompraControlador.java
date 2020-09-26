/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.controlador;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pe.limatambo.entidades.Compra;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.CompraServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.LimatamboUtil;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;
/**
 * @author dev-out-03
 */
@RestController
@RequestMapping("/compra")
public class CompraControlador {
    
    private final Logger loggerControlador = LoggerFactory.getLogger(getClass());
    @Autowired
    private CompraServicio compraServicio;
    
    @RequestMapping(value="notapedido/{id}/descripcion/{descripcion}", method = RequestMethod.GET)
    public ResponseEntity notapedido(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("descripcion") String descripcion) throws GeneralException, IOException {
        Respuesta resp = new Respuesta();
        try {
            Compra g =  compraServicio.obtener(id);
            if (g != null ) {
                g.setDescripcion(descripcion);
                if(!"07".equals(g.getTipooperacion())){
                    g.setAnulado(g.getTipooperacion());
                    g.setTipooperacion("07");
                    g.setId(null);
                    g = compraServicio.guardar(g);
                }else {
                    g.setEstado(Boolean.TRUE);
                    g=compraServicio.actualizar(g);
                }
                compraServicio.generarDocumentoCabNota(g.getId(), g.getAnulado());
                compraServicio.generarDocumentoDet(g.getId());
                g = compraServicio.obtener(id);
                g.setEstado(false);
                compraServicio.actualizar(g);
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(g.getId());
            }else{
                throw new GeneralException(Mensaje.ERROR_CRUD_GUARDAR, "Guardar retorno nulo", loggerControlador);
            }
        } catch (Exception e) {
            throw e;
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity crear(HttpServletRequest request, @RequestBody Compra entidad) throws GeneralException, IOException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Compra g =  compraServicio.guardar(entidad);
                if (g != null ) {
                    if(!"00".equals(g.getTipooperacion())){
//                        compraServicio.generarDocumentoCab(g.getId());
//                        compraServicio.generarDocumentoDet(g.getId());
                    }
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(g.getId());
                }else{
                    throw new GeneralException(Mensaje.ERROR_CRUD_GUARDAR, "Guardar retorno nulo", loggerControlador);
                }
            } catch (Exception e) {
                throw e;
            }
        }else{
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.ERROR.getValor());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @RequestMapping(value = "pagina/{pagina}/cantidadPorPagina/{cantidadPorPagina}", method = RequestMethod.POST)
    public ResponseEntity<BusquedaPaginada> busquedaPaginada(HttpServletRequest request, @PathVariable("pagina") Long pagina, 
                                                             @PathVariable("cantidadPorPagina") Long cantidadPorPagina, 
                                                             @RequestBody Map<String, Object> parametros){
            Integer idPedido;
            Date desde, hasta;
            String dni, nombre, usuario, seriecorrelativo;
            BusquedaPaginada busquedaPaginada = new BusquedaPaginada();
            busquedaPaginada.setBuscar(parametros);
            Compra entidadBuscar = new Compra();
            idPedido = busquedaPaginada.obtenerFiltroComoInteger("idPedido");
            desde = busquedaPaginada.obtenerFiltroComoDate("desde");
            hasta = busquedaPaginada.obtenerFiltroComoDate("hasta");
            dni = busquedaPaginada.obtenerFiltroComoString("dni");
            nombre = busquedaPaginada.obtenerFiltroComoString("nombre");
            seriecorrelativo = busquedaPaginada.obtenerFiltroComoString("seriecorrelativo");
            usuario = busquedaPaginada.obtenerFiltroComoString("usuario");
            busquedaPaginada.setPaginaActual(pagina);
            busquedaPaginada.setCantidadPorPagina(cantidadPorPagina);
            busquedaPaginada = compraServicio.busquedaPaginada(entidadBuscar, busquedaPaginada, idPedido, desde, 
                    hasta, dni, nombre, usuario, seriecorrelativo);
            return new ResponseEntity<>(busquedaPaginada, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity actualizar(HttpServletRequest request, @RequestBody Compra entidad) throws GeneralException, IOException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Compra a = compraServicio.actualizar(entidad);
                if (a != null ) {
//                    if(!"00".equals(a.getTipooperacion())){
//                        compraServicio.generarDocumentoCab(a.getId());
//                        compraServicio.generarDocumentoDet(a.getId());
//                    }
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(a.getId());
                } else {
                    throw new GeneralException(Mensaje.ERROR_CRUD, Mensaje.ERROR_CRUD_ACTUALIZAR, loggerControlador);
                }
            } catch (Exception e) {
                throw e;
            }
        }else{
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.ERROR.getValor());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @RequestMapping(value = "obtenerEntidad", method = RequestMethod.POST)
    public ResponseEntity obtenerEntidad(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Long id = LimatamboUtil.obtenerFiltroComoLong(parametros, "id");
            Compra pedidoBuscado = compraServicio.obtener(id);
            if (pedidoBuscado!= null && pedidoBuscado.getId()>0) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(pedidoBuscado);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                throw new GeneralException(Mensaje.NO_EXISTEN_DATOS, Mensaje.NO_EXISTEN_DATOS, loggerControlador);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    @RequestMapping(value = "eliminardetalle/{id}", method = RequestMethod.GET)
    public ResponseEntity eliminardetalle(HttpServletRequest request, @PathVariable("id") Long id) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            compraServicio.actualizarEstadoDetalle(id);
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
            resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
            resp.setExtraInfo(id);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @RequestMapping(value = "eliminar/{id}", method = RequestMethod.GET)
    public ResponseEntity eliminar(HttpServletRequest request, @PathVariable("id") Long id) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Compra pedido = compraServicio.obtener(id);
            pedido.setEstado(Boolean.FALSE);
            pedido = compraServicio.actualizar(pedido);
            if (pedido!= null && pedido.getId()>0) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(pedido);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                throw new GeneralException(Mensaje.NO_EXISTEN_DATOS, Mensaje.NO_EXISTEN_DATOS, loggerControlador);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
}
