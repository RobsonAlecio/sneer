package sneer.apps.conversations;

import java.util.HashMap;
import java.util.Map;

import sneer.apps.conversations.gui.ConversationFrame;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;

public class ConversationsApp {

	public ConversationsApp(Channel channel) {
		_channel = channel;
		_channel.input().addReceiver(messageReceiver());
	}

	private final Channel _channel;
	private final Map<ContactId, ConversationFrame>_framesByContactId = new HashMap<ContactId, ConversationFrame>();
	private final Map<ContactId, SourceImpl<Message>>_inputsByContactId = new HashMap<ContactId, SourceImpl<Message>>();

	public ContactAction contactAction() {
		return new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return "Start Conversation";
			}
			
		};
	}

	private Omnivore<Packet> messageReceiver() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			produceInputFor(packet._contactId).setter().consume((Message)packet._contents);
		}};
	}
	
	private void actUponContact(Contact contact) {
		ConversationFrame frame = produceFrameFor(contact);
		frame.setVisible(true);
	}

	private ConversationFrame produceFrameFor(Contact contact) {
		ContactId contactId = contact.id();
		ConversationFrame frame = _framesByContactId.get(contactId);
		if (frame == null) {
			frame = new ConversationFrame(contact.nick(), inputFrom(contactId), outputTo(contactId));
			_framesByContactId.put(contactId, frame);
		}
		return frame;
	}

	private Signal<Message> inputFrom(ContactId contactId) {
		return produceInputFor(contactId).output();
	}

	private Source<Message> produceInputFor(ContactId contactId) {
		SourceImpl<Message> result = _inputsByContactId.get(contactId);
		if (result == null) {
			result = new SourceImpl<Message>(null);
			_inputsByContactId.put(contactId, result);
		}
		return result;
	}

	private Omnivore<Message> outputTo(final ContactId contactId) {
		return new Omnivore<Message>() { public void consume(Message message) {
			_channel.output().consume(new Packet(contactId, message));
		}};
	}

}
