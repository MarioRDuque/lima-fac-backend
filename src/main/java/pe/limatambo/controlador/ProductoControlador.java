/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.controlador;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pe.limatambo.dao.GenericoDao;
import pe.limatambo.entidades.Producto;
import pe.limatambo.entidades.Productomedida;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.ProductoServicio;
import pe.limatambo.servicio.ReporteServicio;
import pe.limatambo.util.BusquedaPaginada;
import pe.limatambo.util.LimatamboUtil;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;
import pe.limatambo.util.UtilsJSON;

/**
 *
 * @author dev-out-03
 */
@RestController
@RequestMapping("/producto")
public class ProductoControlador {

    private final Logger loggerControlador = LoggerFactory.getLogger(getClass());
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private ReporteServicio reporteServicio;
    @Autowired
    private GenericoDao<Productomedida, Integer> productomedidaDao;

    @RequestMapping(value = "pagina/{pagina}/cantidadPorPagina/{cantidadPorPagina}", method = RequestMethod.POST)
    public ResponseEntity<BusquedaPaginada> busquedaPaginada(HttpServletRequest request, @PathVariable("pagina") Long pagina,
            @PathVariable("cantidadPorPagina") Long cantidadPorPagina,
            @RequestBody Map<String, Object> parametros) throws GeneralException {
        try {
            String codProducto;
            int idCategoria;
            BusquedaPaginada busquedaPaginada = new BusquedaPaginada();
            busquedaPaginada.setBuscar(parametros);
            Producto entidadBuscar = new Producto();
            codProducto = busquedaPaginada.obtenerFiltroComoString("despro");
            idCategoria = busquedaPaginada.obtenerFiltroComoInteger("idcat");
            busquedaPaginada.setPaginaActual(pagina);
            busquedaPaginada.setCantidadPorPagina(cantidadPorPagina);
            busquedaPaginada = productoServicio.busquedaPaginada(entidadBuscar, busquedaPaginada, codProducto, idCategoria);
            return new ResponseEntity<>(busquedaPaginada, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity crear(HttpServletRequest request, @RequestBody Producto entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if (entidad != null) {
            try {
                Producto guardado = productoServicio.insertar(entidad);
                if (guardado != null) {
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(guardado);
                } else {
                    throw new GeneralException(Mensaje.ERROR_CRUD_GUARDAR, "Guardar retorno nulo", loggerControlador);
                }

            } catch (Exception e) {
                throw e;
            }
        } else {
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.ERROR.getValor());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity actualizar(HttpServletRequest request, @RequestBody Producto entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if (entidad != null) {
            try {
                Producto guardado = productoServicio.actualizar(entidad);
                if (guardado != null) {
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(guardado);
                } else {
                    throw new GeneralException(Mensaje.ERROR_CRUD_GUARDAR, "Guardar retorno nulo", loggerControlador);
                }

            } catch (Exception e) {
                throw e;
            }
        } else {
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.ERROR.getValor());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "obtener", method = RequestMethod.POST)
    public ResponseEntity obtener(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Integer id = LimatamboUtil.obtenerFiltroComoInteger(parametros, "id");
            Producto producto = productoServicio.obtener(id);
            if (producto != null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(producto);
            } else {
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "eliminar", method = RequestMethod.POST)
    public ResponseEntity eliminar(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Integer id = LimatamboUtil.obtenerFiltroComoInteger(parametros, "id");
            Producto producto = productoServicio.obtener(id);
            producto.setEstado(Boolean.FALSE);
            producto = productoServicio.actualizar(producto);
            if (producto.getId() != null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(producto);
            } else {
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "eliminarmedida", method = RequestMethod.POST)
    public ResponseEntity eliminarmedida(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Integer id = LimatamboUtil.obtenerFiltroComoInteger(parametros, "id");
            Productomedida pm = productomedidaDao.obtener(Productomedida.class, id);
            pm.setEstado(Boolean.FALSE);
            pm = productomedidaDao.actualizar(pm);
            if (pm.getId() != null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(pm);
            } else {
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }

    @RequestMapping("/exportarReporteProducto")
    public @ResponseBody
    String exportarReporteProducto(HttpServletResponse response, @RequestBody String json) throws Exception {
        Map<String, Object> map = UtilsJSON.jsonToMap(json);
        Map<String, Object> respuesta;
        List<Producto> productos = UtilsJSON.jsonToList(Producto.class, map.get("listado"));
        String usuario = UtilsJSON.jsonToObjeto(String.class, map.get("usuario"));
        try {
            respuesta = productoServicio.exportarReporteProducto(productos, usuario);
            List<String> listaCabecera = UtilsJSON.jsonToList(String.class, respuesta.get("listaCabecera"));
            List<String> listaCuerpo = UtilsJSON.jsonToList(String.class, respuesta.get("listaCuerpo"));
            reporteServicio.generarReporteXLSAngular(listaCabecera, listaCuerpo, response);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
        return null;
    }

}
