package sneer.pulp.clock.impl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.clock.Clock;
import sneer.pulp.clock.realtime.mocks.RealtimeClockMock;

public class ClockTest {

	int _lastRun = 0;
	int _lastCount = 0;
	
	final List<Integer> _order = new ArrayList<Integer>();
	final RealtimeClockMock mock = new RealtimeClockMock();
	
	@Test
	public void test() throws Exception {

		final Container container = ContainerUtils.newContainer(mock);
		final Clock clock = container.produce(Clock.class);

//		mock.advanceTime(1000);    //The test should work even with this line because everything is relative. No time value here is absolute.
		
		clock.addAlarm(50, new MyRunnable(50));
		clock.addPeriodicAlarm(20, new MyRunnable(20));
		clock.addAlarm(10, new MyRunnable(10));
		clock.addPeriodicAlarm(35, new MyRunnable(35));
		clock.addAlarm(30, new MyRunnable(30));

		step(01, 00, 0); 	//time = 01 []
		step(11, 10, 1); 	//time = 11 [10]
		step(20, 30, 1); 	//time = 31 [10, 20, 30]
		step(20, 50, 1); 	//time = 51 [10, 20, 30, 35, 40, 50]
		step(30, 20, 4); 	//time = 81 [10, 20, 30, 35, 40, 50, 60, 70, 80]
	}

	@Test
	public void testCallTime() {
//		assertFalse(_order.isEmpty());   //_order will always be empty. JUnit runs each test on a new instance of the test class.
		
		Integer lastInteger = null;
		for (Integer timeout : _order) {
			if(lastInteger!=null)
				assertTrue(timeout>=lastInteger);
			lastInteger = timeout;
		}
	}

	private void step(int plusTime, int time, int count) {
		mock.advanceTime(plusTime);	
		assertEquals(time, _lastRun);
		assertEquals(count, _lastCount);
	}
	
	private class MyRunnable implements Runnable{

		private final int _timeout;
		private int count = 0;

		public MyRunnable(int timeout) {
			_timeout = timeout;
		}

		@Override
		public void run() {
			_lastRun = _timeout;
			_lastCount = ++count;
			_order.add(_timeout * _lastCount);
		}
	}
}