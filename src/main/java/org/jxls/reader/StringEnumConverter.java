package org.jxls.reader;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.AbstractConverter;

/**
 * @author Wangtd
 */
public class StringEnumConverter extends AbstractConverter {

    public StringEnumConverter() {
        super();
    }

    @Override
    protected Class getDefaultType() {
        return String.class;
    }

    @Override
    protected Object convertToType(Class type, Object value) throws Throwable {
        if (value == null) {
            throw new ConversionException("No value specified");
        }

        if (Enum.class.isAssignableFrom(type)) {
            if (value instanceof Enum) {
                return value;
            }
            return Enum.valueOf(type, value.toString());
        }

        if (value instanceof String) {
            return value;
        }

        if (Object.class.equals(type)) {
            return value.toString();
        }

        throw new ConversionException("Can't convert value '" + value + "' to type " + type);
    }
}
