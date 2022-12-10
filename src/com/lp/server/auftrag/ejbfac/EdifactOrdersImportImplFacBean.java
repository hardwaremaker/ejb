package com.lp.server.auftrag.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.EdifactOrdersImportImplFac;
import com.lp.server.auftrag.service.EdifactOrdersImportResult;
import com.lp.server.partner.ejb.KundeKennung;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.LieferartQuery;
import com.lp.server.system.ejbfac.EdifactOrderResponseProducer;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeEdifactOrders;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KundeId;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.bean.AuftragBean;
import com.lp.server.util.bean.AuftragpositionBean;
import com.lp.server.util.bean.BelegVerkaufBean;
import com.lp.server.util.bean.BenutzerServicesBean;
import com.lp.server.util.bean.JcrDocBean;
import com.lp.server.util.bean.KundeBean;
import com.lp.server.util.bean.LocaleBean;
import com.lp.server.util.bean.MandantBean;
import com.lp.server.util.bean.ParameterBean;
import com.lp.server.util.bean.PersonalBean;
import com.lp.server.util.bean.SystemBean;
import com.lp.server.util.bean.VersandBean;
import com.lp.server.util.bean.VkPreisfindungBean;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.OrdersRepository;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.orders.DeliveryEntry;
import com.lp.service.edifact.orders.OrdersPosition;
import com.lp.service.edifact.schema.EdifactMessage;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.NadInfo;
import com.lp.util.Helper;

@Stateless
public class EdifactOrdersImportImplFacBean implements EdifactOrdersImportImplFac {
	@PersistenceContext
	private EntityManager em;

	private final ILPLogger log = LPLogService.getInstance().getLogger(EdifactOrdersImportImplFacBean.class);

	private final MandantBean mandantBean = new MandantBean();
	private final VersandBean versandBean = new VersandBean();
	private final ParameterBean parameterBean = new ParameterBean();
	private final PersonalBean personalBean = new PersonalBean();
	private final BenutzerServicesBean benutzerServicesBean = new BenutzerServicesBean();
	private final JcrDocBean jcrDocBean = new JcrDocBean();
	private final AuftragBean auftragBean = new AuftragBean();
	private final AuftragpositionBean auftragpositionBean = new AuftragpositionBean();
	private final LocaleBean localeBean = new LocaleBean();
	private final BelegVerkaufBean belegVerkaufBean = new BelegVerkaufBean();
	private final KundeBean kundeBean = new KundeBean();
	
	private EinheitenCache einheiten;
	private MwstsatzCalculator mwstsatzCalculator;
	private PriceCalculator priceCalculator;
	private EdifactOrderResponseProducer ediProducer = new EdifactOrderResponseProducer();
	
	@Override
	public EdifactOrdersImportResult importOrdersMsg(String content, 
			EdifactMessage msg, OrdersRepository repository,
			TheClientDto theClientDto) throws RemoteException {
		OrdersData data = new OrdersData(content, msg, repository);
		EdifactOrdersImportResult rc = EdifactOrdersImportResult.Unknown;
		if (!isOk(rc = verifySenderReceiver(data, theClientDto))) return rc;
		if (!isOk(rc = verifyOrdernumber(data, theClientDto))) return rc;
		if (!isOk(rc = verifyHasBuyerParty(data, theClientDto))) return rc;
		if (!isOk(rc = verifyUniqueOrdernumber(data, theClientDto))) return rc;
		
		einheiten = new EinheitenCache(theClientDto);
		return createOrder(data, theClientDto);
	}
	
	private boolean isOk(EdifactOrdersImportResult rc) {
		return EdifactOrdersImportResult.Ok.equals(rc);
	}
	
	private String textFromToken(String token, TheClientDto theClientDto) {
		return benutzerServicesBean.get().getTextRespectUISpr(
				token, theClientDto.getMandant(), theClientDto.getLocUi());		
	}
	
	private String textFromToken(String token, TheClientDto theClientDto, Object... params) {
		return benutzerServicesBean.get().getTextRespectUISpr(
				token, theClientDto.getMandant(), theClientDto.getLocUi(), params);
	}
	
	/**
	 * Prueft, ob der UNB Sender / Empfaenger bekannt ist
	 * @param msg
	 * @return
	 */
	private EdifactOrdersImportResult verifySenderReceiver(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		// TODO ghp Edifact-Sender/Receiver gehoeren ueberprueft
		if (!"ORDERS".equals(data.msg().getUnb().getApplicationReference())) {
			return reportUnknownApplication(data, theClientDto);
		}

		return EdifactOrdersImportResult.Ok;
	}
	
	private EdifactOrdersImportResult reportUnknownApplication(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		reportDefaultErrorMessage(
				"auft.edi.email.unknown.applicationReference.header",
				"auft.edi.email.unknown.applicationReference.message",
				data, theClientDto, data.msg().getUnb().getApplicationReference());
	
		return EdifactOrdersImportResult.UnknownApplication;		
	}
	
	/**
	 * Prueft, ob es ueberhaupt eine Bestellnummer gibt
	 * @param msg
	 * @param repository
	 * @param theClientDto
	 * @return
	 */
	private EdifactOrdersImportResult verifyOrdernumber(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		String docCode = data.msg().getBgm().getDocumentNameCode();
		if (!"220".equals(docCode)) {
			return reportUnknownDocCode(data, theClientDto);
		}
		
		String orderNumber = data.msg().getBgm().getDocumentIdentifier();
		if (Helper.isStringEmpty(orderNumber)) {
			return reportMissingOrderNumber(data, theClientDto);
		}
		
		return EdifactOrdersImportResult.Ok;
	}
	
	private EdifactOrdersImportResult reportUnknownDocCode(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		reportDefaultErrorMessage(
				"auft.edi.email.unknown.documentNameCode.header",
				"auft.edi.email.unknown.documentNameCode.message",
				data, theClientDto, data.msg().getBgm().getDocumentNameCode());
		
		return EdifactOrdersImportResult.UnknownDocCode;
	}
	
	private EdifactOrdersImportResult reportMissingOrderNumber(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		reportDefaultErrorMessage(
				"auft.edi.email.missing.orderNumber.header",
				"auft.edi.email.missing.orderNumber.message",
				data, theClientDto);
		return EdifactOrdersImportResult.MissingOrderNumber;		
	}
	
	private EdifactOrdersImportResult verifyHasBuyerParty(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		int size = data.repository().buyerAddress().size();
		if (size == 1) return EdifactOrdersImportResult.Ok;
		if (size == 0) return reportNoBuyerParty(data, theClientDto);
		
		return reportMultipleBuyerParties(data, theClientDto);
	}
	
	private EdifactOrdersImportResult reportNoBuyerParty(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		reportDefaultErrorMessage(
				"auft.edi.email.missing.buyer.header",
				"auft.edi.email.missing.buyer.message",
				data, theClientDto, data.repository().buyerIdentifier());
		return EdifactOrdersImportResult.NoBuyerParty;
	}
	
	private EdifactOrdersImportResult reportMultipleBuyerParties(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		reportDefaultErrorMessage(
				"auft.edi.email.duplicate.buyer.header",
				"auft.edi.email.duplicate.buyer.message",
				data, theClientDto, data.repository().buyerIdentifier());
		return EdifactOrdersImportResult.MultipleBuyerParties;
	}
	
	private EdifactOrdersImportResult verifyUniqueOrdernumber(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		String bestellNummer = data.repository().issuerOrderNumber().get();
		KundeDto kundeDto = data.repository().buyerAddress().get(0);
		AuftragDto[] dtos = auftragBean.get()
				.auftragFindByMandantCnrKundeIIdBestellnummerOhneExc(
						data.repository().buyerAddress().get(0).getIId(),
						theClientDto.getMandant(), bestellNummer) ;
		if (dtos != null && dtos.length > 0) {
			KundeDto kundeReloadDto = kundeBean.get()
					.kundeFindByPrimaryKey(kundeDto.getIId(), theClientDto);
			String kbez = kundeReloadDto.getPartnerDto().getCKbez();
			reportDefaultErrorMessage(
					"auft.edi.email.known.orderNumber.header",
					"auft.edi.email.known.orderNumber.message",
					data, theClientDto, bestellNummer, kbez, dtos[0].getCNr());
			return EdifactOrdersImportResult.DuplicateOrderNumber;
		}
		return EdifactOrdersImportResult.Ok;
	}
	
	private EdifactOrdersImportResult createOrder(
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		AuftragDto auftragDto = new AuftragDto();
		EdifactOrdersImportResult rc = EdifactOrdersImportResult.Unknown;
		if (!isOk(rc = prepareWaehrung(auftragDto, data, theClientDto))) return rc;
		if (!isOk(rc = prepareAuftrag(auftragDto, data, theClientDto))) return rc;
		
		Integer iid = auftragBean.get()
				.createAuftrag(auftragDto, theClientDto);
		AuftragDto returnDto = auftragBean.get().auftragFindByPrimaryKey(iid) ;

		if (!isOk(rc = createPositions(returnDto, data, theClientDto))) return rc;
		
		updateLiefertermine(returnDto, data, theClientDto);

		reportNewOrder(returnDto, data, theClientDto);
		return rc;
	}

	private void updateLiefertermine(AuftragDto auftragDto,
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		auftragDto.setDLiefertermin(data.earliestDelivery());
		auftragDto.setDFinaltermin(data.latestDelivery());
		auftragBean.get().updateAuftragOhneWeitereAktion(auftragDto, theClientDto);
	}
	
	private AuftragpositionDto createPositionText(AuftragDto auftragDto, 
			ChangeEntry<?> changeEntry, TheClientDto theClientDto) throws RemoteException {
		String message = MessageFormat.format(
					getTextFor(changeEntry.getLevel()) + " " + changeEntry.getMessage(), 
					new Object[]{changeEntry.getExpectedValue(), changeEntry.getPresentendValue()});
		return createPositionText(auftragDto, message, theClientDto);
	}
	
	private AuftragpositionDto createPositionText(AuftragDto auftragDto,
			String message, TheClientDto theClientDto) throws RemoteException {
		AuftragpositionDto positionDto = new AuftragpositionDto();
		positionDto.setBelegIId(auftragDto.getIId());
		positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE);
		positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		positionDto.setXTextinhalt(message) ;
		positionDto.setBDrucken(Helper.boolean2Short(true)) ;
		positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
		positionDto.setMwstsatzIId(getMwstsatzIIdForTax(auftragDto, BigDecimal.ZERO, theClientDto)) ;
		positionDto.setBDrucken(Helper.boolean2Short(false)) ;
		positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
		positionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false)) ;
		
		Integer positionIId = auftragpositionBean.get()
				.createAuftragposition(positionDto, theClientDto); 
		return auftragpositionBean.get()
				.auftragpositionFindByPrimaryKeyOhneExc(positionIId);		
	}
	
	private String getTextFor(ChangeEntry.Level level) {
		if (ChangeEntry.Level.Info.equals(level)) return "Info:";
		if (ChangeEntry.Level.Warn.equals(level)) return "Achtung:";
		if (ChangeEntry.Level.Error.equals(level)) return "Fehler:";
		return "Info:";
	}
	
	private Integer getMwstsatzIIdForTax(AuftragDto auftragDto, 
			BigDecimal taxPercent, TheClientDto theClientDto) {
		MwstsatzDto[] mwstsatzDtos = mandantBean.get().mwstsatzfindAllByMandant(
				theClientDto.getMandant(), auftragDto.getTBelegdatum(), true) ;
		double d = taxPercent.doubleValue() ;
		for (MwstsatzDto mwstsatzDto : mwstsatzDtos) {
			if(mwstsatzDto.getFMwstsatz().equals(d)) return mwstsatzDto.getIId() ;
		}

		return null;
	}

	private void setupMwstsatzCalculator(AuftragDto auftragDto,
			OrdersData data, TheClientDto theClientDto) {
		mwstsatzCalculator = new MwstsatzCalculator(theClientDto, 
				new MwstsatzbezId(data.repository().buyerAddress().get(0).getMwstsatzbezIId()),
				auftragDto.getTBelegdatum());
	}
	
	private void setupPriceCalculator(AuftragDto auftragDto,
			OrdersData data) {
		priceCalculator = new PriceCalculator(auftragDto, 
				data.repository().buyerAddress().get(0));
	}

	private void createContractInfo(AuftragDto auftragDto,
			OrdersData data, TheClientDto theClientDto) {
		if (data.repository().contractInfo().isEmpty()) return;

		String contractInfo = data.repository().contractInfo().get();
		String ignoreContractInfo = textFromToken(
				"auft.edi.ignore.rff.ct", theClientDto);
		if (contractInfo.equalsIgnoreCase(ignoreContractInfo)) return;
		
		ChangeEntry<?> entry = new WarnEntry<String>(
				contractInfo, "", 
				"Zus\u00e4tzliche Bestellinformation:\n{0}") ;
		data.add(entry);
	}
	
	private EdifactOrdersImportResult createPositions(AuftragDto auftragDto, 
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		createContractInfo(auftragDto, data, theClientDto);
		
		setupMwstsatzCalculator(auftragDto, data, theClientDto);
		setupPriceCalculator(auftragDto, data);

		createPositionsForChangeEntries(auftragDto, data, theClientDto);
		
		for (OrdersPosition pos : data.repository().positions()) {
			if (pos.getItemDto().isPresent()) {
				createPositionIdent(pos, auftragDto, data, theClientDto);
			} else {
				createPositionHandeingabe(pos, auftragDto, data, theClientDto);
				if (Helper.isStringEmpty(pos.getLinInfo().get().getItemIdentifier())) {
					data.add(new InfoEntry<String>("", "", 
							"Der Artikel hat keine Identifikation, es wurde eine Handeingabe erzeugt"));					
				} else {
					data.add(new InfoEntry<String>(pos.getLinInfo().get().getItemIdentifier(), "", 
						"Der Artikel ''{0}'' wurde nicht gefunden, es wurde eine Handeingabe erzeugt"));
				}
			}
			
			createPositionsForChangeEntries(auftragDto, data, theClientDto);
		}
		
		return EdifactOrdersImportResult.Ok;
	}

	private void createPositionsForChangeEntries(
			AuftragDto auftragDto, OrdersData data, TheClientDto theClientDto) throws RemoteException {
		for (ChangeEntry<?> changeEntry : data.changeEntries()) {
			createPositionText(auftragDto, changeEntry, theClientDto) ;
		}
		data.clear();
	}
	
	private AuftragpositionDto createPositionHandeingabe(OrdersPosition pos,
			AuftragDto auftragDto, OrdersData data, TheClientDto theClientDto) throws RemoteException {
		AuftragpositionDto positionDto = new AuftragpositionDto();
		positionDto.setBelegIId((auftragDto.getIId()));
		positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE);

		LinInfo linInfo = pos.getLinInfo().get();
		
		String shortDescription = pos.getImdInfos().size() > 0 ? 
			pos.getImdInfos().get(0).getDescription(0) : linInfo.getItemIdentifier();
		positionDto.setCBez(shortDescription);
		if (pos.getImdInfos().size() > 1) {
			positionDto.setCZusatzbez(pos.getImdInfos().get(1).getDescription(0));
		}
		
		positionDto.setNMenge(pos.getQuantity());
		positionDto.setNOffeneMenge(pos.getQuantity());
		positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		
		String einheit = pos.getUnit();
		if(!einheiten.getValueOfKey(einheit).isPresent()) {
			String validEinheit = "Stk";
			ChangeEntry<?> changeEntry = new WarnEntry<String>(validEinheit, einheit,
					"Die Einheit {1} wurde nicht gefunden, stattdessen wird {0} verwendet.");
			data.add(changeEntry);
			einheit = validEinheit;
		}			
		positionDto.setEinheitCNr(einheit);

		BigDecimal theirPrice = pos.getNetPrice();

		positionDto.setNEinzelpreis(theirPrice) ;
		positionDto.setNNettoeinzelpreis(theirPrice) ;
		positionDto.setNBruttoeinzelpreis(theirPrice) ;
		positionDto.setNNettoeinzelpreisplusversteckteraufschlag(theirPrice);
		positionDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(theirPrice);

		positionDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		positionDto.setBDrucken(Helper.getShortTrue());
		positionDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		positionDto.setFRabattsatz(new Double(0.0));
		positionDto.setFZusatzrabattsatz(new Double(0.0));
		positionDto.setNRabattbetrag(BigDecimal.ZERO);
		positionDto.setNMaterialzuschlag(BigDecimal.ZERO);

		Timestamp ts = new Timestamp(pos.getDate().getTime());
		data.updateDelivery(ts);		
		positionDto.setTUebersteuerbarerLiefertermin(ts);

		// Es gibt keine Steuerinfo im EDIFACT 
		// ("Preise werden grundsaetzlich exklusive Steuer angegeben")
		KundeDto kundeDto = data.repository().buyerAddress().get(0);
		MwstsatzDto mwstsatzDto = mandantBean.get()
				.mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), 
						auftragDto.getTBelegdatum(), theClientDto);		
		positionDto.setMwstsatzIId(mwstsatzDto.getIId());
		positionDto = (AuftragpositionDto) belegVerkaufBean.get().berechneNeu(
				positionDto, new KundeId(auftragDto.getKundeIIdAuftragsadresse()),
				auftragDto.getTBelegdatum(), theClientDto);

		positionDto = (AuftragpositionDto) ediProducer.setPositionReference(positionDto, pos);
		Integer positionId = handleDeliverEntries(pos, 
				auftragDto, positionDto, data, theClientDto);

		return auftragpositionBean.get()
				.auftragpositionFindByPrimaryKeyOhneExc(positionId) ;		
	}
	
	
	private AuftragpositionDto createPositionIdent(OrdersPosition pos,
			AuftragDto auftragDto, OrdersData data, TheClientDto theClientDto) throws RemoteException {
		AuftragpositionDto positionDto = new AuftragpositionDto();
		ArtikelDto itemDto = pos.getItemDto().get();
		positionDto.setBelegIId((auftragDto.getIId()));
		positionDto.setArtikelIId(itemDto.getIId());
		positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT) ;
		positionDto.setNMenge(pos.getQuantity()) ;
		positionDto.setNOffeneMenge(pos.getQuantity()) ;
		positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN) ;
		
		String einheit = pos.getUnit();
		if(!einheiten.getValueOfKey(einheit).isPresent()) {
			String validEinheit = "Stk" ;
			ChangeEntry<?> changeEntry = new WarnEntry<String>(validEinheit, einheit,
					"Die Einheit {1} wurde nicht gefunden, stattdessen wird {0} verwendet.") ;
			data.add(changeEntry) ;
			einheit = validEinheit ;
		}
		
		positionDto.setEinheitCNr(einheit) ;

		BigDecimal vkPrice = null;
		HvOptional<Integer> mwstsatzId = mwstsatzCalculator.getMwstsatzId(itemDto);
		if(mwstsatzId.isPresent()) {
			vkPrice = priceCalculator.calculate(itemDto, 
					positionDto.getNMenge(), mwstsatzId.get(), theClientDto) ;
		} else {
			vkPrice = BigDecimal.ZERO.setScale(2);
			ChangeEntry<?> warnEntry = new WarnEntry<BigDecimal>(vkPrice, vkPrice, 
					"Es konnnte kein Preis ermittelt werden, weil kein Steuersatz ermittelt werden kann. Es wird {0} verwendet.");
			data.add(warnEntry);				
		}

		if(vkPrice == null) {
			vkPrice = BigDecimal.ZERO.setScale(2);
			ChangeEntry<?> warnEntry = new WarnEntry<BigDecimal>(vkPrice, vkPrice, 
					"Es konnnte kein Preis ermittelt werden. Es wird {0} verwendet.") ;
			data.add(warnEntry);				
		}
		
		BigDecimal theirPrice = pos.getNetPrice();
		if(vkPrice.compareTo(theirPrice) != 0) {
			ChangeEntry<?> infoEntry = new InfoEntry<BigDecimal>(vkPrice, theirPrice, 
					"Der angegebene Preis {1} entspricht nicht dem hinterlegtem Preis, stattdessen wird {0} verwendet.") ;
			data.add(infoEntry) ;
		}
		
		positionDto.setNEinzelpreis(vkPrice);
		positionDto.setNNettoeinzelpreis(vkPrice);
		positionDto.setNNettoeinzelpreisplusversteckteraufschlag(vkPrice);
		positionDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(vkPrice);
		positionDto.setNBruttoeinzelpreis(vkPrice); 
		positionDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse()) ;
		positionDto.setBDrucken(Helper.getShortTrue()) ;
		positionDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		positionDto.setFRabattsatz(new Double(0.0));
		positionDto.setFZusatzrabattsatz(new Double(0.0));
		positionDto.setNRabattbetrag(BigDecimal.ZERO);
		positionDto.setNMaterialzuschlag(BigDecimal.ZERO);	

		positionDto = (AuftragpositionDto) belegVerkaufBean.get().berechneNeu(
				positionDto, new KundeId(auftragDto.getKundeIIdAuftragsadresse()),
				auftragDto.getTBelegdatum(), theClientDto);
		
		Timestamp ts = new Timestamp(pos.getDate().getTime());
		data.updateDelivery(ts);		
		positionDto.setTUebersteuerbarerLiefertermin(ts) ;
		
		positionDto = (AuftragpositionDto) ediProducer.setPositionReference(positionDto, pos);
		Integer positionId = handleDeliverEntries(pos, auftragDto, positionDto, data, theClientDto);

		return auftragpositionBean.get()
				.auftragpositionFindByPrimaryKeyOhneExc(positionId);
	}
	
	private Integer handleDeliverEntries(OrdersPosition pos, 
			AuftragDto auftragDto, AuftragpositionDto positionDto,
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		Integer positionId = null;
		
		if (pos.getDeliveries().size() > 1) {
			BigDecimal totalQuantity = positionDto.getNMenge();
			String message = textFromToken("auft.edi.position.deliveryschedule.start", 
					theClientDto, totalQuantity, positionDto.getEinheitCNr());
			createPositionText(auftragDto, message, theClientDto);
	
			for (DeliveryEntry deliveryEntry : pos.getDeliveries()) {
				if (deliveryEntry.hasDate()) {
					Timestamp dTs = new Timestamp(deliveryEntry.getDate().getTime());
					data.updateDelivery(dTs);
					positionDto.setTUebersteuerbarerLiefertermin(dTs);
				}
				positionDto.setNMenge(deliveryEntry.getQuantity());
				ediProducer.setPositionReference(positionDto, pos, deliveryEntry);
				Integer newPosId = auftragpositionBean.get()
						.createAuftragposition(positionDto, theClientDto);
				if (positionId == null) {
					positionId = newPosId;
				}

				createPositionBestelltext(pos, auftragDto, theClientDto);
			}

			message = textFromToken("auft.edi.position.deliveryschedule.stop", 
					theClientDto, totalQuantity, positionDto.getEinheitCNr());
			createPositionText(auftragDto, message, theClientDto);
		} else {
			positionId = auftragpositionBean.get()
					.createAuftragposition(positionDto, theClientDto);
			createPositionBestelltext(pos, auftragDto, theClientDto);			
		}

		return positionId;
	}
	
	private void createPositionBestelltext(OrdersPosition pos, 
			AuftragDto auftragDto, TheClientDto theClientDto) throws RemoteException {
		if (!pos.getTexts().isEmpty()) {
			Collection<String> filtered = CollectionTools.select(pos.getTexts(), elem -> {
				if ("0".equals(elem)) return false;
				if (elem.length() == 10 && StringUtils.isNumeric(elem)) return false;
				
				return true;
			});
			if (!filtered.isEmpty()) {
				String orderText = StringUtils.join(filtered.iterator(), "\n");			
				String message = textFromToken("auft.edi.position.ordertext.message", theClientDto, orderText);
				createPositionText(auftragDto, message, theClientDto);				
			}
		}	
	}
	
	private EdifactOrdersImportResult prepareWaehrung(AuftragDto auftragDto, 
			OrdersData data, TheClientDto theClientDto) {
		KundeDto kundeDto = data.repository().buyerAddress().get(0);
		if (data.repository().orderCurrency().isPresent()) {
			String theirCurrency = data.repository().orderCurrency().get();
			if (!kundeDto.getCWaehrung().equals(theirCurrency)) {
				data.add(new WarnEntry<String>(kundeDto.getCWaehrung(), theirCurrency,
						"Die Bestellw\u00e4hrung {1} unterscheidet sich von der " +
						"Kundenw\u00e4hrung {0}, es wird die Kundenw\u00e4hrung verwendet."));
			}
		} else {
			data.add(new WarnEntry<String>(kundeDto.getCWaehrung(), "", 
					"Es wurde keine Bestellw\u00e4hrung \u00fcbermittelt, " +
							"deshalb wird die Kundenw\u00e4hrung {0} verwendet."));
		}
		
		auftragDto.setCAuftragswaehrung(kundeDto.getCWaehrung());
		auftragDto.setWaehrungCNr(kundeDto.getCWaehrung());
		return EdifactOrdersImportResult.Ok;
	}
	
	private EdifactOrdersImportResult prepareAuftrag(AuftragDto auftragDto,
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		auftragDto.setMandantCNr(theClientDto.getMandant());
		auftragDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());

		Date orderDate = data.repository().orderDate().orElse(new Date());
		auftragDto.setTBelegdatum(Helper.cut());
		auftragDto.setDBestelldatum(new Timestamp(orderDate.getTime()));
	    auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
	    auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
	    auftragDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
	    
	    auftragDto.setCBestellnummer(data.repository().issuerOrderNumber().get());			    
	    auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1.0));
	    
	    KundeDto kundeDto = data.repository().buyerAddress().get(0);
	    auftragDto.setKundeIIdAuftragsadresse(kundeDto.getIId());
	    prepareAnsprechpartner(auftragDto, data, theClientDto);

	    auftragDto.setLagerIIdAbbuchungslager(kundeDto.getLagerIIdAbbuchungslager());
		auftragDto.setZahlungszielIId(kundeDto.getZahlungszielIId()); 
		auftragDto.setCBezProjektbezeichnung(kundeDto.getCLieferantennr()) ;

	    prepareRechnungskunde(auftragDto, data, theClientDto);
	    
	    prepareLieferkunde(auftragDto, data, theClientDto);
	    prepareLiefertermine(auftragDto, theClientDto);
	    prepareLieferart(auftragDto, data, theClientDto);
		auftragDto.setSpediteurIId(kundeDto.getSpediteurIId());

		/// TODO: WAS IST HIER LOS???? KostIID vs. KostenstelleIID
		auftragDto.setKostIId(kundeDto.getKostenstelleIId());
		auftragDto.setKostenstelleIId(kundeDto.getKostenstelleIId());
		
		auftragDto.setBTeillieferungMoeglich(kundeDto.getBAkzeptiertteillieferung());
		auftragDto.setBPoenale(Helper.boolean2Short(false));
		auftragDto.setILeihtage(0);
		auftragDto.setFVersteckterAufschlag(new Double(0.0));
		auftragDto.setFAllgemeinerRabattsatz(new Double(0.0));
		auftragDto.setFProjektierungsrabattsatz(new Double(0.0));
		auftragDto.setIGarantie(kundeDto.getIGarantieinmonaten());
		auftragDto.setBRoHs(Helper.boolean2Short(false));

		boolean zusammenfassung = parameterBean.get()
				.getANABMitZusammenfassung(theClientDto.getMandant());
		auftragDto.setBMitzusammenfassung(Helper.boolean2Short(zusammenfassung));
		return EdifactOrdersImportResult.Ok;
	}
	
	private void prepareAnsprechpartner(AuftragDto auftragDto,
			OrdersData data, TheClientDto theClientDto) {
	}
	
	private void prepareRechnungskunde(AuftragDto auftragDto,
			OrdersData data, TheClientDto theClientDto) {
	    auftragDto.setKundeIIdRechnungsadresse(auftragDto.getKundeIIdAuftragsadresse());
	    
	    int accounts = data.repository().invoiceAddress().size();
	    if (accounts < 1) {
	    	// TODO PJ21490 Nichts ueber die Kennung gefunden, nach der Adresse suchen
	    	data.add(new WarnEntry<Integer>(1, accounts, 
	    			"Rechnungskunde: Es wurden keine Kunden mit der Kennung '" + 
	    					data.repository().invoiceIdentifier() + "' gefunden." +
	    			"Es wird der Auftragskunde verwendet!"));
	    	NadInfo nadInfo = data.repository.getInvoiceNadParties().get(0);
	    	data.add(new ErrorEntry<Integer>(1, accounts,
	    			"Die Adresssuche des Rechnungskunden muss noch implementiert werden\n" + 
	    			nadInfoToString(nadInfo)));
	    	return;
	    }

	    if (accounts > 1) {
	    	StringBuffer sb = new StringBuffer("\nGefundene Rechnungskunden:");
	    	for (KundeDto kundeDto : data.repository().invoiceAddress()) {
	    		PartnerDto pDto = kundeDto.getPartnerDto();
				sb.append("\n" + pDto.formatFixName1Name2() + "\n" + pDto.formatAdresse() + "\n");
			}
	    	data.add(new WarnEntry<Integer>(1, accounts, 
	    			"Es wurden {1} Kunden mit gleichen Kennung '" + 
	    					data.repository().invoiceIdentifier() + "' gefunden." +
	    			"Es wird der Auftragskunde verwendet!\n" +
	    			"Folgende Kunden wurden gefunden:\n" + sb.toString()));
	    	return;
	    }
	    
	    auftragDto.setKundeIIdRechnungsadresse(
	    		data.repository().invoiceAddress().get(0).getIId());
	}
	
	private String nadInfoToString(NadInfo nadInfo) {
		if (nadInfo == null) return "Keine Adresse vorhanden";
		
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.join(nadInfo.getPartyNames().iterator(), "\n")).append("\n");
		sb.append(StringUtils.join(nadInfo.getNames().iterator(), "\n")).append("\n");
		sb.append(StringUtils.join(nadInfo.getStreets().iterator(), "\n")).append("\n");
		sb.append(Helper.formatString(nadInfo.getCountryNameCode())).append("\n");
		sb.append(Helper.formatString(nadInfo.getPostalIdentifcationCode())).append("\n");
		sb.append(Helper.formatString(nadInfo.getCityName())).append("\n");
		return sb.toString();
	}
	
	private void prepareLieferkunde(AuftragDto auftragDto, 
			OrdersData data, TheClientDto theClientDto) {
	    auftragDto.setKundeIIdLieferadresse(auftragDto.getKundeIIdAuftragsadresse());
		
	    int accounts = data.repository().deliveryAddress().size();
	    if (accounts < 1) {
	    	// TODO PJ21490 Nichts ueber die Kennung gefunden, nach der Adresse suchen
	    	data.add(new WarnEntry<Integer>(1, accounts, 
	    			"Lieferkunde: Es wurden keine Kunden mit der Kennung '" + 
	    					data.repository().deliveryIdentifier() + "' gefunden." +
	    			"Es wird der Auftragskunde verwendet!"));
	    	NadInfo nadInfo = data.repository.getDeliveryNadParties().get(0);
	    	data.add(new ErrorEntry<Integer>(1, accounts, 
	    			"Die Adresssuche des Lieferkunden muss noch implementiert werden\n" + 
	    			nadInfoToString(nadInfo)));
	    	return;
	    }
	    
	    if (accounts > 1) {
	    	StringBuffer sb = new StringBuffer("\nGefundene Lieferkunden:");
	    	for (KundeDto kundeDto : data.repository().deliveryAddress()) {
	    		PartnerDto pDto = kundeDto.getPartnerDto();
				sb.append("\n" + pDto.formatFixName1Name2() + "\n" + pDto.formatAdresse() + "\n");
			}
	    	data.add(new WarnEntry<Integer>(1, accounts, 
	    			"Es wurden {1} Kunden mit gleichen Kennung '" + 
	    					data.repository().deliveryIdentifier() + "' gefunden." +
	    			"Es wird der Auftragskunde verwendet!\n" +
	    			"Folgende Kunden wurden gefunden:\n" + sb.toString()));
	    	return;
	    } 
	    
	    auftragDto.setKundeIIdLieferadresse(
	    		data.repository().deliveryAddress().get(0).getIId());
	}
	
	private void prepareLiefertermine(AuftragDto auftragDto, TheClientDto theClientDto) {		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(auftragDto.getTBelegdatum().getTime());
		c.add(Calendar.DAY_OF_MONTH, getParameterDefaultLieferzeitAuftrag(theClientDto));		
		Timestamp t = new Timestamp(c.getTimeInMillis()) ;
		
	    auftragDto.setDLiefertermin(t);
	    auftragDto.setDFinaltermin(t);
	    auftragDto.setBLieferterminUnverbindlich(Helper.boolean2Short(false));		
	}
	
	private int getParameterDefaultLieferzeitAuftrag(TheClientDto theClientDto) {
		int defaultLieferzeitAuftrag = 0 ;
		
		try {
			defaultLieferzeitAuftrag = parameterBean.get()
					.getDefaultAuftragsLieferzeitTage(theClientDto.getMandant());
		} catch(NumberFormatException e) {
		}
		return defaultLieferzeitAuftrag ;
	}
	
	private void prepareLieferart(AuftragDto auftragDto,
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
	    auftragDto.setLieferartIId(getValidLieferart(data, theClientDto));	
	}
	
	private Integer getValidLieferart(OrdersData data,
			TheClientDto theClientDto) throws RemoteException {
		Lieferart l = null ;
		try {
			if (data.repository().transportationCode().isPresent()) {
				Query query = LieferartQuery.byCnrMandantCnr(
						em, data.repository().transportationCode().get(), theClientDto.getMandant());
				l = (Lieferart) query.getSingleResult();
				return l.getIId();
			}
		} catch(NoResultException e) {
		} catch(NonUniqueResultException e) {
		}

		KundeDto kundeDto = data.repository().buyerAddress().get(0);
		LieferartDto lieferartDto = localeBean.get()
				.lieferartFindByPrimaryKey(kundeDto.getLieferartIId(), theClientDto);
		data.add(new WarnEntry<String>(
				lieferartDto.getCNr() + " " + 
						lieferartDto.getLieferartsprDto().getCBezeichnung(),
						data.repository().transportationCode().orElse("''"), 
					"Lieferart {1} ist unbekannt. Es wurde stattdessen {0} verwendet."));

		return kundeDto.getLieferartIId();			
	}

	private void reportDefaultErrorMessage(String tokenSubject,
			String tokenText, OrdersData data, TheClientDto theClientDto,
			Object... textValues) throws RemoteException {
		String subject = textFromToken(tokenSubject, theClientDto);
		String text = textFromToken(tokenText, theClientDto, textValues);
		postMessage(subject, text, theClientDto, data.originalMsg().getBytes());
		archiveErrorEdiDocument(data, theClientDto);		
	}
	
	private void reportNewOrder(AuftragDto auftragDto, 
			OrdersData data, TheClientDto theClientDto) throws RemoteException {
		String subject = textFromToken(
				"auft.edi.email.new.header", theClientDto, auftragDto.getCNr());
		String text = textFromToken(
				"auft.edi.email.new.message", theClientDto,
				auftragDto.getCNr(), auftragDto.getCBestellnummer(), 
				auftragDto.getDLiefertermin().toString(),
				auftragDto.getDFinaltermin().toString());
		postMessage(subject, text, theClientDto, data.originalMsg().getBytes());
		archiveEdiDocument(auftragDto, data, theClientDto);				
	}

	@Override
	public void reportEdifactException(String edifactContent, 
			EdifactException ex, TheClientDto theClientDto) {
		try {
			OrdersData ordersData = new OrdersData(edifactContent);
			reportDefaultErrorMessage(
					"auft.edi.email.edifactexception.header", 
					"auft.edi.email.edifactexception.message",
					ordersData, theClientDto, ex.getMessage());
		} catch (RemoteException e) {
			log.error("RemoteException bei Benachrichtigung der EdifactException", e);
		}
	}
	
	@Override
	public void reportApplicationException(String edifactContent, 
			Exception ex, TheClientDto theClientDto) {
		OrdersData ordersData = new OrdersData(edifactContent);
		try {
			reportDefaultErrorMessage(
					"auft.edi.email.serverexception.header", 
					"auft.edi.email.serverexception.message",
					ordersData, theClientDto, ex.getMessage());							
		} catch(Exception e) {
			log.error("Exception bei Benachrichtigung der ApplicationException", e);			
		}
	}
	
	private VersandauftragDto postMessage(String subject,
			String message, TheClientDto theClientDto,
			byte[]... attachments) throws RemoteException {
		VersandauftragDto dto = new VersandauftragDto();
		dto.setCBetreff(subject);
		dto.setCText(message);

		MandantDto mandantDto = mandantBean.get().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		
		String absender = parameterBean.get()
				.getMailadresseAdmin(theClientDto.getMandant());
		if (Helper.isStringEmpty(absender)) {
			if (mandantDto.getPartnerDto() != null) {
				absender = mandantDto.getPartnerDto().getCEmail();
			}
		}
		
		if (Helper.isStringEmpty(absender)) {
			absender = "admin@heliumv.com";
		}
		dto.setCAbsenderadresse(absender);
		
		String empfaenger = absender;
		PersonalDto personalDto = personalBean.get()
				.personalFindByPrimaryKey(
						theClientDto.getIDPersonal(), theClientDto);
		if (!Helper.isStringEmpty(personalDto.getCEmail())) {
			empfaenger = personalDto.getCEmail();			
		} else {
			log.warn("Im Personal '" + personalDto
					.getPartnerDto().formatFixTitelName1Name2() +
					"' (" + personalDto.getCKurzzeichen() + 
					") ist keine E-Mail Adresse hinterlegt, es wird '" + 
					empfaenger + "' verwendet.");
		}
		dto.setCEmpfaenger(empfaenger);
		
		if (attachments == null) {
			return versandBean.get()	
				.createVersandauftrag(dto, false, theClientDto);
		}
		
		List<VersandanhangDto> anhaenge = new ArrayList<VersandanhangDto>();
		Integer index = 0;
		for (byte[] attachment : attachments) {
			VersandanhangDto anhangDto = new VersandanhangDto();
			anhangDto.setCDateiname("Anhang_" + index.toString());
			anhangDto.setOInhalt(attachment);
			anhaenge.add(anhangDto);
			index++;
		}
		return versandBean.get()
				.createVersandauftrag(dto, anhaenge, true, theClientDto);
	}

	private void archiveErrorEdiDocument(OrdersData data, TheClientDto theClientDto) {		
		PersonalDto personalDto = personalBean.get()
				.personalFindByPrimaryKey(
						theClientDto.getIDPersonal(), theClientDto);

		Date importDate = new Date(System.currentTimeMillis());
		DocPath dp = new DocPath(new DocNodeEdifactOrders(
				personalDto, importDate))
				.add(new DocNodeFile("Import_Edifact.txt"));
		JCRDocDto jcrDocDto = new JCRDocDto();
		jcrDocDto.setDocPath(dp) ;
		jcrDocDto.setbData(data.originalMsg().getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(personalDto.getPartnerIId());
		jcrDocDto.setlPartner(personalDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(importDate.getTime());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		
		jcrDocDto.setsBelegnummer(data.repository()
				.issuerOrderNumber().orElse("Ohne Belegnummer"));
		jcrDocDto.setsFilename("Import_Edifact.txt");
		jcrDocDto.setsMIME(".txt");
		jcrDocDto.setsName("Import Edifact");
		jcrDocDto.setsRow(personalDto.getIId().toString());
		jcrDocDto.setsTable("PERSONAL");
		String sSchlagworte = "Import Edifact EDI Orders";
		jcrDocDto.setsSchlagworte(sSchlagworte);
		jcrDocBean.get().addNewDocumentOrNewVersionOfDocumentWithinTransaction(
				jcrDocDto, theClientDto);
	}
	
	private void archiveEdiDocument(AuftragDto auftragDto, 
			OrdersData data, TheClientDto theClientDto) {
		PersonalDto personalDto = personalBean.get()
				.personalFindByPrimaryKey(
						theClientDto.getIDPersonal(), theClientDto);
		KundeDto kundeDto = kundeBean.get().kundeFindByPrimaryKey(
				auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
		
		DocPath dp = auftragBean.get().getDocPathEdiImportFile(auftragDto);

		JCRDocDto jcrDocDto = new JCRDocDto();
		jcrDocDto.setDocPath(dp) ;
		jcrDocDto.setbData(data.originalMsg().getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(personalDto.getPartnerIId());	
		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);

		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(auftragDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(auftragDto.getCNr().replace("/", "."));
		jcrDocDto.setsMIME(".txt");
		jcrDocDto.setsName("Import Edifact " + auftragDto.getIId());
		jcrDocDto.setsRow(auftragDto.getIId().toString());
		jcrDocDto.setsTable("AUFTRAG");
		String sSchlagworte = "Import Edifact EDI Orders " + auftragDto.getCBestellnummer();
		jcrDocDto.setsSchlagworte(sSchlagworte);
		jcrDocBean.get().addNewDocumentOrNewVersionOfDocumentWithinTransaction(
				jcrDocDto, theClientDto);
	}
	
	/**
	 * Aus einem EDI-Inhalt die Kennung der NAD+<Qualifier>Info auslesen
	 * 
	 * <p>Es wird davon ausgegangen, dass es sich bei diesem ediContent um
	 * einen bereits validierten Inhalt handelt (sprich: Wir haben daraus
	 * schon mal einen Auftrag erstellen koennen.</p>
	 * <p>Es wird auch davon ausgegangen, dass die Standard-Steuerkennzeichen
	 * wie + und : im ediContent verwendet werden, da wir uns - 
	 * derzeit noch ersparen wollen - die Datei nochmals zu parsen</p>
	 * 
	 * <p>Es wird die <b>erste</b> Party-Info ausgelesen. Der id.-Teil 
	 * wird komplett zurueckgegeben. Beispiel: Aus "NAD+BY+7123::92+++" 
	 * wird dann 7123::92 ermittelt</p>
	 * 
	 *  
	 * @param ediContent
	 * @param nadPartyQualifier zum Beispiel "DP", "IV" oder "BY"
	 * @return die - falls vorhanden - Party id. identification
	 */
	@Override
	public HvOptional<String> extractNADPartyIdentification(String ediContent, String nadPartyQualifier) {
		if (ediContent == null) return HvOptional.empty();

		String search = "NAD+" + nadPartyQualifier + "+";
		Integer startIndex = ediContent.indexOf(search);
		if (startIndex == -1) return HvOptional.empty();
		startIndex += search.length();
		Integer endIndex = ediContent.indexOf('+', startIndex);

		if (endIndex == -1) {
			return HvOptional.empty();
		}
		
		return HvOptional.of(ediContent.substring(startIndex, endIndex));
	}

	@Override
	public KundeKennung filterOursWithEdiId(List<KundeKennung> kks, String ediIdString) {
		final String firstTry = ediIdString;
		KundeKennung kk = CollectionTools.detect(kks,
				k -> k.getCWert().equals(firstTry));
		if (kk == null && ediIdString.contains(":")) {
			final String secondTry = ediIdString.substring(0, ediIdString.indexOf(":"));
			kk = CollectionTools.detect(kks,
					k -> k.getCWert().equals(secondTry));
		}
		return kk;
	}

	class PriceCalculator {
		private final KundeDto kundeDto;
		private final java.sql.Date calcDate;
		private final VkPreisfindungBean preisfindungBean = new VkPreisfindungBean();
		private final String zielWaehrungCnr;
		
		public PriceCalculator(AuftragDto auftragDto, KundeDto kundeDto) {
			this.kundeDto = kundeDto;
			this.calcDate = new java.sql.Date(auftragDto.getDBestelldatum().getTime());
			this.zielWaehrungCnr = auftragDto.getCAuftragswaehrung();
		}
		
		private BigDecimal getMinimumPrice(BigDecimal minimum, VerkaufspreisDto priceDto) {
			if(priceDto != null && priceDto.nettopreis != null) {
				return null == minimum ? priceDto.nettopreis : minimum.min(priceDto.nettopreis) ; 
			}
			
			return minimum;		
		}
		
		private BigDecimal getPriceFromPreisfindung(VkpreisfindungDto vkPreisDto) {
			BigDecimal p = getMinimumPrice(null, vkPreisDto.getVkpStufe3()) ;
			if(p != null) return p ;

			p = getMinimumPrice(null, vkPreisDto.getVkpStufe2()) ;
			if(p != null) return p ;

			p = getMinimumPrice(null, vkPreisDto.getVkpStufe1()) ;
			if(p != null) return p ;

			return getMinimumPrice(null, vkPreisDto.getVkpPreisbasis())  ;
		}
		
		public BigDecimal calculate(ArtikelDto itemDto, 
				BigDecimal quantity, Integer mwstsatzId, TheClientDto theClientDto) {
			VkpreisfindungDto vkpreisfindungDto = preisfindungBean.get().verkaufspreisfindung(
					itemDto.getIId(), kundeDto.getIId(), quantity, 
					calcDate, kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), 
					mwstsatzId, zielWaehrungCnr, theClientDto);
			BigDecimal p = getPriceFromPreisfindung(vkpreisfindungDto);
			return p;
		}
	}
	
	
	class MwstsatzCalculator {
		private final boolean mwstsatzAusArtikel;
		private final Timestamp belegDatum;
		private final MwstsatzDto mwstsatzDesKunden;
		private final MwstsatzCache mwstsatzCache;
		
		public MwstsatzCalculator(TheClientDto theClientDto, MwstsatzbezId bezId, Timestamp belegDatum) {
			this.belegDatum = belegDatum;
			mwstsatzAusArtikel = parameterBean.get()
					.getPositionskontierung(theClientDto.getMandant());
			mwstsatzCache = new MwstsatzCache();
			mwstsatzDesKunden = mwstsatzCache.getValueOfKey(bezId.id());
		}
		
		public HvOptional<Integer> getMwstsatzId(ArtikelDto itemDto) {
			if(!mwstsatzAusArtikel) {
				return HvOptional.of(mwstsatzDesKunden.getIId());				
			}

			// Wenn Kundensteuersatz = steuerfrei, dann gilt immer der Kundensteuersatz
			if (mwstsatzDesKunden.getFMwstsatz().doubleValue() == 0) {
				return HvOptional.of(mwstsatzDesKunden.getIId());
			}
			 
			if (itemDto.getMwstsatzbezIId() != null) {
				return HvOptional.of(
						mwstsatzCache.getValueOfKey(itemDto.getMwstsatzbezIId()).getIId());
			}
			
			return HvOptional.empty();
		}
		
		class MwstsatzCache extends HvCreatingCachingProvider<Integer, MwstsatzDto> {
			@Override
			protected MwstsatzDto provideValue(Integer key, Integer transformedKey) {
				return mandantBean.get().mwstsatzFindZuDatum(key, belegDatum);
			}
		}
	}
	
	
	class EinheitenCache extends HvCreatingCachingProvider<String, HvOptional<EinheitDto>> {
		private final SystemBean systemBean = new SystemBean();
		private final TheClientDto theClientDto;
		
		public EinheitenCache(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
		}
		
		@Override
		protected HvOptional<EinheitDto> provideValue(String key, String transformedKey) {
			try {
				return HvOptional.ofNullable(systemBean.get().
					einheitFindByPrimaryKeyOhneExc(key, theClientDto));
			} catch(RemoteException e) {
				return HvOptional.empty();
			}
		}		
	}
	
	
	class OrdersData {
		private final String originalMessage;
		private final EdifactMessage msg;
		private final OrdersRepository repository;
		private final List<ChangeEntry<?>> changeEntries = new ArrayList<ChangeEntry<?>>();
		private Timestamp earliestDelivery;
		private Timestamp latestDelivery;
		
		public OrdersData(String originalMessage) {
			this(originalMessage, null, new OrdersRepository());

		}
		
		public OrdersData(String originalMessage, 
				EdifactMessage msg, OrdersRepository repository) {
			this.originalMessage = originalMessage;
			this.msg = msg;
			this.repository = repository;
		}
		
		public EdifactMessage msg() {
			return msg;
		}
		
		public String originalMsg() {
			return originalMessage;
		}
		
		public OrdersRepository repository() {
			return repository;
		}
		
		public void add(ChangeEntry<?> changeEntry) {
			changeEntries.add(changeEntry);
		}
		
		public void clear() {
			changeEntries.clear();
		}
		
		public List<ChangeEntry<?>> changeEntries() {
			return changeEntries;
		}
		
		public void updateEarliestDelivery(Timestamp timestamp) {
			if(earliestDelivery == null || earliestDelivery.after(timestamp)) {
				earliestDelivery = timestamp ;
			}
		}
		
		public void updateLatestDelivery(Timestamp timestamp) {
			if(null == latestDelivery || latestDelivery.before(timestamp)) {
				latestDelivery = timestamp ;
			}
		}
		
		public Timestamp earliestDelivery() {
			return this.earliestDelivery;
		}
		
		public Timestamp latestDelivery() {
			return this.latestDelivery;
		}
		
		public void updateDelivery(Timestamp timestamp) {
			updateEarliestDelivery(timestamp);
			updateLatestDelivery(timestamp);
		}
	}
}
