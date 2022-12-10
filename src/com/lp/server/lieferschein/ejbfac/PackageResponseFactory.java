package com.lp.server.lieferschein.ejbfac;

import org.apache.http.HttpResponse;

import com.lp.server.lieferschein.service.PaketVersandAntwortDto;

public class PackageResponseFactory {
	public static PaketVersandAntwortDto httpStatusWrong(HttpResponse response) {
		return new PaketVersandAntwortDto(
				"HV-0000", response.getStatusLine().getStatusCode() +
					": " + response.getStatusLine().getReasonPhrase()) ;
	}
	
	public static PaketVersandAntwortDto shipmentResponseNull() {
		return new PaketVersandAntwortDto(
				"HV-0001", "ImportShipmentResponse hat keinen Wert");
	}
	
	public static PaketVersandAntwortDto shipmentResultNull() {
		return new PaketVersandAntwortDto(
				"HV-0002", "ImportShipmentResult hat keinen Wert");	
	}
	
	public static PaketVersandAntwortDto zusatzfunktionFehlt() {
		return new PaketVersandAntwortDto(
				"HV-0003", "Die Zusatzfunktionsberechtigung POST_PLC_VERSAND fehlt");
	}
}
