package com.lp.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Ein FileVisitor, der einen Ordner rekursiv l&ouml;scht. Mit
 * {@link Files#walkFileTree(Path, java.nio.file.FileVisitor)} verwenden
 * 
 * @author Alexander Daum
 *
 */
public class FileDeleteVisitor extends SimpleFileVisitor<Path> {
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if (exc != null) {
			throw exc;
		}
		Files.delete(dir);
		return FileVisitResult.CONTINUE;
	}
}
