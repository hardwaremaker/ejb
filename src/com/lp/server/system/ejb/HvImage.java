package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.HvImageDto;
import com.lp.server.util.HvImageIId;

@Entity(name = "LP_IMAGE")
public class HvImage {
	@Id
	@Column(name = "I_ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "lp_sequence_generator")
	@TableGenerator(name = "lp_sequence_generator", table = "LP_PRIMARYKEY", pkColumnName = "C_NAME", pkColumnValue = PKConst.PK_HVIMAGE, valueColumnName = "I_INDEX", allocationSize = 10)
	private Integer id;

	@Column(name = "B_DATA")
	private byte[] imageData;

	@Column(name = "DATENFORMAT_C_NR")
	private String imageType;

	public HvImageIId getId() {
		return new HvImageIId(id);
	}

	public void setId(HvImageIId id) {
		this.id = id.id();
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public static HvImage imageFromDto(HvImageDto dto) {
		HvImage img = new HvImage();
		img.setId(dto.getId());
		img.setImageData(dto.getImageBinaryData());
		img.setImageType(dto.getImageFormat());
		return img;
	}

}
