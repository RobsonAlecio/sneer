//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheel.reactive.sets;


import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.CollectionSignal;


public interface SetSignal<T> extends CollectionSignal<T>, EventSource<SetChange<T>> {

	boolean currentContains(T element);
}
