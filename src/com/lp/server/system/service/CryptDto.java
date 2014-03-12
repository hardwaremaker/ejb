/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.system.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class CryptDto {

	public CryptDto() {
		super();
	}

	public byte[] decodeDES(String in) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			SecureRandom sr = new SecureRandom("1234567890".getBytes());
			kg.init(56, sr);
			Key k=kg.generateKey();
			Cipher c;
			try {
				c = Cipher.getInstance("DES");
				c.init(Cipher.ENCRYPT_MODE, k);
				byte[] bin;
				try {
					bin = in.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				}
				byte[] temp = c.update(bin);
				byte[] temp1 = c.doFinal();
				byte[] out = concatBytes(temp, temp1);
				return out;

			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] encodeDES(byte[] in) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			SecureRandom sr = new SecureRandom("1234567890".getBytes());
			kg.init(56, sr);
			Key k=kg.generateKey();
			System.out.println(k.toString());
			Cipher c;
			try {
				c = Cipher.getInstance("DES");
				c.init(Cipher.DECRYPT_MODE,k);
				byte[] temp = c.update(in);
				byte[] temp1 = c.doFinal();
				byte[] out = concatBytes(temp,temp1);
				return out;
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] concatBytes(byte[] in1, byte[] in2) {
		byte[] out = new byte[in1.length + in2.length];
		System.arraycopy(in1, 0, out, 0, in1.length);
		System.arraycopy(in2, 0, out, in1.length, in2.length);
		return out;	
	}


	/** 
	 * in mit privatem key verschl&uuml;sseln
	 * 
	 * @param in
	 * @param encodedPrivateKey
	 * @return codierte Daten
	 */
	public byte[] encodeRSA(String in, byte[] encodedPrivateKey) {
		Cipher c;
		try {
			c = Cipher.getInstance("RSA");
			PKCS8EncodedKeySpec pubKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privKey = keyFactory.generatePrivate(pubKeySpec);
			try {
				c.init(Cipher.ENCRYPT_MODE, privKey);
				byte[] bin;
				try {
					bin = in.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				}
				byte[] temp = c.update(bin);
				byte[] temp1 = c.doFinal();
				byte[] out = concatBytes(temp, temp1);
				return out;
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * in mit publicKey entschl&uuml;sseln
	 * 
	 * @param in
	 * @param encodedPublicKey
	 * @return decodierte Daten
	 */
	public byte[] decodeRSA(byte[] in, byte[] encodedPublicKey) {
		Cipher c;
		try {
			c = Cipher.getInstance("RSA");
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
			try {
				c.init(Cipher.DECRYPT_MODE, pubKey);
				byte[] temp = c.update(in);
				byte[] temp1 = c.doFinal();
				byte[] out = concatBytes(temp,temp1);
				return out;
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

}
