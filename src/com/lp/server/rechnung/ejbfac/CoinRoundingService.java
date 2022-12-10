package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Stateless;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.CoinRoundingResult;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.BelegSteuerDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.IDetect;
import com.lp.server.util.report.MwstsatzReportDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class CoinRoundingService extends Facade implements CoinRoundingServiceFac {

//	@PersistenceContext
//	private EntityManager em;

	@Override
	public CoinRoundingInfo createInfo(RechnungDto rechnungDto, TheClientDto theClientDto) {
		Integer kundeIId = rechnungDto.getKundeIId();
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
		return getInfo(kundeDto.getPartnerDto(), rechnungDto.getWaehrungCNr());
	}

	@Override
	public CoinRoundingInfo createInfo(EingangsrechnungDto erDto, TheClientDto theClientDto) {
		Integer lieferantId = erDto.getLieferantIId();
		if (lieferantId == null) {
			myLogger.info("ER lieferantId == null, assuming no rounding");
			return getInfo(null, erDto.getWaehrungCNr());
		}
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantId, theClientDto);
		return getInfo(lieferantDto.getPartnerDto(), erDto.getWaehrungCNr());
	}

	protected CoinRoundingInfo getInfo(PartnerDto partnerDto, String belegWaehrungCnr) {
		if (null == partnerDto)
			return new CoinRoundingInfo();

		LandplzortDto landplzOrtDto = partnerDto.getLandplzortDto();
		if (null == landplzOrtDto)
			return new CoinRoundingInfo();

		LandDto landDto = landplzOrtDto.getLandDto();
		return new CoinRoundingInfo(landDto, belegWaehrungCnr);
	}

	@Override
	public boolean isRoundingNeeded(RechnungDto rechnungDto, TheClientDto theClientDto) {
		if (rechnungDto.isAnzahlungsRechnung())
			return false;

		CoinRoundingInfo info = createInfo(rechnungDto, theClientDto);
		return info.needsRounding();
	}

	@Override
	public boolean isRoundingNeeded(EingangsrechnungDto erDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(erDto, "erDto");
		CoinRoundingInfo info = createInfo(erDto, theClientDto);
		return info.needsRounding();
	}

	@Override
	public CoinRoundingResult calcMwstBetragFromBrutto(EingangsrechnungDto erDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(erDto, "erDto");
		MwstsatzDto satzDto = getMandantFac().mwstsatzFindByPrimaryKey(erDto.getMwstsatzIId(), theClientDto);
		CoinRoundingInfo info = createInfo(erDto, theClientDto);
		CoinRoundingResult result = createResult(info, satzDto);
		result.setValidBruttoAmount(true);
		result.setTaxAmount(calcSimpleMwstBetrag(erDto.getNBetrag(), result));
		if (erDto.getNBetragfw() != null) {
			result.setTaxAmountFW(calcSimpleMwstBetrag(erDto.getNBetragfw(), result));
		}

		if (info.needsRounding()) {
			CoinRounding rounder = new CoinRounding(info.getValue());
			rounder.setRoundUp(!info.isRoundDown());

			BigDecimal brutto = erDto.getNBetrag();
			BigDecimal roundedBrutto = rounder.round(brutto);
			result.setValidBruttoAmount(roundedBrutto.compareTo(brutto) == 0);

			if (info.isRoundTax() && result.isValidBruttoAmount()) {
				result.setTaxAmount(rounder.round(result.getTaxAmount()));
				if (result.getTaxAmountFW() != null) {
					result.setTaxAmountFW(rounder.round(result.getTaxAmountFW()));
				}
			}
		}

		return result;
	}

	@Override
	public CoinRoundingResult calcMwstBetragFromNetto(EingangsrechnungDto erDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(erDto, "erDto");
		MwstsatzDto satzDto = getMandantFac().mwstsatzFindByPrimaryKey(erDto.getMwstsatzIId(), theClientDto);
		CoinRoundingInfo info = createInfo(erDto, theClientDto);
		CoinRoundingResult result = createResult(info, satzDto);
		CoinRounding rounder = new CoinRounding(info.getValue());
		rounder.setRoundUp(!info.isRoundDown());

		BigDecimal ust = Helper.getProzentWert(erDto.getNBetrag(), new BigDecimal(satzDto.getFMwstsatz()),
				FinanzFac.NACHKOMMASTELLEN);
		result.setTaxAmount(ust);

		BigDecimal ustFW =null;
		if (erDto.getNBetragfw() != null) {
			ustFW = Helper.getProzentWert(erDto.getNBetragfw(), new BigDecimal(satzDto.getFMwstsatz()),
					FinanzFac.NACHKOMMASTELLEN);
			result.setTaxAmountFW(ustFW);
		}

		BigDecimal brutto = erDto.getNBetrag().add(ust);
		if (info.needsRounding()) {
			if (info.isRoundTax()) {
				BigDecimal roundedUst = rounder.round(ust);
				result.setTaxAmount(roundedUst);
				
				if(ustFW!=null) {
					BigDecimal roundedUstFW = rounder.round(ustFW);
					result.setTaxAmountFW(roundedUstFW);
				}
				
				ust = roundedUst;
				brutto = erDto.getNBetrag().add(ust);
			}

			brutto = rounder.round(brutto);
		}

		result.setValidBruttoAmount(true);
		result.setBruttoAmount(brutto);
		return result;
	}

	private CoinRoundingResult createResult(CoinRoundingInfo info, MwstsatzDto satzDto) {
		CoinRoundingResult result = new CoinRoundingResult();
		result.setNeedsRounding(info.needsRounding());
		result.setNeedsTaxRounding(info.isRoundTax());
		result.setMwstsatzDto(satzDto);
		return result;
	}

	private BigDecimal calcSimpleMwstBetrag(BigDecimal brutto, CoinRoundingResult result) {
		return Helper.getMehrwertsteuerBetrag(brutto, result.getMwstsatzDto().getFMwstsatz());
	}

	private boolean hasRechnungRounding(RechnungDto rechnungDto) {
		return !rechnungDto.isAnzahlungsRechnung();
	}

	@Override
	public void round(RechnungDto rechnungDto, BelegpositionVerkaufDto[] posDtos, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		Validator.dtoNotNull(rechnungDto, "rechnungDto");

		if (!hasRechnungRounding(rechnungDto))
			return;

		CoinRoundingInfo info = createInfo(rechnungDto, theClientDto);
		if (!info.needsRounding())
			return;

		CoinRounding rounder = new CoinRounding(info.getValue());
		rounder.setRoundUp(!info.isRoundDown());

		BigDecimal brutto = rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw());
		BigDecimal bruttoRounded = rounder.round(brutto);

		BigDecimal diff = bruttoRounded.subtract(brutto);
		BigDecimal taxDiff = BigDecimal.ZERO;
		if (info.isRoundTax()) {
			BelegSteuerDto steuerDto = getBelegVerkaufFac().getNettoGesamtWertUSTInfo(posDtos, rechnungDto,
					FinanzFac.NACHKOMMASTELLEN, theClientDto);
			for (MwstsatzReportDto reportDto : steuerDto) {
				BigDecimal sum = reportDto.getNSummeMwstbetrag();
				BigDecimal roundedSum = rounder.round(sum);
				taxDiff = taxDiff.add(roundedSum).subtract(sum);
			}
		}

		if (diff.signum() == 0 && taxDiff.signum() == 0)
			return;
		BigDecimal d = updateRechnungswerte(rechnungDto, diff, taxDiff);

		final ArtikelDto artikelDto = getItemIIdForRounding(theClientDto);
		if (null == artikelDto) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT,
					"Rundungsartikel nicht definiert");
		}

		removeRoundingPosition(posDtos, artikelDto, theClientDto);

		if (d.signum() != 0) {
			createRoundingPosition(rechnungDto, d, artikelDto, theClientDto);
		}
	}

	public BigDecimal roundUst(BelegVerkaufDto belegDto, BigDecimal ust, TheClientDto theClientDto) {
		Validator.dtoNotNull(belegDto, "belegDto");
		if (!(belegDto instanceof RechnungDto))
			return ust;

		RechnungDto rechnungDto = (RechnungDto) belegDto;
		if (!hasRechnungRounding(rechnungDto))
			return ust;

		CoinRoundingInfo info = createInfo(rechnungDto, theClientDto);
		if (!info.needsRounding())
			return ust;

		if (info.isRoundTax()) {
			CoinRounding rounder = new CoinRounding(info.getValue());
			rounder.setRoundUp(!info.isRoundDown());

			return rounder.round(ust);
		}

		return ust;
	}

	private BigDecimal updateRechnungswerte(RechnungDto rechnungDto, BigDecimal bruttoDiff, BigDecimal taxDiff) {
		BigDecimal originalNetto = rechnungDto.getNWertfw();
		BigDecimal originalUst = rechnungDto.getNWertustfw();
		BigDecimal originalBrutto = originalNetto.add(originalUst);
//		rechnungDto.setNWertfw(originalBrutto.add(bruttoDiff)) ;

		BigDecimal newBrutto = originalBrutto.add(bruttoDiff);
		BigDecimal newUst = originalUst.add(taxDiff);
		rechnungDto.setNWertustfw(newUst);
		BigDecimal newNetto = newBrutto.subtract(newUst);
		rechnungDto.setNWertfw(newNetto);
		BigDecimal d = newNetto.subtract(originalNetto);

		BigDecimal bdWert = rechnungDto.getNWertfw().divide(rechnungDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN,
				BigDecimal.ROUND_HALF_EVEN);
		rechnungDto.setNWert(bdWert);
		return d;
	}

	private void createRoundingPosition(RechnungDto rechnungDto, BigDecimal diff, final ArtikelDto artikelDto,
			TheClientDto theClientDto) throws RemoteException {
		BelegpositionVerkaufDto belegDto = new BelegpositionVerkaufDto();
		belegDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		belegDto.setArtikelIId(artikelDto.getIId());
		belegDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		belegDto.setBMwstsatzuebersteuert(Helper.getShortFalse());
		belegDto.setBRabattsatzuebersteuert(Helper.getShortFalse());
		belegDto.setBelegIId(rechnungDto.getIId());
		belegDto.setEinheitCNr(artikelDto.getEinheitCNr());
		if (artikelDto.getMwstsatzbezIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_KEIN_MWSTSATZ_DEFINIERT,
					"Artikel '" + artikelDto.getCNr() + "' hat keinen Mwstsatz definiert");
		}

		MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindZuDatum(artikelDto.getMwstsatzbezIId(),
				rechnungDto.getTBelegdatum());
		belegDto.setMwstsatzIId(mwstsatzDto.getIId());

		// belegDto.setKostentraegerIId(rechnungDto.getK) ; // vorerst mal nicht setzen,
		// 23.2.2012
		RechnungPositionDto rundungsposDto = new RechnungPositionDto(belegDto);

		/*
		 * Wir gehen hier vorerst mal davon aus, dass es keine MwSt fuer den Artikel
		 * gibt. Daher ist brutto == netto
		 */
		rundungsposDto.setNMenge(new BigDecimal("1"));
		rundungsposDto.setBNettopreisuebersteuert(Helper.getShortTrue());
		rundungsposDto.setNEinzelpreis(diff);
		rundungsposDto.setNEinzelpreisplusversteckteraufschlag(diff);
		rundungsposDto.setNNettoeinzelpreis(diff);
		rundungsposDto.setNBruttoeinzelpreis(diff);
		rundungsposDto.setFRabattsatz(new Double(0));
		rundungsposDto.setFZusatzrabattsatz(new Double(0));
		getRechnungFac().createRechnungPosition(rundungsposDto, rechnungDto.getLagerIId(), theClientDto);
	}
	
	
	public void createRoundingPositionForAuftrag(AuftragDto auftragDto, BigDecimal diff, 
			TheClientDto theClientDto) throws RemoteException {
		
		final ArtikelDto artikelDto = getItemIIdForRounding(theClientDto);
		if (null == artikelDto) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT,
					"Rundungsartikel nicht definiert");
		}

		
		BelegpositionVerkaufDto belegDto = new BelegpositionVerkaufDto();
		belegDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		belegDto.setArtikelIId(artikelDto.getIId());
		belegDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		belegDto.setBMwstsatzuebersteuert(Helper.getShortFalse());
		belegDto.setBRabattsatzuebersteuert(Helper.getShortFalse());
		belegDto.setBelegIId(auftragDto.getIId());
		belegDto.setEinheitCNr(artikelDto.getEinheitCNr());
		if (artikelDto.getMwstsatzbezIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_KEIN_MWSTSATZ_DEFINIERT,
					"Artikel '" + artikelDto.getCNr() + "' hat keinen Mwstsatz definiert");
		}

		MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindZuDatum(artikelDto.getMwstsatzbezIId(),
				auftragDto.getTBelegdatum());
		belegDto.setMwstsatzIId(mwstsatzDto.getIId());

		
		AuftragpositionDto rundungsposDto = new AuftragpositionDto(belegDto);

		/*
		 * Wir gehen hier vorerst mal davon aus, dass es keine MwSt fuer den Artikel
		 * gibt. Daher ist brutto == netto
		 */
		rundungsposDto.setNMenge(new BigDecimal("1"));
		rundungsposDto.setBNettopreisuebersteuert(Helper.getShortTrue());
		rundungsposDto.setNEinzelpreis(diff);
		rundungsposDto.setNEinzelpreisplusversteckteraufschlag(diff);
		rundungsposDto.setNNettoeinzelpreis(diff);
		rundungsposDto.setNBruttoeinzelpreis(diff);
		rundungsposDto.setFRabattsatz(new Double(0));
		rundungsposDto.setFZusatzrabattsatz(new Double(0));
		
		rundungsposDto.setTUebersteuerbarerLiefertermin(auftragDto.getDLiefertermin());
		
		getAuftragpositionFac().createAuftragposition(rundungsposDto, theClientDto);
	}
	

	private BelegpositionVerkaufDto findPositionWithArtikelId(BelegpositionVerkaufDto[] posDtos,
			final ArtikelDto artikelDto) {
		BelegpositionVerkaufDto rundungsposDto = CollectionTools.detect(posDtos,
				new IDetect<BelegpositionVerkaufDto>() {
					public boolean accept(BelegpositionVerkaufDto element) {
						return artikelDto.getIId().equals(element.getArtikelIId());
					};
				});
		return rundungsposDto;
	}

	/**
	 * Eine etwaige vorhande Rundungsposition aus der Rechnung entfernen.
	 * 
	 * @param rePosDto
	 * @param artikelIId
	 * @param theClientDto
	 * @return true wenn eine Rundungsposition gefunden werden konnte
	 * @throws RemoteException
	 */
	public boolean removeRoundingPosition(BelegpositionVerkaufDto[] rePosDto, ArtikelDto artikelDto,
			TheClientDto theClientDto) throws RemoteException {
		BelegpositionVerkaufDto posDto = findPositionWithArtikelId(rePosDto, artikelDto);
		if (posDto != null) {
			getRechnungFac().removeRechnungPosition((RechnungPositionDto) posDto, theClientDto);
		}

		return posDto != null;
	}

	public boolean removeRoundingPosition(BelegpositionVerkaufDto[] dtos, TheClientDto theClientDto)
			throws RemoteException {
		try {
			ArtikelDto artikelDto = getItemIIdForRounding(theClientDto);
			if (null == artikelDto)
				return false;
			return removeRoundingPosition(dtos, artikelDto, theClientDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT,
					"Rundungsartikel nicht definiert");
		}
	}

	private ArtikelDto getItemIIdForRounding(TheClientDto theClientDto) throws RemoteException {
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_RUNDUNGSAUSGLEICH_ARTIKEL);
			String artikelCnr = parameterDto.getCWert();
			return getArtikelFac().artikelFindByCNrOhneExc(artikelCnr, theClientDto);
		} catch (Exception e) {
			return null;
		}
	}
}
