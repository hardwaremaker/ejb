package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLossollmaterial implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer i_beginnterminoffset;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_sollpreis;

    /** nullable persistent field */
    private Short b_nachtraeglich;

    /** nullable persistent field */
    private Short b_dringend;

    /** nullable persistent field */
    private Integer montageart_i_id;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id_original;

    /** nullable persistent field */
    private Integer i_lfdnummer;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private BigDecimal n_menge_stklpos;

    /** nullable persistent field */
    private String einheit_c_nr_stklpos;

    /** nullable persistent field */
    private String c_position;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Double f_dimension1;

    /** nullable persistent field */
    private Double f_dimension2;

    /** nullable persistent field */
    private Double f_dimension3;

    /** nullable persistent field */
    private String c_fehlercode;

    /** nullable persistent field */
    private String c_fehlertext;

    /** nullable persistent field */
    private Date t_export_beginn;

    /** nullable persistent field */
    private Date t_export_ende;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_original;

    /** persistent field */
    private Set istmaterialset;

    /** full constructor */
    public FLRLossollmaterial(Integer los_i_id, Integer i_sort, Integer i_beginnterminoffset, BigDecimal n_menge, BigDecimal n_sollpreis, Short b_nachtraeglich, Short b_dringend, Integer montageart_i_id, Integer lossollmaterial_i_id_original, Integer i_lfdnummer, Date t_aendern, BigDecimal n_menge_stklpos, String einheit_c_nr_stklpos, String c_position, String c_kommentar, Double f_dimension1, Double f_dimension2, Double f_dimension3, String c_fehlercode, String c_fehlertext, Date t_export_beginn, Date t_export_ende, FLRArtikel flrartikel, FLRArtikelliste flrartikelliste, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_original, Set istmaterialset) {
        this.los_i_id = los_i_id;
        this.i_sort = i_sort;
        this.i_beginnterminoffset = i_beginnterminoffset;
        this.n_menge = n_menge;
        this.n_sollpreis = n_sollpreis;
        this.b_nachtraeglich = b_nachtraeglich;
        this.b_dringend = b_dringend;
        this.montageart_i_id = montageart_i_id;
        this.lossollmaterial_i_id_original = lossollmaterial_i_id_original;
        this.i_lfdnummer = i_lfdnummer;
        this.t_aendern = t_aendern;
        this.n_menge_stklpos = n_menge_stklpos;
        this.einheit_c_nr_stklpos = einheit_c_nr_stklpos;
        this.c_position = c_position;
        this.c_kommentar = c_kommentar;
        this.f_dimension1 = f_dimension1;
        this.f_dimension2 = f_dimension2;
        this.f_dimension3 = f_dimension3;
        this.c_fehlercode = c_fehlercode;
        this.c_fehlertext = c_fehlertext;
        this.t_export_beginn = t_export_beginn;
        this.t_export_ende = t_export_ende;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
        this.flrlos = flrlos;
        this.flrlossollmaterial_original = flrlossollmaterial_original;
        this.istmaterialset = istmaterialset;
    }

    /** default constructor */
    public FLRLossollmaterial() {
    }

    /** minimal constructor */
    public FLRLossollmaterial(Set istmaterialset) {
        this.istmaterialset = istmaterialset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getI_beginnterminoffset() {
        return this.i_beginnterminoffset;
    }

    public void setI_beginnterminoffset(Integer i_beginnterminoffset) {
        this.i_beginnterminoffset = i_beginnterminoffset;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_sollpreis() {
        return this.n_sollpreis;
    }

    public void setN_sollpreis(BigDecimal n_sollpreis) {
        this.n_sollpreis = n_sollpreis;
    }

    public Short getB_nachtraeglich() {
        return this.b_nachtraeglich;
    }

    public void setB_nachtraeglich(Short b_nachtraeglich) {
        this.b_nachtraeglich = b_nachtraeglich;
    }

    public Short getB_dringend() {
        return this.b_dringend;
    }

    public void setB_dringend(Short b_dringend) {
        this.b_dringend = b_dringend;
    }

    public Integer getMontageart_i_id() {
        return this.montageart_i_id;
    }

    public void setMontageart_i_id(Integer montageart_i_id) {
        this.montageart_i_id = montageart_i_id;
    }

    public Integer getLossollmaterial_i_id_original() {
        return this.lossollmaterial_i_id_original;
    }

    public void setLossollmaterial_i_id_original(Integer lossollmaterial_i_id_original) {
        this.lossollmaterial_i_id_original = lossollmaterial_i_id_original;
    }

    public Integer getI_lfdnummer() {
        return this.i_lfdnummer;
    }

    public void setI_lfdnummer(Integer i_lfdnummer) {
        this.i_lfdnummer = i_lfdnummer;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public BigDecimal getN_menge_stklpos() {
        return this.n_menge_stklpos;
    }

    public void setN_menge_stklpos(BigDecimal n_menge_stklpos) {
        this.n_menge_stklpos = n_menge_stklpos;
    }

    public String getEinheit_c_nr_stklpos() {
        return this.einheit_c_nr_stklpos;
    }

    public void setEinheit_c_nr_stklpos(String einheit_c_nr_stklpos) {
        this.einheit_c_nr_stklpos = einheit_c_nr_stklpos;
    }

    public String getC_position() {
        return this.c_position;
    }

    public void setC_position(String c_position) {
        this.c_position = c_position;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Double getF_dimension1() {
        return this.f_dimension1;
    }

    public void setF_dimension1(Double f_dimension1) {
        this.f_dimension1 = f_dimension1;
    }

    public Double getF_dimension2() {
        return this.f_dimension2;
    }

    public void setF_dimension2(Double f_dimension2) {
        this.f_dimension2 = f_dimension2;
    }

    public Double getF_dimension3() {
        return this.f_dimension3;
    }

    public void setF_dimension3(Double f_dimension3) {
        this.f_dimension3 = f_dimension3;
    }

    public String getC_fehlercode() {
        return this.c_fehlercode;
    }

    public void setC_fehlercode(String c_fehlercode) {
        this.c_fehlercode = c_fehlercode;
    }

    public String getC_fehlertext() {
        return this.c_fehlertext;
    }

    public void setC_fehlertext(String c_fehlertext) {
        this.c_fehlertext = c_fehlertext;
    }

    public Date getT_export_beginn() {
        return this.t_export_beginn;
    }

    public void setT_export_beginn(Date t_export_beginn) {
        this.t_export_beginn = t_export_beginn;
    }

    public Date getT_export_ende() {
        return this.t_export_ende;
    }

    public void setT_export_ende(Date t_export_ende) {
        this.t_export_ende = t_export_ende;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial getFlrlossollmaterial_original() {
        return this.flrlossollmaterial_original;
    }

    public void setFlrlossollmaterial_original(com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial_original) {
        this.flrlossollmaterial_original = flrlossollmaterial_original;
    }

    public Set getIstmaterialset() {
        return this.istmaterialset;
    }

    public void setIstmaterialset(Set istmaterialset) {
        this.istmaterialset = istmaterialset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
