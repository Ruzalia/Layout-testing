package ua.nure.yakunina.galen.testBase;

import com.galenframework.reports.nodes.TestReportNode;
import ua.nure.yakunina.galen.core.TestProperties;
import ua.nure.yakunina.galen.helpers.ExcelHelper;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;


public class SAPTestBase extends GalenTestBase {

    protected ThreadLocal<String> fullURL = new ThreadLocal();

    protected static final String BEFORE_PSEUDOCLASS = ":before";
    protected static final String AFTER_PSEUDOCLASS = ":after";

    @DataProvider(parallel = true)
    public Object[][] TestDataFromExcel(ITestContext c) throws Exception {
        browserName = c.getSuite().getXmlSuite().getParameter("browserName");
        Object[][] testObArray = ExcelHelper.readExcelAndFormData(TestProperties.getProperty(TestProperties.Property.TEST_DATA_FILE), TestProperties.getProperty(TestProperties.Property.EXCEL_REGRESSION_SHEET_NAME));
        return testObArray;
    }

    @DataProvider
    public Object[][] SingleRunTestData(ITestContext c) throws Exception {
        Object[][] testObArray = ExcelHelper.readExcelAndFormData(TestProperties.getProperty(TestProperties.Property.TEST_DATA_FILE), TestProperties.getProperty(TestProperties.Property.EXCEL_SINGLE_RUN_SHEET_NAME));
        return testObArray;
    }

    public String getfullURL() {
        return fullURL.get();
    }

    public synchronized void setfullURL(String url) {
        fullURL.set(url);
    }

    protected void goToUrl(String url){
        setfullURL(url);
        loadFullUrl();
    }

    @Override
    protected void loadFullUrl() {
        getDriver().get(getfullURL());
    }

    protected void verifyOnDevices(String targetPageURL, String specFile, String... devices){
        for(String device : devices) {
            addArgumentToReport(device);
        }
        reinitReport();
        goToUrl(targetPageURL);

        for(String device : devices) {
            resizeAndRefresh(device);
            checkLayout(specFile, device);
        }

        evaluateTestResults();
    }

    protected void addComputedStyleReportNode(String cssSelector, String pseudoclass, String cssProperty, String elementName, String expectedValue) {
        String actualValue = getComputedStyleOfPseudoclass(cssSelector, pseudoclass, cssProperty);
        formAndAddReportNode(cssProperty, elementName, expectedValue, actualValue);
    }

    protected void addComputedStyleReportNodeForElementWithNumber(String cssSelector, String pseudoclass, String cssProperty, String elementName, String expectedValue, int orderNumber) {
        String actualValue = getComputedStyleOfPseudoclassWithNumber(cssSelector, pseudoclass, cssProperty, orderNumber);
        formAndAddReportNode(cssProperty, elementName, expectedValue, actualValue);
    }

    private void formAndAddReportNode(String property, String elementName, String expectedValue, String actualValue) {
        String modifiedActualValue = actualValue.replace("\"", "");
        String nodeName = property + " of " + elementName + " is '" +
                modifiedActualValue + "'. It should be '" + expectedValue + "'.";
        if (expectedValue.contains(modifiedActualValue)) {
            getReport().addNode(getInfoNode(nodeName));
        } else {
            testFailedFlag.set(true);
            getReport().addNode(getErrorNode(nodeName));
        }
    }

    protected void addReportNodeIntoSection(String dimension, String sectionName) {
        getReport().sectionStart(sectionName);
        getReport().addNode(formReport(dimension));
        getReport().sectionEnd();
    }

    protected TestReportNode formReport(String dimension) {
        return new TestReportNode(this.getReport().getFileStorage());
    }

    protected TestReportNode getInfoNode(String info) {
        TestReportNode tempNode = new TestReportNode(getReport().getFileStorage());
        tempNode.setName(info);
        return tempNode;
    }

    protected TestReportNode getErrorNode(String error) {
        testFailedFlag.set(true);
        TestReportNode tempNode = getInfoNode(error);
        tempNode.setStatus(TestReportNode.Status.ERROR);
        return tempNode;
    }

    public void addInfoReportNode(String name) {
        getReport().addNode(getInfoNode(name));
    }

    public String getComputedStyleOfPseudoclass(String cssSelector, String pseudoclass, String property) {
        return driverHelper.executeScript("return window.getComputedStyle(document" +
                        ".querySelector(arguments[0]),arguments[1]).getPropertyValue(arguments[2]);",
                cssSelector, pseudoclass, property).toString();
    }

    public String getComputedStyleOfPseudoclassWithNumber(String cssSelector, String pseudoclass, String property, int number) {
        return driverHelper.executeScript("return window.getComputedStyle(document" +
                        ".querySelectorAll(arguments[0])[" + number + "],arguments[1]).getPropertyValue(arguments[2]);",
                cssSelector, pseudoclass, property).toString();
    }
}