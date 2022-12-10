package com.lp.service.edifact.visitor.orders;

import java.math.BigDecimal;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.IOrdersEjbService;
import com.lp.service.edifact.IOrdersRepository;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.FtxSegment;
import com.lp.service.edifact.parser.ImdSegment;
import com.lp.service.edifact.parser.LinSegment;
import com.lp.service.edifact.parser.PiaSegment;
import com.lp.service.edifact.parser.PriSegment;
import com.lp.service.edifact.parser.QtySegment;
import com.lp.service.edifact.schema.DtmInfo;
import com.lp.service.edifact.schema.FtxInfo;
import com.lp.service.edifact.schema.ImdInfo;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.PiaInfo;
import com.lp.service.edifact.schema.PriInfo;
import com.lp.service.edifact.schema.QtyInfo;
import com.lp.service.edifact.visitor.LoggingVisitorIgnored;
import com.lp.util.Helper;

public class OrdersGrp25HV extends LoggingVisitorIgnored {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(OrdersGrp25HV.class); 

	private final IOrdersEjbService ejbService;
	private final IOrdersRepository repository;
	

	public OrdersGrp25HV(IOrdersEjbService ordersEjbService, IOrdersRepository ordersRepository) {
		this.ejbService = ordersEjbService;
		this.repository = ordersRepository;
	}
	
	public IOrdersRepository repository() {
		return this.repository;
	}
	
	@Override
	public void visit(DtmSegment dtmSegment) {
		DtmInfo dtmInfo = dtmSegment.get();
		
		if ("2".equals(dtmInfo.getFunctionCode())) {
			if (!repository().hasDeliveryPosition()) {
				log.info("\tdelivery date " + dtmInfo.getDateString());
				repository().apply(dtmInfo.getDate());
			} else {
				log.info("\tdelivery position date " + dtmInfo.getDateString());
				repository().applyDeliveryDate(dtmInfo.getDate());
			}
			return;
		}
		super.visit(dtmSegment);
	}
	

	@Override
	public void visit(LinSegment linSegment) {
		LinInfo linInfo = linSegment.get();
		log.info("visit lin " + linInfo.getLineItemIdentifier() + ":" + 
				linInfo.getItemIdentifier());
		
		findArtikel(linInfo);
	}
	
	private void findArtikel(LinInfo linInfo) {
		if (!Helper.isStringEmpty(linInfo.getItemIdentifier())) {
			ArtikelDto artikelDto = ejbService
					.artikelFindByCustomerNr(linInfo.getItemIdentifier());
			if (artikelDto != null) {
				log.info("\tadding item '" + artikelDto.getCNr() +
						"' (item-id " + artikelDto.getIId() + ")");
				repository().apply(artikelDto, linInfo);
				return;
			}			
		}
		
		repository().applyLinInfo(linInfo);
	}
	
	@Override
	public void visit(PiaSegment piaSegment) {
		log.info("visit pia " + piaSegment.get().getIdentifierCode());
		
		PiaInfo piaInfo = piaSegment.get();
		repository().applyPiaInfo(piaInfo);
	}
	
	@Override
	public void visit(ImdSegment imdSegment) {
		log.info("visit imd " + imdSegment.get().getDescriptionCode());

		ImdInfo imdInfo = imdSegment.get();
		repository().applyImdInfo(imdInfo);
	}
	
	@Override
	public void visit(PriSegment priSegment) {
		PriInfo priInfo = priSegment.get();
		log.info("visit pri " + priInfo.getQualifier() + ":" + priInfo.getPrice().toPlainString());
		
		if ("AAA".equals(priInfo.getQualifier())) {
			BigDecimal price = priInfo.getPrice();
			if (price != null) {
				repository().applyPrice(price);
				return;
			}
		}
		super.visit(priSegment);
	}
	
	
	@Override
	public void visit(QtySegment qtySegment) {
		QtyInfo qtyInfo = qtySegment.get();
		if ("21".equals(qtySegment.get().getTypeCode())) {
			if (!repository().hasDeliveryPosition()) {
				log.info("\torder quantity " + qtyInfo.getAmount().toPlainString());
			
				repository().applyQuantity(qtyInfo.getAmount(), qtyInfo.getUnitCode());
			} else {
				log.info("\tdelivery quantity " + qtyInfo.getAmount().toPlainString());
				
				repository().applyDeliveryQuantity(qtyInfo.getAmount());
			}
			
			return;
		}
		super.visit(qtySegment);
	}
	
	@Override
	public void visit(FtxSegment ftxSegment) {
		FtxInfo ftxInfo = ftxSegment.get();

		log.info("visit ftx " + ftxInfo.getFunctionCode() + ":" + 
				ftxInfo.getText(0));
		if ("ZZZ".equals(ftxInfo.getSubjectCode())) {
			repository().applyText(ftxInfo.getText(0));
			return;
		}
		
		super.visit(ftxSegment);
	}
}
