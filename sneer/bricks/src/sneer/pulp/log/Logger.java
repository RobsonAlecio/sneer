package sneer.pulp.log;

import sneer.brickness.Brick;

@Brick
public interface Logger extends LogWorker {

	void setDelegate(LogWorker worker);
	
}