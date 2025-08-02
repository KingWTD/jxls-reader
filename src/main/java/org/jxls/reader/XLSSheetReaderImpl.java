package org.jxls.reader;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Leonid Vysochyn
 */
public class XLSSheetReaderImpl implements XLSSheetReader {
    protected final Log log = LogFactory.getLog(getClass());

    private List<XLSBlockReader> blockReaders = new ArrayList<XLSBlockReader>();
    private String sheetName;
    private int sheetIdx = -1;

    private boolean multiSheet;
    private String items;
    private String var;
    private Class varType;
    private SectionCheck matchesCheck;

    XLSReadStatus readStatus = new XLSReadStatus();

    ConvertUtilsBeanProviderDelegate convertUtilsBeanProvider = new ConvertUtilsBeanProviderDelegate();

    public XLSReadStatus read(Sheet sheet, Map beans) {
        readStatus.clear();
        if (multiSheet) {
            JexlContext context = new MapContext(beans);
            ExpressionCollectionParser parser = new ExpressionCollectionParser(context, items + ";", true);
            Collection itemsCollection = parser.getCollection();
            createNewCollectionItem(itemsCollection, beans);
        }
        XLSRowCursor cursor = new XLSRowCursorImpl( sheetName, sheet );
        for (XLSBlockReader blockReader : blockReaders) {
            readStatus.mergeReadStatus(blockReader.read(cursor, beans));
            cursor.moveForward();
        }
        return readStatus;
    }

    private void createNewCollectionItem(Collection itemsCollection, Map beans) {
        Object obj = null;
        try {
            obj = varType.newInstance();
        } catch (Exception e) {
            String message = "Can't create a new collection item for " + items + ". varType = " + varType.getName();
            readStatus.addMessage(new XLSReadMessage(message));
            if (!ReaderConfig.getInstance().isSkipErrors()) {
                readStatus.setStatusOK(false);
                throw new XLSDataReadException(message, readStatus, e);
            }
            if (log.isWarnEnabled()) {
                log.warn(message);
            }
        }
        itemsCollection.add(obj);
        beans.put(var, obj);
    }

    public boolean matchesSheet(Sheet sheet) {
        if (matchesCheck != null) {
            XLSRowCursor cursor = new XLSRowCursorImpl( sheet );
            return matchesCheck.isCheckSuccessful(cursor);
        }
        return true;
    }

    public String getSheetNameBySheetIdx(Sheet sheet, int idx){
        Sheet sheetAtIdx = sheet.getWorkbook().getSheetAt(idx);
        return sheetAtIdx.getSheetName();
    }
    
    public void setConvertUtilsBeanProvider( ConvertUtilsBeanProvider provider ){
    	this.convertUtilsBeanProvider.setDelegate( provider ) ;
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
    	blockReader.setConvertUtilsBeanProvider( convertUtilsBeanProvider );
        blockReaders.add( blockReader );
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getSheetIdx(){
        return sheetIdx;
    }

    public void setSheetIdx(int sheetIdx){
        this.sheetIdx = sheetIdx;
    }

    public SectionCheck getMatchesCondition() {
        return matchesCheck;
    }

    public void setMatchesCondition(SectionCheck sectionCheck) {
        this.matchesCheck = sectionCheck;
    }

    public boolean isMultiSheet() {
        return multiSheet;
    }

    public void setMultiSheet(boolean multiSheet) {
        this.multiSheet = multiSheet;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setVarType(Class varType) {
        this.varType = varType;
    }

    public String getItems() {
        return items;
    }

    public String getVar() {
        return var;
    }

    public Class getVarType() {
        return varType;
    }
}
