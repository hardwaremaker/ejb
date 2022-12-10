package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ZeilensummenEinesTages implements Serializable {

		public java.sql.Timestamp tVon;

		public java.sql.Timestamp tBis;

		public double dDauer = 0;

		public Integer diaetenIId;

		public Integer diaetenIIdHaupteintrag;

		public String lkz;
		public java.sql.Timestamp tDatum;

		public boolean haupteintrag;

		public BigDecimal bdBetrag;
		
		public static ZeilensummenEinesTages clone(ZeilensummenEinesTages orig) {
			ZeilensummenEinesTages klon = new ZeilensummenEinesTages();
			klon.tVon=orig.tVon;
			klon.tBis=orig.tBis;
			klon.dDauer=orig.dDauer;
			klon.diaetenIId=orig.diaetenIId;
			klon.diaetenIIdHaupteintrag=orig.diaetenIIdHaupteintrag;
			klon.lkz=orig.lkz;
			klon.tDatum=orig.tDatum;
			klon.haupteintrag=orig.haupteintrag;
			klon.bdBetrag=orig.bdBetrag;
			return klon;
		}	

}
