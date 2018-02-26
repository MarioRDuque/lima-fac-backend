/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.controlador;

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
import pe.limatambo.entidades.Tipodocumento;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.TipoDocumentoServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.LimatamboUtil;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;
/**
 *
 * @author dev-out-03
 */
@RestController
@RequestMapping("/tipodocumento")
public class TipoDocumentoControlador {
    
    private final Logger loggerControlador = LoggerFactory.getLogger(getClass());
    @Autowired
    private TipoDocumentoServicio tipoDocumentoServicio;
    
    @RequestMapping(value = "pagina/{pagina}/cantidadPorPagina/{cantidadPorPagina}", method = RequestMethod.POST)
    public ResponseEntity<BusquedaPaginada> busquedaPaginada(HttpServletRequest request, @PathVariable("pagina") Long pagina, 
                                                             @PathVariable("cantidadPorPagina") Long cantidadPorPagina, 
                                                             @RequestBody Map<String, Object> parametros) throws GeneralException{
        try {
            String abr;
            BusquedaPaginada busquedaPaginada = new BusquedaPaginada();
            busquedaPaginada.setBuscar(parametros);
            Tipodocumento entidadBuscar = new Tipodocumento();
            abr = busquedaPaginada.obtenerFiltroComoString("abr");
            busquedaPaginada.setPaginaActual(pagina);
            busquedaPaginada.setCantidadPorPagina(cantidadPorPagina);
            busquedaPaginada = tipoDocumentoServicio.busquedaPaginada(entidadBuscar, busquedaPaginada, abr);
            return new ResponseEntity<>(busquedaPaginada, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity crear(HttpServletRequest request, @RequestBody Tipodocumento entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Tipodocumento guardado = tipoDocumentoServicio.insertar(entidad);
                if (guardado != null ) {
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(guardado);
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
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity actualizar(HttpServletRequest request, @RequestBody Tipodocumento entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if(entidad != null){
            try {
                Tipodocumento guardado = tipoDocumentoServicio.actualizar(entidad);
                if (guardado != null ) {
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(guardado);
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
    
    @RequestMapping(value="obtener", method = RequestMethod.POST)
    public ResponseEntity obtener(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException{
        Respuesta resp = new Respuesta();
        try {
            Integer id = LimatamboUtil.obtenerFiltroComoInteger(parametros, "id");
            Tipodocumento unidad = tipoDocumentoServicio.obtener(Tipodocumento.class, id);
            if (unidad!=null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(unidad);
            }else{
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }
    
    @RequestMapping(value="eliminar", method = RequestMethod.POST)
    public ResponseEntity eliminar(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException{
        Respuesta resp = new Respuesta();
        try {
            Integer id = LimatamboUtil.obtenerFiltroComoInteger(parametros, "id");
            Tipodocumento unidad = tipoDocumentoServicio.obtener(Tipodocumento.class, id);
            unidad.setEstado(Boolean.FALSE);
            unidad = tipoDocumentoServicio.actualizar(unidad);
            if (unidad.getId()!=null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(unidad);
            }else{
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }
    
}
