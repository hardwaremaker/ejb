package com.lp.service.edifact.visitor;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
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

public class Grp2Visitor implements IVisitor {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(Grp2Visitor.class); 

	@Override
	public void visit(UnbSegment unbSegment) {
	}
	
	@Override
	public void visit(BgmSegment bgmSegment) {
	}

	@Override
	public void visit(ComSegment comSegment) {
	}

	@Override
	public void visit(CtaSegment ctaSegment) {
	}

	@Override
	public void visit(CuxSegment cuxSegment) {
	}

	@Override
	public void visit(DtmSegment dtmSegment) {
	}

	@Override
	public void visit(FtxSegment ftxSegment) {
	}

	@Override
	public void visit(GisSegment gisSegment) {
	}

	@Override
	public void visit(ImdSegment imdSegment) {
	}

	@Override
	public void visit(LinSegment linSegment) {
	}

	@Override
	public void visit(LocSegment locSegment) {
		log.info("visit loc " + locSegment.get().getFunctionCode());
	}

	@Override
	public void visit(NadSegment nadSegment) {
		log.info("visit nad " + nadSegment.get().getPartyIdentifier());
	}

	@Override
	public void visit(PatSegment patSegment) {
	}

	@Override
	public void visit(PiaSegment piaSegment) {
	}

	@Override
	public void visit(PriSegment priSegment) {
	}

	@Override
	public void visit(QtySegment qtySegment) {
	}

	@Override
	public void visit(RffSegment rffSegment) {
	}

	@Override
	public void visit(SccSegment sccSegment) {
	}

	@Override
	public void visit(TodSegment todSegment) {
	}

	@Override
	public void visit(UnhSegment unhSegment) {
	}

	@Override
	public void visit(UnsSegment unsSegment) {
	}

	@Override
	public void visit(UntSegment untSegment) {
	}

	@Override
	public void visit(UnzSegment unzSegment) {
	}
	
	@Override
	public void visit(CntSegment cntSegment) {
	}
	
	@Override
	public void visit(DocSegment docSegment) {
	}

	@Override
	public void visit(TdtSegment tdtSegment) {
	}
}
