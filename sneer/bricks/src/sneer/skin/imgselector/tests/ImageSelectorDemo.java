package sneer.skin.imgselector.tests;

import java.awt.Image;

import sneer.kernel.container.ContainerUtils;
import sneer.skin.imgselector.ImageSelector;
import wheel.io.Logger;
import wheel.lang.Consumer;
import wheel.lang.Environments;
import wheel.lang.Threads;

public class ImageSelectorDemo  {

	public static void main(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		Environments.runWith(ContainerUtils.newContainer(), new Runnable(){ @Override public void run() {
			try {
				ImageSelector imageSelector = Environments.my(ImageSelector.class);
				imageSelector.open(new Consumer<Image>(){@Override public void consume(Image valueObject) {
					//OK
				}});

				Threads.sleepWithoutInterruptions(30000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}