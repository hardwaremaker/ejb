package com.lp.server.rechnung.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface AbrechnungsvorschlagFac {

	public static final String LOCKME_ABRECHNUNGSVORSCHLAG_TP = "lockme_abrechnungsvorschlag_tp";

	public void erstelleAbrechnungsvorschlag(java.sql.Date dStichtag, boolean bAltenLoeschen, boolean bMitZeitdaten,
			boolean bMitTelefonzeiten, boolean bMitReisezeiten, boolean bMitEingangsrechnungen,
			boolean bMitMaschinenzeiten, TheClientDto theClientDto);

	public void erstelleAbrechnungsvorschlagTelefonzeiten(java.sql.Date dStichtag,
			Integer abrechnungsvorschlagIIdVorhanden, TheClientDto theClientDto);

	public void erstelleAbrechnungsvorschlagER(java.sql.Date dStichtag, Integer abrechnungsvorschlagIIdVorhanden,
			TheClientDto theClientDto);

	public void erstelleAbrechnungsvorschlagReise(java.sql.Date dStichtag, Integer abrechnungsvorschlagIIdVorhanden,
			TheClientDto theClientDto);

	public void erstelleAbrechnungsvorschlagZeitdaten(java.sql.Date dStichtag, Integer abrechnungsvorschlagIIdVorhanden,
			TheClientDto theClientDto);

	public void erstelleAbrechnungsvorschlagMaschinenzeitdaten(java.sql.Date dStichtag,
			Integer abrechnungsvorschlagIIdVorhanden, TheClientDto theClientDto);

	public AbrechnungsvorschlagDto abrechnungsvorschlagFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public Integer createAbrechnungsvorschlag(AbrechnungsvorschlagDto dto, TheClientDto theClientDto);

	public void abrechnungsvorschlagUeberleitenReise(ArrayList<Integer> selektierteIIds, Integer artikelIId,
			BigDecimal bdBetragInMandantenwaehrung, String kommentar, Integer artikelIIdKilometer,
			BigDecimal bdKilometer, String kommentarKilometer, BigDecimal bdSpesen, String kommentarSpesen,
			Integer artikelIIdSpesen, boolean bErledigt, java.sql.Date tBelegdatum, TheClientDto theClientDto);

	public void abrechnungsvorschlagUeberleitenER(ArrayList<Integer> selektierteIIds, Integer artikelIId,
			BigDecimal bdBetragInMandantenwaehrung, BigDecimal bdBetragInMandantenwaehrungInclAufschlag,
			String kommentar, boolean bErledigt, java.sql.Date tBelegdatum, TheClientDto theClientDto);

	public void selektiertePositionenManuellErledigen(ArrayList<Integer> iids, TheClientDto theClientDto);

	public BigDecimal getSelektiertenBetrag(ArrayList<Integer> iids);

	public BigDecimal getSelektierteStunden(ArrayList<Integer> iids);

	public void selektiertenPositionenKundeZuordnen(ArrayList<Integer> iids, Integer kundeIId,
			TheClientDto theClientDto);

	public Integer createVerrechnungsmodell(VerrechnungsmodellDto dto);

	public void updateVerrechnungsmodell(VerrechnungsmodellDto dto);

	public void updateVerrechnungsmodelltag(VerrechnungsmodelltagDto dto);

	public Integer createVerrechnungsmodelltag(VerrechnungsmodelltagDto dto);

	public VerrechnungsmodellDto verrechnungsmodellFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public VerrechnungsmodelltagDto verrechnungsmodelltagFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public void removeVerrechnungsmodell(VerrechnungsmodellDto dto);

	public void removeVerrechnungsmodelltag(VerrechnungsmodelltagDto dto);

	public AbrechnungsvorschlagUeberleitenDto erzeugeUeberleitungsvorschlag(String art,
			ArrayList<Integer> selektierteIIds, TheClientDto theClientDto);

	public void abrechnungsvorschlagUeberleitenZeitdaten(
			Map<String, ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto> zeilen, boolean bErledigt,
			java.sql.Date tBelegdatum, TheClientDto theClientDto);

	public void aendereVerrechenbarkeit(ArrayList<Integer> abrechnugnsvorschlagIIds, Double fProzent,
			TheClientDto theClientDto);

	public JasperPrintLP printAbrechnungsvorschlag(TheClientDto theClientDto);

	public JasperPrintLP printVerrechnungsmodell(Integer verrechnungsmodellIId, TheClientDto theClientDto);

	public Integer kopiereVerrechnungsmodelltag(Integer verrechnungsmodellmodelltagIId, TheClientDto theClientDto);

	public VerrechnungsmodellDto holeVerrechnungsmodellFuerERUeberleitungAnhandSelektierterZeilen(
			ArrayList<Integer> selektierteIIds, TheClientDto theClientDto);

	public VerrechnungsmodellDto holeVerrechnungsmodellFuerReiseUeberleitungAnhandSelektierterZeilen(
			ArrayList<Integer> selektierteIIds, TheClientDto theClientDto);

	public BigDecimal getSelektierteKilometer(ArrayList<Integer> iids);

	public void removeLockDesAbrechungsvorschlagesWennIchIhnSperre(TheClientDto theClientDto);

	public void pruefeBearbeitenDesAbrechungsvorschlagesErlaubt(TheClientDto theClientDto);

	public Integer sindEintraegeInAbrechnungsvorschlag(Integer zeitdatenIId, Integer maschinenzeitdatenIId,
			Integer telefonzeitenIId, Integer auftragszuordnungIId, Integer reiseIId, TheClientDto theClientDto);

	public static String ART_ZEITDATEN = "ZEIT";
	public static String ART_ER = "ER";
	public static String ART_TELEFON = "TELEFON";
	public static String ART_REISE = "REISE";
	public static String ART_MASCHINE = "MASCHINE";

	public static final String REPORT_ABRECHNUNGSVORSCHLAG = "rech_abrechnungsvorschlag.jasper";
	public static final String REPORT_VERRECHNUNGSMODELL = "rech_verrechnungsmodell.jasper";
	public static final String REPORT_MANUELLERLEDIGTEZEITEN = "rech_manuellerledigtezeiten.jasper";

	public BigDecimal getSelektierteSpesen(ArrayList<Integer> iids);

	public BigDecimal getSummeZeitdatenVerrechnet(Integer zeitdatenIId);

	public BigDecimal getSummeTelefonzeitenVerrechnet(Integer telefonzeitenIId);

	public JasperPrintLP printManuellErledigteEnterledigen(Integer personalIId, Integer kundeIId, Integer auftragIId,DatumsfilterVonBis dVonBis, TheClientDto theClientDto);
	public void enterledigen(HashMap<String, ArrayList<Integer>> hmEnterledigen);
}
