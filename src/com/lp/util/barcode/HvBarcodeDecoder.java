package com.lp.util.barcode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.lp.util.Helper;

public class HvBarcodeDecoder implements Serializable {
	private static final long serialVersionUID = 8765405211104655583L;

	private BarcodeVisitor barcodeVisitor;
	private Integer laengeBelegnummer;
	private Integer laengeBelegnummerAuftragsbezogen;
	
	public HvBarcodeDecoder(Integer gesamtlaengeBelegnummer) {
		this(gesamtlaengeBelegnummer, new Integer(0));
	}
	
	private void initBelegnummernlaengen(Integer gesamtlaengeBelegnummer, Integer paramLosnummerAuftragsbezogen) {
		laengeBelegnummer = gesamtlaengeBelegnummer;
		laengeBelegnummerAuftragsbezogen = gesamtlaengeBelegnummer;
		
		if ((laengeBelegnummer - 5) > 0) {
			if (paramLosnummerAuftragsbezogen == 1
					|| paramLosnummerAuftragsbezogen == 2) {
				laengeBelegnummerAuftragsbezogen += 2;
			}
		} else {
			if (paramLosnummerAuftragsbezogen == 1) {
				laengeBelegnummerAuftragsbezogen += 4;
			} else if (paramLosnummerAuftragsbezogen == 2) {
				laengeBelegnummerAuftragsbezogen += 5;
			}
		}
	}

	public HvBarcodeDecoder(Integer gesamtlaengeBelegnummer, Integer paramLosnummerAuftragsbezogen) {
		initBelegnummernlaengen(gesamtlaengeBelegnummer, paramLosnummerAuftragsbezogen);
	}

	public void acceptBarcodeVisitor(BarcodeVisitor barcodeVisitor) {
		this.barcodeVisitor = barcodeVisitor;
	}
	
	private BarcodeVisitor getBarcodeVisitor() {
		if (barcodeVisitor == null) {
			barcodeVisitor = new DefaultBarcodeVisitor();
		}
		return barcodeVisitor;
	}
	
	public void decode(String barcode) throws BarcodeException {
		if (Helper.isStringEmpty(barcode)) {
			throw new UnknownBarcodeException(barcode);
		}
		
		decodeImpl(barcode);
	}
	
	private void decodeImpl(String barcode) throws BarcodeException {
		if (barcode.startsWith(HvBarcodeTyp.LosKombiAg.getText())) {
			decodeLosKombiAg(barcode);
			
		} else if (barcode.startsWith(HvBarcodeTyp.LosKombiTaetigkeit.getText())) {
			decodeLosKombiTaetigkeit(barcode);
		
		} else if (barcode.startsWith(HvBarcodeTyp.AuftragKombiTaetigkeit.getText())) {
			decodeAuftragKombiTaetigkeit(barcode);
		
		} else if (barcode.startsWith(HvBarcodeTyp.Los.getText())) {
			decodeLosTaetigkeit(barcode);
			
		} else {
			throw new UnknownBarcodeException(barcode);
		}

	}

	private void decodeLosTaetigkeit(String barcode) throws BarcodeException {
		BarcodeLosTaetigkeit losTaetigkeit = createLosTaetigkeit(barcode);
		visit(losTaetigkeit);
	}

	private void visit(BarcodeLosTaetigkeit losTaetigkeit) {
		List<BarcodeLosTaetigkeit> barcodes = new ArrayList<BarcodeLosTaetigkeit>();
		barcodes.add(losTaetigkeit);
		getBarcodeVisitor().visitLosTaetigkeit(barcodes);
	}

	private BarcodeLosTaetigkeit createLosTaetigkeit(String barcode) throws BarcodeException {
		Integer cnrLaenge = getLosCnrLaenge(barcode);
		
		PartLeadIn partLeadIn = new PartLeadIn(barcode, HvBarcodeTyp.Los);
		PartCnr partCnr = new PartCnr(partLeadIn, cnrLaenge);
		PartTaetigkeit partTaetigkeit = new PartTaetigkeit(partCnr);
		
		BarcodeLosTaetigkeit losTaetigkeit = new BarcodeLosTaetigkeit(barcode);
		losTaetigkeit.setLosCnr(partCnr.read());
		losTaetigkeit.setTaetigkeitCnr(partTaetigkeit.read());
		
		return losTaetigkeit;
	}

	private void decodeLosKombiAg(String barcode) throws BarcodeException {
		if (barcode.endsWith(HvBarcodeTyp.MaschineStopp.getText())) {
			BarcodeMaschineStopp maschineStoppData = createBarcodeMaschineStopp(barcode);
			visit(maschineStoppData);
		} else {
			BarcodeLosKombiAg losKombiAg = createBarcodeLosKombiAg(barcode);
			visit(losKombiAg);
		}
	}
	
	public Integer getCnrLaenge() {
		return laengeBelegnummer;
	}
	
	public Integer getCnrLaengeAuftragsbezogen() {
		return laengeBelegnummerAuftragsbezogen;
	}
	
	private Integer getLosCnrLaenge(String barcode) {
		return barcode.contains("-") ? laengeBelegnummerAuftragsbezogen : laengeBelegnummer;
	}
	
	private BarcodeLosKombiAg createBarcodeLosKombiAg(String barcode) throws BarcodeException {
		Integer cnrLaenge = getLosCnrLaenge(barcode);
		
		PartLeadIn partLeadIn = new PartLeadIn(barcode, HvBarcodeTyp.LosKombiAg);
		PartCnr partCnr = new PartCnr(partLeadIn, cnrLaenge);
		PartMaschine partMaschine = new PartMaschine(partCnr);
		PartArbeitsgang partAg = new PartArbeitsgang(partMaschine);
		PartUnterarbeitsgang partUnterAg = new PartUnterarbeitsgang(partAg);
		
		BarcodeLosKombiAg losKombiAg = new BarcodeLosKombiAg(barcode);
		losKombiAg.setLosCnr(partCnr.read());
		losKombiAg.setMaschineCnr(partMaschine.read());
		losKombiAg.setArbeitsgangNr(partAg.read());
		losKombiAg.setUnterarbeitsgangNr(partUnterAg.read());

		return losKombiAg;
	}

	private void visit(BarcodeLosKombiAg barcodeData) {
		List<BarcodeLosKombiAg> barcodes = new ArrayList<BarcodeLosKombiAg>();
		barcodes.add(barcodeData);
		getBarcodeVisitor().visitLosKombiAg(barcodes);
	}
	
	private void decodeLosKombiTaetigkeit(String barcode) throws BarcodeException {
		BarcodeLosKombiTaetigkeit losKombiTaetigkeit = createLosKombiTaetigkeit(barcode);
		visit(losKombiTaetigkeit);
	}

	private BarcodeLosKombiTaetigkeit createLosKombiTaetigkeit(String barcode) throws BarcodeException {
		Integer cnrLaenge = getLosCnrLaenge(barcode);
		
		PartLeadIn partLeadIn = new PartLeadIn(barcode, HvBarcodeTyp.LosKombiTaetigkeit);
		PartCnr partCnr = new PartCnr(partLeadIn, cnrLaenge);
		PartMaschine partMaschine = new PartMaschine(partCnr);
		PartTaetigkeit partTaetigkeit = new PartTaetigkeit(partMaschine);
		
		BarcodeLosKombiTaetigkeit losKombiTaetigkeit = new BarcodeLosKombiTaetigkeit(barcode);
		losKombiTaetigkeit.setLosCnr(partCnr.read());
		losKombiTaetigkeit.setMaschineCnr(partMaschine.read());
		losKombiTaetigkeit.setTaetigkeitCnr(partTaetigkeit.read());
		
		return losKombiTaetigkeit;
	}

	private void visit(BarcodeLosKombiTaetigkeit barcodeData) {
		List<BarcodeLosKombiTaetigkeit> barcodes = new ArrayList<BarcodeLosKombiTaetigkeit>();
		barcodes.add(barcodeData);
		getBarcodeVisitor().visitLosKombiTaetigkeit(barcodes);
	}
	
	private void decodeAuftragKombiTaetigkeit(String barcode) throws BarcodeException {
		BarcodeAuftragKombiTaetigkeit auftragKombiTaetigkeit = createAuftragKombiTaetigkeit(barcode);
		visit(auftragKombiTaetigkeit);
	}

	private BarcodeAuftragKombiTaetigkeit createAuftragKombiTaetigkeit(String barcode) throws BarcodeException {
		Integer cnrLaenge = getCnrLaenge();
		
		PartLeadIn partLeadIn = new PartLeadIn(barcode, HvBarcodeTyp.AuftragKombiTaetigkeit);
		PartCnr partCnr = new PartCnr(partLeadIn, cnrLaenge);
		PartTaetigkeit partTaetigkeit = new PartTaetigkeit(partCnr);
		
		BarcodeAuftragKombiTaetigkeit auftragKombiTaetigkeit = new BarcodeAuftragKombiTaetigkeit(barcode);
		auftragKombiTaetigkeit.setAuftragCnr(partCnr.read());
		auftragKombiTaetigkeit.setTaetigkeitCnr(partTaetigkeit.read());
		
		return auftragKombiTaetigkeit;
	}

	private void visit(BarcodeAuftragKombiTaetigkeit barcodeData) {
		List<BarcodeAuftragKombiTaetigkeit> barcodes = new ArrayList<BarcodeAuftragKombiTaetigkeit>();
		barcodes.add(barcodeData);
		getBarcodeVisitor().visitAuftragKombiTaetigkeit(barcodes);
	}

	private BarcodeMaschineStopp createBarcodeMaschineStopp(String barcode) throws BarcodeException {
		BarcodeLosKombiAg losKombiAgData = createBarcodeLosKombiAg(barcode);
		BarcodeMaschineStopp maschineStoppData = new BarcodeMaschineStopp(barcode);
		maschineStoppData.setLosCnr(losKombiAgData.getLosCnr());
		maschineStoppData.setMaschineCnr(losKombiAgData.getMaschineCnr());
		maschineStoppData.setArbeitsgangNr(losKombiAgData.getArbeitsgangNr());
		maschineStoppData.setUnterarbeitsgangNr(losKombiAgData.getUnterarbeitsgangNr());
		
		return maschineStoppData;
	}

	private void visit(BarcodeMaschineStopp barcodeData) {
		List<BarcodeMaschineStopp> barcodes = new ArrayList<BarcodeMaschineStopp>();
		barcodes.add(barcodeData);
		getBarcodeVisitor().visitMaschineStopp(barcodes);
	}
}
