package sneerapps.wind.impl;

import sneer.bricks.keymanager.PublicKey;
import sneerapps.wind.Tuple;

public class Affinity extends Tuple {

	public final float percentage;
	public final PublicKey peer;

	Affinity(PublicKey pPublisher, PublicKey pPeer, float pPercentage) {
		super(pPublisher);
		percentage = pPercentage;
		peer = pPeer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((peer == null) ? 0 : peer.hashCode());
		result = prime * result + Float.floatToIntBits(percentage);
		result = prime * result
				+ ((publisher == null) ? 0 : publisher.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Affinity other = (Affinity) obj;
		if (peer == null) {
			if (other.peer != null)
				return false;
		} else if (!peer.equals(other.peer))
			return false;
		if (Float.floatToIntBits(percentage) != Float
				.floatToIntBits(other.percentage))
			return false;
		if (publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		return true;
	}

	

}
