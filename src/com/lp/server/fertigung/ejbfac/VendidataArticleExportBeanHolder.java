package com.lp.server.fertigung.ejbfac;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.system.service.MandantFac;

public class VendidataArticleExportBeanHolder {
	
	private ArtikelFac artikelFac;
	private VkPreisfindungFac vkPreisfindungFac;
	private MandantFac mandantFac;

	public VendidataArticleExportBeanHolder(ArtikelFac artikelFac, VkPreisfindungFac vkPreisfindungFac, MandantFac mandantFac) {
		this.artikelFac = artikelFac;
		this.vkPreisfindungFac = vkPreisfindungFac;
		this.mandantFac = mandantFac;
	}

	public ArtikelFac getArtikelFac() {
		return artikelFac;
	}

	public VkPreisfindungFac getVkPreisfindungFac() {
		return vkPreisfindungFac;
	}

	public MandantFac getMandantFac() {
		return mandantFac;
	}

}
