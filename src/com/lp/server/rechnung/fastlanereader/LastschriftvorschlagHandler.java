package com.lp.server.rechnung.fastlanereader;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRLastschriftvorschlag;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

public class LastschriftvorschlagHandler extends UseCaseHandler {
	private static final long serialVersionUID = 5305242505206191477L;

	private static final String FLR_LASTSCHRIFTVORSCHLAG = 
			"flrlastschriftvorschlag";
	private static final String FLR_LASTSCHRIFTVORSCHLAG_FROM_CLAUSE = 
			" from FLRLastschriftvorschlag " + FLR_LASTSCHRIFTVORSCHLAG + " ";
	private static final String FLR_SORTQUERY = "select " + FLR_LASTSCHRIFTVORSCHLAG + ".i_id " 
			+ FLR_LASTSCHRIFTVORSCHLAG_FROM_CLAUSE;

	private String getFromClause() {
		return FLR_LASTSCHRIFTVORSCHLAG_FROM_CLAUSE;
	}
	
	private String buildWhereClause() {
		return buildDefaultWhereClause(FLR_LASTSCHRIFTVORSCHLAG + ".");
	}
	
	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (this.getQuery() != null) {
			SortierKriterium[] kriterien = this.getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(FLR_LASTSCHRIFTVORSCHLAG + "." + kriterien[i].kritName);
							orderBy.append(" ");
							orderBy.append(kriterien[i].value);
						}
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(FLR_LASTSCHRIFTVORSCHLAG)
						.append(".t_faellig")
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LASTSCHRIFTVORSCHLAG + ".t_faellig") < 0) {
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_LASTSCHRIFTVORSCHLAG)
						.append(".t_faellig")
						.append(" ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}
	
	@Override
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;
			
			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();
			Query query = session.createQuery(queryString);
			session = setFilter(session);
			
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			
			while (resultListIterator.hasNext()) {
				FLRLastschriftvorschlag lastschrift = (FLRLastschriftvorschlag) resultListIterator.next();
				FLRRechnungReport rechnung = lastschrift.getFlrrechnungreport();
				
				rows[row][col++] = lastschrift.getI_id();
				rows[row][col++] = rechnung.getFlrrechnungart().getC_nr().substring(0,1);
				rows[row][col++] = rechnung.getC_nr();
				rows[row][col++] = rechnung.getStatus_c_nr();
				rows[row][col++] = rechnung.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();
				rows[row][col++] = lastschrift.getT_faellig();
				rows[row][col++] = lastschrift.getN_rechnungsbetrag_brutto();
				rows[row][col++] = lastschrift.getN_rechnungsbetrag_brutto().subtract(
						lastschrift.getN_bereits_bezahlt());
				rows[row][col++] = rechnung.getWaehrung_c_nr();
				
				PartnerbankDto[] bvLF = getBankFac().partnerbankFindByPartnerIId(
						rechnung.getFlrkunde().getFlrpartner().getI_id(), theClientDto);
				if (bvLF == null || bvLF.length == 0) {
					rows[row][col++] = "";
					rows[row][col++] = "";
					rows[row][col++] = "";
				} else {
					BankDto bankDto = getBankFac().bankFindByPrimaryKey(
							bvLF[0].getBankPartnerIId(), theClientDto);
					rows[row][col++] = bankDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();
					rows[row][col++] = bvLF[0].getCIban();
					rows[row][col++] = bankDto.getCBic();

				}
				rows[row][col++] = lastschrift.getT_gespeichert() != null;
				row++;
				col = 0;
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			sessionClose(session);  
		}
		return result;
	}
	
	@Override
	protected long getRowCountFromDataBase() {
		String countQueryString = "select count(*) " + getFromClause() + buildWhereClause();
		return getRowCountFromDataBaseByQuery(countQueryString);
	}

	@Override
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		return defaultSort(sortierKriterien, selectedId, FLR_SORTQUERY + buildWhereClause() + buildOrderByClause());
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { 
							Integer.class, 
							String.class, 
							String.class,
							String.class, 
							String.class,
							java.util.Date.class,
							BigDecimal.class,
							BigDecimal.class,
							String.class,
							String.class,
							String.class,
							String.class,
							Boolean.class },
					new String[] {
							"Id",
							"",
							getTextRespectUISpr("lp.rechnr", mandantCNr, locUI),
							getTextRespectUISpr("lp.aktuellerstatus", mandantCNr, locUI),
							getTextRespectUISpr("lp.kunde", mandantCNr, locUI),
							getTextRespectUISpr("er.zv.faellig", mandantCNr, locUI),
							getTextRespectUISpr("lp.bruttowert", mandantCNr, locUI),
							getTextRespectUISpr("lp.bruttooffen", mandantCNr, locUI),
							getTextRespectUISpr("lp.waehrung", mandantCNr, locUI),
							getTextRespectUISpr("lp.bankverbindung", mandantCNr, locUI), 
							getTextRespectUISpr("lp.iban", mandantCNr, locUI), 
							getTextRespectUISpr("lp.bic", mandantCNr, locUI), 
							getTextRespectUISpr("rechnung.lastschrift.bereitsgespeichert", mandantCNr, locUI) 
							},
					new int[] { 
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, 
							QueryParameters.FLR_BREITE_XXS,
							QueryParameters.FLR_BREITE_M, 
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M, 
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_PREIS, 
							QueryParameters.FLR_BREITE_WAEHRUNG,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XXS},
					new String[] {
							"i_id",
							Facade.NICHT_SORTIERBAR,
							FinanzFac.FLR_MAHNUNG_FLRRECHNUNGREPORT + "." + RechnungFac.FLR_RECHNUNG_C_NR,
							FinanzFac.FLR_MAHNUNG_FLRRECHNUNGREPORT + "." + RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
							FinanzFac.FLR_MAHNUNG_FLRRECHNUNGREPORT	+ "." + RechnungFac.FLR_RECHNUNG_FLRKUNDE + "."	+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							"t_faellig",
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							"t_gespeichert"
							}));
		}
		
		return super.getTableInfo();
	}
}
