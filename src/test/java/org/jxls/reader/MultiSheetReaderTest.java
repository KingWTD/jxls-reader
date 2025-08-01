package org.jxls.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.sample.Department;
import org.jxls.reader.sample.Employee;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * @author Wangtd
 */
public class MultiSheetReaderTest extends TestCase {
    public static final String dataXLS = "/templates/multisheetData.xlsx";
    public static final String xmlConfig = "/xml/multisheets.xml";

    public void testMultiSheet() throws IOException, SAXException, InvalidFormatException {
        buildAndValidate(xmlConfig, dataXLS);
    }

    private void buildAndValidate(String xmlConfigParam, String dataXLSParam) throws IOException, SAXException, InvalidFormatException {
        InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream(xmlConfigParam));
        XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
        InputStream inputXLS = new BufferedInputStream(getClass().getResourceAsStream(dataXLSParam));
        List departments = new ArrayList();
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("departments", departments);
        mainReader.read(inputXLS, beans);
        inputXLS.close();
        Department department = (Department) departments.get(0);
        // check sheet1 data
        assertEquals("IT", department.getName());
        assertEquals("Derek", department.getChief().getName());
        assertEquals(new Integer(35), department.getChief().getAge());
        assertEquals(3000.0, department.getChief().getPayment());
        assertEquals(0.30, department.getChief().getBonus());
        assertEquals(5, department.getStaff().size());
        Employee employee = (Employee) department.getStaff().get(0);
        checkEmployee(employee, "Elsa", 28, 1500.0, 0.15);
        employee = (Employee) department.getStaff().get(1);
        checkEmployee(employee, "Oleg", 32, 2300.0, 0.25);
        employee = (Employee) department.getStaff().get(2);
        checkEmployee(employee, "Neil", 34, 2500.0, 0.0);
        employee = (Employee) department.getStaff().get(3);
        checkEmployee(employee, "Maria", 34, 1700.0, 0.15);
        employee = (Employee) department.getStaff().get(4);
        checkEmployee(employee, "John", 35, 2800.0, 0.20);
    }

    private void checkEmployee(Employee employee, String name, Integer age, Double payment, Double bonus){
        assertNotNull( employee );
        assertEquals( name, employee.getName() );
        assertEquals( age, employee.getAge() );
        assertEquals( payment, employee.getPayment() );
        assertEquals( bonus, employee.getBonus() );
    }
}
