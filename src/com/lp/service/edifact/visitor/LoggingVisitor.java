package com.lp.service.edifact.visitor;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.parser.BgmSegment;
import com.lp.service.edifact.parser.CntSegment;
import com.lp.service.edifact.parser.ComSegment;
import com.lp.service.edifact.parser.CtaSegment;
import com.lp.service.edifact.parser.CuxSegment;
import com.lp.service.edifact.parser.DocSegment;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.FtxSegment;
import com.lp.service.edifact.parser.GisSegment;
import com.lp.service.edifact.parser.ImdSegment;
import com.lp.service.edifact.parser.LinSegment;
import com.lp.service.edifact.parser.LocSegment;
import com.lp.service.edifact.parser.NadSegment;
import com.lp.service.edifact.parser.PatSegment;
import com.lp.service.edifact.parser.PiaSegment;
import com.lp.service.edifact.parser.PriSegment;
import com.lp.service.edifact.parser.QtySegment;
import com.lp.service.edifact.parser.RffSegment;
import com.lp.service.edifact.parser.SccSegment;
import com.lp.service.edifact.parser.TdtSegment;
import com.lp.service.edifact.parser.TodSegment;
import com.lp.service.edifact.parser.UnbSegment;
import com.lp.service.edifact.parser.UnhSegment;
import com.lp.service.edifact.parser.UnsSegment;
import com.lp.service.edifact.parser.UntSegment;
import com.lp.service.edifact.parser.UnzSegment;

public class LoggingVisitor implements IVisitor {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(LoggingVisitor.class); 
	
	private final ILoggingVisitorFunction loggingFunction;
	
	public LoggingVisitor(ILoggingVisitorFunction loggingFunction) {
		this.loggingFunction = loggingFunction;
	}
	
	@Override
	public void visit(UnbSegment unbSegment) {
		loggingFunction.accept(unbSegment);
//		logSegment(unbSegment);
	}

	@Override
	public void visit(BgmSegment bgmSegment) {
		loggingFunction.accept(bgmSegment);
//		logSegment(bgmSegment);
	}

	@Override
	public void visit(CntSegment cntSegment) {
		loggingFunction.accept(cntSegment);
//		logSegment(cntSegment);
	}

	@Override
	public void visit(ComSegment comSegment) {
		loggingFunction.accept(comSegment);
//		logSegment(comSegment);
	}

	@Override
	public void visit(CtaSegment ctaSegment) {
		loggingFunction.accept(ctaSegment);
//		logSegment(ctaSegment);
	}

	@Override
	public void visit(CuxSegment cuxSegment) {
		loggingFunction.accept(cuxSegment);
//		logSegment(cuxSegment);
	}

	@Override
	public void visit(DocSegment docSegment) {
		loggingFunction.accept(docSegment);
//		logSegment(docSegment);
	}

	@Override
	public void visit(DtmSegment dtmSegment) {
		loggingFunction.accept(dtmSegment);
//		logSegment(dtmSegment);
	}

	@Override
	public void visit(FtxSegment ftxSegment) {
		loggingFunction.accept(ftxSegment);
//		logSegment(ftxSegment);
	}

	@Override
	public void visit(GisSegment gisSegment) {
		loggingFunction.accept(gisSegment);
//		logSegment(gisSegment);
	}

	@Override
	public void visit(ImdSegment imdSegment) {
		loggingFunction.accept(imdSegment);
//		logSegment(imdSegment);
	}

	@Override
	public void visit(LinSegment linSegment) {
		loggingFunction.accept(linSegment);
//		logSegment(linSegment);
	}

	@Override
	public void visit(LocSegment locSegment) {
		loggingFunction.accept(locSegment);
//		logSegment(locSegment);
	}

	@Override
	public void visit(NadSegment nadSegment) {
		loggingFunction.accept(nadSegment);
//		logSegment(nadSegment);
	}

	@Override
	public void visit(PatSegment patSegment) {
		loggingFunction.accept(patSegment);
//		logSegment(patSegment);
	}

	@Override
	public void visit(PiaSegment piaSegment) {
		loggingFunction.accept(piaSegment);
//		logSegment(piaSegment);
	}

	@Override
	public void visit(PriSegment priSegment) {
		loggingFunction.accept(priSegment);		
	}

	@Override
	public void visit(QtySegment qtySegment) {
		loggingFunction.accept(qtySegment);
//		logSegment(qtySegment);
	}

	@Override
	public void visit(RffSegment rffSegment) {
		loggingFunction.accept(rffSegment);
//		logSegment(rffSegment);
	}

	@Override
	public void visit(SccSegment sccSegment) {
		loggingFunction.accept(sccSegment);
//		logSegment(sccSegment);
	}

	@Override
	public void visit(TdtSegment tdtSegment) {
		loggingFunction.accept(tdtSegment);
//		logSegment(tdtSegment);
	}

	@Override
	public void visit(TodSegment todSegment) {
		loggingFunction.accept(todSegment);
	}

	@Override
	public void visit(UnhSegment unhSegment) {
		loggingFunction.accept(unhSegment);
//		logSegment(unhSegment);
	}

	@Override
	public void visit(UnsSegment unsSegment) {
		loggingFunction.accept(unsSegment);
//		logSegment(unsSegment);
	}

	@Override
	public void visit(UntSegment untSegment) {
		loggingFunction.accept(untSegment);
//		logSegment(untSegment);
	}

	@Override
	public void visit(UnzSegment unzSegment) {
		loggingFunction.accept(unzSegment);
//		logSegment(unzSegment);
	}

	protected ILoggingVisitorFunction getLogFunction() {
		return loggingFunction;
	}
	
	protected void logSegment(EdifactSegment<?> segment) {
		loggingFunction.accept(segment);
	}
}
