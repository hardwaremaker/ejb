package com.lp.server.lieferschein.bl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xml.sax.SAXException;

import com.lp.server.schema.easydata.stm.XMLActivity;
import com.lp.server.schema.easydata.stm.XMLActivityName;
import com.lp.server.schema.easydata.stm.XMLProduct;
import com.lp.server.schema.easydata.stm.XMLStockMovement;
import com.lp.server.schema.easydata.stm.XMLTour;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EasydataXMLFieldInstantiator;

public class EasydataStockMovementTransformer {
	private final ILPLogger log = LPLogService.getInstance().getLogger(this.getClass());

	private EasydataStockInfoUnmarshaller<XMLStockMovement> unmarshaller;
	private XMLStockMovement xmlHead;
	
	public EasydataStockMovementTransformer() {
		unmarshaller = new EasydataStockInfoUnmarshaller<XMLStockMovement>(XMLStockMovement.class);
	}

	public List<EasydataStockMovement> transform(String xmlDaten) throws Throwable {
		try {
			xmlHead = unmarshaller.unmarshal(xmlDaten);
			
			EasydataXMLFieldInstantiator<XMLStockMovement> fieldInstantiator = new EasydataXMLFieldInstantiator<XMLStockMovement>();
			xmlHead = fieldInstantiator.instantiateNullFields(xmlHead);
			
			return transformImpl();
		} catch (JAXBException e) {
			log.error("JAXBException", e);
			throw e;
		} catch (SAXException e) {
			log.error("SAXException", e);
			throw e;
		} catch (Exception e) {
			log.error("Exception", e);
			throw e;
		}
	}

	private List<EasydataStockMovement> transformImpl() {
		List<EasydataStockMovement> results = new ArrayList<EasydataStockMovement>();
		
		for (XMLTour tour : xmlHead.getTours()) {
			for (XMLActivity activity : tour.getActivities()) {
				if (!XMLActivityName.StockMovement.equals(activity.getName())) {
					log.info("Unexpected Activity '" + activity.getName() + "'. Skip.");
					continue;
				}
				if (activity.getProducts().isEmpty()) {
					continue;
				}
				
				EasydataStockMovement stm = createStockMovement(tour, activity);
				results.add(stm);
			}
		}
		return results;
	}

	private EasydataStockMovement createStockMovement(XMLTour tour, XMLActivity activity) {
		EasydataStockMovement stm = new EasydataStockMovement();
		stm.setSerialNumber(tour.getSerialNumber());
		stm.setPersonalNumber(tour.getPersonalNumber());
		stm.setTourTime(parseTime(tour.getTourDate()));
		
		stm.setLoginTime(parseTime(activity.getTimestampLogin()));
		stm.setLogoutTime(parseTime(activity.getTimestampLogout()));
		stm.setAutoIndex(activity.getAutoindex());
		stm.setStock(activity.getReference());
		stm.setTargetStock(activity.getTarget());
		
		for (XMLProduct product : activity.getProducts()) {
			stm.getProducts().add(createProduct(product));
		}
		
		return stm;
	}

	private EasydataProduct createProduct(XMLProduct product) {
		EasydataProduct edp = new EasydataProduct();
		edp.setBarcode(product.getBarcode());
		edp.setQuantity(product.getQuantity());
		edp.setTime(parseTime(product.getTimestamp()));
		
		return edp;
	}

	protected Timestamp parseTime(XMLGregorianCalendar xmlCalendar) {
		if(xmlCalendar == null)
			return null;
		
		return new Timestamp(xmlCalendar.toGregorianCalendar().getTimeInMillis());
	}
}
