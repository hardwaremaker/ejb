package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.lp.server.personal.service.MaschineDto;
import com.lp.util.Helper;

/**
 * Daten zum Vergleich von Soll Zeitdaten und Maschinen im Arbeitsplan und den
 * gebuchten Istzeitdaten und Maschinen im Los
 * 
 * @author Alexander Daum
 *
 */
public class LosArbeitsplanZeitVergleichDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID des StuecklisteArbeitsplans
	 */
	private final int stuecklisteArbeitsplanIId;
	/**
	 * Arbeitsgangnummer
	 */
	private final int iArbeitsgang;
	/**
	 * ID des sollarbeitsplans
	 */
	private final int losSollarbeitsplanIId;

	private final long sollRuestzeit;
	private final long sollStueckzeit;
	private final int aufspannung;

	private final BigDecimal menge;

	private final BigDecimal istZeitMaschine;
	private final BigDecimal istZeitPerson;

	private final int sollmaschineIId;
	private final String cSollmaschine;

	private final boolean hatMaschine;

	/**
	 * Map von ID zu Name von Istmaschinen
	 */
	private final Map<Integer, String> istmaschinen;

	public int getiArbeitsgang() {
		return iArbeitsgang;
	}

	public int getiLosSollarbeitsplanIId() {
		return losSollarbeitsplanIId;
	}

	public String getcSollmaschine() {
		return cSollmaschine;
	}

	public Map<Integer, String> getIstmaschinenMap() {
		return Collections.unmodifiableMap(istmaschinen);
	}

	public Collection<String> getIstmaschinenBez() {
		return Collections.unmodifiableCollection(istmaschinen.values());
	}

	public LosArbeitsplanZeitVergleichDto(boolean hatMaschine, int iArbeitsgang, int iLosSollarbeitsplanIId,
			String cSollmaschine, int iSollmaschineIId, int iStuecklisteAP, long sollRuestzeit, long sollStueckzeit,
			int aufspannung, BigDecimal menge, BigDecimal istZeitPerson, BigDecimal istZeitMaschine,
			Map<Integer, String> istMaschinenMap) {
		this.aufspannung = aufspannung;
		this.hatMaschine = hatMaschine;
		this.stuecklisteArbeitsplanIId = iStuecklisteAP;
		this.iArbeitsgang = iArbeitsgang;
		this.losSollarbeitsplanIId = iLosSollarbeitsplanIId;
		this.sollRuestzeit = sollRuestzeit;
		this.sollStueckzeit = sollStueckzeit;
		this.menge = Helper.getOrDefault(menge, BigDecimal.ZERO);
		this.sollmaschineIId = iSollmaschineIId;
		this.cSollmaschine = cSollmaschine;
		this.istZeitPerson = Helper.getOrDefault(istZeitPerson, BigDecimal.ZERO);
		this.istZeitMaschine = Helper.getOrDefault(istZeitMaschine, BigDecimal.ZERO);
		this.istmaschinen = Helper.getOrDefault(istMaschinenMap, ImmutableMap.of());
	}

	public static LosArbeitsplanZeitVergleichDto create(int arbeitsgang, LossollarbeitsplanDto sollarbeitsplan,
			Optional<MaschineDto> maschine, long sollRuestzeit, long sollStueckzeit, BigDecimal menge,
			BigDecimal istZeitPerson, BigDecimal istZeitMaschine, int stuecklisteArbeitsplanIId,
			List<MaschineDto> maschinen) {
		boolean hatMaschine = maschine.isPresent();
		String sollmaschineBez = "";
		int sollmaschineIId = -1;
		if (hatMaschine) {
			sollmaschineBez = maschine.get().getBezeichnung();
			sollmaschineIId = maschine.get().getIId();
		}
		Integer iAufspannung = sollarbeitsplan.getIAufspannung();
		int aufspannung = iAufspannung == null ? 0 : iAufspannung;

		Map<Integer, String> maschinenMap = maschinen.stream()
				.collect(Collectors.toMap(MaschineDto::getIId, MaschineDto::getBezeichnung));

		return new LosArbeitsplanZeitVergleichDto(hatMaschine, arbeitsgang, sollarbeitsplan.getIId(), sollmaschineBez,
				sollmaschineIId, stuecklisteArbeitsplanIId, sollRuestzeit, sollStueckzeit, aufspannung, menge,
				istZeitPerson, istZeitMaschine, maschinenMap);
	}

	public int getiSollmaschineIId() {
		return sollmaschineIId;
	}

	public long getSollRuestzeit() {
		return sollRuestzeit;
	}

	public BigDecimal getIstZeitMaschine() {
		return istZeitMaschine;
	}

	public int getStuecklisteArbeitsplanIId() {
		return stuecklisteArbeitsplanIId;
	}

	public long getSollStueckzeit() {
		return sollStueckzeit;
	}

	public BigDecimal getMenge() {
		return menge;
	}

	public BigDecimal getIstZeitPerson() {
		return istZeitPerson;
	}

	public boolean hatMaschine() {
		return hatMaschine;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LosArbeitsplanZeitVergleichDto other = (LosArbeitsplanZeitVergleichDto) obj;
		if (cSollmaschine == null) {
			if (other.cSollmaschine != null)
				return false;
		} else if (!cSollmaschine.equals(other.cSollmaschine))
			return false;
		if (iArbeitsgang != other.iArbeitsgang)
			return false;
		if (losSollarbeitsplanIId != other.losSollarbeitsplanIId)
			return false;
		if (istmaschinen == null) {
			if (other.istmaschinen != null)
				return false;
		} else if (!istmaschinen.equals(other.istmaschinen))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cSollmaschine == null) ? 0 : cSollmaschine.hashCode());
		result = prime * result + iArbeitsgang;
		result = prime * result + losSollarbeitsplanIId;
		result = prime * result + ((istmaschinen == null) ? 0 : istmaschinen.hashCode());
		return result;
	}

	public BigDecimal getSollGesamtArbeitszeit() {
		return Helper.berechneGesamtzeitInStunden(sollRuestzeit, sollStueckzeit, menge, BigDecimal.ZERO, aufspannung);
	}

}
