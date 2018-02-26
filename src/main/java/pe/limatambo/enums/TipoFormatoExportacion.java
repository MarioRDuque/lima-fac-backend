package pe.limatambo.enums;

public enum TipoFormatoExportacion {

	PDF("PDF"), EXCEL("XLS"), WORD("RTF"),HTML("HTML"),STREAM("STREAM");

	private String value;

	private TipoFormatoExportacion(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}