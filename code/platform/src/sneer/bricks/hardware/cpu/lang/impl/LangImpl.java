package sneer.bricks.hardware.cpu.lang.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;

import sneer.bricks.hardware.cpu.lang.Lang;

class LangImpl implements Lang {

	private final Arrays _arrays = new Lang.Arrays(){
		@Override public void reverse(Object[] array) { ArrayUtils.reverse(array);}
	};
	
	private final Serialization _serialization = new Lang.Serialization(){
		@Override public byte[] serialize(Serializable obj) { return SerializationUtils.serialize(obj); }
		@Override public <T> T serialize(byte[] data) { return (T)SerializationUtils.deserialize(data); }
	};

	private Strings _strings = new Lang.Strings(){
		@Override public boolean isEmpty(String str) { return str == null || str.isEmpty();	}
		@Override public String join(Collection<?> collection, String separator) {return StringUtils.join(collection, separator); }
		@Override public String trimToNull(String str) {return StringUtils.trimToNull(str);}
		@Override public String chomp(String str, String separator) { return StringUtils.chomp(str, separator);}
		@Override public String substringBeforeLast(String str, String separator) {	return StringUtils.substringBeforeLast(str, separator); }

		@Override public String deleteWhitespace(String str) {return StringUtils.deleteWhitespace(str);}
		
		@Override public List<String> readLines(String input) throws IOException {
	        BufferedReader reader = new BufferedReader(new StringReader(input));
			List<String> list = new ArrayList<String>();
			String line = reader.readLine();
			while (line != null) {
				list.add(line);
				line = reader.readLine();
			}
			return list;		
		}
		
		@Override	public byte[] toByteArray(String string) { 
			try {
				return string.getBytes("UTF8");
			} catch (UnsupportedEncodingException e) { 
				throw new IllegalStateException(); 
			}
		}

		@Override
		public String[] splitRight(String line, char separator, int maxParts) {
			final List<String> parts = new ArrayList<String>(maxParts);
			int endIndex = line.length();
			
			for (int i = maxParts - 1; i > 0; --i) {
				final int index = line.lastIndexOf(separator, endIndex - 1);
				if (index < 0) break;
				parts.add(line.substring(index + 1, endIndex));
				endIndex = index;
			}
			
			parts.add(line.substring(0, endIndex));
			return reversedArrayGiven(parts);
		}			

		private String[] reversedArrayGiven(final List<String> parts) {
			String[] array = parts.toArray(new String[parts.size()]);
			my(Lang.class).arrays().reverse(array);
			return array;
		}
	};

	@Override	public Arrays arrays() { return _arrays; }
	@Override public Serialization serialization() {	 return _serialization;}
	@Override public Strings strings() { return _strings;}
}