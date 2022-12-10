package com.lp.server.lieferschein.ejbfac;

import java.rmi.RemoteException;
import java.util.Locale;

import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;
import com.lp.server.lieferschein.ejbfac.AddressBlockFormatted.Tag;
import com.lp.server.partner.service.AnredesprDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class AddressBlockFormatter {
	private final SystemFac systemFac;
	private final ParameterFac parameterFac;
	private final PartnerFac partnerFac;
	private final BenutzerServicesFacLocal benutzerServicesFac;
	
	public AddressBlockFormatter(SystemFac systemFac, 
			BenutzerServicesFacLocal benutzerServicesFac, 
			ParameterFac parameterFac, PartnerFac partnerFac) {
		this.systemFac = systemFac;
		this.parameterFac = parameterFac;
		this.partnerFac = partnerFac;
		this.benutzerServicesFac = benutzerServicesFac;
	}
	
	public AddressBlockFormatted formatAdresseFuerAusdruck(PartnerDto partnerDto, AnsprechpartnerDto anspDto, MandantDto mandantDto,
			Locale locale, String belegartCNr) throws RemoteException {
		return formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto, locale, false, belegartCNr, false);
	}

	public AddressBlockFormatted formatAdresseFuerAusdruck(PartnerDto partnerDto, AnsprechpartnerDto anspDto, MandantDto mandantDto,
			Locale locale, boolean bLandImmerAnhaengen, String belegartCNr, boolean postfachIgnorieren) throws RemoteException {
		AddressBlockFormatted abf = new AddressBlockFormatted();
		
		if (partnerDto != null) {
			// PJ19839
			if (partnerDto.getLandplzortDto() != null && partnerDto.getLandplzortDto().getIId() != null) {
				LandDto landDto = systemFac.landFindByPrimaryKey(partnerDto.getLandplzortDto().getIlandID(),
						Helper.locale2String(locale));

				if (landDto.getLandsprDto() != null && landDto.getLandsprDto().getCBez() != null) {
					partnerDto.getLandplzortDto().getLandDto().setCName(landDto.getLandsprDto().getCBez());
				}
			}

			if (partnerDto.getLandplzortDto_Postfach() != null
					&& partnerDto.getLandplzortDto_Postfach().getIId() != null) {
				LandDto landDto = systemFac.landFindByPrimaryKey(
						partnerDto.getLandplzortDto_Postfach().getIlandID(), Helper.locale2String(locale));

				if (landDto.getLandsprDto() != null && landDto.getLandsprDto().getCBez() != null) {
					partnerDto.getLandplzortDto_Postfach().getLandDto().setCName(landDto.getLandsprDto().getCBez());
				}

			}

			String sAnredeCNr = partnerDto.getAnredeCNr();
			String titel = getTrimmed(partnerDto.getCTitel());
			String ntitel = getTrimmed(partnerDto.getCNtitel());

			if (isAnredePerson(sAnredeCNr)) {
				// SP629
				ParametermandantDto parametermandantDto = parameterFac.getMandantparameter(mandantDto.getCNr(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ADRESSE_FUER_AUSDRUCK_MIT_ANREDE);

				if (((Boolean) parametermandantDto.getCWertAsObject()) == true && sAnredeCNr != null) {

					String ret = partnerDto.getAnredeCNr().trim();

					if (locale != null) {
						AnredesprDto anredesprDto = partnerFac
								.anredesprFindByAnredeCNrLocaleCNrOhneExc(sAnredeCNr, Helper.locale2String(locale));
						if (anredesprDto != null) {
							ret = anredesprDto.getCBez().trim();
						}
					}

					abf.setLine(0, Tag.ANREDE, ret);
					abf.setLine(1, Tag.NAME1, getBlankSuffixed(titel)
							+ getBlankSuffixed(partnerDto.getCName2vornamefirmazeile2())
							+ getBlankSuffixed(partnerDto.getCName1nachnamefirmazeile1()) + ntitel);

					abf.setLine(2, Tag.NAME3, getTrimmed(partnerDto.getCName3vorname2abteilung()));
				} else {
					abf.setLine(0, Tag.NAME1, getBlankSuffixed(titel)
							+ getBlankSuffixed(partnerDto.getCName2vornamefirmazeile2())
							+ getBlankSuffixed(partnerDto.getCName1nachnamefirmazeile1()) + ntitel);

					abf.setLine(2, Tag.NAME3, getTrimmed(partnerDto.getCName3vorname2abteilung()));
				}

			} else {
				// Firma
				abf.setLine(0, Tag.NAME1, getBlankSuffixed(titel) + getBlankSuffixed(partnerDto.getCName1nachnamefirmazeile1())
						+ ntitel);
				abf.setLine(1, Tag.NAME2, partnerDto.getCName2vornamefirmazeile2());

				if (anspDto != null && anspDto.getCAbteilung() != null) {
					abf.setLine(2, Tag.NAME3, anspDto.getCAbteilung());
				} else {
					abf.setLine(2, Tag.NAME3,partnerDto.getCName3vorname2abteilung());
				}

			}

			String postfach = getTrimmed(partnerDto.getCPostfach());
			// PJ18752
			if (postfach.length() > 0 && postfachIgnorieren == false) {

				if (partnerDto.getLandplzortDto() != null && Helper
						.short2boolean(partnerDto.getLandplzortDto().getLandDto().getBPostfachmitstrasse())) {
					abf.setLine(4, Tag.STRASSE, getTrimmed(partnerDto.getCStrasse()));
				}
				abf.setLine(5, Tag.POSTFACH, getBlankSuffixed(
						getTextRespectUISpr("lp.postfach", mandantDto.getCNr(), locale))
						+ partnerDto.getCPostfach());
			} else {
				// sonst Strasse
				abf.setLine(5, Tag.STRASSE, 
						getTrimmed(partnerDto.getCStrasse()));
			}

			// Postfach - LandPLZOrt (wenn vorhanden)
			if (partnerDto.getLandplzortDto_Postfach() != null && postfachIgnorieren == false) {
				abf.setLine(6, Tag.PLZORT, partnerDto.getLandplzortDto_Postfach().formatPlzOrt());
				if (partnerDto.getLandplzortDto_Postfach().getLandDto() != null) {
					// das Land nur drucken, wenn es nicht dem Land des
					// Mandanten entspricht
					if (mandantDto == null || mandantDto.getPartnerDto().getLandplzortDto() == null
							|| mandantDto.getPartnerDto().getLandplzortDto().getLandDto() == null
							|| !mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
									.equals(partnerDto.getLandplzortDto_Postfach().getLandDto().getIID())
							|| bLandImmerAnhaengen) {
						abf.setLine(7, Tag.LAND, partnerDto.getLandplzortDto_Postfach().getLandDto().getCName().toUpperCase());

						// Land entfernen, wenn gemeinsames Postland
						if (bLandImmerAnhaengen == false && mandantDto != null
								&& mandantDto.getPartnerDto().getLandplzortDto() != null
								&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto() != null

								&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
										.getLandIIdGemeinsamespostland() != null

								&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
										.getLandIIdGemeinsamespostland()
										.equals(partnerDto.getLandplzortDto_Postfach().getLandDto().getIID())) {
							// TODO: ghp, muss eigentlich gar nicht aufgerufen werden
							//abf.setLine(7, AddressBlockFormatted.Tag.UNKNOWN, null);
						}

					}
				}
			}
			// sonst den "normalen" Ort
			else if (partnerDto.getLandplzortDto() != null) {
				abf.setLine(6, Tag.PLZORT, partnerDto.getLandplzortDto().formatPlzOrt());
				if (partnerDto.getLandplzortDto().getLandDto() != null) {

					LandDto landDto = null;
					ParametermandantDto parametermandantDto = parameterFac.getMandantparameter(
							mandantDto.getCNr(), ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_POSTVERSENDUNGSLAND);
					if (parametermandantDto.getCWert() != null)
						;
					if (!parametermandantDto.getCWert().equals("")) {
						landDto = systemFac.landFindByLkz(parametermandantDto.getCWert());
					}
					if (landDto != null) {
						if (!landDto.getIID().equals(partnerDto.getLandplzortDto().getLandDto().getIID()))
							abf.setLine(7, Tag.LAND, partnerDto.getLandplzortDto().getLandDto().getCName().toUpperCase());
					} else {
						// das Land nur drucken, wenn es nicht dem Land des
						// Mandanten entspricht
						if (mandantDto == null || mandantDto.getPartnerDto().getLandplzortDto() == null
								|| mandantDto.getPartnerDto().getLandplzortDto().getLandDto() == null
								|| !mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
										.equals(partnerDto.getLandplzortDto().getLandDto().getIID())
								|| bLandImmerAnhaengen) {

							// SP581 das Land nicht andrucken, wenn es das
							// gemeinsame Postland des Mandanten-Landes

							abf.setLine(7, Tag.LAND, partnerDto.getLandplzortDto().getLandDto().getCName().toUpperCase());

							// Land entfernen, wenn gemeinsames Postland
							if (mandantDto != null && mandantDto.getPartnerDto() != null
									&& mandantDto.getPartnerDto().getLandplzortDto() != null
									&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
											.getLandIIdGemeinsamespostland() != null) {

								if (bLandImmerAnhaengen == false && mandantDto != null
										&& mandantDto.getPartnerDto().getLandplzortDto() != null
										&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto() != null
										&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
												.getLandIIdGemeinsamespostland()
												.equals(partnerDto.getLandplzortDto().getLandDto().getIID())) {
//									sZeilen[7] = null;
								}

								if (bLandImmerAnhaengen == false
										&& partnerDto.getLandplzortDto().getLandDto()
												.getLandIIdGemeinsamespostland() != null
										&& mandantDto != null
										&& mandantDto.getPartnerDto().getLandplzortDto() != null
										&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto() != null
										&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
												.equals(partnerDto.getLandplzortDto().getLandDto().getIID())) {
	//								sZeilen[7] = null;
								}
							}
						}
					}
				}
			}
		}

		if (anspDto != null) {
			boolean bAnsprechpartnerAndrucken = true;
			if (belegartCNr != null) {
				if (belegartCNr.equals(LocaleFac.BELEGART_ANFRAGE)) {
					ParametermandantDto parametermandantDtoAnspAndrucken = parameterFac.getMandantparameter(
							mandantDto.getCNr(), ParameterFac.KATEGORIE_ANFRAGE,
							ParameterFac.PARAMETER_ANFRAGE_ANSPRECHPARTNER_ANDRUCKEN);
					bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
				} else if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
					ParametermandantDto parametermandantDtoAnspAndrucken = parameterFac.getMandantparameter(
							mandantDto.getCNr(), ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_AUFTRAG_ANSPRECHPARTNER_ANDRUCKEN);
					bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
				} else if (belegartCNr.equals(LocaleFac.BELEGART_BESTELLUNG)) {
					ParametermandantDto parametermandantDtoAnspAndrucken = parameterFac.getMandantparameter(
							mandantDto.getCNr(), ParameterFac.KATEGORIE_BESTELLUNG,
							ParameterFac.PARAMETER_BESTELLUNG_ANSPRECHPARTNER_ANDRUCKEN);
					bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
				} else if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {
					ParametermandantDto parametermandantDtoAnspAndrucken = parameterFac.getMandantparameter(
							mandantDto.getCNr(), ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_ANGEBOT_ANSPRECHPARTNER_ANDRUCKEN);
					bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
				} else if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
					ParametermandantDto parametermandantDtoAnspAndrucken = parameterFac.getMandantparameter(
							mandantDto.getCNr(), ParameterFac.KATEGORIE_LIEFERSCHEIN,
							ParameterFac.PARAMETER_LIEFERSCHEIN_ANSPRECHPARTNER_ANDRUCKEN);
					bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
				} else if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
					ParametermandantDto parametermandantDtoAnspAndrucken = parameterFac.getMandantparameter(
							mandantDto.getCNr(), ParameterFac.KATEGORIE_RECHNUNG,
							ParameterFac.PARAMETER_RECHNUNG_ANSPRECHPARTNER_ANDRUCKEN);
					bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
				}
			}

			if (bAnsprechpartnerAndrucken == true) {
				abf.setLine(3, Tag.ANSPRECHPARTNER, partnerFac.formatFixAnredeTitelName2Name1FuerAdresskopf(anspDto.getPartnerDto(),
						locale, null));
			}
		}
		return abf;
	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr, Locale loI) throws EJBExceptionLP {
		return benutzerServicesFac.getTextRespectUISpr(sTokenI, mandantCNr, loI);
	}


	private boolean isAnredePerson(String sAnredeCNr) {

		// 2012-10-22 WH: Wenn Anrede vorhandenm welche nicht 'Firma' ist, dann
		// ist es eine Person

		if (sAnredeCNr != null && !PartnerFac.PARTNER_ANREDE_FIRMA.equals(sAnredeCNr)) {
			return true;
		} else {
			return false;
		}

	}

	private String getTrimmed(String value) {
		if (null == value)
			return "";
		return value.trim();
	}

	private String getBlankSuffixed(String value) {
		if (null == value)
			return "";
		if (value.trim().length() == 0)
			return "";
		return value.trim() + " ";
	}
}
