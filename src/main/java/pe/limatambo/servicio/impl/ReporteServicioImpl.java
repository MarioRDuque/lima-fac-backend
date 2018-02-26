package pe.limatambo.servicio.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.limatambo.servicio.ReporteServicio;

/**
 *
 * @author dev-out-03
 */
@Service
public class ReporteServicioImpl implements ReporteServicio{
    
    @Autowired
    protected DataSource dataSource;
    
    @Value("${rpt.urljasper}")
    private String URL_REPORTES;
    
    @Override
    public String rptDescargar(Map parametros, HttpServletResponse response) throws Exception {
        Connection connection = null;
        Date fecha = new Date();
        try {
            parametros.put("SUBREPORTE_DIR", directorioReportes());
            String reporte = (String) parametros.get("report") + ".jrxml";
            String nombre_archivo_salida = "rpt"+fecha.getTime();
            connection = dataSource.getConnection();
            ServletOutputStream out = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment; filename=" + nombre_archivo_salida + ".pdf;");
            response.setHeader("Cache-Control", "max-age=30");
            response.setContentType("application/pdf");
            response.setHeader("Pragma", "No-cache");
            response.setDateHeader("Expires", 0);
            JasperReport jasperReport = JasperCompileManager.compileReport(directorioReportes() + reporte);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.exportReport();
            out.flush();
            out.close();
        } catch (SQLException | IOException | JRException e) {
            if(connection != null){
                connection.close();
            }
            throw e;
        } catch (Exception e) {
            if(connection != null){
                connection.close();
            }
            throw e;
        } 
        return null;
    }
    
    public String directorioReportes() {
        return URL_REPORTES;
    }
}
