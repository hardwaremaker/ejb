package com.lp.server.finanz.ejbfac;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SteuerkontoInfo implements Serializable {
	private static final long serialVersionUID = 3198512624638809240L;
	
	private Integer kontoId ;
	private Integer mwstsatzbezId ;
	private Integer reversechargeartId ;
	
	private List<Integer> otherMwstsatzbezId ;
	private List<Integer> otherReversechargeartId ;
	
	public SteuerkontoInfo(Integer kontoId, Integer reversechargeartId, Integer mwstsatzbezId) {
		this.kontoId = kontoId ;
		this.mwstsatzbezId = mwstsatzbezId ;
		this.reversechargeartId = reversechargeartId ;
		this.otherMwstsatzbezId = new ArrayList<Integer>();
		this.otherReversechargeartId = new ArrayList<Integer>() ;
	}
	
	public Integer getKontoId() {
		return kontoId;
	}

	public void setKontoId(Integer kontoId) {
		this.kontoId = kontoId;
	}

	public Integer getMwstsatzbezId() {
		return mwstsatzbezId;
	}

	public void setMwstsatzbezId(Integer mwstsatzbezId) {
		this.mwstsatzbezId = mwstsatzbezId;
	}

	public Integer getReversechargeartId() {
		return reversechargeartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}

	public List<Integer> getOtherMwstsatzbezId() {
		return otherMwstsatzbezId ;
	}

	public List<Integer> getOtherReversechargeartId() {
		return otherReversechargeartId ;
	}	
	
	/**
	 * Eine doppelte Reversechargeart / mwstsatzbezId  speichern</br>
	 * <p>Die Intention ist, dass offensichtlich schon jemand
	 * anderer festgestellt hat, dass bereits eine Id
	 * vorhanden ist. Deshalb soll die bereits vorhandene und
	 * die nun hinzuzufuegende Id in die Liste der "otherIds" 
	 * wandern.</p>
	 * <p>Dabei gilt es zu beruecksichtigen, dass wegen der 
	 * temporaeren Aenderung der Mwstsaetze im Juli - Dezember 2020 
	 * zu spaeteren Zeitpunkten vermeintliche doppelte Werte eben doch
	 * nicht als Duplikate interpretiert werden sollen.</p>
	 * <p>Prinzipiell soll gelten, dass fuer den Fall, dass es
	 * echte Duplikate gibt, kein "richtiger" Steuersatz/Reversecharge
	 * ausgegeben werden soll. Wenn es zwei Werte gibt, ist egal
	 * welcher genommen wird, es sicher der falsche in der jeweiligen
	 * Situation.</p>
	 * 
	 * <p>TODO Es stellt sich die Frage, warum jemand vorher schon
	 * entschieden hat, dass es ein "Duplikat" ist (die Methode wird
	 * aufgerufen). Ist es nicht sinnvoller hier zu entscheiden, ob
	 * es mehr als einen Wert gibt?</p>
	 * 
	 * @param reversechargeartId
	 * @param mwstsatzbezId
	 */
	public void addDuplicate(Integer reversechargeartId, Integer mwstsatzbezId) {
		if (this.reversechargeartId == null) {
			addIfNotKnown(getOtherReversechargeartId(), new Integer(reversechargeartId));			
		} else {
			if (!this.reversechargeartId.equals(reversechargeartId)) {
				addIfNotKnown(getOtherReversechargeartId(), new Integer(this.reversechargeartId));			
				addIfNotKnown(getOtherReversechargeartId(), new Integer(reversechargeartId));
				this.reversechargeartId = null ;
			}
		}

		if (this.mwstsatzbezId == null) {
			addIfNotKnown(getOtherMwstsatzbezId(), new Integer(mwstsatzbezId));			
		} else {
			if (!this.mwstsatzbezId.equals(mwstsatzbezId)) {
				addIfNotKnown(getOtherMwstsatzbezId(), new Integer(this.mwstsatzbezId));
				addIfNotKnown(getOtherMwstsatzbezId(), new Integer(mwstsatzbezId));
				this.mwstsatzbezId = null;
			}
		}
/*		
		if(this.reversechargeartId != null) {
			addIfNotKnown(getOtherReversechargeartId(), new Integer(this.reversechargeartId));
//			getOtherReversechargeartId().add(this.reversechargeartId) ;
			this.reversechargeartId = null ;
		}
		addIfNotKnown(getOtherReversechargeartId(), new Integer(reversechargeartId));
//		getOtherReversechargeartId().add(reversechargeartId) ;
		
		if(this.mwstsatzbezId != null) {
			addIfNotKnown(getOtherMwstsatzbezId(), new Integer(this.mwstsatzbezId));
//			getOtherMwstsatzbezId().add(this.mwstsatzbezId) ;
			this.mwstsatzbezId = null ;
		}
		addIfNotKnown(getOtherMwstsatzbezId(), new Integer(mwstsatzbezId));
//		getOtherMwstsatzbezId().add(mwstsatzbezId);

 */
	}
	
	private void addIfNotKnown(List<Integer> list, Integer value) {
		if(!list.contains(value)) {
			list.add(value) ;
		}
	}
}
