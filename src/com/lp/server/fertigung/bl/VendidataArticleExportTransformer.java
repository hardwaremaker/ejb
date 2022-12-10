package com.lp.server.fertigung.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.fertigung.service.IVendidataArticleExportBeanServices;
import com.lp.server.schema.vendidata.articles.ObjectFactory;
import com.lp.server.schema.vendidata.articles.XMLArticle;
import com.lp.server.schema.vendidata.articles.XMLArticleStatus;
import com.lp.server.schema.vendidata.articles.XMLArticles;
import com.lp.server.schema.vendidata.articles.XMLBaseArticle;
import com.lp.server.schema.vendidata.articles.XMLItemNumber;
import com.lp.server.schema.vendidata.articles.XMLReferenceCriterion;
import com.lp.server.schema.vendidata.articles.XMLSalesArticle;
import com.lp.server.schema.vendidata.commonobjects.XMLBarcode;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataArticleExceptionLP;
import com.lp.util.Helper;

public class VendidataArticleExportTransformer {
	
	private List<EJBVendidataArticleExceptionLP> exportErrors;
	private IVendidataArticleExportBeanServices beanServices;
	private VkpfartikelpreislisteDto vkpArtikelPreislisteDto;
	private XMLArticles xmlArticles;
	private VendidataExportStats stats;
	
	public VendidataArticleExportTransformer(IVendidataArticleExportBeanServices beanServices) {
		this.beanServices = beanServices;
	}
	
	private void setDefaults() {
		vkpArtikelPreislisteDto = null;
		exportErrors = new ArrayList<EJBVendidataArticleExceptionLP>();
		ObjectFactory objectFactory = new ObjectFactory();
		xmlArticles  = objectFactory.createXMLArticles();
		stats = new VendidataExportStats();
	}

	public VendidataXmlArticleTransformResult checkExport4VendingArticles() throws RemoteException {
		setDefaults();
		checkGrunddaten();
		if (!exportErrors.isEmpty()) {
			return new VendidataXmlArticleTransformResult(xmlArticles, exportErrors, stats);
		}
		
		List<ArtikelDto> fourVDArtikel = get4VendingArtikel();
		for (ArtikelDto artikel : fourVDArtikel) {
			if (artikel.getCUL() == null || artikel.getCUL().isEmpty()) continue;

			XMLArticle xmlArtikel = createXMLArtikel(artikel);
			if (xmlArtikel != null) {
				stats.incrementTotalExports();
				xmlArticles.getArticle().add(xmlArtikel);
			} 
		}
		
		return new VendidataXmlArticleTransformResult(xmlArticles, exportErrors, stats);
	}

	public VendidataXmlArticleTransformResult export4VendingArticles() throws RemoteException {
		return checkExport4VendingArticles();
	}
	
	private void checkGrunddaten() throws RemoteException {
		initStdRabattsatz();
		if (vkpArtikelPreislisteDto == null) {
			stats.incrementErrorCounts();
			exportErrors.add(new EJBVendidataArticleExceptionLP(EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEINE_AKTIVE_PREISLISTE_VORHANDEN));
		}
	}

	/**
	 * @throws RemoteException
	 */
	private void initStdRabattsatz() throws RemoteException {
		VkpfartikelpreislisteDto[] preislisten = beanServices.getAlleAktivenPreislisten();
		for (VkpfartikelpreislisteDto plDto : preislisten) {
			if (Helper.short2boolean(plDto.getBPreislisteaktiv())) {
				vkpArtikelPreislisteDto = plDto;
				return;
			}
		}
	}

	private XMLArticle createXMLArtikel(ArtikelDto artikel) throws RemoteException {
		ArtgruDto artikelgruppeDto = getArtikelgruppe(artikel);
		MwstsatzDto mwstSatzDto = getMWstSatz(artikel);
		if (artikelgruppeDto == null || mwstSatzDto == null) return null;
		artikel.setArtgruDto(artikelgruppeDto);
		
		XMLArticle xmlArtikel = new XMLArticle();
		xmlArtikel.setReferenceCriterion(XMLReferenceCriterion.BASE_ITEM_NUMBER);
		
		XMLBaseArticle xmlBaseArtikel = createXMLBaseArtikel(artikel, mwstSatzDto);
		if (xmlBaseArtikel == null) return null;
		xmlArtikel.setBaseArticle(xmlBaseArtikel);
		
		xmlArtikel.setSalesArticle(createXMLSalesArtikel(artikel, mwstSatzDto));
		
		return xmlArtikel;
	}

	private XMLSalesArticle createXMLSalesArtikel(ArtikelDto artikel, MwstsatzDto mwstSatzDto) throws RemoteException {
		BigDecimal vkpBasis = getEinzelverkaufspreisDto(artikel);
		if (vkpBasis == null) return null;
		
		XMLSalesArticle xmlSalesArtikel = new XMLSalesArticle();
		xmlSalesArtikel.setIsSalesArticle(true);
		XMLItemNumber xmlItemNr = new XMLItemNumber();
		xmlItemNr.setAutoGenerate(true);
		xmlItemNr.setValue(1);
		xmlSalesArtikel.setItemNumber(xmlItemNr);
		
		BigDecimal rabattsumme = vkpArtikelPreislisteDto.getNStandardrabattsatz() != null ?
				vkpBasis.multiply(vkpArtikelPreislisteDto.getNStandardrabattsatz().movePointLeft(2)) :
					BigDecimal.ZERO;
		xmlSalesArtikel.setPrice(vkpBasis.subtract(rabattsumme).doubleValue());
		xmlSalesArtikel.setName(artikel.getArtikelsprDto().getCBez());
		xmlSalesArtikel.setGroup(artikel.getArtgruDto().getCNr());
		xmlSalesArtikel.setVATRate(mwstSatzDto.getFMwstsatz());
		
		return xmlSalesArtikel;
	}

	private XMLBaseArticle createXMLBaseArtikel(ArtikelDto artikel, MwstsatzDto mwstSatzDto) throws RemoteException {
		BigDecimal einzelEKP = getEinkaufspreis(artikel);
		if (einzelEKP == null) return null;
		
		XMLBaseArticle xmlBaseArtikel = new XMLBaseArticle();
		xmlBaseArtikel.setDontShowInLoadingLists(false);
		xmlBaseArtikel.setIsBaseArticle(true);
		xmlBaseArtikel.setStrictBarcodeSynchronisation(true);
		xmlBaseArtikel.setStatus(XMLArticleStatus.ACTIVE);
		XMLItemNumber xmlItemNr = new XMLItemNumber();
		xmlItemNr.setAutoGenerate(false);
		xmlItemNr.setValue(Integer.parseInt(artikel.getCUL()));
		xmlBaseArtikel.setItemNumber(xmlItemNr);
		xmlBaseArtikel.setErpId(artikel.getCNr());
		
		xmlBaseArtikel.setPrice(einzelEKP.doubleValue());
		xmlBaseArtikel.setName(artikel.getArtikelsprDto().getCBez());
		xmlBaseArtikel.setGroup(artikel.getArtgruDto().getCNr());
		xmlBaseArtikel.setVATRate(mwstSatzDto.getFMwstsatz());
		List<XMLBarcode> xmlBarcodes = createBarcodes(artikel);
		xmlBaseArtikel.getBarcode().addAll(xmlBarcodes);
		
		return xmlBaseArtikel;
	}
	
	private List<ArtikelDto> get4VendingArtikel() throws RemoteException {
		List<ArtikelDto> artikelList = new ArrayList<ArtikelDto>();
		try {
			artikelList = beanServices.artikelFindAllWith4VendingId();
		} catch (EJBExceptionLP ex) {
			ex.printStackTrace();
		}
		
		return artikelList;
	}

	private MwstsatzDto getMWstSatz(ArtikelDto artikel) throws RemoteException {
		MwstsatzDto mwstSatzDto = null;
		try {
			mwstSatzDto = artikel.getMwstsatzbezIId() == null ? null :
				beanServices.mwstSatzFindByPrimaryKey(artikel.getMwstsatzbezIId());
		} catch (EJBExceptionLP ex) {
		}
		
		if (mwstSatzDto == null || mwstSatzDto.getFMwstsatz() == null) {
			stats.incrementErrorCounts();
			exportErrors.add(new EJBVendidataArticleExceptionLP(Integer.parseInt(artikel.getCUL()), artikel.getCNr(), null, 
					EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEIN_ARTIKEL_MWST_SATZ_DEFINIERT));
			return null;
		}
		
		return mwstSatzDto;
	}

	private ArtgruDto getArtikelgruppe(ArtikelDto artikelDto) throws RemoteException {
		ArtgruDto artikelgruppeDto = null;
		try {
			artikelgruppeDto = artikelDto.getArtgruIId() == null ? null : 
				beanServices.artikelgruppeFindByArtikelIId(artikelDto.getArtgruIId());
		} catch (EJBExceptionLP ex) {
		}
		
		if (artikelgruppeDto == null) {
			stats.incrementErrorCounts();
			exportErrors.add(new EJBVendidataArticleExceptionLP(Integer.parseInt(artikelDto.getCUL()), artikelDto.getCNr(), null, 
					EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_EXPORT_4VENDING_ARTIKELGRUPPE_NICHT_GEFUNDEN));
			return null;
		}
		
		return artikelgruppeDto;
	}

	private List<XMLBarcode> createBarcodes(ArtikelDto artikel) throws RemoteException {
		List<EinkaufseanDto> einkaufseanDtoList = new ArrayList<EinkaufseanDto>();
		try {
			einkaufseanDtoList = beanServices.einkaufseanFindByArtikelIId(artikel.getIId());
		} catch (EJBExceptionLP ex) {
		}

		List<XMLBarcode> barcodes = new ArrayList<XMLBarcode>();
		for (EinkaufseanDto einkaufseanDto : einkaufseanDtoList) {
			XMLBarcode xmlBarcode = new XMLBarcode();
			xmlBarcode.setMultiplier(einkaufseanDto.getNMenge().doubleValue());
			xmlBarcode.setValue(Long.parseLong(einkaufseanDto.getCEan()));
			barcodes.add(xmlBarcode);
		}
		
		return barcodes;
	}

	private BigDecimal getEinzelverkaufspreisDto(ArtikelDto artikelDto) throws RemoteException {
		VkPreisfindungEinzelverkaufspreisDto vkEinzelVKP = null;
		try {
			vkEinzelVKP = beanServices.getArtikeleinzelverkaufspreis(artikelDto.getIId());
		} catch (EJBExceptionLP ex) {
		}
		
		if (vkEinzelVKP == null || vkEinzelVKP.getNVerkaufspreisbasis() == null 
				|| vkEinzelVKP.getNVerkaufspreisbasis().compareTo(BigDecimal.ZERO) == 0) {
			stats.incrementErrorCounts();
			exportErrors.add(new EJBVendidataArticleExceptionLP(Integer.parseInt(artikelDto.getCUL()), artikelDto.getCNr(), null, 
					EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEIN_AKTUELL_GUELTIGER_VKPREIS_VORHANDEN));
			return null;
		}
		
		return vkEinzelVKP.getNVerkaufspreisbasis();
	}
	
	private BigDecimal getEinkaufspreis(ArtikelDto artikelDto) throws RemoteException {
		ArtikellieferantDto artLiefDto = null;
		try {
			artLiefDto = beanServices.getArtikeleinkaufspreis(artikelDto.getIId());
		} catch (EJBExceptionLP ex) {
		}

		if (artLiefDto == null) {
			stats.incrementErrorCounts();
			exportErrors.add(new EJBVendidataArticleExceptionLP(Integer.parseInt(artikelDto.getCUL()), artikelDto.getCNr(), null, 
					EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEIN_LIEF1PREIS_VORHANDEN));
			return null;
		}
		return artLiefDto.getLief1Preis();
	}
}
