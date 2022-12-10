package com.lp.server.system.automatikjob;

import java.sql.Timestamp;

import com.lp.server.shop.ejbfac.JobDetailsSyncItemDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.AutomatikjobId;
import com.lp.server.util.WebshopId;

public class AutomatikjobWebArtikelAenderungen extends AutomatikjobBasis {

	private JobDetailsSyncItemDto detailsDto;
	private AutomatikjobId automatikjobId;

	public AutomatikjobWebArtikelAenderungen(AutomatikjobId automatikjobId) {
		this.automatikjobId = automatikjobId;		
	}

	private boolean initJobDetailsJobDto(TheClientDto theClientDto) {
		detailsDto = getJobdetailsSyncItemFac()
				.findByPrimaryKey(automatikjobId.id());
		if (detailsDto == null) {
			myLogger.error("Details Automatikjob Artikelaenderungen not found in DB");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean performJob(TheClientDto theClientDto) {
		if(initJobDetailsJobDto(theClientDto)) {
			Timestamp now = getTimestamp();
			WebshopId shopId = detailsDto.getShopId();
			
			getSyncShopFac(shopId).pushTaxClasses(shopId, theClientDto);
			getSyncShopFac(shopId).pushCustomerGroups(shopId, theClientDto);
			
			getSyncShopFac(shopId).pushChangedCategories(shopId,  detailsDto.getTGeaendert(), theClientDto);
			getSyncShopFac(shopId).pushChangedItems(shopId, detailsDto.getTGeaendert(), theClientDto);
			detailsDto.setTGeaendert(now);			
			getJobdetailsSyncItemFac().update(detailsDto);
			return false;
		}
		
		return true;
	}
}
