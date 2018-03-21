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
import pe.limatambo.entidades.Venta;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.VentaServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.LimatamboUtil;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;
/**
 * @author dev-out-03
 */
@RestController
@RequestMapping("/venta")
public class VentaControlador {
    
    private final Logger loggerControlador = LoggerFactory.getLogger(getClass());
    @Autowired
    private VentaServicio ventaServicio;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity crear(HttpServletRequest request, @RequestBody Venta entidad) throws GeneralException, IOException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Venta g =  ventaServicio.guardar(entidad);
                if (g != null ) {
                    ventaServicio.generarDocumentoCab(g.getId());
                    ventaServicio.generarDocumentoDet(g.getId());
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
            String dni, nombre, usuario;
            BusquedaPaginada busquedaPaginada = new BusquedaPaginada();
            busquedaPaginada.setBuscar(parametros);
            Venta entidadBuscar = new Venta();
            idPedido = busquedaPaginada.obtenerFiltroComoInteger("idPedido");
            desde = busquedaPaginada.obtenerFiltroComoDate("desde");
            hasta = busquedaPaginada.obtenerFiltroComoDate("hasta");
            dni = busquedaPaginada.obtenerFiltroComoString("dni");
            nombre = busquedaPaginada.obtenerFiltroComoString("nombre");
            usuario = busquedaPaginada.obtenerFiltroComoString("usuario");
            busquedaPaginada.setPaginaActual(pagina);
            busquedaPaginada.setCantidadPorPagina(cantidadPorPagina);
            busquedaPaginada = ventaServicio.busquedaPaginada(entidadBuscar, busquedaPaginada, idPedido, desde, 
                    hasta, dni, nombre, usuario);
            return new ResponseEntity<>(busquedaPaginada, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity actualizar(HttpServletRequest request, @RequestBody Venta entidad) throws GeneralException, IOException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Venta a = ventaServicio.actualizar(entidad);
                if (a != null ) {
                    ventaServicio.generarDocumentoCab(a.getId());
                    ventaServicio.generarDocumentoDet(a.getId());
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
            Venta pedidoBuscado = ventaServicio.obtener(id);
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
            ventaServicio.actualizarEstadoDetalle(id);
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
            Venta pedido = ventaServicio.obtener(id);
            pedido.setEstado(Boolean.FALSE);
            pedido = ventaServicio.actualizar(pedido);
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
