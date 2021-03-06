package sneer.bricks.software.code.compilers.classpath.impl;

import java.io.File;

/**
 * Elements are .jar files
 */
class JarBasedClasspath extends ClasspathSupport {

	public JarBasedClasspath(File jarFile) {
		add(jarFile);
	}

	public JarBasedClasspath(File... jarFiles) {
		for (File file : jarFiles) add(file);
	}
}
