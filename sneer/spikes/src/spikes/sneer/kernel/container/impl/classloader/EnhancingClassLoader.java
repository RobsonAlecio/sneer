package spikes.sneer.kernel.container.impl.classloader;

import spikes.sneer.kernel.container.impl.classloader.enhancer.ByteCodeGuardian;
import spikes.sneer.kernel.container.impl.classloader.enhancer.EnhancingByteCodeGuardian;

public abstract class EnhancingClassLoader extends ClassLoader {

	private ByteCodeGuardian _guardian = EnhancingByteCodeGuardian.instance();

	public EnhancingClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		// First, check if the class has already been loaded
		ClassLoader parent = getParent();
		Class<?> c = findLoadedClass(name);
		if (c == null) {
			c = findClass2(name);
			if (c == null)
				if (parent != null)
					c = parent.loadClass(name);
		}
		if (c == null) {
			throw new ClassNotFoundException(name);
		}
		
		if (resolve) {
			resolveClass(c);
		}
		return c;
	}

	protected abstract Class<?> findClass2(String name);

	protected Class<?> defineClass(String name, byte[] bytes) {
		byte[] byteArray = _guardian.enhance(name, bytes);
		return defineClass(name, byteArray, 0, byteArray.length);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.err.println("This code is not dead.");
		Class<?> result = findClass2(name);
		if (result == null)
			 throw new ClassNotFoundException(name);
		return result;
	}
}