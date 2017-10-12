package ua.nure.yakunina.galen;

import ua.nure.yakunina.galen.testBase.SAPTestBase;
import org.testng.annotations.Test;


public class RegressionTest extends SAPTestBase {
    @Test(dataProvider = "TestDataFromExcel", groups = {"devices.regression"})
    public void testsOnDevices(String targetPageURL, String specFile) {
        verifyOnDevices(targetPageURL, specFile, DESKTOP, TABLET, MOBILE);
    }
    @Test(dataProvider = "TestDataFromExcel", groups = {"desktop.regression"})
    public void testsOnDesktop(String targetPageURL, String specFile) {
        verifyOnDevices(targetPageURL, specFile, DESKTOP);
    }

    @Test(dataProvider = "TestDataFromExcel", groups = {"tablet.regression"})
    public void testsOnTablet(String targetPageURL, String specFile) {
        verifyOnDevices(targetPageURL, specFile, TABLET);
    }

    @Test(dataProvider = "TestDataFromExcel", groups = {"mobile.regression"})
    public void testsOnMobile(String targetPageURL, String specFile) {
        verifyOnDevices(targetPageURL, specFile, MOBILE);
    }
}
