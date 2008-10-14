package sneer.pulp.keymanager.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.crypto.Crypto;
import sneer.pulp.crypto.Sneer1024;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.mesh.Party;
import sneer.pulp.own.name.OwnNameKeeper;
import wheel.lang.Functor;

class KeyManagerImpl implements KeyManager {

	private PublicKey _ownKey;
	
	private final Map<Contact, PublicKey> _keyByContact = new HashMap<Contact, PublicKey>();

	private final Map<PublicKey, Party> _partiesByPublicKey = new HashMap<PublicKey, Party>();

	@Inject
	private static OwnNameKeeper _ownName;

	@Inject
	private static Crypto _crypto;


	@Override
	public PublicKey ownPublicKey() {
		if (_ownKey == null)
			_ownKey = generateMickeyMouseKey();
		return _ownKey;
	}

	private PublicKey generateMickeyMouseKey() {
		String name = _ownName.name().currentValue();
		
		String string = name.equals("[My Full Name]")
			? 	"" + System.currentTimeMillis() + System.nanoTime() + hashCode()
			: name;
	
		return generateMickeyMouseKey(string);
	}

	@Override
	public PublicKey keyGiven(Contact contact) {
		return _keyByContact.get(contact);
	}

	@Override
	public Party partyGiven(PublicKey pk) {
		return partyGiven(pk, null);
	}

	@Override
	public synchronized Party partyGiven(PublicKey pk, Functor<PublicKey, Party> factoryToUseIfAbsent) {
		Party result = _partiesByPublicKey.get(pk);
		if (result == null && factoryToUseIfAbsent != null) {
			result = factoryToUseIfAbsent.evaluate(pk);
			_partiesByPublicKey.put(pk, result);
		}
		return result;
	}

	@Override
	public synchronized Contact contactGiven(PublicKey peersPublicKey, Functor<PublicKey, Contact> factoryToUseIfAbsent) {
		Contact result = contactGiven(peersPublicKey);
		if (result != null) return result;
		
		result = factoryToUseIfAbsent.evaluate(peersPublicKey);
		addKey(result, peersPublicKey);
		return result;
	}


	@Override
	public synchronized void addKey(Contact contact, PublicKey publicKey) {
		if(keyGiven(contact) != null) throw new IllegalArgumentException("There already was a public key registered for contact: " + contact.nickname().currentValue());
		_keyByContact.put(contact, publicKey);
	}


	@Override
	public synchronized Contact contactGiven(PublicKey peersPublicKey) {
		for (Contact candidate : _keyByContact.keySet())
			if(_keyByContact.get(candidate).equals(peersPublicKey))
				return candidate;
		
		return null;
	}

	@Override
	public PublicKey unmarshall(byte[] bytes) {
		return new PublicKeyImpl(_crypto.unmarshallSneer1024(bytes));
	}

	@SuppressWarnings("deprecation")
	@Override
	public PublicKey generateMickeyMouseKey(String string) {
		Sneer1024 sneer1024 = _crypto.digest(string.getBytes());
		return new PublicKeyImpl(sneer1024);
	}
}
