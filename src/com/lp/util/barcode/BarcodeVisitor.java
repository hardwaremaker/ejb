package com.lp.util.barcode;

import java.util.List;

public interface BarcodeVisitor {

	void visitLosKombiAg(List<BarcodeLosKombiAg> losKombiAg);
	
	void visitLosKombiTaetigkeit(List<BarcodeLosKombiTaetigkeit> losKombiTaetigkeit);
	
	void visitAuftragKombiTaetigkeit(List<BarcodeAuftragKombiTaetigkeit> auftragKombiTaetigkeit);
	
	void visitMaschineStopp(List<BarcodeMaschineStopp> maschineStopp);
	
	void visitLosTaetigkeit(List<BarcodeLosTaetigkeit> losTaetigkeit);
}
