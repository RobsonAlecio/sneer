package wheel.io.ui.impl;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;

import wheel.io.ui.JFrameBoundsKeeper;

public class JFrameBoundsKeeperImpl implements JFrameBoundsKeeper {

	private final BoundsPersistence _persistence;
	
	public JFrameBoundsKeeperImpl(BoundsPersistence persistence) {
		_persistence = persistence;
	}
	
	public void keepBoundsFor(final JFrame frame, final String id){
		restorePreviousBounds(id, frame);
		startKeepingBounds(id, frame);
	}

	private void restorePreviousBounds(String id, JFrame frame) {
		Rectangle storedBounds = _persistence.getStoredBounds(id);
		if (storedBounds != null){
			frame.setBounds(storedBounds);
		}
	}

	private void startKeepingBounds(final String id, final JFrame frame) {
		frame.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				boundsChanged();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				boundsChanged();
			}	
			
			private void boundsChanged() {
				_persistence.storeBounds(id, frame.getBounds());
			}	
		
		});
	}
	
	
	

}
