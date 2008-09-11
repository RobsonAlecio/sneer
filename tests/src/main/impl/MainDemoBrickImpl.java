package main.impl;

import main.MainDemoBrick;
import snapps.blinkinglights.BlinkingLightsSnapp;
import snapps.contacts.ContactsSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.skin.dashboard.Dashboard;

class MainDemoBrickImpl implements MainDemoBrick {

	@Inject	@SuppressWarnings("unused")
	private static Dashboard _gui1;
	
	@Inject	@SuppressWarnings("unused")
	private static ContactsSnapp _gui3;
	
	@Inject	@SuppressWarnings("unused")
	private static BlinkingLightsSnapp _gui4;
	
	@Inject	@SuppressWarnings("unused")
	private static SocketOriginator _networkDaemon1;
	
	@Inject	@SuppressWarnings("unused")
	private static SocketReceiver _networkDaemon2;

	@Inject
	private static OwnNameKeeper _ownName;

	@Inject
	private static PortKeeper _portKeeper;

	@Inject
	private static ContactManager _contactManager;

	@Inject
	private static KeyManager _keyManager;
	
	@Inject
	private static InternetAddressKeeper _addressKeeper;

	@Override
	public void start(String ownName, int port) {
		assertIsHardcodedNick(ownName);
		
		_ownName.nameSetter().consume(ownName);
		
		_portKeeper.portSetter().consume(port);
		
		addContacts();
	}

	private void assertIsHardcodedNick(String nick) {
		if (!isHardcodedNick(nick))
			throw new IllegalArgumentException(nick + " is not one of the hardcoded nicknames.");
	}

	private boolean isHardcodedNick(String nick) {
		for (ContactInfo contact : hardcodedContacts())
			if (contact._nick.equals(nick)) return true;
		return false;
	}

	private void addContacts() {
		for (ContactInfo contact : hardcodedContacts()) {
			addConnectionTo(contact);
		}
	}

	private ContactInfo[] hardcodedContacts() {
		return new ContactInfo[] {
			new ContactInfo("Bamboo", "rodrigobamboo.dyndns.org", 5923),
			new ContactInfo("Bihaiko", "bihaiko.dyndns.org", 5923),
			new ContactInfo("Daniel", "dfcsantos.homelinux.com"),
			new ContactInfo("Klaus", "klaus.selfip.net", 5923),
			new ContactInfo("Klaus", "200.169.90.89", 5923),
			new ContactInfo("Nell", "anelisedaux.dyndns.org", 5924),
			new ContactInfo("Localhost", "localhost"),
		};
	}

	public static class ContactInfo {
		public final String _nick;
		public final String _host;
		public final int _port;

		public ContactInfo(String nick, String host, int port) {
			_nick = nick;
			_host = host;
			_port = port;
		}

		public ContactInfo(String nick, String host) {
			this(nick, host, 7777);
		}
	}

	private void addConnectionTo(ContactInfo endPoint) {
		if (endPoint._nick.equals(_ownName.name().currentValue())) return;
		
		Contact contact = produceContact(endPoint._nick);
		_addressKeeper.add(contact, endPoint._host, endPoint._port);
	}

	private PublicKey mickeyMouseKey(String nick) {
		return _keyManager.generateMickeyMouseKey(nick);
	}

	private Contact produceContact(String nick) {
		Contact result = _contactManager.contactGiven(nick);
		if (result != null) return result;
		
		result = _contactManager.addContact(nick);
		_keyManager.addKey(result, mickeyMouseKey(nick));
		return result;
	}

}