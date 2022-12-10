package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class LosInfosFuerWerteAusUnterlosen implements Serializable{
	public LosDto losDto = null;
	public LosablieferungDto laDto = null;
	public Double dPersonalzeiten = null;
	public Double dMaschinenzeiten = null;
	public BigDecimal bdGesamtAbgeliefert = null;

}
