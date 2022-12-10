package com.lp.server.partner.bl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.partner.service.IVendidataSuppliersExportBeanServices;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.schema.vendidata.commonobjects.XMLCountry;
import com.lp.server.schema.vendidata.commonobjects.XMLPostalcode;
import com.lp.server.schema.vendidata.commonobjects.XMLStatus;
import com.lp.server.schema.vendidata.suppliers.ObjectFactory;
import com.lp.server.schema.vendidata.suppliers.XMLSupplier;
import com.lp.server.schema.vendidata.suppliers.XMLSuppliers;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class VendidataSuppliersExportTransformer {

	private static final String UNBEKANNT = "unbekannt";
	private IVendidataSuppliersExportBeanServices beanServices;
	private ObjectFactory suppliersObjectFactory;
	private XMLSuppliers xmlSuppliers;
	private List<EJBExceptionLP> exportErrors;
	private List<LieferantDto> hvSuppliers;
	private Map<Integer, KostenstelleDto> kostenstelleMap;
	private VendidataExportStats stats;
	
	public VendidataSuppliersExportTransformer(IVendidataSuppliersExportBeanServices beanServices) {
		this.beanServices = beanServices;
	}

	private void setDefaults() throws RemoteException {
		suppliersObjectFactory = new ObjectFactory();
		xmlSuppliers = suppliersObjectFactory.createXMLSuppliers();
		exportErrors = new ArrayList<EJBExceptionLP>();
		stats = new VendidataExportStats();
		initKostenstelleMap();
		init4VendingSuppliers();
	}
	
	private void init4VendingSuppliers() {
		List<LieferantDto> suppliersAll = new ArrayList<LieferantDto>();
		suppliersAll = beanServices.lieferantFindAll();
		hvSuppliers = new ArrayList<LieferantDto>();
		
		for (LieferantDto lieferantDto : suppliersAll) {
			if (!Helper.short2boolean(lieferantDto.getBVersteckterkunde()) 
					&& kostenstelleMap.containsKey(lieferantDto.getIIdKostenstelle())) {
				hvSuppliers.add(lieferantDto);
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

	public VendidataXmlSupplierTransformResult exportSuppliers() throws RemoteException {
		setDefaults();
		
		return exportSuppliersImpl();
	}
	
	private VendidataXmlSupplierTransformResult exportSuppliersImpl() throws RemoteException {
		for (LieferantDto lieferantDto : hvSuppliers) {
			XMLSupplier xmlSupplier = createXMLSupplier(lieferantDto);
			if (xmlSupplier != null) {
				xmlSuppliers.getSupplier().add(xmlSupplier);
			}
		}
		
		return new VendidataXmlSupplierTransformResult(xmlSuppliers, stats);
	}
	
	public VendidataXmlSupplierTransformResult checkExportSuppliers() throws RemoteException {
		setDefaults();
		checkFields();
		
		return new VendidataXmlSupplierTransformResult(exportErrors, stats);

	}
	
	private void checkFields() throws RemoteException {
		for (LieferantDto lieferantDto : hvSuppliers) {
			getKreditorennummer(lieferantDto);
			getFremdsystemnummer(lieferantDto, false);
		}
	}

	private XMLSupplier createXMLSupplier(LieferantDto lieferantDto) throws RemoteException {
		XMLSupplier xmlSupplier = suppliersObjectFactory.createXMLSupplier();
		
		xmlSupplier.setIsVendor(true);
		xmlSupplier.setIsSupplier(true);
		xmlSupplier.setStatus(createXMLStatus(lieferantDto.getPartnerDto()));
		
		xmlSupplier.setSupplierId(getFremdsystemnummer(lieferantDto, true));
		xmlSupplier.setAccountingId(getKreditorennummer(lieferantDto));
		xmlSupplier.setName1(lieferantDto.getPartnerDto().getCName1nachnamefirmazeile1());
		xmlSupplier.setStreet(lieferantDto.getPartnerDto().getCStrasse());
		xmlSupplier.setPhoneNumber(lieferantDto.getPartnerDto().getCTelefon());
		
		xmlSupplier.setPostalCode(createXMLPostalCode(lieferantDto.getPartnerDto()));
//		xmlSupplier.setLanguage(null);
		return xmlSupplier;
	}

	/**
	 * @param lieferantDto
	 * @return Fremdsystemnummer
	 * @throws RemoteException 
	 */
	private Integer getFremdsystemnummer(LieferantDto lieferantDto, boolean generateIfNotExist) throws RemoteException {
		try {
			if (generateIfNotExist && lieferantDto.getCFremdsystemnr() == null) {
				return beanServices.generiere4VendingSupplierId(lieferantDto.getIId());
			}
			return lieferantDto.getCFremdsystemnr() == null ? null : Integer.parseInt(lieferantDto.getCFremdsystemnr());
		} catch (NumberFormatException ex) {
			exportErrors.add(EJBExcFactory.lieferantFremdsystemnrNichtNummerisch(lieferantDto));
			stats.setErrorCounts(stats.getErrorCounts() + 1);
		}
		return null;
	}

	private Integer getKreditorennummer(LieferantDto lieferantDto) {
		stats.setTotalExports(stats.getTotalExports() + 1);
		if (lieferantDto.getIKreditorenkontoAsIntegerNotiId() == null) {
			exportErrors.add(EJBExcFactory.kreditorkontoFehlt(lieferantDto));
			stats.setErrorCounts(stats.getErrorCounts() + 1);
			return null;
		}
		return lieferantDto.getIKreditorenkontoAsIntegerNotiId();
	}

	private XMLPostalcode createXMLPostalCode(PartnerDto partnerDto) {
		com.lp.server.schema.vendidata.commonobjects.ObjectFactory commonObjectFactory = 
				new com.lp.server.schema.vendidata.commonobjects.ObjectFactory();
		
		XMLPostalcode xmlPostalcode = commonObjectFactory.createXMLPostalcode();
		XMLCountry xmlCountry = commonObjectFactory.createXMLCountry();
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

	private XMLStatus createXMLStatus(PartnerDto partnerDto) {
		return Helper.short2boolean(partnerDto.getBVersteckt()) ? XMLStatus.BLOCKED : XMLStatus.ACTIVE;
	}
	
	private String getUnbekanntIfNullOrEmpty(String value) {
		return value == null || value.isEmpty() ? UNBEKANNT : value;
	}

}
