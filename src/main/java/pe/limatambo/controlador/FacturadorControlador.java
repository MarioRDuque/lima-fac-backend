/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.controlador;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pe.limatambo.entidades.Categoria;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta;

/**
 *
 * @author dev-out-03
 */
@RestController
@RequestMapping("/facturador")
public class FacturadorControlador {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listar(HttpServletRequest request) throws GeneralException, IOException {
        try {
            Runtime.getRuntime().exec("cmd /c start D:\\Jetty\\iniciarSistema.bat");
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
        return null;
    }

}
