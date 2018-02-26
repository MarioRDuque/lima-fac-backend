/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.controlador;

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
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.entidades.Detallepedido;
import pe.limatambo.entidades.Pedido;
import pe.limatambo.entidades.Usuario;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.security.TokenUtil;
import pe.limatambo.servicio.PedidoServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.LimatamboUtil;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;
/**
 * @author dev-out-03
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoControlador {
    
    private final Logger loggerControlador = LoggerFactory.getLogger(getClass());
    @Autowired
    private PedidoServicio pedidoServicio;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private GenericoDao<Detallepedido, Integer> detallepedidoDao;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity crear(HttpServletRequest request, @RequestBody Pedido entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Pedido pedidoGuardado =  pedidoServicio.guardar(entidad);
                if (pedidoGuardado != null ) {
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(pedidoGuardado.getId());
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
            Integer idPedido, tipoUsuario = 0;
            Date desde, hasta;
            String dni, nombre, idubigeo, usuario;
            BusquedaPaginada busquedaPaginada = new BusquedaPaginada();
            busquedaPaginada.setBuscar(parametros);
            Pedido entidadBuscar = new Pedido();

            idPedido = busquedaPaginada.obtenerFiltroComoInteger("idPedido");
            tipoUsuario = busquedaPaginada.obtenerFiltroComoInteger("tipoUsuario");
            desde = busquedaPaginada.obtenerFiltroComoDate("desde");
            hasta = busquedaPaginada.obtenerFiltroComoDate("hasta");
            dni = busquedaPaginada.obtenerFiltroComoString("dni");
            nombre = busquedaPaginada.obtenerFiltroComoString("nombre");
            idubigeo = busquedaPaginada.obtenerFiltroComoString("idubigeo");
            usuario = busquedaPaginada.obtenerFiltroComoString("usuario");

            busquedaPaginada.setPaginaActual(pagina);
            busquedaPaginada.setCantidadPorPagina(cantidadPorPagina);
            busquedaPaginada = pedidoServicio.busquedaPaginada(entidadBuscar, busquedaPaginada, idPedido, desde, 
                    hasta, dni, nombre, idubigeo, usuario, tipoUsuario);
            return new ResponseEntity<>(busquedaPaginada, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity actualizar(HttpServletRequest request, @RequestBody Pedido entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Usuario usuario = tokenUtil.parseTokenFromUser(request);
                if(usuario != null){
                    Pedido pedidoGuardado = pedidoServicio.actualizar(entidad, usuario);
                    if (pedidoGuardado != null ) {
                        resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                        resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                        resp.setExtraInfo(pedidoGuardado.getId());
                    }
                    else{
                        throw new GeneralException(Mensaje.ERROR_CRUD, Mensaje.ERROR_CRUD_ACTUALIZAR, loggerControlador);
                    }
                }else{
                    throw new GeneralException(Mensaje.ERROR_CRUD, Mensaje.NO_AUTORIZADO, loggerControlador);
                }
            } catch (Exception e) {
                throw e;
            }
        }else{
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.ERROR.getValor());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @RequestMapping(value = "cerrar", method = RequestMethod.POST)
    public ResponseEntity cerrar(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            int id = LimatamboUtil.obtenerFiltroComoInteger(parametros, "id");
            pedidoServicio.terminarVenta(id);
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
            resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
            resp.setExtraInfo(id);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @RequestMapping(value = "obtenerEntidad", method = RequestMethod.POST)
    public ResponseEntity obtenerEntidad(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Integer id = LimatamboUtil.obtenerFiltroComoInteger(parametros, "id");
            Pedido pedidoBuscado = pedidoServicio.obtener(id);
            if (pedidoBuscado!= null && pedidoBuscado.getId()>0) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(pedidoBuscado);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            else{
                throw new GeneralException(Mensaje.NO_EXISTEN_DATOS, Mensaje.NO_EXISTEN_DATOS, loggerControlador);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    @RequestMapping(value = "eliminardetalle/{id}", method = RequestMethod.GET)
    public ResponseEntity eliminardetalle(HttpServletRequest request, @PathVariable("id") Integer id) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            pedidoServicio.actualizarEstadoDetalle(id);
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
            resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
            resp.setExtraInfo(id);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @RequestMapping(value = "eliminar/{id}", method = RequestMethod.GET)
    public ResponseEntity eliminar(HttpServletRequest request, @PathVariable("id") Integer id) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Pedido pedido = pedidoServicio.obtener(id);
            pedido.setEstado(Boolean.FALSE);
            pedido = pedidoServicio.actualizar(pedido, null);
            if (pedido!= null && pedido.getId()>0) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(pedido);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            else{
                throw new GeneralException(Mensaje.NO_EXISTEN_DATOS, Mensaje.NO_EXISTEN_DATOS, loggerControlador);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
}
