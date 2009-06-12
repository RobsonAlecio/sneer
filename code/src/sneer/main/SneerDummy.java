package sneer.main;

import java.io.File;

public class SneerDummy {
	
	public static void main(String[] args) throws Exception {
		System.setProperty("home_override", dummyHome());
		SneerSession.main(args);
	}

	private static String dummyHome() {
		return new File(System.getProperty("user.home"), ".sneerdummy").getAbsolutePath();
	}

}