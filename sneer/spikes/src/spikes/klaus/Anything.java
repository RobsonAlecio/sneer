package spikes.klaus;

import javax.swing.JWindow;

public class Anything {

	public static void main(String[] args) {
		JWindow window = new JWindow();
		window.setBounds(0, 0, 300, 300);
		window.setName("Banana");
		window.setVisible(true);
		
//		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//		DisplayMode mode = device.getDisplayMode();
//		System.out.println("" + mode.getWidth() + " x " + mode.getHeight());
	}

}
