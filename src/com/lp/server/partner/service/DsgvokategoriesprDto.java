package com.lp.server.partner.service;

import java.io.Serializable;

public class DsgvokategoriesprDto implements Serializable {
	 
		private static final long serialVersionUID = 1L;
		private String localeCNr;
		private Integer dsgvokategorieIId;
		private String cBez;

		public String getLocaleCNr() {
			return localeCNr;
		}

		public void setLocaleCNr(String localeCNr) {
			this.localeCNr = localeCNr;
		}

		public String getCBez() {
			return cBez;
		}

		public Integer getDsgvokategorieIId() {
			return dsgvokategorieIId;
		}

		public void setCBez(String cBez) {
			this.cBez = cBez;
		}

		public void setDsgvokategorieIId(Integer dsgvokategorieIId) {
			this.dsgvokategorieIId = dsgvokategorieIId;
		}
}
