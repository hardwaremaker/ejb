package com.lp.server.partner.bl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.partner.service.IVendidataCustomersExportBeanServices;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.schema.vendidata.customers.ObjectFactory;
import com.lp.server.schema.vendidata.customers.XMLCountry;
import com.lp.server.schema.vendidata.customers.XMLCustomer;
import com.lp.server.schema.vendidata.customers.XMLCustomers;
import com.lp.server.schema.vendidata.customers.XMLLanguage;
import com.lp.server.schema.vendidata.customers.XMLPostalcode;
import com.lp.server.schema.vendidata.customers.XMLStatus;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class VendidataCustomersExportTransformer {
	
	private static final String UNBEKANNT = "unbekannt";
	private IVendidataCustomersExportBeanServices beanServices;
	private ObjectFactory customerObjectFactory;
	private XMLCustomers xmlCustomers;
	private List<EJBExceptionLP> exportErrors;
	private VendidataExportStats stats;
	private List<KundeDto> hvCustomers;
	private Map<Integer, KostenstelleDto> kostenstelleMap;
	
	public VendidataCustomersExportTransformer(IVendidataCustomersExportBeanServices beanServices) {
		this.beanServices = beanServices;
	}

	private void setDefaults() throws RemoteException {
		customerObjectFactory = new ObjectFactory();
		xmlCustomers = customerObjectFactory.createXMLCustomers();
		exportErrors = new ArrayList<EJBExceptionLP>();
		stats = new VendidataExportStats();
		initKostenstelleMap();
		init4VendingCustomers();
	}
	
	private void init4VendingCustomers() {
		List<KundeDto> customersAll = new ArrayList<KundeDto>();
		customersAll = beanServices.kundeFindAll();
		hvCustomers = new ArrayList<KundeDto>();
		
		for (KundeDto kundeDto : customersAll) {
			if (kostenstelleMap.containsKey(kundeDto.getKostenstelleIId())) {
				hvCustomers.add(kundeDto);
			}
		}
	}
	
	private void initKostenstelleMap() throws RemoteException {
		kostenstelleMap = new HashMap<Integer, KostenstelleDto>();
		List<KostenstelleDto> kostenstelleList = beanServices.kostenstelleFindAll();
		for (KostenstelleDto kostenstelleDto : kostenstelleList) {
			if (Helper.short2boolean(kostenstelleDto.getBProfitcenter())) {
				kostenstelleMap.put(kostenstelleDto.getIId(), kostenstelleDto);
			}
		}
	}
	
	public VendidataXmlCustomerTransformResult checkExportCustomers() throws RemoteException {
		setDefaults();
		checkFields();
		
		return new VendidataXmlCustomerTransformResult(exportErrors, stats);
	}

	public VendidataXmlCustomerTransformResult exportCustomers() throws RemoteException {
		setDefaults();
		
		return exportCustomersImpl();
	}
	
	private void checkFields() throws RemoteException {
		for (KundeDto kundeDto : hvCustomers) {
			getDebitorennummer(kundeDto);
			getFremdsystemnummer(kundeDto, false);
		}
	}
	
	private VendidataXmlCustomerTransformResult exportCustomersImpl() throws RemoteException {
		for (KundeDto kundeDto : hvCustomers) {
			XMLCustomer xmlCustomer = createXMLCustomer(kundeDto);
			if (xmlCustomer != null) {
				xmlCustomers.getCustomer().add(xmlCustomer);
			}
		}
		
		return new VendidataXmlCustomerTransformResult(xmlCustomers, stats);
	}

	private XMLCustomer createXMLCustomer(KundeDto kundeDto) throws RemoteException {
		XMLCustomer xmlCustomer = customerObjectFactory.createXMLCustomer();
		xmlCustomer.setIsCustomer(true);
		xmlCustomer.setIsOperating(true);
		xmlCustomer.setStatus(createXMLStatus(kundeDto.getPartnerDto()));

		xmlCustomer.setCustomerId(getFremdsystemnummer(kundeDto, true));
		xmlCustomer.setAccountingId(getDebitorennummer(kundeDto));
		xmlCustomer.setMatchcode(kundeDto.getPartnerDto().getCKbez());
		xmlCustomer.setName1(kundeDto.getPartnerDto().getCName1nachnamefirmazeile1());
		xmlCustomer.setStreet(kundeDto.getPartnerDto().getCStrasse());
		xmlCustomer.setPhoneNumber(kundeDto.getPartnerDto().getCTelefon());
		
		xmlCustomer.setPostalcode(createXMLPostalcode(kundeDto.getPartnerDto()));
//		xmlCustomer.setLanguage(createXMLLanguage(kundeDto.getPartnerDto()));
		xmlCustomer.setGroup(kostenstelleMap.get(kundeDto.getKostenstelleIId()).getCBez());
		
		return xmlCustomer;
	}
	
	private Integer getFremdsystemnummer(KundeDto kundeDto, boolean generateIfNotExist) throws RemoteException {
		try {
			if (generateIfNotExist && kundeDto.getCFremdsystemnr() == null) {
				return beanServices.generiere4VendingCustomerId(kundeDto.getIId());
			}
			return kundeDto.getCFremdsystemnr() == null ? null : Integer.parseInt(kundeDto.getCFremdsystemnr());
		} catch (NumberFormatException ex) {
			exportErrors.add(EJBExcFactory.kundeFremdsystemnrNichtNummerisch(kundeDto));
			stats.setErrorCounts(stats.getErrorCounts() + 1);
		}
		return null;
	}
	
	private Integer getDebitorennummer(KundeDto kundeDto) {
		stats.setTotalExports(stats.getTotalExports() + 1);
		if (kundeDto.getIDebitorenkontoAsIntegerNotiId() == null) {
			exportErrors.add(EJBExcFactory.debitorkontoFehlt(kundeDto));
			stats.setErrorCounts(stats.getErrorCounts() + 1);
		}
		return kundeDto.getIDebitorenkontoAsIntegerNotiId();
	}

	private XMLStatus createXMLStatus(PartnerDto partnerDto) {
		return Helper.short2boolean(partnerDto.getBVersteckt()) ? XMLStatus.BLOCKED : XMLStatus.ACTIVE;
	}

	private XMLPostalcode createXMLPostalcode(PartnerDto partnerDto) {
		XMLPostalcode xmlPostalcode = customerObjectFactory.createXMLPostalcode();
		XMLCountry xmlCountry = customerObjectFactory.createXMLCountry();
		boolean hasLand = partnerDto.getLandplzortDto() != null && partnerDto.getLandplzortDto().getLandDto() != null;
		xmlCountry.setFullname(hasLand ? getUnbekanntIfNullOrEmpty(partnerDto.getLandplzortDto().getLandDto().getCName()) : UNBEKANNT);
		xmlCountry.setShortname(hasLand ? getUnbekanntIfNullOrEmpty(partnerDto.getLandplzortDto().getLandDto().getCLkz()) : UNBEKANNT);
		xmlPostalcode.setCountry(xmlCountry);
		boolean hasOrt = partnerDto.getLandplzortDto() != null && partnerDto.getLandplzortDto().getOrtDto() != null;
		xmlPostalcode.setCity(hasOrt ? getUnbekanntIfNullOrEmpty(partnerDto.getLandplzortDto().getOrtDto().getCName()) : UNBEKANNT);
		xmlPostalcode.setZipCode(hasOrt ? getUnbekanntIfNullOrEmpty(partnerDto.getLandplzortDto().getCPlz()) : UNBEKANNT);
		xmlPostalcode.setProvince(UNBEKANNT);
		xmlPostalcode.setCounty(UNBEKANNT);

		return xmlPostalcode;
	}
	
	private XMLLanguage createXMLLanguage(PartnerDto partnerDto) {
		XMLLanguage xmlLanguage = customerObjectFactory.createXMLLanguage();
		xmlLanguage.setISO3166(partnerDto.getLandplzortDto().getLandDto().getCLkz());
		xmlLanguage.setLanguage("");
		// TODO kundeDto.getPartnerDto().getLocaleCNrKommunikation()
		return xmlLanguage;
	}

	private String getUnbekanntIfNullOrEmpty(String value) {
		return value == null || value.isEmpty() ? UNBEKANNT : value;
	}
}
