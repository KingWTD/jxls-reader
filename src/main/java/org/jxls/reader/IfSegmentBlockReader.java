package org.jxls.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wangtd
 */
public class IfSegmentBlockReader extends BaseBlockReader {

    SectionCheck segmentCheck;
    List<XLSBlockReader> blockReaders = new ArrayList<XLSBlockReader>();
    ConvertUtilsBeanProviderDelegate convertUtilsBeanProvider = new ConvertUtilsBeanProviderDelegate();

    @Override
    public XLSReadStatus read(XLSRowCursor cursor, Map beans) {
        readStatus.clear();
        if (segmentCheck.isCheckSuccessful(cursor)) {
            for (XLSBlockReader blockReader : blockReaders) {
                readStatus.mergeReadStatus(blockReader.read(cursor, beans));
                cursor.moveForward();
            }
        }
        cursor.moveBackward();
        return readStatus;
    }

    @Override
    public void setConvertUtilsBeanProvider(ConvertUtilsBeanProvider provider) {
        this.convertUtilsBeanProvider.setDelegate(provider);
    }

    public List getBlockReaders() {
        return blockReaders;
    }

    public void setBlockReaders(List blockReaders) {
        this.blockReaders = blockReaders;
        for (XLSBlockReader blockReader : this.blockReaders) {
            blockReader.setConvertUtilsBeanProvider(convertUtilsBeanProvider);
        }
    }

    public void addBlockReader(XLSBlockReader blockReader) {
        blockReader.setConvertUtilsBeanProvider(convertUtilsBeanProvider);
        blockReaders.add(blockReader);
    }

    public SectionCheck getIfSegmentCondition() {
        return segmentCheck;
    }

    public void setIfSegmentCondition(SectionCheck sectionCheck) {
        this.segmentCheck = sectionCheck;
    }
}
