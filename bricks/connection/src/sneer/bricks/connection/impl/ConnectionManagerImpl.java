package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.Startable;
import sneer.log.Logger;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.OldNetwork;
import wheel.io.network.OldNetworkImpl;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ConnectionManagerImpl implements ConnectionManager, Startable {

	private Map<String, Connection> _map = new HashMap<String, Connection>();

	private Object receiver;
	
	private int _sneerPort;
	
	private OldNetwork _network;
	
	private ObjectServerSocket _serverSocket;
	
    @Brick
    private Logger _log;

	@Brick
	private ContactManager _contactManager;
	
	@Override
	public void start() throws Exception {
		_network = new OldNetworkImpl();
		receiver = new SimpleListReceiver<Contact>(_contactManager.contacts()){
			
			@Override
			protected void elementAdded(Contact contact) {
				openConnection(contact.host(), contact.port());
			}
			
			@Override
			protected void elementPresent(Contact contact) {
				openConnection(contact.host(), contact.port());
			}
			
			@Override
			protected void elementToBeRemoved(Contact contact) {
				Connection conn = getConnection(connectionId(contact.host(), contact.port()));
				conn.close();
			}};
			
			receiver.toString();
	}

	private String connectionId(String host, int port) {
		return host+":"+port;
	}
	
	protected Connection getConnection(String connectionId) {
		for(String key : _map.keySet()) {
			if(key.equals(connectionId)) {
				return _map.get(key);
			}
		}
		throw new IllegalArgumentException("Can't find connection: "+connectionId);
	}

	@Override
	public List<Connection> listConnections() {
		List<Connection> result = new ArrayList<Connection>(_map.values());
		return result;
	}

	@Override
	public Connection openConnection(String host, int port) {
		String connectionId = connectionId(host, port);
		Connection conn = _map.get(connectionId);
		if(conn != null) return conn;
		_log.info("Opening connection to {}:{}",host, port);
		conn = new ConnectionImpl();
		_map.put(connectionId, conn);
		return conn;
	}

	@Override
	public void sneerPort(int port) {
		if(_serverSocket != null) {
			_log.info("closing server socket at {}", _sneerPort);
			_serverSocket.close();
		}
		_sneerPort = port;
		try {
			_log.info("starting server socket at {}", _sneerPort);
			_serverSocket = _network.openObjectServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace(); //FixUrgent: handle exception
		}
	}

	@Override
	public int sneerPort() {
		return _sneerPort;
	}
}