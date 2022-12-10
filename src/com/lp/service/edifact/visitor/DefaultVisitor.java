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

public class DefaultVisitor implements IVisitor {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(DefaultVisitor.class); 

	public DefaultVisitor() {
	}
	
	@Override
	public void visit(UnbSegment unbSegment) {
		log.info("visit unb ");		
	}
	
	@Override
	public void visit(BgmSegment bgmSegment) {
		log.info("visit bgm ");
	}
	
	@Override
	public void visit(ComSegment comSegment) {
		log.info("visit com ");		
	}
	
	@Override
	public void visit(CtaSegment ctaSegment) {
		log.info("visit cta ");		
	}

	@Override
	public void visit(CuxSegment ctaSegment) {
		log.info("visit cux ");		
	}

	@Override
	public void visit(DtmSegment dtmSegment) {
		log.info("visit dtm ");		
	}
	
	@Override
	public void visit(FtxSegment ftxSegment) {
		log.info("visit ftx ");				
	}
	
	@Override
	public void visit(GisSegment gisSegment) {
		log.info("visit gis ");		
	}
	
	@Override
	public void visit(ImdSegment imdSegment) {
		log.info("visit imd ");
	}
	
	@Override
	public void visit(LinSegment linSegment) {
		log.info("visit lin ");		
	}
	
	@Override
	public void visit(LocSegment locSegment) {
		log.info("visit loc ");		
	}
	
	@Override
	public void visit(NadSegment nadSegment) {
		log.info("visit nad " + nadSegment.get().getPartyFunctionCode() + ":" + nadSegment.get().getPartyIdentifier());				
	}
	
	@Override
	public void visit(PatSegment patSegment) {
		log.info("visit pat ");		
	}

	@Override
	public void visit(PiaSegment piaSegment) {
		log.info("visit pia ");				
	}

	@Override
	public void visit(PriSegment priSegment) {
		log.info("visit pri");		
	}
	
	@Override
	public void visit(QtySegment qtySegment) {
		log.info("visit qty " + qtySegment.get().getQuantity() + ".");
	}
	
	@Override
	public void visit(RffSegment rffSegment) {
		log.info("visit rff ");		
	}
	
	@Override
	public void visit(SccSegment sccSegment) {
		log.info("visit scc ");		
	}

	@Override
	public void visit(TodSegment todSegment) {
		log.info("visit tod ");		
	}
	
	@Override
	public void visit(UnhSegment unhSegment) {
		log.info("visit unh ");
	}
	
	@Override
	public void visit(UnsSegment unsSegment) {
		log.info("visit uns ");
	}
	
	@Override
	public void visit(UntSegment untSegment) {
		log.info("visit unt ");
	}
	
	@Override
	public void visit(UnzSegment unzSegment) {
		log.info("visit unz ");
	}
	@Override
	public void visit(CntSegment cntSegment) {
		log.info("visit cnt ");		
	}
	
	@Override
	public void visit(DocSegment docSegment) {
		log.info("visit doc ");		
	}

	@Override
	public void visit(TdtSegment tdtSegment) {
		log.info("visit tdt ");		
	}
}

