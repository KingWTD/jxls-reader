package org.jxls.reader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leonid Vysochyn
 */
public class SimpleSectionCheck implements SectionCheck {

    String operator;
    List<OffsetRowCheck> offsetRowChecks = new ArrayList<OffsetRowCheck>();

    public SimpleSectionCheck() {
    }

    public SimpleSectionCheck(List relativeRowChecks) {
        this.offsetRowChecks = relativeRowChecks;
    }

    public boolean isCheckSuccessful(XLSRowCursor cursor) {
        if (LogicalOperator.isAny(operator)) {
            for (OffsetRowCheck offsetRowCheck : offsetRowChecks) {
                if (offsetRowCheck.isCheckSuccessful(cursor)) {
                    return true;
                }
            }
            return false;
        } else {
            for (OffsetRowCheck offsetRowCheck : offsetRowChecks) {
                if (!offsetRowCheck.isCheckSuccessful(cursor)) {
                    return false;
                }
            }
            return true;
        }
    }

    public void addRowCheck(OffsetRowCheck offsetRowCheck) {
        offsetRowChecks.add( offsetRowCheck );
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List getOffsetRowChecks() {
        return offsetRowChecks;
    }

    public void setOffsetRowChecks(List<OffsetRowCheck> offsetRowChecks) {
        this.offsetRowChecks = offsetRowChecks;
    }
}
