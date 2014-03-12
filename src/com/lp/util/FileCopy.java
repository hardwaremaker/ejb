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
package com.lp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;

public class FileCopy {
	long chunckSizeInBytes;
	boolean verbose;

	public FileCopy() {
		this.chunckSizeInBytes = 1024 * 1024; // Standard: Buffer 1MB
		this.verbose = false; // Statistics about Copy Process
	}

	public FileCopy(boolean verbose) {
		this.chunckSizeInBytes = 1024 * 1024; // Standard: Buffer 1MB
		this.verbose = verbose; // Statistics about Copy Process
	}

	public FileCopy(long chunckSizeInBytes) {
		this.chunckSizeInBytes = chunckSizeInBytes; // Custom Buffer (Bytes)
		this.verbose = false; // Statistics about Copy Process
	}

	public FileCopy(long chunckSizeInBytes, boolean verbose) {
		this.chunckSizeInBytes = chunckSizeInBytes; // Custom Buffer (Bytes)
		this.verbose = verbose; // Statistics about Copy Process
	}

	public void copy(File source, File destination) {
		try {
			FileInputStream fileInputStream = new FileInputStream(source);
			FileOutputStream fileOutputStream = new FileOutputStream(
					destination);
			FileChannel inputChannel = fileInputStream.getChannel();
			FileChannel outputChannel = fileOutputStream.getChannel();
			transfer(inputChannel, outputChannel, source.length(), false);
			fileInputStream.close();
			fileOutputStream.close();
			destination.setLastModified(source.lastModified());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void transfer(FileChannel fileChannel, ByteChannel byteChannel,
			long lengthInBytes, boolean verbose) throws IOException {
		long overallBytesTransfered = 0L;
		long time = -System.currentTimeMillis();
		while (overallBytesTransfered < lengthInBytes) {
			long bytesTransfered = 0L;
			bytesTransfered = fileChannel.transferTo(overallBytesTransfered,
					Math.min(chunckSizeInBytes, lengthInBytes
							- overallBytesTransfered), byteChannel);
			overallBytesTransfered += bytesTransfered;
			if (verbose) {
				System.out.println("overall bytes transfered: "
						+ overallBytesTransfered
						+ " progress "
						+ (Math.round(overallBytesTransfered
								/ ((double) lengthInBytes) * 100.0)) + "%");
			}
		}
		time += System.currentTimeMillis();
		if (verbose) {
			System.out.println("Transfered: " + overallBytesTransfered
					+ " bytes in: " + (time / 1000) + " s -> "
					+ (overallBytesTransfered / 1024.0) / (time / 1000.0)
					+ " kbytes/s");
		}
	}
}
