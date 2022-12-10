package com.lp.server.system.ejbfac;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.server.util.report.JasperPrintLP;

public class HvPrinterDummy implements HvPrinter {
	private final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	public HvPrinterDummy() {
	}

	@Override
	public void print(JasperPrintLP print) {
		myLogger.warn("This printer dummy is not supposed to print!");
	}

	@Override
	public void print(JasperPrintLP[] prints) {
		myLogger.warn("This printer dummy is not supposed to print!");
	}

	@Override
	public boolean exists() {
		return true;
	}
}
