package sneer.bricks.ownName.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.bricks.ownName.OwnNameKeeper;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import sneer.widgets.reactive.RFactory;
import sneer.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;

public class OwnNameKeeperDemo extends BrickTestSupport {

	@Inject
	private RFactory rfactory;
	
	@Inject
	private OwnNameKeeper ownNameKeeper;

	public static void main(String[] args) throws Exception {
		OwnNameKeeperDemo demo = initializeDemo();
		createWidgets(demo);
		
		demo.ownNameKeeper.name().addReceiver(
				new Omnivore<String>(){
					@Override
					public void consume(String valueObject) {
						System.out.println(valueObject);
					}
				}
			);
	}
	
	private static OwnNameKeeperDemo initializeDemo() {
		OwnNameKeeperDemo demo = new OwnNameKeeperDemo();
		Container container = ContainerUtils.getContainer();
		demo.rfactory = container.produce(RFactory.class);	
		demo.ownNameKeeper = container.produce(OwnNameKeeper.class);
		demo.ownNameKeeper.setName("Sandro Luiz Bihaiko");
		return demo;
	}

	private static void createWidgets(OwnNameKeeperDemo demo) {
		
		TextWidget newTextField1 = demo.rfactory.newTextField(demo.ownNameKeeper.name(), demo.ownNameKeeper.nameSetter());
		final JFrame frm1 =createTestFrame(newTextField1);
		
		TextWidget newTextField2 = demo.rfactory.newEditableLabel(demo.ownNameKeeper.name(), demo.ownNameKeeper.nameSetter());
		final JFrame frm2 =createTestFrame(newTextField2);
		
		frm1.setBounds(10, 10, 300, 100);
		frm2.setBounds(10, 120, 300, 100);
		
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					frm1.setVisible(true);
					frm2.setVisible(true);
				}
			}
		);
	}

	private static JFrame createTestFrame(final TextWidget textWidget) {
		JFrame frm = new JFrame(textWidget.getClass().getSimpleName());
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(new FlowLayout());
		frm.getContentPane().add(textWidget.getContainer());
		return frm;
	}
}