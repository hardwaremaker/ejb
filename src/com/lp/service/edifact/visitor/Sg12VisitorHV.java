package com.lp.service.edifact.visitor;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
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
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.RffInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;

public class Sg12VisitorHV implements IVisitor {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(Sg12VisitorHV.class); 

	private IEjbService service;
	private IDelforRepository repository;
	
	private DelforPosition lastDelforPosition;
	
	public Sg12VisitorHV(IEjbService service, IDelforRepository repository) {
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
		log.info("visit ftx '" + ftxSegment.get().getText(0) + "'");
		if(lastDelforPosition == null) {
			log.error("got ftx without previous LIN Segment");
			return;
		}
		lastDelforPosition.setTexts(ftxSegment.get().getTexts());
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
		LinInfo linInfo = linSegment.get();
		log.info("visit lin " + linInfo.getLineItemIdentifier() + ":" + linInfo.getItemIdentifier());
		
		findArtikel(linInfo);
	}

	private void findArtikel(LinInfo linInfo) {
		if(lastDelforPosition != null) {
			log.error("Unprocessed item '" + lastDelforPosition.getItemDto().getCNr() +
					", id=" + lastDelforPosition.getItemDto().getIId() + "'");
		}
		
		List<ArtikelDto> artikels = service.listArtikelFindByCustomerNr(linInfo.getItemIdentifier());
		if(artikels != null && artikels.size() == 1) {
			lastDelforPosition = new DelforPosition();
			lastDelforPosition.setItemDto(artikels.get(0));
			
			repository.push(lastDelforPosition);
			return;
		}
		lastDelforPosition = null;

		if(artikels == null || artikels.size() == 0) {
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(linInfo.getItemIdentifier());
			repository.addError(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ARTIKEL_NICHT_GEFUNDEN, 
							clientInfo, null)));
			return;
		}

		List<Object> clientInfo = new ArrayList<Object>();
		clientInfo.add(linInfo.getItemIdentifier());
		repository.addError(new EJBLineNumberExceptionLP(0, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
				new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN, 
						clientInfo, null)));		
/*		
		ArtikelDto artikelDto = service.artikelFindByCustomerNr(linInfo.getItemIdentifier());
		if(artikelDto == null) {
			log.error("couldn't find item '" + linInfo.getItemIdentifier() + "'");
			return;
		}
		
		lastDelforPosition = new DelforPosition();
		lastDelforPosition.setItemDto(artikelDto);
		
		repository.push(lastDelforPosition);
*/		
	}
	
	@Override
	public void visit(LocSegment locSegment) {
		log.warn("visit loc ignored.");
	}

	@Override
	public void visit(NadSegment nadSegment) {
		log.warn("visit nad ignored.");
	}
	
	@Override
	public void visit(PatSegment patSegment) {
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
		RffInfo rffInfo = rffSegment.get();
		log.info("visit rff '" + rffInfo.getFunctionCode() + "', " + rffInfo.getIdentifier() + "'");
		if("CO".equals(rffInfo.getFunctionCode())) {
			repository.applyOrderReference(rffInfo.getIdentifier(), rffInfo.getDocumentLineIdentifier());
		}
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
