package sneer.kernel.container.utils.metaclass.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sneer.kernel.container.utils.metaclass.tests.bean.Bean;
import wheel.io.codegeneration.ClassUtils;
import wheel.io.codegeneration.MetaClass;

public class MetaClassTest {

	@Test
	public void testMetaClass() throws Exception {
		MetaClass metaClass = ClassUtils.metaClass(Bean.class);
		assertTrue(metaClass.isInterface());
		assertEquals("sneer.kernel.container.utils.metaclass.tests.bean.Bean", metaClass.getName());
		assertEquals("sneer.kernel.container.utils.metaclass.tests.bean", metaClass.getPackageName());
		
		//assertTrue(metaClass.isAssignanbleTo(Object.class));
	}
}