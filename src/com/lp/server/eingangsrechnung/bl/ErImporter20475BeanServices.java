package com.lp.server.eingangsrechnung.bl;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.UstUebersetzungDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.service.CoinRoundingResult;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.MwstsatzbezId;
import com.lp.util.Helper;

public class ErImporter20475BeanServices {

	private EingangsrechnungFac erFac;
	private LieferantFac lieferantFac;
	private FinanzFac finanzFac;
	private ParameterFac parameterFac;
	private PartnerFac partnerFac;
	private MandantFac mandantFac;
	private LocaleFac localeFac;
	private TheClientDto theClientDto;
	private FinanzServiceFac finanzServiceFac;
	private BuchenFac buchenFac;
	private SystemFac systemFac;
	
	private Integer erLiefnrLaenge = null;
	private Integer stellenGj = null;
	
	public ErImporter20475BeanServices(EingangsrechnungFac erFac, LieferantFac lieferantFac, FinanzFac finanzFac,
			PartnerFac partnerFac, MandantFac mandantFac, LocaleFac localeFac, FinanzServiceFac finanzServiceFac,
			ParameterFac parameterFac, BuchenFac buchenFac, SystemFac systemFac, TheClientDto theClientDto) {
		this.erFac = erFac;
		this.lieferantFac = lieferantFac;
		this.finanzFac = finanzFac;
		this.partnerFac = partnerFac;
		this.mandantFac = mandantFac;
		this.localeFac = localeFac;
		this.finanzServiceFac = finanzServiceFac;
		this.parameterFac = parameterFac;
		this.buchenFac = buchenFac;
		this.systemFac = systemFac;
		this.theClientDto = theClientDto;
	}

	public TheClientDto getTheClientDto() {
		return theClientDto;
	}
	
	public KontoDto findKreditorenkontoByKontonummer(String kontonummer) throws RemoteException {
		return finanzFac.kontoFindByCnrKontotypMandantOhneExc(kontonummer, 
				FinanzServiceFac.KONTOTYP_KREDITOR, getMandant(), getTheClientDto());
	}
	
	public LieferantDto[] findLieferantByKontoIId(Integer kontoIId) {
		return lieferantFac.lieferantfindByKontoIIdKreditorenkonto(kontoIId);
	}
	
	public PartnerDto findPartnerByIId(Integer partnerIId) {
		return partnerFac.partnerFindByPrimaryKeyOhneExc(partnerIId, getTheClientDto());
	}
	
	public KontoDto findSachkontoByKontonummer(String kontonummer) throws RemoteException {
		return finanzFac.kontoFindByCnrKontotypMandantOhneExc(kontonummer, 
				FinanzServiceFac.KONTOTYP_SACHKONTO, getMandant(), getTheClientDto());
	}
	
	public EingangsrechnungDto findEingangsrechnungByIId(Integer iId) throws RemoteException {
		return erFac.eingangsrechnungFindByPrimaryKeyOhneExc(iId);
	}
	
	public EingangsrechnungDto createEingangsrechnung(EingangsrechnungDto erDto) throws RemoteException {
		return erFac.createEingangsrechnung(erDto, getTheClientDto());
	}
	
	public CoinRoundingResult calcMwstBetragFromBrutto(EingangsrechnungDto erDto) {
		return erFac.calcMwstBetragFromBrutto(erDto, getTheClientDto());
	}
	
	public CoinRoundingResult calcMwstBetragFromNetto(EingangsrechnungDto erDto) {
		return erFac.calcMwstBetragFromNetto(erDto, getTheClientDto());
	}
	
	public EingangsrechnungDto getZuletztErstellteEingangsrechnung(Integer geschaeftsjahr) {
		return erFac.getZuletztErstellteEingangsrechnung(geschaeftsjahr, getMandant());
	}
	
	public List<UstUebersetzungDto> getAlleEingangsrechnungSteuercodes() {
		return finanzServiceFac.ustUebersetzungFindByMandant(getMandant());
	}
	
	public List<MwstsatzDto> findMwstsatzFindJuengsteZuDatum(MwstsatzbezId mwstSatzBezId, Timestamp timestamp) {
		return mandantFac.mwstsatzFindJuengsteZuDatumByBez(mwstSatzBezId, timestamp, getTheClientDto());
	}
	
	public WaehrungDto[] findWaehrungen() throws RemoteException {
		return localeFac.waehrungFindAll();
	}
	
	public ReversechargeartDto findReversechargeartOhne() throws RemoteException {
		return finanzServiceFac.reversechargeartFindOhne(getTheClientDto());
	}
	
	public Integer getBelegnummerStellenGeschaeftsjahr() {
		if (stellenGj == null) {
			stellenGj = parameterFac.getBelegnummernformatStellenGeschaeftsjahr(getMandant());
		}
		return stellenGj;
	}

	public Integer getGeschaeftsjahrZuDatum(Date date) throws RemoteException {
		return parameterFac.getGeschaeftsjahr(getMandant(), date);
	}

	public String getMandant() {
		return getTheClientDto().getMandant();
	}
	
	public Timestamp[] getDatumbereichPeriodeGJ(Integer geschaeftsjahr) {
		return buchenFac.getDatumbereichPeriodeGJ(geschaeftsjahr, -1, getTheClientDto());
	}

	public WechselkursDto getKursZuDatum(String waehrung, String mandantenWaehrung, Date date) throws RemoteException {
		return localeFac.getKursZuDatum(waehrung, mandantenWaehrung, date, getTheClientDto());
	}
	
	public Integer getMaximaleLaengeLieferantenrechnungsnummer() {
		if (erLiefnrLaenge == null) {
			Integer paramLaenge = parameterFac.getEingangsrechnungLieferantenrechnungsnummerLaenge(getMandant());
			Integer laengeDb = EingangsrechnungFac.FieldLength.LIEFERANTENRECHNUNGSNR;
			erLiefnrLaenge = Helper.isBetween(paramLaenge, 0, laengeDb) ? paramLaenge : laengeDb;
		}
		return erLiefnrLaenge;
	}
	
	public boolean isGeschaeftsjahrGesperrt(Integer geschaeftsjahr) {
		try {
			GeschaeftsjahrMandantDto gj = systemFac.geschaeftsjahrFindByPrimaryKeyOhneExc(geschaeftsjahr, getMandant());
			return gj != null && gj.getTSperre() != null;
		} catch (RemoteException e) {
			return false;
		}
	}
}
