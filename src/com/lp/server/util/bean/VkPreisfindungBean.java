package com.lp.server.util.bean;

import com.lp.server.artikel.ejbfac.VkPreisfindungFacBean;
import com.lp.server.artikel.service.VkPreisfindungFac;

public class VkPreisfindungBean extends HvBean<VkPreisfindungFac> {
	public VkPreisfindungBean() {
		super(VkPreisfindungFacBean.class, VkPreisfindungFac.class);
	}
}
