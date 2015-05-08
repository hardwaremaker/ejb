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
package com.lp.server.rechnung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRVerleih;
import com.lp.server.auftrag.fastlanereader.generated.FLRPositionenSichtAuftrag;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.system.fastlanereader.generated.FLRMwstsatz;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRRechnungPosition implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer rechnung_i_id;

    /** nullable persistent field */
    private Integer rechnung_i_id_gutschrift;

    /** nullable persistent field */
    private Integer auftragposition_i_id;

    /** nullable persistent field */
    private String positionsart_c_nr;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zusatzbezeichnung;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_einzelpreis;

    /** nullable persistent field */
    private BigDecimal n_materialzuschlag;

    /** nullable persistent field */
    private BigDecimal n_einzelpreis_plus_aufschlag;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis_plus_aufschlag;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis_plus_aufschlag_minus_rabatt;

    /** nullable persistent field */
    private Double f_rabattsatz;

    /** nullable persistent field */
    private BigDecimal n_bruttoeinzelpreis;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private String c_snrchnr_mig;

    /** nullable persistent field */
    private Integer zwsvonposition_i_id;

    /** nullable persistent field */
    private Integer zwsbisposition_i_id;

    /** nullable persistent field */
    private BigDecimal zwsnettosumme;

    /** nullable persistent field */
    private FLRPositionenSichtAuftrag flrpositionensichtauftrag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMwstsatz flrmwstsatz;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung;

    /** nullable persistent field */
    private FLRLieferschein flrlieferschein;

    /** nullable persistent field */
    private FLRVerleih flrverleih;

    /** persistent field */
    private Set setartikel_set;

    /** full constructor */
    public FLRRechnungPosition(Integer i_sort, Integer rechnung_i_id, Integer rechnung_i_id_gutschrift, Integer auftragposition_i_id, String positionsart_c_nr, Integer artikel_i_id, String c_bez, String c_zusatzbezeichnung, String x_textinhalt, BigDecimal n_menge, BigDecimal n_einzelpreis, BigDecimal n_materialzuschlag, BigDecimal n_einzelpreis_plus_aufschlag, BigDecimal n_nettoeinzelpreis, BigDecimal n_nettoeinzelpreis_plus_aufschlag, BigDecimal n_nettoeinzelpreis_plus_aufschlag_minus_rabatt, Double f_rabattsatz, BigDecimal n_bruttoeinzelpreis, String einheit_c_nr, Integer position_i_id, Integer position_i_id_artikelset, String typ_c_nr, String c_snrchnr_mig, Integer zwsvonposition_i_id, Integer zwsbisposition_i_id, BigDecimal zwsnettosumme, FLRPositionenSichtAuftrag flrpositionensichtauftrag, FLRArtikel flrartikel, FLRMwstsatz flrmwstsatz, com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung, FLRLieferschein flrlieferschein, FLRVerleih flrverleih, Set setartikel_set) {
        this.i_sort = i_sort;
        this.rechnung_i_id = rechnung_i_id;
        this.rechnung_i_id_gutschrift = rechnung_i_id_gutschrift;
        this.auftragposition_i_id = auftragposition_i_id;
        this.positionsart_c_nr = positionsart_c_nr;
        this.artikel_i_id = artikel_i_id;
        this.c_bez = c_bez;
        this.c_zusatzbezeichnung = c_zusatzbezeichnung;
        this.x_textinhalt = x_textinhalt;
        this.n_menge = n_menge;
        this.n_einzelpreis = n_einzelpreis;
        this.n_materialzuschlag = n_materialzuschlag;
        this.n_einzelpreis_plus_aufschlag = n_einzelpreis_plus_aufschlag;
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
        this.n_nettoeinzelpreis_plus_aufschlag = n_nettoeinzelpreis_plus_aufschlag;
        this.n_nettoeinzelpreis_plus_aufschlag_minus_rabatt = n_nettoeinzelpreis_plus_aufschlag_minus_rabatt;
        this.f_rabattsatz = f_rabattsatz;
        this.n_bruttoeinzelpreis = n_bruttoeinzelpreis;
        this.einheit_c_nr = einheit_c_nr;
        this.position_i_id = position_i_id;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.typ_c_nr = typ_c_nr;
        this.c_snrchnr_mig = c_snrchnr_mig;
        this.zwsvonposition_i_id = zwsvonposition_i_id;
        this.zwsbisposition_i_id = zwsbisposition_i_id;
        this.zwsnettosumme = zwsnettosumme;
        this.flrpositionensichtauftrag = flrpositionensichtauftrag;
        this.flrartikel = flrartikel;
        this.flrmwstsatz = flrmwstsatz;
        this.flrrechnung = flrrechnung;
        this.flrlieferschein = flrlieferschein;
        this.flrverleih = flrverleih;
        this.setartikel_set = setartikel_set;
    }

    /** default constructor */
    public FLRRechnungPosition() {
    }

    /** minimal constructor */
    public FLRRechnungPosition(Set setartikel_set) {
        this.setartikel_set = setartikel_set;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getRechnung_i_id() {
        return this.rechnung_i_id;
    }

    public void setRechnung_i_id(Integer rechnung_i_id) {
        this.rechnung_i_id = rechnung_i_id;
    }

    public Integer getRechnung_i_id_gutschrift() {
        return this.rechnung_i_id_gutschrift;
    }

    public void setRechnung_i_id_gutschrift(Integer rechnung_i_id_gutschrift) {
        this.rechnung_i_id_gutschrift = rechnung_i_id_gutschrift;
    }

    public Integer getAuftragposition_i_id() {
        return this.auftragposition_i_id;
    }

    public void setAuftragposition_i_id(Integer auftragposition_i_id) {
        this.auftragposition_i_id = auftragposition_i_id;
    }

    public String getPositionsart_c_nr() {
        return this.positionsart_c_nr;
    }

    public void setPositionsart_c_nr(String positionsart_c_nr) {
        this.positionsart_c_nr = positionsart_c_nr;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_zusatzbezeichnung() {
        return this.c_zusatzbezeichnung;
    }

    public void setC_zusatzbezeichnung(String c_zusatzbezeichnung) {
        this.c_zusatzbezeichnung = c_zusatzbezeichnung;
    }

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_einzelpreis() {
        return this.n_einzelpreis;
    }

    public void setN_einzelpreis(BigDecimal n_einzelpreis) {
        this.n_einzelpreis = n_einzelpreis;
    }

    public BigDecimal getN_materialzuschlag() {
        return this.n_materialzuschlag;
    }

    public void setN_materialzuschlag(BigDecimal n_materialzuschlag) {
        this.n_materialzuschlag = n_materialzuschlag;
    }

    public BigDecimal getN_einzelpreis_plus_aufschlag() {
        return this.n_einzelpreis_plus_aufschlag;
    }

    public void setN_einzelpreis_plus_aufschlag(BigDecimal n_einzelpreis_plus_aufschlag) {
        this.n_einzelpreis_plus_aufschlag = n_einzelpreis_plus_aufschlag;
    }

    public BigDecimal getN_nettoeinzelpreis() {
        return this.n_nettoeinzelpreis;
    }

    public void setN_nettoeinzelpreis(BigDecimal n_nettoeinzelpreis) {
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
    }

    public BigDecimal getN_nettoeinzelpreis_plus_aufschlag() {
        return this.n_nettoeinzelpreis_plus_aufschlag;
    }

    public void setN_nettoeinzelpreis_plus_aufschlag(BigDecimal n_nettoeinzelpreis_plus_aufschlag) {
        this.n_nettoeinzelpreis_plus_aufschlag = n_nettoeinzelpreis_plus_aufschlag;
    }

    public BigDecimal getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt() {
        return this.n_nettoeinzelpreis_plus_aufschlag_minus_rabatt;
    }

    public void setN_nettoeinzelpreis_plus_aufschlag_minus_rabatt(BigDecimal n_nettoeinzelpreis_plus_aufschlag_minus_rabatt) {
        this.n_nettoeinzelpreis_plus_aufschlag_minus_rabatt = n_nettoeinzelpreis_plus_aufschlag_minus_rabatt;
    }

    public Double getF_rabattsatz() {
        return this.f_rabattsatz;
    }

    public void setF_rabattsatz(Double f_rabattsatz) {
        this.f_rabattsatz = f_rabattsatz;
    }

    public BigDecimal getN_bruttoeinzelpreis() {
        return this.n_bruttoeinzelpreis;
    }

    public void setN_bruttoeinzelpreis(BigDecimal n_bruttoeinzelpreis) {
        this.n_bruttoeinzelpreis = n_bruttoeinzelpreis;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public Integer getPosition_i_id() {
        return this.position_i_id;
    }

    public void setPosition_i_id(Integer position_i_id) {
        this.position_i_id = position_i_id;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public String getC_snrchnr_mig() {
        return this.c_snrchnr_mig;
    }

    public void setC_snrchnr_mig(String c_snrchnr_mig) {
        this.c_snrchnr_mig = c_snrchnr_mig;
    }

    public Integer getZwsvonposition_i_id() {
        return this.zwsvonposition_i_id;
    }

    public void setZwsvonposition_i_id(Integer zwsvonposition_i_id) {
        this.zwsvonposition_i_id = zwsvonposition_i_id;
    }

    public Integer getZwsbisposition_i_id() {
        return this.zwsbisposition_i_id;
    }

    public void setZwsbisposition_i_id(Integer zwsbisposition_i_id) {
        this.zwsbisposition_i_id = zwsbisposition_i_id;
    }

    public BigDecimal getZwsnettosumme() {
        return this.zwsnettosumme;
    }

    public void setZwsnettosumme(BigDecimal zwsnettosumme) {
        this.zwsnettosumme = zwsnettosumme;
    }

    public FLRPositionenSichtAuftrag getFlrpositionensichtauftrag() {
        return this.flrpositionensichtauftrag;
    }

    public void setFlrpositionensichtauftrag(FLRPositionenSichtAuftrag flrpositionensichtauftrag) {
        this.flrpositionensichtauftrag = flrpositionensichtauftrag;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRMwstsatz getFlrmwstsatz() {
        return this.flrmwstsatz;
    }

    public void setFlrmwstsatz(FLRMwstsatz flrmwstsatz) {
        this.flrmwstsatz = flrmwstsatz;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRRechnung getFlrrechnung() {
        return this.flrrechnung;
    }

    public void setFlrrechnung(com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung) {
        this.flrrechnung = flrrechnung;
    }

    public FLRLieferschein getFlrlieferschein() {
        return this.flrlieferschein;
    }

    public void setFlrlieferschein(FLRLieferschein flrlieferschein) {
        this.flrlieferschein = flrlieferschein;
    }

    public FLRVerleih getFlrverleih() {
        return this.flrverleih;
    }

    public void setFlrverleih(FLRVerleih flrverleih) {
        this.flrverleih = flrverleih;
    }

    public Set getSetartikel_set() {
        return this.setartikel_set;
    }

    public void setSetartikel_set(Set setartikel_set) {
        this.setartikel_set = setartikel_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
