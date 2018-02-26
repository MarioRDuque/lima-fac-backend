/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.controlador;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.ReporteServicio;
import pe.limatambo.util.LimatamboUtil;
/**
 *
 * @author dev-out-03
 */
@RestController
@RequestMapping("/reporte")
public class ReporteCtrl {
   
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ReporteServicio reporteServicio;
    
    @RequestMapping(value = "/generar",  method = RequestMethod.POST)
    public @ResponseBody String getReporte(@RequestBody Map<String, Object> parameter, HttpServletResponse response) throws ParseException, GeneralException, Exception {   
        Date inicio = LimatamboUtil.obtenerFiltroComoDate(parameter, "inicio");
        Date fin = LimatamboUtil.obtenerFiltroComoDate(parameter, "fin");
        String  report = LimatamboUtil.obtenerFiltroComoString(parameter, "report");
        String codusu = LimatamboUtil.obtenerFiltroComoString(parameter, "codusu");
        Integer idPedido = LimatamboUtil.obtenerFiltroComoInteger(parameter, "idPedido");
        try {
            if(codusu.toUpperCase() != null){
                Map parametros = new HashMap();
                parametros.put("report", report);
                parametros.put("inicio", inicio);
                parametros.put("fin", fin);
                parametros.put("id", idPedido);
                parametros.put("p_usuari", codusu.toUpperCase());
                return reporteServicio.rptDescargar(parametros,response);   
            }else{
                return null;
            } 
        } catch (Exception exception) {
            throw exception;
        }
    }
}
