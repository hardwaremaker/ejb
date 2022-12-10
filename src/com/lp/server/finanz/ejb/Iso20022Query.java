package com.lp.server.finanz.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.BankverbindungId;

public class Iso20022Query {

	public static final String StandardAll = "Iso20022StandardFindAll";
	public static final String StandardFindByCNr = "Iso20022StandardFindByCNr";
	public static final String ZahlungsauftragSchemaByStandardCNr = "Iso20022ZahlungsauftragSchemaFindByStandardCNr";
	public static final String LastschriftSchemaByStandardCNr = "Iso20022LastschriftSchemaFindByStandardCNr";
	public static final String BankverbindungByBankverbindungIId = "Iso20022BankverbindungFindByBankverbindungIId";
	
	public static List<Iso20022Standard> standardListAll(EntityManager em) {
		return HvTypedQuery.<Iso20022Standard>namedQuery(em, StandardAll).getResultList();
	}
	
	public static Iso20022Standard standardFindByStandardEnumNoExc(EntityManager em, Iso20022StandardEnum standardEnum) {
		return HvTypedQuery.<Iso20022Standard>namedQuery(em, StandardFindByCNr, standardEnum.name()).getSingleResultNoEx();
	}
	
	public static List<Iso20022ZahlungsauftragSchema> zahlungsauftragSchemasListByStandard(EntityManager em, Iso20022StandardEnum standardEnum) {
		return HvTypedQuery.<Iso20022ZahlungsauftragSchema>namedQuery(em, ZahlungsauftragSchemaByStandardCNr, standardEnum.name()).getResultList();
	}

	public static List<Iso20022LastschriftSchema> lastschriftSchemasListByStandard(EntityManager em, Iso20022StandardEnum standardEnum) {
		return HvTypedQuery.<Iso20022LastschriftSchema>namedQuery(em, LastschriftSchemaByStandardCNr, standardEnum.name()).getResultList();
	}
	
	public static Iso20022Bankverbindung bankverbindungFindByBankverbindungIIdNoEx(EntityManager em, BankverbindungId id) {
		return HvTypedQuery.<Iso20022Bankverbindung>namedQuery(em, BankverbindungByBankverbindungIId, id.id()).getSingleResultNoEx();
	}
}
