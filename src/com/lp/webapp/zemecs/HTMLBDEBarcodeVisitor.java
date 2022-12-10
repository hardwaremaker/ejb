package com.lp.webapp.zemecs;

import java.util.List;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.barcode.BarcodeAuftragKombiTaetigkeit;
import com.lp.util.barcode.BarcodeLosKombiAg;
import com.lp.util.barcode.BarcodeMaschineStopp;
import com.lp.util.barcode.BarcodeLosKombiTaetigkeit;
import com.lp.util.barcode.BarcodeLosTaetigkeit;
import com.lp.util.barcode.BarcodeVisitor;

public class HTMLBDEBarcodeVisitor implements BarcodeVisitor {

	private List<BarcodeLosKombiAg> losKombiAg = null;
	public List<BarcodeLosKombiAg> getLosKombiAg() {
		return losKombiAg;
	}

	public List<BarcodeLosKombiTaetigkeit> getLosKombiTaetigkeit() {
		return losKombiTaetigkeit;
	}

	private List<BarcodeLosKombiTaetigkeit> losKombiTaetigkeit = null;

	public HTMLBDEBarcodeVisitor() {
	}

	
	private List<BarcodeMaschineStopp> maschineStopp = null;
	
	public List<BarcodeMaschineStopp> getMaschineStopp() {
		return maschineStopp;
	}

	@Override
	public void visitLosKombiAg(List<BarcodeLosKombiAg> losKombiAg) {
		this.losKombiAg = losKombiAg;
	}

	@Override
	public void visitLosKombiTaetigkeit(List<BarcodeLosKombiTaetigkeit> losKombiTaetigkeit) {
		this.losKombiTaetigkeit = losKombiTaetigkeit;
	}

	@Override
	public void visitAuftragKombiTaetigkeit(List<BarcodeAuftragKombiTaetigkeit> auftragKombiTaetigkeit) {
		// Kann HTML-BDE nicht
	}

	@Override
	public void visitMaschineStopp(List<BarcodeMaschineStopp> maschineStopp) {
		this.maschineStopp = maschineStopp;
	}

	@Override
	public void visitLosTaetigkeit(List<BarcodeLosTaetigkeit> losTaetigkeit) {
		// Kann HTML-BDE nicht
	}
}
