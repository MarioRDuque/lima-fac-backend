package pe.limatambo.enums;

public enum ReporteEnum {
	
	PARAMETRO_NOMBRE_REPORTE("nombreReporte"),
	PARAMETRO_FORMATO_EXPORTACION("formatoExportacion");
	
	private String value;

	private ReporteEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
