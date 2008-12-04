package tests;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.junit.internal.runners.InitializationError;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import wheel.lang.Environment;
import wheel.testutil.WheelEnvironment;

public class ContainerEnvironment extends WheelEnvironment {

	private Container _container; 
	
	public ContainerEnvironment(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@Override
	protected Environment testMethodEnvironment() {
		return new Environment() {
			@Override
			public <T> T provide(Class<T> intrface) {
				if (_container == null) return null;
				return _container.provide(intrface);
			}
		};
	}
	
	@Override
	protected Object createTest() throws Exception {
		final Object test = super.createTest();
		_container = ContainerUtils.newContainer(boundBrickFieldsFor(test));
		return test;
	}

	public static Object[] boundBrickFieldsFor(Object instance) {
		final ArrayList<Object> result = new ArrayList<Object>();
		Class<? extends Object> klass = instance.getClass();
		while (klass != Object.class) {
			collectBoundBrickFields(result, instance, klass);
			klass = klass.getSuperclass();
		}
		return result.toArray();
	}

	private static void collectBoundBrickFields(
			final ArrayList<Object> collector, Object instance,
			final Class<? extends Object> klass) {
		
		for (Field field : klass.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			if (field.getClass().isPrimitive())
				continue;
			final Object fieldValue = getFieldValue(field, instance);
			if (fieldValue == null)
				continue;
			collector.add(fieldValue);
		}
	}

	private static Object getFieldValue(Field field, Object instance) {
		try {
			field.setAccessible(true);
			return field.get(instance);
		} catch (Exception e) {
			throw new IllegalStateException(e); 
		}
	}

}