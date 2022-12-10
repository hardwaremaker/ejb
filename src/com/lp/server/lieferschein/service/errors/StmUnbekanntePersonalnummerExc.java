package com.lp.server.lieferschein.service.errors;

public class StmUnbekanntePersonalnummerExc extends StmException {
	private static final long serialVersionUID = 5673565294388245008L;

	private String personalnummer;
	
	public StmUnbekanntePersonalnummerExc(String personalnummer) {
		super("Unbekannte Personalnummer '" + personalnummer + "'");
		setPersonalnummer(personalnummer);
		setExcCode(StmExceptionCode.PERSONALNUMMER_UNBEKANNT);
	}

	public String getPersonalnummer() {
		return personalnummer;
	}
	
	public void setPersonalnummer(String personalnummer) {
		this.personalnummer = personalnummer;
	}
}
