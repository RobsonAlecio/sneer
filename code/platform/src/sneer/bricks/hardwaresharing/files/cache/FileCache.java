package sneer.bricks.hardwaresharing.files.cache;

import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileCache {

	Sneer1024 putFileContents(byte[] contents);
	Sneer1024 putFolderContents(FolderContents contents);

	Object getContents(Sneer1024 hashOfFileOrFolder);
	
	EventSource<Sneer1024> contentsAdded();
}
