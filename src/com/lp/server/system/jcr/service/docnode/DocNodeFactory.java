/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.system.jcr.service.docnode;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.finanz.service.ReportSaldenlisteKriterienDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;

public class DocNodeFactory {
	
	/**
	 * Wird nur von der Dokumentenpfad-Neuzuordnung verwendet.
	 * F&uuml;r neue Belegarten muss hier nichts hinzugef&uuml;gt werden.
	 * @param dto
	 * @param belegart
	 * @return die Belegspezifische DocNode-Implementierung
	 */
	public static DocNodeBase createDocNodeFromDtoBelegart(Object dto[], String belegart) {
		
		try {
			
			if(DocNodeBase.BELEGART_LAGERSTANDSLISTE.equals(belegart))
				return new DocNodeLagerstandsliste((String)dto[0]);
			if(DocNodeBase.BELEGART_ARTIKELGRUPPE.equals(belegart))
				return new DocNodeArtikelgruppe((ArtgruDto)dto[0]);
			if(DocNodeBase.BELEGART_ARTIKELKLASSE.equals(belegart))
				return new DocNodeArtikelklasse((ArtklaDto)dto[0]);
			/**
			 * Angebot
			 */
			if(DocNodeBase.BELEGART_ANGEBOT.equals(belegart))
				return new DocNodeAngebot((AngebotDto)dto[0]);
	
			if(DocNodeBase.BELEGART_AGPOSITION.equals(belegart))
				return new DocNodeAgPosition((AngebotpositionDto)dto[0], (AngebotDto)dto[1]);
			
			if(DocNodeBase.BELEGART_AGSTUECKLISTE.equals(belegart))
				return new DocNodeAgStueckliste((AgstklDto)dto[0]);
			
			if(DocNodeBase.BELEGART_AGSTKLPOSITION.equals(belegart))
				return new DocNodeAgstklPosition((AgstklpositionDto)dto[0], (AgstklDto)dto[1]);
	
			/**
			 * Anfrage
			 */
			if(DocNodeBase.BELEGART_ANFRAGE.equals(belegart))
				return new DocNodeAnfrage((AnfrageDto)dto[0]);
			
			if(DocNodeBase.BELEGART_ANFPOSITION.equals(belegart))
				return new DocNodeAnfPosition((AnfragepositionDto)dto[0], (AnfrageDto)dto[1]);
	
			/**
			 * Artikel
			 */
			if(DocNodeBase.BELEGART_ARTIKEL.equals(belegart) || DocNodeBase.BELEGART_STUECKLISTE.equals(belegart))
				return new DocNodeArtikel((ArtikelDto)dto[0]);
			
			if(DocNodeBase.BELEGART_STKLPOSITION.equals(belegart))
				return new DocNodeStuecklistePosition((StuecklistepositionDto)dto[0], (ArtikelDto)dto[1]);
	
			
			/**
			 * Auftrag
			 */
			if(DocNodeBase.BELEGART_AUFTRAG.equals(belegart))
				return new DocNodeAuftrag((AuftragDto)dto[0]);
			
			if(DocNodeBase.BELEGART_AUFPOSITION.equals(belegart))
				return new DocNodeAufPosition((AuftragpositionDto)dto[0], (AuftragDto)dto[1]);
			
			/**
			 * Benutzer, Partner, Personal, Kunde, Lieferant, ect...
			 */
			if(DocNodeBase.BELEGART_PARTNER.equals(belegart))
				return new DocNodePartner((PartnerDto)dto[0]);
			
			if(DocNodeBase.BELEGART_PERSONAL.equals(belegart))
				return new DocNodePersonal((PersonalDto)dto[0],(PartnerDto)dto[1]);
			
			if(DocNodeBase.BELEGART_KUNDE.equals(belegart))
				return new DocNodeKunde((KundeDto)dto[0], (PartnerDto)dto[1]);
	
			if(DocNodeBase.BELEGART_LIEFERANT.equals(belegart))
				return new DocNodeLieferant((LieferantDto)dto[0], (PartnerDto)dto[1]);
			
			/**
			 * Bestellung
			 */
			if(DocNodeBase.BELEGART_BESTELLUNG.equals(belegart))
				return new DocNodeBestellung((BestellungDto)dto[0]);
	
			if(DocNodeBase.BELEGART_BESTPOSITION.equals(belegart))
				return new DocNodeBestPosition((BestellpositionDto)dto[0], (BestellungDto)dto[1]);
	
			if(DocNodeBase.BELEGART_WARENEINGANG.equals(belegart))
				return new DocNodeWareneingang((WareneingangDto)dto[0], (BestellungDto)dto[1]);
	
			if(DocNodeBase.BELEGART_WEPOSITION.equals(belegart))
				return new DocNodeWEPosition((WareneingangspositionDto)dto[0], (WareneingangDto)dto[1], (BestellungDto)dto[2]);
			
			
			if(DocNodeBase.BELEGART_EINGANGSRECHNG.equals(belegart))
				return new DocNodeEingangsrechnung((EingangsrechnungDto)dto[0]);
			if(DocNodeBase.BELEGART_ER_ZAHLUNG.equals(belegart))
				return new DocNodeErZahlung((EingangsrechnungzahlungDto)dto[0], (EingangsrechnungDto)dto[1]);
			
			if(DocNodeBase.BELEGART_UVA.equals(belegart))
				return new DocNodeUVAVerprobung((ReportUvaKriterienDto)dto[0], (FinanzamtDto)dto[1]);
			
			if(DocNodeBase.BELEGART_SALDENLISTE.equals(belegart)) 
				return new DocNodeSaldenliste((ReportSaldenlisteKriterienDto)dto[0], (String)dto[1]);
			
			if(DocNodeBase.BELEGART_KASSENBUCH.equals(belegart))
				return new DocNodeKassenbuch((PrintKontoblaetterModel)dto[0], (String)dto[1]);
			/**
			 * Los (Fertigung)
			 */
			if(DocNodeBase.BELEGART_LOS.equals(belegart))
				return new DocNodeLos((LosDto)dto[0]);
			
			if(DocNodeBase.BELEGART_LOSABLIEFERUNG.equals(belegart))
				return new DocNodeLosAblieferung((LosablieferungDto)dto[0], (LosDto)dto[1]);
			
			/**
			 * Lieferschein
			 */
			if(DocNodeBase.BELEGART_LIEFERSCHEIN.equals(belegart))
				return new DocNodeLieferschein((LieferscheinDto)dto[0]);
			
			if(DocNodeBase.BELEGART_LIEFERSPOSITION.equals(belegart))
				return new DocNodeLieferscheinPosition((LieferscheinpositionDto) dto[0], (LieferscheinDto) dto[1]);
			
			/**
			 * Projekt
			 */
			if(DocNodeBase.BELEGART_PROJEKT.equals(belegart))
				return new DocNodeProjekt((ProjektDto)dto[0], (BereichDto)dto[1]);
			if(DocNodeBase.BELEGART_PROJHISTORY.equals(belegart))
				return new DocNodeProjektHistory((HistoryDto)dto[0], (ProjektDto)dto[1], (BereichDto)dto[2]);
			
			/**
			 * Rechnung
			 */
//			if(DocNodeBase.BELEGART_RECHNUNG.equals(belegart) 
//					|| DocNodeBase.BELEGART_GUTSCHRIFT.equals(belegart)
//					|| DocNodeBase.BELEGART_PROFORMARECHNG.equals(belegart)) {
//				RechnungDto rechnung = (RechnungDto)dto[0];
//				if(rechnung.getRechnungartCNr().equals(LocaleFac.BELEGART_GUTSCHRIFT))
//					return new DocNodeGutschrift((RechnungDto)dto[0]);
//				if(rechnung.getRechnungartCNr().equals(LocaleFac.BELEGART_PROFORMARECHNUNG))
//					return new DocNodeProformarechnung((RechnungDto)dto[0]);
//				
//				return new DocNodeRechnung((RechnungDto)dto[0]);
//			}
			
			if(DocNodeBase.BELEGART_RECHNUNG.equals(belegart)) {
				return new DocNodeRechnung((RechnungDto)dto[0]);
			}
			if(DocNodeBase.BELEGART_GUTSCHRIFT.equals(belegart)) {
				return new DocNodeGutschrift((RechnungDto)dto[0]);
			}
			if(DocNodeBase.BELEGART_PROFORMARECHNG.equals(belegart)) {
				return new DocNodeProformarechnung((RechnungDto)dto[0]);
			}
			
			if(DocNodeBase.BELEGART_RECHPOSITION.equals(belegart)) {
				return new DocNodeRechPosition((RechnungPositionDto)dto[0], (RechnungDto)dto[1]);
			}
			if(DocNodeBase.BELEGART_GUTSPOSITION.equals(belegart)) {
				return new DocNodeGutsPosition((RechnungPositionDto)dto[0], (RechnungDto)dto[1]);
			}
			if(DocNodeBase.BELEGART_PROFORMAPOS.equals(belegart)) {
				return new DocNodeProformaPosition((RechnungPositionDto)dto[0], (RechnungDto)dto[1]);
			}

			if(DocNodeBase.BELEGART_RE_ZAHLUNG.equals(belegart))
				return new DocNodeRechnungZahlung((RechnungzahlungDto)dto[0], (RechnungDto)dto[1]);
			
			/**
			 * Reklamation
			 */
			if(DocNodeBase.BELEGART_REKLAMATION.equals(belegart))
				return new DocNodeReklamation((ReklamationDto)dto[0]);
			
			/**
			 * System (versand)
			 */
//			if(DocNodeBase.BELEGART_VERSANDANHANG.equals(belegart))
//				return new DocNodeVersandanhang((VersandanhangDto)dto[0], (PersonalDto)dto[1]);
//			
//			if(DocNodeBase.BELEGART_VERSANDAUFTRAG.equals(belegart))
//				return new DocNodeVersandauftrag((VersandauftragDto)dto[0], (PersonalDto)dto[1]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException(belegart.trim() + " ben\u00F6tigt weitere dto's im dto[] Object");
		}
		throw new IllegalArgumentException("Belegart " + belegart.trim() + " nicht implementiert!");
	}
	
	public static DocNodeBase createDocNodeByNode(Node node) throws ValueFormatException, PathNotFoundException, RepositoryException, IOException {
		int nodeType = (int)node.getProperty(DocNodeBase.NODEPROPERTY_NODETYPE).getLong();
		
		if(nodeType==DocNodeBase.FOLDER) {
			return new DocNodeFolder(node);
		}
		if(nodeType==DocNodeBase.LITERAL) {
			return new DocNodeLiteral(node);
		}
		if(nodeType==DocNodeBase.FILE) {
			return new DocNodeFile(node);
		}
		if(nodeType==DocNodeBase.LAGERSTANDSLISTE) {
			return new DocNodeLagerstandsliste(node);
		}
		if(nodeType==DocNodeBase.MAIL) {
			return new DocNodeMail(node);
		}
		if(nodeType==DocNodeBase.JCRDOC) {
			String belegart = node.getProperty(DocNodeBase.NODEPROPERTY_BELEGART).getString();
				
			/**
			 * Angebot
			 */
			if(DocNodeBase.BELEGART_ANGEBOT.equals(belegart))
				return new DocNodeAngebot(node);
	
			if(DocNodeBase.BELEGART_AGPOSITION.equals(belegart))
				return new DocNodeAgPosition(node);
			
			if(DocNodeBase.BELEGART_AGSTUECKLISTE.equals(belegart))
				return new DocNodeAgStueckliste(node);
			
			if(DocNodeBase.BELEGART_AGSTKLPOSITION.equals(belegart))
				return new DocNodeAgstklPosition(node);
	
			/**
			 * Anfrage
			 */
			if(DocNodeBase.BELEGART_ANFRAGE.equals(belegart))
				return new DocNodeAnfrage(node);
			
			if(DocNodeBase.BELEGART_ANFPOSITION.equals(belegart))
				return new DocNodeAnfPosition(node);
	
			/**
			 * Artikel
			 */				
			if(DocNodeBase.BELEGART_ARTIKEL.equals(belegart) || DocNodeBase.BELEGART_STUECKLISTE.equals(belegart))
				return new DocNodeArtikel(node);
			if(DocNodeBase.BELEGART_ARTIKELGRUPPE.equals(belegart))
				return new DocNodeArtikelgruppe(node);
			if(DocNodeBase.BELEGART_ARTIKELKLASSE.equals(belegart))
				return new DocNodeArtikelklasse(node);
			
			if(DocNodeBase.BELEGART_STKLPOSITION.equals(belegart))
				return new DocNodeStuecklistePosition(node);
	
			/**
			 * Inserat
			 */
			if(DocNodeBase.BELEGART_INSERAT.equals(belegart))
				return new DocNodeInserat(node);
			/**
			 * Auftrag
			 */
			if(DocNodeBase.BELEGART_AUFTRAG.equals(belegart))
				return new DocNodeAuftrag(node);
			
			if(DocNodeBase.BELEGART_AUFPOSITION.equals(belegart))
				return new DocNodeAufPosition(node);
			
			/**
			 * Benutzer, Partner, Personal, Kunde, Lieferant, ect...
			 */
			if(DocNodeBase.BELEGART_PARTNER.equals(belegart))
				return new DocNodePartner(node);
			
			if(DocNodeBase.BELEGART_PERSONAL.equals(belegart))
				return new DocNodePersonal(node);
			
			if(DocNodeBase.BELEGART_KUNDE.equals(belegart))
				return new DocNodeKunde(node);
	
			if(DocNodeBase.BELEGART_LIEFERANT.equals(belegart))
				return new DocNodeLieferant(node);
			
			/**
			 * Bestellung
			 */
			if(DocNodeBase.BELEGART_BESTELLUNG.equals(belegart))
				return new DocNodeBestellung(node);
	
			if(DocNodeBase.BELEGART_BESTPOSITION.equals(belegart))
				return new DocNodeBestPosition(node);
	
			if(DocNodeBase.BELEGART_WARENEINGANG.equals(belegart))
				return new DocNodeWareneingang(node);
	
			if(DocNodeBase.BELEGART_WEPOSITION.equals(belegart))
				return new DocNodeWEPosition(node);
			

			if(DocNodeBase.BELEGART_EINGANGSRECHNG.equals(belegart))
				return new DocNodeEingangsrechnung(node);

			if(DocNodeBase.BELEGART_ER_ZAHLUNG.equals(belegart))
				return new DocNodeErZahlung(node);
			
			if(DocNodeBase.BELEGART_UVA.equals(belegart))
				return new DocNodeUVAVerprobung(node);
			
			if(DocNodeBase.BELEGART_KASSENBUCH.equals(belegart))
				return new DocNodeKassenbuch(node);
			
			if(DocNodeBase.BELEGART_SALDENLISTE.equals(belegart)) 
				return new DocNodeSaldenliste(node);
			
			if(DocNodeBase.BELEGART_BUCHUNGDETAIL.equals(belegart))
				return new DocNodeBuchungdetail(node);
			
			/**
			 * Los (Fertigung)
			 */
			if(DocNodeBase.BELEGART_LOS.equals(belegart))
				return new DocNodeLos(node);
			
			if(DocNodeBase.BELEGART_LOSABLIEFERUNG.equals(belegart))
				return new DocNodeLosAblieferung(node);
			
			/**
			 * Lieferschein
			 */
			if(DocNodeBase.BELEGART_LIEFERSCHEIN.equals(belegart))
				return new DocNodeLieferschein(node);
			
			if(DocNodeBase.BELEGART_LIEFERSPOSITION.equals(belegart))
				return new DocNodeLieferscheinPosition(node);
			
			/**
			 * Projekt
			 */
			if(DocNodeBase.BELEGART_PROJEKT.equals(belegart))
				return new DocNodeProjekt(node);
			if(DocNodeBase.BELEGART_PROJHISTORY.equals(belegart))
				return new DocNodeProjektHistory(node);
			
			/**
			 * Rechnung
			 */
			if(DocNodeBase.BELEGART_RECHNUNG.equals(belegart))
				return new DocNodeRechnung(node);
			
			if(DocNodeBase.BELEGART_GUTSCHRIFT.equals(belegart))
				return new DocNodeGutschrift(node);
			
			if(DocNodeBase.BELEGART_RECHPOSITION.equals(belegart))
				return new DocNodeRechPosition(node);
		
			if(DocNodeBase.BELEGART_GUTSPOSITION.equals(belegart))
				return new DocNodeGutsPosition(node);
			
			if(DocNodeBase.BELEGART_RE_ZAHLUNG.equals(belegart))
				return new DocNodeRechnungZahlung(node);
			
			/**
			 * Reklamation
			 */
			if(DocNodeBase.BELEGART_REKLAMATION.equals(belegart))
				return new DocNodeReklamation(node);
			
			/**
			 * System (versand)
			 */
			if(DocNodeBase.BELEGART_VERSANDANHANG.equals(belegart))
				return new DocNodeVersandanhang(node);
			
			if(DocNodeBase.BELEGART_VERSANDAUFTRAG.equals(belegart))
				return new DocNodeVersandauftrag(node);
		}
		throw new PathNotFoundException("Belegart nicht implementiert!");
	}
}
