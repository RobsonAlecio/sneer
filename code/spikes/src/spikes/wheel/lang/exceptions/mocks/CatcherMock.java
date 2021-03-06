package spikes.wheel.lang.exceptions.mocks;

import spikes.wheel.lang.exceptions.Catcher;

public class CatcherMock implements Catcher {

	private Throwable _lastThrowable;

	public void catchThis(Throwable t) {
		_lastThrowable = t;
	}

	public Throwable getLastThrowable() {
		return _lastThrowable;
	}

}
