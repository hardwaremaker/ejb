package com.lp.server.system.automatikjob;

import com.lp.server.shop.ejbfac.JobDetailsSyncOrderDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.AutomatikjobId;
import com.lp.server.util.WebshopId;

public class AutomatikjobWebBestellungenVerarbeiten extends AutomatikjobBasis {

	private JobDetailsSyncOrderDto detailsDto;
	private AutomatikjobId automatikjobId;
	
	public AutomatikjobWebBestellungenVerarbeiten(AutomatikjobId automatikjobId) {
		this.automatikjobId = automatikjobId;
	}

	
	private boolean initJobDetailsJobDto(TheClientDto theClientDto) {
		detailsDto = getJobdetailsSyncOrderFac()
				.findByPrimaryKey(automatikjobId.id());
		if (detailsDto == null) {
			myLogger.error("Details Automatikjob Bestellungverarbeiten not found in DB");
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean performJob(TheClientDto theClientDto) {
		if(initJobDetailsJobDto(theClientDto)) {
			WebshopId shopId = detailsDto.getShopId();
			getSyncShopFac(shopId).fetchOrders(shopId, theClientDto);
			return false;
		}
		
		return true;
	}
}
