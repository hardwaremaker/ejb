package com.lp.server.system.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import com.lp.util.EditorBlockAlignment;

public class EditorImageBlockDto extends EditorBaseBlockDto {

	private static final long serialVersionUID = 1L;

	private HvImageDto imageDto;

	private float width;
	private float height;
	private EditorBlockAlignment alignment;

	public EditorImageBlockDto(int row, int column, HvImageDto image) {
		super(row, column);
		this.imageDto = image;
	}

	public EditorImageBlockDto() {
	}

	public HvImageDto getImageDto() {
		return imageDto;
	}

	public BufferedImage getScaledImage() {
		// mm -> px
		int widthPx = mm2px(getWidth());
		int heightPx = mm2px(getHeight());
		return scaleImage(getImageDto().getBufferedImage(),
				widthPx, heightPx, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
/*		
		return scaleImage(getImageDto().getBufferedImage(),
				(int) getWidth(), (int) getHeight(), 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
*/				
	}
	
	private int mm2px(float mm) {
		return (int) ((mm * 72.0) / 25.4);
	}
	
	private BufferedImage scaleImage(BufferedImage src, int width, int height, Object interpolation) {
		if (width < 0) {
			if (height < 0) {
				return src;
			}
			width = (int) (src.getWidth() * ((double) height) / src.getHeight());
		} else if (height < 0) {
			height = (int) (src.getHeight() * ((double) width) / src.getWidth());
		}
		int type = src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage newImg = new BufferedImage(width, height, type);
		Graphics2D g2d = newImg.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
		g2d.drawImage(src, 0, 0, width, height, null);
		g2d.dispose();
		return newImg;
	}
	
	public EditorImageBlockDto setImageDto(HvImageDto imageDto) {
		this.imageDto = imageDto;
		return this;
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
	public EditorImageBlockDto setWidth(float width) {
		this.width = width;
		return this;
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
	public EditorImageBlockDto setHeight(float height) {
		this.height = height;
		return this;
	}

	public EditorBlockAlignment getAlignment() {
		return alignment;
	}

	public EditorImageBlockDto setAlignment(EditorBlockAlignment alignment) {
		this.alignment = alignment;
		return this;
	}

}
