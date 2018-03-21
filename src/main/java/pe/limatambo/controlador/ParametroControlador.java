/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.controlador;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.limatambo.entidades.Parametro;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.ParametroServicio;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;
/**
 *
 * @author dev-out-03
 */
@RestController
@RequestMapping("/parametro")
public class ParametroControlador {
    
    private final Logger loggerControlador = LoggerFactory.getLogger(getClass());
    @Autowired
    private ParametroServicio parametroServicio;
    
    @GetMapping("igv")
    public ResponseEntity listar(HttpServletRequest request) throws GeneralException{
        Respuesta resp = new Respuesta();
        try {
            List<Parametro> m = parametroServicio.listar();
            if (!m.isEmpty()) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(m);
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
