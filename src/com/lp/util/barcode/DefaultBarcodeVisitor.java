package com.lp.util.barcode;

import java.util.List;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class DefaultBarcodeVisitor implements BarcodeVisitor {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(DefaultBarcodeVisitor.class); 

	public DefaultBarcodeVisitor() {
	}

	@Override
	public void visitLosKombiAg(List<BarcodeLosKombiAg> losKombiAg) {
		log.info("visit LosKombiAg ");		
	}

	@Override
	public void visitLosKombiTaetigkeit(List<BarcodeLosKombiTaetigkeit> losKombiTaetigkeit) {
		log.info("visit LosKombiTaetigkeit ");		
	}

	@Override
	public void visitAuftragKombiTaetigkeit(List<BarcodeAuftragKombiTaetigkeit> auftragKombiTaetigkeit) {
		log.info("visit AuftragKombiTaetigkeit ");		
	}

	@Override
	public void visitMaschineStopp(List<BarcodeMaschineStopp> maschineStopp) {
		log.info("visit MaschineStopp ");		
	}

	@Override
	public void visitLosTaetigkeit(List<BarcodeLosTaetigkeit> losTaetigkeit) {
		log.info("visit LosTaetigkeit ");		
	}
}
