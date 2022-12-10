package com.lp.service.edifact.visitor;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
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
import com.lp.service.edifact.schema.NadInfo;

public class Sg2VisitorHV implements IVisitor {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(Sg2VisitorHV.class); 

	private IEjbService service;
	private IDelforRepository repository;
	
	public Sg2VisitorHV(IEjbService service, IDelforRepository repository) {
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
		log.warn("visit dtm ignored.");
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
		log.info("visit nad " + nadSegment.get().getPartyFunctionCode() + ":" + nadSegment.get().getPartyIdentifier());
		
		findKunde(nadSegment);
	}

	private void findKunde(NadSegment nadSegment) {
		NadInfo nadInfo = nadSegment.get();
		if("SE".equals(nadInfo.getPartyFunctionCode())) {
			log.info("ignored 'SE' NAD Segment");
			return;
		}
		
		if("BY".equals(nadInfo.getPartyFunctionCode())) {
			KundeDto kundeDto = service.kundeFindByIdentification(nadInfo.getPartyIdentifier());
			if(kundeDto != null) {
				log.info("  found KundeDto.IId(" + kundeDto.getIId() + ") by identification");
				repository.setBuyer(kundeDto);
				return;
			}
			
			kundeDto = service.kundeFindByAddress(
					nadInfo.getName(0), nadInfo.getStreet(0),
					nadInfo.getCityName(), nadInfo.getPostalIdentifcationCode(), nadInfo.getCountryIndentificationCode());
			if(kundeDto != null) {
				log.info("  found KundeDto.IId(" + kundeDto.getIId() + ") by address.");
				repository.setBuyer(kundeDto);
				return;
			}			
		}	

		log.error("Could not find KundeDto for FunctionCode '" + 
				nadInfo.getPartyFunctionCode() + "' and Identification '" + nadInfo.getPartyIdentifier() + "'.");
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
		log.warn("visit qty ignored.");
	}

	@Override
	public void visit(RffSegment rffSegment) {
		log.warn("visit rff ignored.");
	}

	@Override
	public void visit(SccSegment sccSegment) {
		log.warn("visit scc ignored.");
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
		log.warn("visit uns ignored.");
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
