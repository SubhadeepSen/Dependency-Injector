package sdp.injector.scanner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ClasspathScanner {

	private static final String DOT_CLASS = ".class";
	private static final String DOT_REGEX = "\\.";
	private static final String PATH_REGEX = "/|\\\\";
	private static final String SEMI_COLON = ";";
	private static final String JAVA_CLASS_PATH = "java.class.path";

	@SuppressWarnings("rawtypes")
	public List<Class> getListOfClasses() throws ClasspathScanningException {
		List<Class> classFiles = new ArrayList<Class>();
		File path = new File(System.getProperty(JAVA_CLASS_PATH).split(SEMI_COLON)[0]);
		if (path.isDirectory()) {
			classFiles.addAll(getClassesFromDirectory(path));
		}
		return classFiles;
	}

	@SuppressWarnings("rawtypes")
	private List<Class> getClassesFromDirectory(File path) throws ClasspathScanningException {
		List<Class> classes = new ArrayList<Class>();
		List<File> classFiles = listOfFiles(path, (dir, name) -> name.endsWith(DOT_CLASS), true);
		String className = "";
		int index = path.getAbsolutePath().length() + 1;
		for (File classfile : classFiles) {
			className = classfile.getAbsolutePath().substring(index);
			className = toFullyQualifiedClassName(className);
			try {
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				throw new ClasspathScanningException("Exception in scanning: " + e.getMessage());
			}
		}
		return classes;
	}

	private List<File> listOfFiles(File directory, FilenameFilter filter, boolean isRecursive) {
		List<File> files = new ArrayList<File>();
		for (File file : directory.listFiles()) {
			if (filter == null || filter.accept(directory, file.getName())) {
				files.add(file);
			}
			if (isRecursive && file.isDirectory()) {
				files.addAll(listOfFiles(file, filter, isRecursive));
			}
		}
		return files;
	}

	private String toFullyQualifiedClassName(final String fileName) {
		return fileName.substring(0, fileName.length() - 6).replaceAll(PATH_REGEX, DOT_REGEX);
	}

}
