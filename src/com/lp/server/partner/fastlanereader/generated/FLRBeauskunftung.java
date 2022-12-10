package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRBeauskunftung implements Serializable {

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer partner_i_id;

	/** nullable persistent field */
	private Short b_kostenpflichtig;

	/** nullable persistent field */
	private Date t_anlegen;

	/** nullable persistent field */
	private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

	/** nullable persistent field */
	private FLRPersonal flrpersonal_anlegen;

	/** nullable persistent field */
	private com.lp.server.partner.fastlanereader.generated.FLRIdentifikation flridentifikation;

	/** full constructor */
	public FLRBeauskunftung(
			Integer partner_i_id,
			Short b_kostenpflichtig,
			Date t_anlegen,
			com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner,
			FLRPersonal flrpersonal_anlegen,
			com.lp.server.partner.fastlanereader.generated.FLRIdentifikation flridentifikation) {
		this.partner_i_id = partner_i_id;
		this.b_kostenpflichtig = b_kostenpflichtig;
		this.t_anlegen = t_anlegen;
		this.flrpartner = flrpartner;
		this.flrpersonal_anlegen = flrpersonal_anlegen;
		this.flridentifikation = flridentifikation;

	}

	/** default constructor */
	public FLRBeauskunftung() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getPartner_i_id() {
		return this.partner_i_id;
	}

	public void setPartner_i_id(Integer partner_i_id) {
		this.partner_i_id = partner_i_id;
	}

	public Short getB_kostenpflichtig() {
		return this.b_kostenpflichtig;
	}

	public void setB_kostenpflichtig(Short b_kostenpflichtig) {
		this.b_kostenpflichtig = b_kostenpflichtig;
	}

	public Date getT_anlegen() {
		return this.t_anlegen;
	}

	public void setT_anlegen(Date t_anlegen) {
		this.t_anlegen = t_anlegen;
	}

	public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
		return this.flrpartner;
	}

	public void setFlrpartner(
			com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
		this.flrpartner = flrpartner;
	}

	public com.lp.server.partner.fastlanereader.generated.FLRIdentifikation getFlridentifikation() {
		return this.flridentifikation;
	}

	public void setFlridentifikation(
			com.lp.server.partner.fastlanereader.generated.FLRIdentifikation flridentifikation) {
		this.flridentifikation = flridentifikation;
	}

	public FLRPersonal getFlrpersonal_anlegen() {
		return this.flrpersonal_anlegen;
	}

	public void setFlrpersonal_anlegen(FLRPersonal flrpersonal_anlegen) {
		this.flrpersonal_anlegen = flrpersonal_anlegen;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
