package sneer.pulp.own_Tagline;

import sneer.kernel.container.Brick;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface OwnTaglineKeeper extends Brick {

	Signal<String> tagline();

	Omnivore<String> taglineSetter();

}