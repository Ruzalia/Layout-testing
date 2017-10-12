package ua.nure.yakunina.galen;

import ua.nure.yakunina.galen.testBase.SAPTestBase;
import org.testng.annotations.Test;


public class LocalTest extends SAPTestBase {

    @Test(dataProvider = "SingleRunTestData", groups = {"test"})
    public void localTestsOnDevices(String url, String spec) {
        verifyOnDevices(url, spec, DESKTOP, TABLET, MOBILE);
    }

    @Test(dataProvider = "SingleRunTestData", groups = {"test"})
    public void localTestOnDesktop(String url, String spec) {
        verifyOnDevices(url, spec, DESKTOP);
    }

    @Test(dataProvider = "SingleRunTestData", groups = {"test"})
    public void localTestsOnTablet(String url, String spec){
        verifyOnDevices(url, spec, TABLET);
    }

    @Test(dataProvider = "SingleRunTestData", groups = {"test"})
    public void localTestsOnMobile(String url, String spec) {
        verifyOnDevices(url, spec, MOBILE);
    }
}