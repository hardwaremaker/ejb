package com.lp.service.plscript;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

/**
 * Compile a String or other {@link CharSequence}, returning a Java
 * {@link Class} instance that may be instantiated. This class is a Facade
 * around {@link JavaCompiler} for a narrower use case, but a bit easier to use.
 * <p>
 * To compile a String containing source for a Java class which implements
 * MyInterface:
 * 
 * <pre>
 * 
 * ClassLoader classLoader = MyClass.class.getClassLoader(); // optional; null is
 * // also OK
 * List<Diagnostic> diagnostics = new ArrayList<Diagnostic>(); // optional; null is
 * // also OK
 * JavaStringCompiler<Object> compiler = new JavaStringCompiler<MyInterface>(classLoader, null);
 * try {
 * 	Class<MyInterface> newClass = compiler.compile("com.mypackage.NewClass", stringContaininSourceForNewClass,
 * 			diagnostics, MyInterface);
 * 	MyInterface instance = newClass.newInstance();
 * 	instance.someOperation(someArgs);
 * } catch (JavaStringCompilerException e) {
 * 	handle(e);
 * } catch (IllegalAccessException e) {
 * 	handle(e);
 * }
 * </pre>
 * 
 * The source can be in a String, {@link StringBuffer}, or your own class which
 * implements {@link CharSequence}. If you implement your own, it must be thread
 * safe (preferably, immutable.)
 * 
 * @author <a href="mailto:David.Biesack@sas.com">David J. Biesack</a>
 */
public class CharSequenceCompiler<T> {
	// Compiler requires source files with a ".java" extension:
	static final String JAVA_EXTENSION = ".java";

	private final ClassLoaderImpl classLoader;

	// The compiler instance that this facade uses.
	private final JavaCompiler compiler;

	// The compiler options (such as "-target" "1.5").
	private final List<String> options;

	// collect compiler diagnostics in this instance.
	private DiagnosticCollector<JavaFileObject> diagnostics;

	// The FileManager which will store source and class "files".
	private final CustomClassloaderJavaFileManager javaFileManager;

	private boolean compileOnly;
	
	/**
	 * Construct a new instance which delegates to the named class loader.
	 * 
	 * @param loader
	 *            the application ClassLoader. The compiler will look through to
	 *            this // class loader for dependent classes
	 * @param options
	 *            The compiler options (such as "-target" "1.5"). See the usage for
	 *            javac
	 * @throws IllegalStateException
	 *             if the Java compiler cannot be loaded.
	 */
	public CharSequenceCompiler(final ClassLoader loader, final Iterable<String> options) {
		this.compiler = ToolProvider.getSystemJavaCompiler();
		if (this.compiler == null) {
			throw new IllegalStateException(
					"Cannot find the system Java compiler. " + "Check that your class path includes tools.jar");
		}
		this.classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoaderImpl>() {
			@Override
			public ClassLoaderImpl run() {
				return new ClassLoaderImpl(loader);
			}
		});

		this.diagnostics = new DiagnosticCollector<JavaFileObject>();
		final StandardJavaFileManager fileManager = this.compiler.getStandardFileManager(this.diagnostics, null, null);
		// create our FileManager which chains to the default file manager
		// and our ClassLoader
//		this.javaFileManager = new FileManagerImpl(fileManager, this.classLoader);
//		this.javaFileManager = new CustomClassloaderJavaFileManger(loader, fileManager);
		this.javaFileManager = new CustomClassloaderJavaFileManager((ClassLoader)this.classLoader, fileManager);
		this.options = new ArrayList<String>();
		if (options != null) { // make a save copy of input options
			for (final String option : options) {
				this.options.add(option);
			}
		}
	}

	/**
	 * Compile Java source in <var>javaSource</name> and return the resulting class.
	 * <p>
	 * Thread safety: this method is thread safe if the <var>javaSource</var> and
	 * <var>diagnosticsList</var> are isolated to this thread.
	 * 
	 * @param qualifiedClassName
	 *            The fully qualified class name.
	 * @param javaSource
	 *            Complete java source, including a package statement and a class,
	 *            interface, or annotation declaration.
	 * @param diagnosticsList
	 *            Any diagnostics generated by compiling the source are added to
	 *            this collector.
	 * @param types
	 *            zero or more Class objects representing classes or interfaces that
	 *            the resulting class must be assignable (castable) to.
	 * @return a Class which is generated by compiling the source
	 * @throws CharSequenceCompilerException
	 *             if the source cannot be compiled - for example, if it contains
	 *             syntax or semantic errors or if dependent classes cannot be
	 *             found.
	 * @throws ClassCastException
	 *             if the generated class is not assignable to all the optional
	 *             <var>types</var>.
	 */
	public synchronized Class<T> compile(final String qualifiedClassName, final CharSequence javaSource,
			final DiagnosticCollector<JavaFileObject> diagnosticsList, final Class<?>... types)
			throws CharSequenceCompilerException, ClassCastException {
		if (diagnosticsList != null)
			this.diagnostics = diagnosticsList;
		else
			this.diagnostics = new DiagnosticCollector<JavaFileObject>();
		final Map<String, CharSequence> classes = new HashMap<String, CharSequence>(1);
		classes.put(qualifiedClassName, javaSource);
		final Map<String, Class<T>> compiled = this.compile(classes, diagnosticsList);
		final Class<T> newClass = compiled.get(qualifiedClassName);
		return newClass == null ? null : this.castable(newClass, types);
	}

	/**
	 * Compile multiple Java source strings and return a Map containing the
	 * resulting classes.
	 * <p>
	 * Thread safety: this method is thread safe if the <var>classes</var> and
	 * <var>diagnosticsList</var> are isolated to this thread.
	 * 
	 * @param classes
	 *            A Map whose keys are qualified class names and whose values are
	 *            the Java source strings containing the definition of the class. A
	 *            map value may be null, indicating that compiled class is expected,
	 *            although no source exists for it (it may be a non-public class
	 *            contained in one of the other strings.)
	 * @param diagnosticsList
	 *            Any diagnostics generated by compiling the source are added to
	 *            this list.
	 * @return A mapping of qualified class names to their corresponding classes.
	 *         The map has the same keys as the input <var>classes</var>; the values
	 *         are the corresponding Class objects.
	 * @throws CharSequenceCompilerException
	 *             if the source cannot be compiled
	 */
	public synchronized Map<String, Class<T>> compile(final Map<String, CharSequence> classes,
			final DiagnosticCollector<JavaFileObject> diagnosticsList) throws CharSequenceCompilerException {
		final List<JavaFileObject> sources = new ArrayList<JavaFileObject>();
		for (final Entry<String, CharSequence> entry : classes.entrySet()) {
			final String qualifiedClassName = entry.getKey();
			final CharSequence javaSource = entry.getValue();
			if (javaSource != null) {
				final int dotPos = qualifiedClassName.lastIndexOf('.');
				final String className = dotPos == -1 ? qualifiedClassName : qualifiedClassName.substring(dotPos + 1);
				final String packageName = dotPos == -1 ? "" : qualifiedClassName.substring(0, dotPos);
				final JavaFileObjectImpl source = new JavaFileObjectImpl(className, javaSource);
				sources.add(source);
				// Store the source file in the FileManager via package/class
				// name.
				// For source files, we add a .java extension
				this.javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName,
						className + CharSequenceCompiler.JAVA_EXTENSION, source);
			}
		}
		// Get a CompliationTask from the compiler and compile the sources
		final CompilationTask task = this.compiler.getTask(null, this.javaFileManager, this.diagnostics, this.options,
				null, sources);
		final Boolean result = task.call();
		if (result == null || !result.booleanValue()) {
			throw new CharSequenceCompilerException("Compilation failed.", classes.keySet(), this.diagnostics);
		}
		
		if(compileOnly) {
			return new HashMap<String, Class<T>>();
		}
		
		try {
			// For each class name in the inpput map, get its compiled
			// class and put it in the output map
			final Map<String, Class<T>> compiled = new HashMap<String, Class<T>>();
			for (final String qualifiedClassName : classes.keySet()) {
				final Class<T> newClass = this.loadClass(qualifiedClassName);
				compiled.put(qualifiedClassName, newClass);
			}
			return compiled;
		} catch (final ClassNotFoundException e) {
			throw new CharSequenceCompilerException(classes.keySet(), e, this.diagnostics);
		} catch (final IllegalArgumentException e) {
			throw new CharSequenceCompilerException(classes.keySet(), e, this.diagnostics);
		} catch (final SecurityException e) {
			throw new CharSequenceCompilerException(classes.keySet(), e, this.diagnostics);
		}
	}

	public void beCompileOnly() {
		compileOnly = true;
	}
	
	public boolean isCompileOnly() {
		return compileOnly;
	}
	
	/**
	 * Load a class that was generated by this instance or accessible from its
	 * parent class loader. Use this method if you need access to additional classes
	 * compiled by
	 * {@link #compile(String, CharSequence, DiagnosticCollector, Class...)
	 * compile()}, for example if the primary class contained nested classes or
	 * additional non-public classes.
	 * 
	 * @param qualifiedClassName
	 *            the name of the compiled class you wish to load
	 * @return a Class instance named by <var>qualifiedClassName</var>
	 * @throws ClassNotFoundException
	 *             if no such class is found.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> loadClass(final String qualifiedClassName) throws ClassNotFoundException {
		return (Class<T>) this.classLoader.loadClass(qualifiedClassName);
	}

	/**
	 * Check that the <var>newClass</var> is a subtype of all the type parameters
	 * and throw a ClassCastException if not.
	 * 
	 * @param types
	 *            zero of more classes or interfaces that the <var>newClass</var>
	 *            must be castable to.
	 * @return <var>newClass</var> if it is castable to all the types
	 * @throws ClassCastException
	 *             if <var>newClass</var> is not castable to all the types.
	 */
	private Class<T> castable(final Class<T> newClass, final Class<?>... types) throws ClassCastException {
		for (final Class<?> type : types)
			if (!type.isAssignableFrom(newClass)) {
				throw new ClassCastException(type.getName());
			}
		return newClass;
	}

	/**
	 * COnverts a String to a URI.
	 * 
	 * @param name
	 *            a file name
	 * @return a URI
	 */
	static URI toURI(final String name) {
		try {
			return new URI(name);
		} catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return This compiler's class loader.
	 */
	public ClassLoader getClassLoader() {
		return this.javaFileManager.getClassLoader();
	}
}

/**
 * A JavaFileManager which manages Java source and classes. This FileManager
 * delegates to the JavaFileManager and the ClassLoaderImpl provided in the
 * constructor. The sources are all in memory CharSequence instances and the
 * classes are all in memory byte arrays.
 */
final class FileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
	// the delegating class loader (passed to the constructor)
	private final ClassLoaderImpl classLoader;

	// Internal map of filename URIs to JavaFileObjects.
	private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();

	/**
	 * Construct a new FileManager which forwards to the <var>fileManager</var> for
	 * source and to the <var>classLoader</var> for classes
	 * 
	 * @param fileManager
	 *            another FileManager that this instance delegates to for additional
	 *            source.
	 * @param classLoader
	 *            a ClassLoader which contains dependent classes that the compiled
	 *            classes will require when compiling them.
	 */
	public FileManagerImpl(final JavaFileManager fileManager, final ClassLoaderImpl classLoader) {
		super(fileManager);
		this.classLoader = classLoader;
	}

	/**
	 * @return the class loader which this file manager delegates to
	 */
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	/**
	 * For a given file <var>location</var>, return a FileObject from which the
	 * compiler can obtain source or byte code.
	 * 
	 * @param location
	 *            an abstract file location
	 * @param packageName
	 *            the package name for the file
	 * @param relativeName
	 *            the file's relative name
	 * @return a FileObject from this or the delegated FileManager
	 * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public FileObject getFileForInput(final Location location, final String packageName, final String relativeName)
			throws IOException {
		final FileObject o = this.fileObjects.get(this.uri(location, packageName, relativeName));
		if (o != null)
			return o;
		return super.getFileForInput(location, packageName, relativeName);
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
		this.fileObjects.put(this.uri(location, packageName, relativeName), file);
	}

	/**
	 * Convert a location and class name to a URI
	 */
	private URI uri(final Location location, final String packageName, final String relativeName) {
		return CharSequenceCompiler.toURI(location.getName() + '/' + packageName + '/' + relativeName);
	}

	/**
	 * Create a JavaFileImpl for an output class file and store it in the
	 * classloader.
	 * 
	 * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location,
	 *      java.lang.String, javax.tools.JavaFileObject.Kind,
	 *      javax.tools.FileObject)
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(final Location location, final String qualifiedName, final Kind kind,
			final FileObject outputFile) throws IOException {
		final JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
		this.classLoader.add(qualifiedName, file);
		return file;
	}

	@Override
	public ClassLoader getClassLoader(final JavaFileManager.Location location) {
		return this.classLoader;
	}

	@Override
	public String inferBinaryName(final Location loc, final JavaFileObject file) {
		String result;
		// For our JavaFileImpl instances, return the file's name, else
		// simply run the default implementation
		if (file instanceof JavaFileObjectImpl)
			result = file.getName();
		else
			result = super.inferBinaryName(loc, file);
		return result;
	}

	@Override
	public Iterable<JavaFileObject> list(final Location location, final String packageName, final Set<Kind> kinds,
			final boolean recurse) throws IOException {
		final Iterable<JavaFileObject> result = super.list(location, packageName, kinds, recurse);
		final ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();
		if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
			for (final JavaFileObject file : this.fileObjects.values()) {
				if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName))
					files.add(file);
			}
			files.addAll(this.classLoader.files());
		} else if (location == StandardLocation.SOURCE_PATH && kinds.contains(JavaFileObject.Kind.SOURCE)) {
			for (final JavaFileObject file : this.fileObjects.values()) {
				if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName))
					files.add(file);
			}
		}
		for (final JavaFileObject file : result) {
			files.add(file);
		}
		return files;
	}
}

/**
 * A JavaFileObject which contains either the source text or the compiler
 * generated class. This class is used in two cases.
 * <ol>
 * <li>This instance uses it to store the source which is passed to the
 * compiler. This uses the
 * {@link JavaFileObjectImpl#JavaFileObjectImpl(String, CharSequence)}
 * constructor.
 * <li>The Java compiler also creates instances (indirectly through the
 * FileManagerImplFileManager) when it wants to create a JavaFileObject for the
 * .class output. This uses the
 * {@link JavaFileObjectImpl#JavaFileObjectImpl(String, JavaFileObject.Kind)}
 * constructor.
 * </ol>
 * This class does not attempt to reuse instances (there does not seem to be a
 * need, as it would require adding a Map for the purpose, and this would also
 * prevent garbage collection of class byte code.)
 */
final class JavaFileObjectImpl extends SimpleJavaFileObject {
	// If kind == CLASS, this stores byte code from openOutputStream
	private ByteArrayOutputStream byteCode;

	// if kind == SOURCE, this contains the source text
	private final CharSequence source;

	/**
	 * Construct a new instance which stores source
	 * 
	 * @param baseName
	 *            the base name
	 * @param source
	 *            the source code
	 */
	JavaFileObjectImpl(final String baseName, final CharSequence source) {
		super(CharSequenceCompiler.toURI(baseName + CharSequenceCompiler.JAVA_EXTENSION), Kind.SOURCE);
		this.source = source;
	}

	/**
	 * Construct a new instance
	 * 
	 * @param name
	 *            the file name
	 * @param kind
	 *            the kind of file
	 */
	JavaFileObjectImpl(final String name, final Kind kind) {
		super(CharSequenceCompiler.toURI(name), kind);
		this.source = null;
	}

	/**
	 * Return the source code content
	 * 
	 * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
	 */
	@Override
	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
		if (this.source == null)
			throw new UnsupportedOperationException("getCharContent()");
		return this.source;
	}

	/**
	 * Return an input stream for reading the byte code
	 * 
	 * @see javax.tools.SimpleJavaFileObject#openInputStream()
	 */
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(this.getByteCode());
	}

	/**
	 * Return an output stream for writing the bytecode
	 * 
	 * @see javax.tools.SimpleJavaFileObject#openOutputStream()
	 */
	@Override
	public OutputStream openOutputStream() {
		this.byteCode = new ByteArrayOutputStream();
		return this.byteCode;
	}

	/**
	 * @return the byte code generated by the compiler
	 */
	byte[] getByteCode() {
		return this.byteCode.toByteArray();
	}
}

/**
 * A custom ClassLoader which maps class names to JavaFileObjectImpl instances.
 */
final class ClassLoaderImpl extends ClassLoader {
	private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

	ClassLoaderImpl(final ClassLoader parentClassLoader) {
		super(parentClassLoader);
	}

	/**
	 * @return An collection of JavaFileObject instances for the classes in the
	 *         class loader.
	 */
	Collection<JavaFileObject> files() {
		return Collections.unmodifiableCollection(this.classes.values());
	}

	@Override
	protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
		final JavaFileObject file = this.classes.get(qualifiedClassName);
		if (file != null) {
			final byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
			return this.defineClass(qualifiedClassName, bytes, 0, bytes.length);
		}
		// Workaround for "feature" in Java 6
		// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
		try {
			final Class<?> c = Class.forName(qualifiedClassName);
			return c;
		} catch (final ClassNotFoundException nf) {
			// Ignore and fall through
		}
		return super.findClass(qualifiedClassName);
	}

	/**
	 * Add a class name/JavaFileObject mapping
	 * 
	 * @param qualifiedClassName
	 *            the name
	 * @param javaFile
	 *            the file associated with the name
	 */
	void add(final String qualifiedClassName, final JavaFileObject javaFile) {
		this.classes.put(qualifiedClassName, javaFile);
	}

	@Override
	protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
		return super.loadClass(name, resolve);
	}

	@Override
	public InputStream getResourceAsStream(final String name) {
		if (name.endsWith(".class")) {
			final String qualifiedClassName = name.substring(0, name.length() - ".class".length()).replace('/', '.');
			final JavaFileObjectImpl file = (JavaFileObjectImpl) this.classes.get(qualifiedClassName);
			if (file != null) {
				return new ByteArrayInputStream(file.getByteCode());
			}
		}
		return super.getResourceAsStream(name);
	}
}
