package sneer.bricks.hardwaresharing.files.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardwaresharing.files.publisher.FilePublisher;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.software.code.classutils.ClassUtils;
import sneer.foundation.brickness.testsupport.BrickTest;

//public abstract class FileCopyTest extends BrickTest {
public class FileCopyTest extends BrickTest {

	private final FilePublisher _publisher = my(FilePublisher.class);

	
	@Test (timeout = 3000)
	public void testWithSmallFile() throws IOException {
		if (mustWorkaroundHudson()) return;
		testWith(anySmallFile());
	}

	
	@Test (timeout = 3000)
	public void testWithAFewFiles() throws IOException {
		if (mustWorkaroundHudson()) return;
		testWith(folderWithAFewFiles());
	}


	private boolean mustWorkaroundHudson() { //This should be an abstract class, but Hudson tries to execute it and gets an Exception. :(
		return getClass() == FileCopyTest.class;
	}


	private void testWith(File fileOrFolder) throws IOException {
		Sneer1024 hash = _publisher.publish(fileOrFolder);
		assertNotNull(hash);

		File copy = newTempFile(); 
		copyFromFileCache(hash, copy);
		
		assertSameContents(fileOrFolder, copy);
	}


	//abstract protected void copyFromFileCache(Sneer1024 hashOfContents, File destination) throws IOException;
	@SuppressWarnings("unused")
	protected void copyFromFileCache(Sneer1024 hashOfContents, File destination) throws IOException {
		throw new IllegalStateException("This method should have been overriden in the subclass.");
	}


	private File anySmallFile() {
		return myClassFile();
	}

	private File myClassFile() {
		return my(ClassUtils.class).toFile(getClass());
	}

	private File folderWithAFewFiles() {
		return new File(myClassFile().getParent(), "fixtures");
	}

	private void assertSameContents(File file1, File file2) throws IOException {
		my(IO.class).files().assertSameContents(file1, file2);
	}

	private File newTempFile() {
		return new File(tmpFolder(), "copy" + System.nanoTime());
	}

}