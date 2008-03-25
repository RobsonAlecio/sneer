package sneer.bricks.connection.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import sneer.bricks.connection.IncomingSocketValidator;
import sneer.bricks.connection.KeyManager;
import sneer.bricks.network.SocketAccepter;
import sneer.lego.Brick;
import sneer.lego.Startable;
import wheel.io.Streams;
import wheel.io.network.ByteArraySocket;

public class IncomingSocketValidatorImpl implements IncomingSocketValidator, Startable {

	private static final byte[] SNEER_WIRE_PROTOCOL_1 = toByteArray("Sneer Wire Protocol 1");
	private static final byte[] FALLBACK = toByteArray("Fallback");
	private static final byte[] OK = toByteArray("OK");
	
	@Brick
	private SocketAccepter _socketAccepter;
	
	@Brick
	private KeyManager _keyManager; 
	
	@Override
	public void start() throws Exception {
//		_socketAccepter.lastAcceptedSocket().addReceiver(new Omnivore<ObjectSocket>() { @Override public void consume(ObjectSocket socket) {
//			validate(socket);
//		}});
	}


	private static byte[] toByteArray(String string) {
		try {
			return string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}

	private void validate(ByteArraySocket socket) {
		try {
			tryToValidate(socket);
		} catch (IOException e) {
			reject(socket);
		}
	}

	private void tryToValidate(ByteArraySocket socket) throws IOException {
		validateProtocolVersion(socket);
		byte[] publicKey = socket.read();
		
		//ContactId id = _keyManager.getContact(publicKey);
		
		
	}


	private void validateProtocolVersion(ByteArraySocket socket) throws IOException {
		while (true) {
			byte[] header = socket.read();
			if (Arrays.equals(header, SNEER_WIRE_PROTOCOL_1)) {
				socket.write(OK);
				return;
			}
			discardFirstDataPacket(socket);
			socket.write(FALLBACK);
		}
	}
		
	private void discardFirstDataPacket(ByteArraySocket socket) throws IOException {
		socket.read();
	}

	private void reject(ByteArraySocket socket) {
		Streams.crash(socket);
	}

}
