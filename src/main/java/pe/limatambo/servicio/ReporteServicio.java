/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.limatambo.servicio;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dev-out-03
 */
public interface ReporteServicio {

    String rptDescargar(Map parametros, HttpServletResponse response) throws Exception;

    public void generarReporteXLSAngular(List<String> listaCabecera, List<String> listaCuerpo, HttpServletResponse response) throws Exception;
}
