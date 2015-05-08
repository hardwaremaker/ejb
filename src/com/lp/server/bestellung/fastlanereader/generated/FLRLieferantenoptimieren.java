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
package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLieferantenoptimieren implements Serializable {

    /** identifier field */
    private Integer bestellvorschlag_i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id_artikellieferant;

    /** nullable persistent field */
    private Integer lieferant_i_id_bestellvorschlag;

    /** nullable persistent field */
    private FLRLieferant flrlieferant_bestellvorschlag;

    /** nullable persistent field */
    private FLRLieferant flrlieferant_artikellieferant;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag flrbestellvorschlag;

    /** nullable persistent field */
    private FLRArtikellieferant flrartikellieferant;

    /** full constructor */
    public FLRLieferantenoptimieren(Integer artikel_i_id, Integer lieferant_i_id_artikellieferant, Integer lieferant_i_id_bestellvorschlag, FLRLieferant flrlieferant_bestellvorschlag, FLRLieferant flrlieferant_artikellieferant, FLRArtikelliste flrartikelliste, com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag flrbestellvorschlag, FLRArtikellieferant flrartikellieferant) {
        this.artikel_i_id = artikel_i_id;
        this.lieferant_i_id_artikellieferant = lieferant_i_id_artikellieferant;
        this.lieferant_i_id_bestellvorschlag = lieferant_i_id_bestellvorschlag;
        this.flrlieferant_bestellvorschlag = flrlieferant_bestellvorschlag;
        this.flrlieferant_artikellieferant = flrlieferant_artikellieferant;
        this.flrartikelliste = flrartikelliste;
        this.flrbestellvorschlag = flrbestellvorschlag;
        this.flrartikellieferant = flrartikellieferant;
    }

    /** default constructor */
    public FLRLieferantenoptimieren() {
    }

    public Integer getBestellvorschlag_i_id() {
        return this.bestellvorschlag_i_id;
    }

    public void setBestellvorschlag_i_id(Integer bestellvorschlag_i_id) {
        this.bestellvorschlag_i_id = bestellvorschlag_i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getLieferant_i_id_artikellieferant() {
        return this.lieferant_i_id_artikellieferant;
    }

    public void setLieferant_i_id_artikellieferant(Integer lieferant_i_id_artikellieferant) {
        this.lieferant_i_id_artikellieferant = lieferant_i_id_artikellieferant;
    }

    public Integer getLieferant_i_id_bestellvorschlag() {
        return this.lieferant_i_id_bestellvorschlag;
    }

    public void setLieferant_i_id_bestellvorschlag(Integer lieferant_i_id_bestellvorschlag) {
        this.lieferant_i_id_bestellvorschlag = lieferant_i_id_bestellvorschlag;
    }

    public FLRLieferant getFlrlieferant_bestellvorschlag() {
        return this.flrlieferant_bestellvorschlag;
    }

    public void setFlrlieferant_bestellvorschlag(FLRLieferant flrlieferant_bestellvorschlag) {
        this.flrlieferant_bestellvorschlag = flrlieferant_bestellvorschlag;
    }

    public FLRLieferant getFlrlieferant_artikellieferant() {
        return this.flrlieferant_artikellieferant;
    }

    public void setFlrlieferant_artikellieferant(FLRLieferant flrlieferant_artikellieferant) {
        this.flrlieferant_artikellieferant = flrlieferant_artikellieferant;
    }

    public FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag getFlrbestellvorschlag() {
        return this.flrbestellvorschlag;
    }

    public void setFlrbestellvorschlag(com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag flrbestellvorschlag) {
        this.flrbestellvorschlag = flrbestellvorschlag;
    }

    public FLRArtikellieferant getFlrartikellieferant() {
        return this.flrartikellieferant;
    }

    public void setFlrartikellieferant(FLRArtikellieferant flrartikellieferant) {
        this.flrartikellieferant = flrartikellieferant;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("bestellvorschlag_i_id", getBestellvorschlag_i_id())
            .toString();
    }

}
