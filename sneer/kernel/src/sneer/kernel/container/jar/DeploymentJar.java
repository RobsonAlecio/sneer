package sneer.kernel.container.jar;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import sneer.kernel.container.utils.InjectedBrick;

public interface DeploymentJar extends Serializable, Closeable {

	File file();

	String brickName();

	List<InjectedBrick> injectedBricks() throws IOException;

	String role();

	byte[] sneer1024();
	
	void explode(File target) throws IOException;
}