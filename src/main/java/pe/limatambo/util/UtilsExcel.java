package pe.limatambo.util;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UtilsExcel {

    private static XSSFCellStyle styleStringCabecera;
    private static XSSFCellStyle styleString;
    private static XSSFCellStyle styleInteger;
    private static XSSFCellStyle styleDecimal;
    private static XSSFCellStyle styleDecimalSD;
    private static XSSFCellStyle styleDecimalSF;
    private static XSSFCellStyle styleFecha;
    private static XSSFCellStyle styleVacio;
    private static DataFormat df;

    @Value("${rpt.urljasper}")
    private String URL_REPORTES;

    public String comprobarNumericoString(String dato) {
        try {
            return "D" + new BigDecimal(dato) + "¬";
        } catch (Exception e) {
            return "S" + dato + "¬";
        }
    }

    public String crearExcel(List<String> listaCabecera, List<String> listaCuerpo, String namesheet) {
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet(namesheet);
            styleStringCabecera = wb.createCellStyle();
            styleString = wb.createCellStyle();
            styleInteger = wb.createCellStyle();
            styleDecimal = wb.createCellStyle();
            styleDecimalSD = wb.createCellStyle();
            styleDecimalSF = wb.createCellStyle();
            styleFecha = wb.createCellStyle();
            styleVacio = wb.createCellStyle();
            XSSFFont fontCabecera = wb.createFont();
            XSSFFont font = wb.createFont();
            XSSFRow row;
            df = wb.createDataFormat();
            for (int i = 0; i < listaCabecera.size() + listaCuerpo.size(); i++) {
                String fila = "";
                if (i < listaCabecera.size()) {
                    fila = (String) listaCabecera.get(i);
                } else {
                    fila = (String) listaCuerpo.get(i - listaCabecera.size());
                }
                StringTokenizer st = new StringTokenizer(fila, "¬");
                sheet.setColumnWidth(0, 25 * 256);
                row = sheet.createRow(i);
                int j = 0;
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (i <= listaCabecera.size()) {
                        styleStringCabecera = wb.createCellStyle();
                        fontCabecera = wb.createFont();
                        fontCabecera.setFontHeightInPoints((short) 8);
                        fontCabecera.setFontName("Arial");
                        fontCabecera.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                        if (i == 0) {
                            fontCabecera.setFontHeightInPoints((short) 12);
                            fontCabecera.setColor(IndexedColors.AUTOMATIC.getIndex());
                        }
                        styleStringCabecera.setFont(fontCabecera);
                        createCell(row, j, token, styleStringCabecera);
                    }
                    if (i == listaCabecera.size()) {
                        styleStringCabecera = wb.createCellStyle();
                        fontCabecera = wb.createFont();
                        fontCabecera.setFontHeightInPoints((short) 10);
                        fontCabecera.setFontName("Arial");
                        fontCabecera.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                        fontCabecera.setColor(IndexedColors.AUTOMATIC.getIndex());
                        styleStringCabecera.setFont(fontCabecera);
                        createCell(row, j, token, styleStringCabecera);
                    }
                    if (i > listaCabecera.size()) {
                        font.setFontHeightInPoints((short) 8);
                        font.setFontName("Arial");
                        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                        font.setColor(IndexedColors.AUTOMATIC.getIndex());
                        switch (token.charAt(0)) {
                            case 'S':
                                styleString.setFont(font);
                                createCell(row, j, token, styleString);
                                break;
                            case 'I':
                                styleInteger.setFont(font);
                                createCell(row, j, token, styleInteger);
                                break;
                            case 'D':
                                styleDecimal.setFont(font);
                                createCell(row, j, token, styleDecimal);
                                break;
                            case 'C':
                                styleDecimalSD.setFont(font);
                                createCell(row, j, token, styleDecimalSD);
                                break;
                            case 'F':
                                styleDecimalSF.setFont(font);
                                createCell(row, j, token, styleDecimalSF);
                                break;
                            case 'T':
                                styleFecha.setFont(font);
                                createCell(row, j, token, styleFecha);
                                break;
                            case 'B':
                                styleVacio.setFont(font);
                                createCell(row, j, token, styleVacio);
                                break;
                            case 'R'://String speciales Resaltados de Rojo
                                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                                font.setColor(IndexedColors.RED.getIndex());
                                styleString.setFont(font);
                                createCell(row, j, token, styleString);
                                break;
                            default:
                                break;
                        }
                    }
                    j = j + 1;
                }
            }
//            for (int i = 1; i < listaCabecera.size() + listaCuerpo.size(); i++) {
//                //sheet.autoSizeColumn((short) i);
//                System.out.println("valor de la segunda i: " + i);
//                sheet.autoSizeColumn(i);
//            }
//            return obtenerRutaDeReporteTemporal(wb);
            String nombreXLS = URL_REPORTES + "" + "excel" + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(nombreXLS);
            wb.write(fileOut);
            fileOut.close();
            return nombreXLS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createCell(XSSFRow row, int i, String value, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(i);
        value = value + " ";
        if ((value.charAt(0) == 'I' || value.charAt(0) == 'D' || value.charAt(0) == 'C' || value.charAt(0) == 'F') && !value.substring(1).equals(" ")) {
            switch (value.charAt(0)) {
                case 'I':
                    style.setDataFormat(df.getFormat("0"));
                    break;
                case 'D':
                    style.setDataFormat(df.getFormat("#,##0.00"));
                    break;
                case 'C':
                    style.setDataFormat(df.getFormat("#,##0"));
                    break;
                case 'F':
                    style.setDataFormat(df.getFormat("0.00"));
                    break;
                default:
                    break;
            }
            cell.setCellValue(Double.parseDouble(value.substring(1).trim()));
        } else {
            switch (value.charAt(0)) {
                case 'B':
                    cell.setCellType(cell.CELL_TYPE_BLANK);
                    break;
                case 'T'://fecha
                    style.setDataFormat(df.getFormat("d/m/yy h.mm;@"));
                    cell.setCellValue(value.substring(1).trim());
                    break;
                default:
                    cell.setCellValue(value.substring(1).trim());
                    break;
            }
        }
        if (value.charAt(0) == 'S') {
            style.setDataFormat(df.getFormat("text"));
            cell.setCellValue(new XSSFRichTextString(value.substring(1).trim()));
        }
        cell.setCellStyle(style);
    }
}
