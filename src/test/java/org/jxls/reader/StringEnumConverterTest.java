package org.jxls.reader;

import junit.framework.TestCase;

/**
 * @author Wangtd
 */
public class StringEnumConverterTest extends TestCase {

    public void testConverter() {
        StringEnumConverter converter = new StringEnumConverter();
        Object value;
        value = converter.convert(String.class, "OK");
        assertEquals("OK", value);
        value = converter.convert(String.class, new TestObj());
        assertEquals("TestObj", value);
        value = converter.convert(TestEnum.class, "OK");
        assertEquals(TestEnum.OK, value);
        value = converter.convert(TestEnum.class, TestEnum.OK);
        assertEquals(TestEnum.OK, value);
    }

    public enum TestEnum {
        OK, NG;
    }

    public static class TestObj {
        @Override
        public String toString() {
            return TestObj.class.getSimpleName();
        }
    }
}
