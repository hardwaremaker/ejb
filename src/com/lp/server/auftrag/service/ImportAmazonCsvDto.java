package com.lp.server.auftrag.service;

import java.io.Serializable;

public class ImportAmazonCsvDto implements Serializable{
	public String amazonBestellnummer=null;
	public String email=null;
	public String artikelnummer=null;
	public String bruttopreis=null;
	public String versandkosten=null;
	public String waehrung=null;
	public String menge=null;
	public String belegdatum=null;
	
	public String recipientName=null;
	
	public String lieferadresseName=null;
	public String lieferadresseAdresse1=null;
	public String lieferadresseAdresse2=null;
	public String lieferadresseLand=null;
	public String lieferadressePLZ=null;
	public String lieferadresseOrt=null;
	
	public String rechnungsadresseName=null;
	public String rechnungsadresseAdresse1=null;
	public String rechnungsadresseAdresse2=null;
	public String rechnungsadresseLand=null;
	public String rechnungsadressePLZ=null;
	public String rechnungsadresseOrt=null;
	
	public String versandrabatt=null;
	public String rabatt=null;
	public String trackingNumber=null;
	public int zeile;
	
}
