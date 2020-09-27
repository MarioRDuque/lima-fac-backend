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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pe.limatambo.entidades.Compra;
import pe.limatambo.entidades.Venta;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.CompraServicio;
import pe.limatambo.servicio.ReporteServicio;
import pe.limatambo.servicio.VentaServicio;
import pe.limatambo.util.LimatamboUtil;

/**
 *
 * @author dev-out-03
 */
@RestController
@RequestMapping("/reporte")
public class ReporteCtrl {

    @Autowired
    private ReporteServicio reporteServicio;
    @Autowired
    private VentaServicio ventaServicio;
    @Autowired
    private CompraServicio compraServicio;

    @RequestMapping(value = "/generar", method = RequestMethod.POST)
    public @ResponseBody
    String getReporte(@RequestBody Map<String, Object> parameter, HttpServletResponse response) throws ParseException, GeneralException, Exception {
        Date inicio = LimatamboUtil.obtenerFiltroComoDate(parameter, "inicio");
        Date fin = LimatamboUtil.obtenerFiltroComoDate(parameter, "fin");
        String report = LimatamboUtil.obtenerFiltroComoString(parameter, "report");
        String codusu = LimatamboUtil.obtenerFiltroComoString(parameter, "codusu");
        Integer idPedido = LimatamboUtil.obtenerFiltroComoInteger(parameter, "idPedido");
        try {
            if (codusu.toUpperCase() != null) {
                Map parametros = new HashMap();
                parametros.put("report", report);
                parametros.put("inicio", inicio);
                parametros.put("fin", fin);
                parametros.put("id", idPedido);
                parametros.put("p_usuari", codusu.toUpperCase());
                return reporteServicio.rptDescargar(parametros, response);
            } else {
                return null;
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

    @RequestMapping(value = "/generarsunat", method = RequestMethod.POST)
    public @ResponseBody
    String generarsunat(@RequestBody Map<String, Object> parameter, HttpServletResponse response) throws ParseException, GeneralException, Exception {
        String report = LimatamboUtil.obtenerFiltroComoString(parameter, "report");
        String codusu = LimatamboUtil.obtenerFiltroComoString(parameter, "codusu");
        Long idVenta = LimatamboUtil.obtenerFiltroComoLong(parameter, "idVenta");
        Venta g = ventaServicio.obtener(idVenta);
        try {
            if (codusu.toUpperCase() != null && g != null) {
                String nombredoc = "";
                String correlativo = LimatamboUtil.completarNumeroConCeros(8, g.getCorrelativo());
                switch (g.getTipooperacion()) {
                    case "03":
                        nombredoc = "BOLETA DE VENTA";
                        break;
                    case "01":
                        nombredoc = "FACTURA DE VENTA";
                        break;
                    case "07":
                        nombredoc = "NOTA DE CREDITO";
                        break;
                    case "00":
                        nombredoc = "NOTA DE PEDIDO";
                        break;
                }
                Map parametros = new HashMap();
                parametros.put("report", report);
                parametros.put("nombredoc", nombredoc);
                parametros.put("correlativo", correlativo);
                parametros.put("id", idVenta);
                parametros.put("p_usuari", codusu.toUpperCase());
                return reporteServicio.rptDescargar(parametros, response);
            } else {
                return null;
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

    @RequestMapping(value = "/geenerarComprobanteCompra", method = RequestMethod.POST)
    public @ResponseBody
    String geenerarComprobanteCompra(@RequestBody Map<String, Object> parameter, HttpServletResponse response) throws ParseException, GeneralException, Exception {
        String report = LimatamboUtil.obtenerFiltroComoString(parameter, "report");
        String codusu = LimatamboUtil.obtenerFiltroComoString(parameter, "codusu");
        Long idCompra = LimatamboUtil.obtenerFiltroComoLong(parameter, "idCompra");
        Compra g = compraServicio.obtener(idCompra);
        try {
            if (codusu.toUpperCase() != null && g != null) {
                String nombredoc = "";
                String correlativo = LimatamboUtil.completarNumeroConCeros(8, g.getCorrelativo());
                switch (g.getTipooperacion()) {
                    case "03":
                        nombredoc = "BOLETA DE COMPRA";
                        break;
                    case "01":
                        nombredoc = "FACTURA DE COMPRA";
                        break;
                    case "07":
                        nombredoc = "NOTA DE CREDITO";
                        break;
                    case "00":
                        nombredoc = "NOTA DE PEDIDO";
                        break;
                }
                Map parametros = new HashMap();
                parametros.put("report", report);
                parametros.put("nombredoc", nombredoc);
                parametros.put("correlativo", correlativo);
                parametros.put("id", idCompra);
                parametros.put("p_usuari", codusu.toUpperCase());
                return reporteServicio.rptDescargar(parametros, response);
            } else {
                return null;
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

}
