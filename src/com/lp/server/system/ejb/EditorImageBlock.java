package com.lp.server.system.ejb;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorImageBlockDto;
import com.lp.server.system.service.HvImageDto;
import com.lp.server.system.service.HvImageDtoAssembler;
import com.lp.server.util.EditorBlockIId;
import com.lp.util.EditorBlockAlignment;

@Entity(name = "LP_EDITOR_IMAGE_BLOCK")
public class EditorImageBlock extends EditorBaseBlock {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "LP_IMAGE_I_ID")
	private HvImage image;

	/*
	 * Groesse des Bildes in mm.
	 */
	@Column(name = "WIDTH")
	private float width;

	@Column(name = "HEIGHT")
	private float height;

	@Column(name = "ALIGNMENT")
	@Enumerated(EnumType.STRING)
	private EditorBlockAlignment alignment;

	protected EditorImageBlock() {
	}

	public EditorImageBlock(EditorBlockIId iid) {
		super(iid);
	}

	public HvImage getImage() {
		return image;
	}

	public void setImage(HvImage image) {
		this.image = image;
	}

	/**
	 * @return Breite des Bildes in mm
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width Breite des Bildes in mm
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return H&ouml;he des Bildes in mm
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height neue H&ouml;he des Bildes in mm
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	public EditorBlockAlignment getAlignment() {
		return alignment;
	}

	public void setAlignment(EditorBlockAlignment alignment) {
		this.alignment = alignment;
	}

	@Override
	public EditorBaseBlockDto toDto() {
		HvImageDto imageDto = HvImageDtoAssembler.assembleHvImageDto(getImage());
		return new EditorImageBlockDto(getRow(), getColumn(), imageDto).setWidth(getWidth()).setHeight(getHeight())
				.setAlignment(alignment).setId(getIid());
	}

	@Override
	protected void loadFromDto(EditorBaseBlockDto dto) {
		super.loadFromDto(dto);
		EditorImageBlockDto imageBlock = (EditorImageBlockDto) dto;
		setWidth(imageBlock.getWidth());
		setHeight(imageBlock.getHeight());
		setImage(HvImage.imageFromDto(imageBlock.getImageDto()));
		setAlignment(imageBlock.getAlignment());
	}
}
