package com.lp.service.edifact.visitor;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.DelforPosition;
import com.lp.service.edifact.IDelforRepository;
import com.lp.service.edifact.IEjbService;
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
import com.lp.service.edifact.schema.DtmInfo;
import com.lp.service.edifact.schema.QtyInfo;
import com.lp.service.edifact.schema.SccInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

public class Grp11VisitorHV implements IVisitor {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(Grp11VisitorHV.class); 

	private IEjbService service;
	private IDelforRepository repository;
	
	private DelforPosition lastDelforPosition;
	private String lastScc;
	
	public Grp11VisitorHV(IEjbService service, IDelforRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	@Override
	public void visit(UnbSegment unbSegment) {
		log.warn("visit unb ignored.");
	}
	
	@Override
	public void visit(BgmSegment bgmSegment) {
		log.warn("visit bgm ignored.");
	}

	@Override
	public void visit(ComSegment comSegment) {
		log.warn("visit com ignored.");
	}

	@Override
	public void visit(CtaSegment ctaSegment) {
		log.warn("visit cta ignored.");
	}

	@Override
	public void visit(CuxSegment cuxSegment) {
		log.warn("visit cux ignored.");
	}

	@Override
	public void visit(DtmSegment dtmSegment) {
		DtmInfo dtmInfo = dtmSegment.get();
		log.info("visit dtm '" + dtmInfo.getDate() + "'.");
		repository.apply(dtmInfo.getDate());
	 
		if(Helper.isOneOf(dtmInfo.getFunctionCode(), "50", "52", "159")) {
			log.info("dtm info '" + dtmInfo.getDateString() + "' fc '" + dtmInfo.getFunctionCode() + "' ignored.");
			return;
		}
		
		if(Helper.isOneOf(dtmInfo.getFunctionCode(), "2", "17", "42", "64", "158")) {
			if(repository.hasQuantity()) {
				addPosition();							
			} else {
				log.warn("Couldn't add position for dtm info '" + dtmInfo.getDateString() + "' because no quantity is set!");
			}
		} else {
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(dtmInfo.getFunctionCode());
			clientInfo.add(dtmInfo.getDateString());
			repository.addError(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNGUELTIGES_DATUM, 
							clientInfo, null)));			
		}
	}

	private void addPosition() {
		if(Helper.isOneOf(lastScc, "2", "1")) {
			repository.addCommitForManufactoringPosition();
			return;
		}

		if("3".equals(lastScc)) {
			repository.addCommitForOrderPosition();
			return;
		}

		if(Helper.isOneOf(lastScc,  "4", "9", "12")) {
			repository.addForecastPosition();
			return;
		}

		List<Object> clientInfo = new ArrayList<Object>();
		clientInfo.add(lastScc);
		repository.addError(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_WARNING, 
				new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNBEKANNTE_FORECAST_ART, 
						clientInfo, null)));
		
		log.error("Unknown SCC '" + lastScc + "'.");
	}
	
	@Override
	public void visit(FtxSegment ftxSegment) {
		log.warn("visit ftx ignored.");
	}

	@Override
	public void visit(GisSegment gisSegment) {
		log.warn("visit gis ignored.");
	}

	@Override
	public void visit(ImdSegment imdSegment) {
		log.warn("visit imd ignored.");
	}

	@Override
	public void visit(LinSegment linSegment) {
		log.warn("visit lin ignored.");
	}

	
	@Override
	public void visit(LocSegment locSegment) {
		log.warn("visit loc ignored.");
	}

	@Override
	public void visit(NadSegment nadSegment) {
		log.warn("visit loc ignored.");
	}
	
	@Override
	public void visit(PatSegment patSegment) {
		log.warn("visit pat ignored.");
	}

	@Override
	public void visit(PiaSegment piaSegment) {
		log.warn("visit pia ignored.");
	}

	@Override
	public void visit(PriSegment priSegment) {
		log.warn("visit pri ignored");		
	}

	@Override
	public void visit(QtySegment qtySegment) {
		QtyInfo qtyInfo = qtySegment.get();
		log.warn("visit qty '" + qtyInfo.getAmount() + 
				"' for qualifier " + qtySegment.get().getTypeCode() + ".");
		
		
		if(Helper.isOneOf(qtyInfo.getTypeCode(), "113", "99")) {
			repository.apply(qtyInfo.getAmount());
			return;
		}

//		if(Helper.isOneOf(qtyInfo.getTypeCode(), "48", "70", "71", "94")) {
		if(Helper.isOneOf(qtyInfo.getTypeCode(), "48", "70", "71")) {
			log.warn("Delivered quantity '" + qtyInfo.getQuantity() + "' for typecode '" + 
					qtyInfo.getTypeCode() + "'. Ignored!");		
			return;
		}

		if("94".equals(qtyInfo.getTypeCode())) {
			if(repository.hasQuantity()) {
				log.warn("Replacing existing quantity with this '" 
					+ qtyInfo.getAmount() + "' [QTY+" + qtyInfo.getTypeCode() + "].");
				repository.apply(qtyInfo.getAmount());
			} else { 
				log.warn("Using QTY+" + qtyInfo.getTypeCode() 
					+ " as to be delivered amount for amount of '" + qtyInfo.getAmount() + "'.");
				repository.apply(qtyInfo.getAmount());				
			}
			return;
		}
		
		if("79".equals(qtyInfo.getTypeCode())) {
			log.info(" applyCumulativeAmount " + qtyInfo.getAmount() + ".");
			repository.applyCumulativeAmount(qtyInfo.getAmount());
			repository.addCumulativePosition();
			return;
		}
		
		if("83".equals(qtyInfo.getTypeCode())) {
			log.info("  applyBacklog " + qtyInfo.getAmount() + ".");
			repository.applyBacklog(qtyInfo.getAmount());
		}
		
		log.error("Could not interpret quantity '" + qtyInfo.getQuantity() + "' for typecode '" + 
				qtyInfo.getTypeCode() + "'. Ignored! (lastScc='" + lastScc + "')");
//			List<Object> clientInfo = new ArrayList<Object>();
//			clientInfo.add(qtyInfo.getTypeCode());
//			clientInfo.add(qtyInfo.getQuantity());
//			repository.addError(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
//					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_MENGE_LEER, 
//							clientInfo, null)));
//			
//			log.error("Could not interpret quantity '" + qtyInfo.getQuantity() + "' for typecode '" + 
//					qtyInfo.getTypeCode() + "'.");			
	}

	@Override
	public void visit(RffSegment rffSegment) {
		log.warn("visit rff ignored.");
	}

	@Override
	public void visit(SccSegment sccSegment) {
		SccInfo sccInfo = sccSegment.get();
		log.info("visit scc '" + sccInfo.getPlanCommitmentCode() + "'");
		lastScc = sccInfo.getPlanCommitmentCode();		
	}

	@Override
	public void visit(TodSegment todSegment) {
		log.warn("visit tod ignored.");		
	}
	
	@Override
	public void visit(UnhSegment unhSegment) {
		log.warn("visit unh ignored.");
	}

	@Override
	public void visit(UnsSegment unsSegment) {
		log.warn("visit uns '" + unsSegment.getSegmentCode() + "' ignored.");
	}

	@Override
	public void visit(UntSegment untSegment) {
		log.warn("visit unt ignored.");
	}
	
	@Override
	public void visit(UnzSegment unzSegment) {
		log.warn("visit unz ignored.");
	}
	@Override
	public void visit(CntSegment cntSegment) {
		log.warn("visit cnt ignored.");
	}
	
	@Override
	public void visit(DocSegment docSegment) {
		log.warn("visit doc ignored.");
	}

	@Override
	public void visit(TdtSegment tdtSegment) {
		log.warn("visit tdt ignored.");
	}	
}
