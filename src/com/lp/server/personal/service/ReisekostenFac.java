package com.lp.server.personal.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface ReisekostenFac {
	public final static String REPORT_REISEZEITEN = "pers_reisezeiten.jasper";

	public JasperPrintLP printReisezeiten(Integer personalIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			Integer iOption, Integer kostenstelleIIdAbteilung, boolean bPlusVersteckte, boolean bNurAnwesende, TheClientDto theClientDto);

	public ArrayList<AbschnittEinerReiseDto> erstelleEinzelneReiseeintraege(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto, Integer landIIdHeimat, Integer personalIId, boolean bMitReisespesen);

	public ReisezeitenEinerPersonDto getSummenDerEinzelnenTageUndVerteileKostenAufAbschnitte(
			ArrayList<AbschnittEinerReiseDto> alAbschnitteEinerReise, TheClientDto theClientDto);

	public ArrayList<AbschnittEinerReiseDto> holeReisenEinesBeleges(String belegartCNr, Integer belegartIId,
			TheClientDto theClientDto);
	public BigDecimal getKmKostenEinesAbschnitts(AbschnittEinerReiseDto abschnittEinerReiseDto,
			TheClientDto theClientDto);
}
