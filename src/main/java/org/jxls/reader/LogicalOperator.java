package org.jxls.reader;

/**
 * @author Wangtd
 */
public interface LogicalOperator {
    /**
     * Logical OR
     */
    String OR = "or";
    /**
     * Logical AND
     */
    String AND = "and";

    static boolean isAny(String operator) {
        return OR.equalsIgnoreCase(operator);
    }

    static boolean isAll(String operator) {
        return AND.equalsIgnoreCase(operator);
    }
}
