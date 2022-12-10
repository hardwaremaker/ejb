package com.lp.server.system.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.lp.server.util.HvImageIId;
import com.lp.util.Helper;

public class HvImageDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private HvImageIId id = new HvImageIId();
	private byte[] imageData;
	private String imageMimeFormat;

	private transient BufferedImage image;

	public HvImageDto() {
	}

	public HvImageDto(byte[] binaryImage, String imageMimeFormat) {
		setImageBinaryData(binaryImage, imageMimeFormat);
	}

	public HvImageDto(BufferedImage image, String imageMimeFormat) {
		setBufferedImage(image, imageMimeFormat);
	}

	public HvImageIId getId() {
		return id;
	}

	public void setId(HvImageIId id) {
		this.id = id;
	}
	
	public String getImageFormat() {
		return imageMimeFormat;
	}

	public byte[] getImageBinaryData() {
		updateSerializedData();
		return imageData;
	}

	public void setImageBinaryData(byte[] imageData, String imageMimeFormat) {
		this.imageData = imageData;
		setImageFormat(imageMimeFormat);
		image = null;
	}

	public BufferedImage getBufferedImage() {
		updateBufferedImage();
		return image;
	}

	public void setBufferedImage(BufferedImage image, String imageMimeFormat) {
		this.image = image;
		setImageFormat(imageMimeFormat);
		imageData = null;
	}

	private void setImageFormat(String imageFormat) {
		if (!imageFormat.startsWith("image/")) {
			throw new IllegalArgumentException("Erwartet: MIME Format image/xxx, erhalten: " + imageFormat);
		}
		this.imageMimeFormat = imageFormat;
	}

	private void updateBufferedImage() {
		if (image == null && imageData != null) {
			image = Helper.byteArrayToImage(imageData);
		}
	}

	private void updateSerializedData() {
		if (imageData == null && image != null) {
			String imageFormatCode = imageMimeFormat.substring("image/".length());
			imageData = Helper.imageToByteArrayWithType(image, imageFormatCode);
		}
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		updateSerializedData();
		oos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}

}
