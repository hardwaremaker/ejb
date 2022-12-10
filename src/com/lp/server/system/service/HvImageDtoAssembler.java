package com.lp.server.system.service;

import com.lp.server.system.ejb.HvImage;

public class HvImageDtoAssembler {
	public static HvImageDto assembleHvImageDto(HvImage bean) {
		HvImageDto dto = new HvImageDto(bean.getImageData(), bean.getImageType());
		dto.setId(bean.getId());
		return dto;
	}

	public static HvImageDto[] assembleHvImageDtos(HvImage[] beans) {
		HvImageDto[] dtos = new HvImageDto[beans.length];
		for (int i = 0; i < dtos.length; i++) {
			dtos[i] = assembleHvImageDto(beans[i]);
		}
		return dtos;
	}
}
