package com.lp.service.plscript;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

public class CustomClassloaderJavaFileManager implements JavaFileManager {
	private final ClassLoader classLoader;
	private final StandardJavaFileManager standardFileManager;
	private final PackageInternalsFinder finder;
	// Internal map of filename URIs to JavaFileObjects.
	private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();
	
	public CustomClassloaderJavaFileManager(ClassLoader classLoader, StandardJavaFileManager standardFileManager) {
		this.classLoader = classLoader;
		this.standardFileManager = standardFileManager;
		finder = new PackageInternalsFinder(this.classLoader);
	}
	
	@Override
	public int isSupportedOption(String option) {
		return standardFileManager.isSupportedOption(option);
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return classLoader;
	} 
	
	public ClassLoader getClassLoader() {
		return classLoader;
	}


	public Iterable<JavaFileObject> list0(Location location, String packageName, Set<Kind> kinds, boolean recurse)
			throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) { // let standard manager hanfle
			return standardFileManager.list(location, packageName, kinds, recurse);
		} else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
			if(packageName.startsWith("com.lp.")) {
				return finder.find(packageName);
			} else {
				return standardFileManager.list(location, packageName, kinds, recurse);				
			}
		}
		return Collections.emptyList();

	}

	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
			throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) { // let standard manager hanfle
			return standardFileManager.list(location, packageName, kinds, recurse);
		} else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
			if (packageName.startsWith("java")) { // a hack to let standard manager handle locations like "java.lang" or
													// "java.util". Prob would make sense to join results of standard
													// manager with those of my finder here
				return standardFileManager.list(location, packageName, kinds, recurse);
			} else { // app specific classes are here
				return finder.find(packageName);
			}
		}
		return Collections.emptyList();

	}

	@Override
	public String inferBinaryName(Location location, JavaFileObject file) {
		if (file instanceof CustomJavaFileObject) {
			return ((CustomJavaFileObject) file).binaryName();
		} else { // if it's not CustomJavaFileObject, then it's coming from standard file manager
					// - let it handle the file
			return standardFileManager.inferBinaryName(location, file);
		}
	}

	@Override
	public boolean isSameFile(FileObject a, FileObject b) {
		return standardFileManager.isSameFile(a, b);
	}

	@Override
	public boolean handleOption(String current, Iterator<String> remaining) {
		return standardFileManager.handleOption(current, remaining);
	}

	@Override
	public boolean hasLocation(Location location) {
		return location == StandardLocation.CLASS_PATH || location == StandardLocation.PLATFORM_CLASS_PATH; // we don't care about source and other location types - not needed for compilation
	}

	@Override
	public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
		return standardFileManager.getJavaFileForInput(location, className, kind);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
			throws IOException {
		return standardFileManager.getJavaFileForOutput(location, className, kind, sibling);
	}

	@Override
	public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
		return standardFileManager.getFileForInput(location, packageName, relativeName);
	}

	@Override
	public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling)
			throws IOException {
		return standardFileManager.getFileForOutput(location, packageName, relativeName, sibling);
	}

	@Override
	public void flush() throws IOException {
		standardFileManager.flush();
	}

	@Override
	public void close() throws IOException {
		standardFileManager.close();
	}
	
	/**
	 * Store a file that may be retrieved later with
	 * {@link #getFileForInput(javax.tools.JavaFileManager.Location, String, String)}
	 * 
	 * @param location
	 *            the file location
	 * @param packageName
	 *            the Java class' package name
	 * @param relativeName
	 *            the relative name
	 * @param file
	 *            the file object to store for later retrieval
	 */
	public void putFileForInput(final StandardLocation location, final String packageName, final String relativeName,
			final JavaFileObject file) {
		try {
		URI uri = new URI(location.getName() + '/' + packageName + '/' + relativeName);
		this.fileObjects.put(uri, file);
		} catch(URISyntaxException ex) {
			System.out.println("URISyntaxException " + ex.getMessage());
		}
	}

}
