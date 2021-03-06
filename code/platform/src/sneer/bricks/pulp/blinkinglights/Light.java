package sneer.bricks.pulp.blinkinglights;

public interface Light {

	boolean isOn();
	
	LightType type();
	String caption();
	Throwable error();
	String helpMessage();
	
	boolean hasConfirmation();
	void sayYes();
	void sayNo();
}
