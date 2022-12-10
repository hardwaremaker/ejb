package com.lp.server.system.jcr.service;

import java.io.Serializable;

public class FehlerVersandauftraegeDto implements Serializable {
	private long iFehlgeschlagenGesamt = 0;
	private long iFehlgeschlagenMeine = 0;

	private long iOffenGesamt = 0;
	private long iOffenMeine = 0;

	public long getIFehlgeschlagenGesamt() {
		return iFehlgeschlagenGesamt;
	}

	public void setIFehlgeschlagenGesamt(long iFehlgeschlagenGesamt) {
		this.iFehlgeschlagenGesamt = iFehlgeschlagenGesamt;
	}

	public long getIFehlgeschlagenMeine() {
		return iFehlgeschlagenMeine;
	}

	public void setIFehlgeschlagenMeine(long iFehlgeschlagenMeine) {
		this.iFehlgeschlagenMeine = iFehlgeschlagenMeine;
	}

	public long getIOffenGesamt() {
		return iOffenGesamt;
	}

	public void setIOffenGesamt(long iOffenGesamt) {
		this.iOffenGesamt = iOffenGesamt;
	}

	public long getIOffenMeine() {
		return iOffenMeine;
	}

	public void setIOffenMeine(long iOffenMeine) {
		this.iOffenMeine = iOffenMeine;
	}

	public boolean sindOffeneOderFehlgschlageneVorhanden() {
		if (iFehlgeschlagenGesamt > 0 || iOffenGesamt > 0) {
			return true;
		} else {
			return false;
		}

	}

}
