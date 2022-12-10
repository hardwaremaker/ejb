package com.lp.service.plscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.tools.JavaFileObject;

/**
 * @author atamur
 * @since 15-Oct-2009
 */
class PackageInternalsFinder {
	private ClassLoader classLoader;
	private static final String CLASS_FILE_EXTENSION = ".class";
	private static final Map<String, String> deflatedJars = new HashMap<String, String>();
	private static final ReentrantLock deflatedLock = new ReentrantLock();
	
	public PackageInternalsFinder(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public List<JavaFileObject> find(String packageName) throws IOException {
		String javaPackageName = packageName.replaceAll("\\.", "/");

		List<JavaFileObject> result = new ArrayList<JavaFileObject>();

		Enumeration<URL> urlEnumeration = classLoader.getResources(javaPackageName);
		while (urlEnumeration.hasMoreElements()) { // one URL for each jar on the classpath that has the given package
			URL packageFolderURL = urlEnumeration.nextElement();
			result.addAll(listUnder(packageName, packageFolderURL));
		}

		return result;
	}

	private Collection<JavaFileObject> listUnder(String packageName, URL packageFolderURL) {
		File directory = new File(packageFolderURL.getFile());
		if (directory.isDirectory()) { // browse local .class files - useful for local execution
			return processDir(packageName, directory);
		} else { // browse a jar file
			return processJar(packageFolderURL);
		} // maybe there can be something else for more involved class loaders
	}

	private List<JavaFileObject> processJar(URL packageFolderURL) {
		List<JavaFileObject> result = new ArrayList<JavaFileObject>();
		try {
//			String jarUri = packageFolderURL.toExternalForm().split("!")[0];
//			System.out.println("processJar:" + packageFolderURL.toString() + ", class:" + packageFolderURL.getClass().getName());
//			System.out.println("jarUri:" + jarUri + ".");
	
			URLConnection urlConnection = packageFolderURL.openConnection();
			if(urlConnection instanceof JarURLConnection) {
				result = processJarUrl(packageFolderURL);
			} else {
				result = processVfsUrl(packageFolderURL);
			}
			
//			JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
//			String rootEntryName = jarConn.getEntryName();
//			int rootEnd = rootEntryName.length() + 1;
//
//			Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
//			while (entryEnum.hasMoreElements()) {
//				JarEntry jarEntry = entryEnum.nextElement();
//				String name = jarEntry.getName();
//				if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1
//						&& name.endsWith(CLASS_FILE_EXTENSION)) {
//					URI uri = URI.create(jarUri + "!/" + name);
//					String binaryName = name.replaceAll("/", ".");
//					binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
//
//					result.add(new CustomJavaFileObject(binaryName, uri));
//				}
//			}
		} catch (Exception e) {
			throw new RuntimeException("Wasn't able to open " + packageFolderURL + " as a jar file", e);
		}
		return result;
	}

	private List<JavaFileObject> processJarUrl(URL packageFolderURL) throws Exception {
//		System.out.println("processing jar url ...");
//		List<JavaFileObject> result = new ArrayList<JavaFileObject>();

		String jarUri = packageFolderURL.toExternalForm().split("!")[0];
		JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
		return processJarUrlConnection(jarUri, jarConn);
/*		
		String rootEntryName = jarConn.getEntryName();
		int rootEnd = rootEntryName.length() + 1;

		Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
		while (entryEnum.hasMoreElements()) {
			JarEntry jarEntry = entryEnum.nextElement();
			String name = jarEntry.getName();
			if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1
					&& name.endsWith(CLASS_FILE_EXTENSION)) {
				URI uri = URI.create(jarUri + "!/" + name);
				String binaryName = name.replaceAll("/", ".");
				binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");

				result.add(new CustomJavaFileObject(binaryName, uri));
			}
		}
		
		return result;
*/		
	}

	private List<JavaFileObject> processJarUrlConnection(
			String jarUri, JarURLConnection jarConn) throws Exception {
		String rootEntryName = jarConn.getEntryName();
		if(rootEntryName == null) {
			rootEntryName = "";
		}
		int rootEnd = rootEntryName.length() + 1;

		List<JavaFileObject> result = new ArrayList<JavaFileObject>();
		Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
		while (entryEnum.hasMoreElements()) {
			JarEntry jarEntry = entryEnum.nextElement();
			String name = jarEntry.getName();
			
//			System.out.println("urlconnection:" + name + " @ " + rootEntryName);
			
			if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1
					&& name.endsWith(CLASS_FILE_EXTENSION)) {
				URI uri = URI.create(jarUri + "!/" + name);
				String binaryName = name.replaceAll("/", ".");
				binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");

				result.add(new CustomJavaFileObject(binaryName, uri));
			}
		}
		
		return result;	
	}
	
	
	private File deflateJar(ZipEntry zipEntry, InputStream is) throws IOException {
		deflatedLock.lock();

		try {
			String filename = deflatedJars.get(zipEntry.getName());
			if(filename != null) {
				File f = new File(filename);
				f.deleteOnExit();
				return f;
			}

			File f = File.createTempFile("hv_" + zipEntry.getName()
					.replace("/", "_")
					.replace(".jar", ""), ".jar");
			f.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(f);

			byte[] buffer = new byte[8192];
			int count = -1;
			while((count = is.read(buffer)) != -1) {
				fos.write(buffer, 0, count);
			}
			
			fos.close();
			
			deflatedJars.put(zipEntry.getName(), f.getAbsolutePath());
			return f;
		} finally {
			deflatedLock.unlock();
		}
	}
	
	private List<JavaFileObject> processVfsUrl(URL packageFolderURL) throws Exception {
//		System.out.println("processing jar vfs url ...");
		List<JavaFileObject> result = new ArrayList<JavaFileObject>();
		String vfsUri = packageFolderURL.toExternalForm();
		if(!vfsUri.startsWith("vfs:")) {
			return result;
		}

		String search = "/content/lpserver.ear/"; 
		int searchIndex = vfsUri.indexOf(search);
		if(searchIndex != -1) {
			String earFile = "../standalone/deployments/lpserver.ear";
			String jarName = vfsUri.substring(searchIndex + search.length());
//			String jarName = vfsUri.replace("vfs:/content/lpserver.ear/", "");
			
			int jarIndex = jarName.indexOf(".jar");
			if(jarIndex == -1) return result;
			
			String jarFilename = jarName.substring(0, jarIndex+4);
			String jarPackageName = "";
			if(jarName.length() > (jarIndex+4+1)) {
				jarPackageName = jarName.substring(jarIndex+4+1);
			}
			
			ZipFile zipFile = new ZipFile(earFile);
			ZipEntry zipEntry = zipFile.getEntry(jarFilename);
			InputStream is = zipFile.getInputStream(zipEntry);
			
			File f = deflateJar(zipEntry, is);
			zipFile.close();
	
			String jarUri = "jar:file:" + f.getAbsolutePath().replace('\\',	'/') + "!/" + jarPackageName;
			URL url = new URL(jarUri);
			jarUri = url.toExternalForm().split("!")[0];
			JarURLConnection jarConn = (JarURLConnection) url.openConnection();

			// packagename gesucht, der im jar nicht enthalten ist
			try {
				ZipEntry z = jarConn.getJarFile().getEntry(jarPackageName);
				if(z == null) {
					return result;
				}			
			} catch(FileNotFoundException e) {
				return result;
			}

			result = processJarUrlConnection(jarUri, jarConn);
			
//			result = processJarUrlConnection2(jarUri, jarConn);
		}
		return result;
	}
	
	
	private List<JavaFileObject> processDir(String packageName, File directory) {
		List<JavaFileObject> result = new ArrayList<JavaFileObject>();

		File[] childFiles = directory.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isFile()) {
				// We only want the .class files.
				if (childFile.getName().endsWith(CLASS_FILE_EXTENSION)) {
					String binaryName = packageName + "." + childFile.getName();
					binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");

					result.add(new CustomJavaFileObject(binaryName, childFile.toURI()));
				}
			}
		}

		return result;
	}
}