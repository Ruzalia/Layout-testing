package ua.nure.yakunina.galen.testBase;

import com.galenframework.api.Galen;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.support.LayoutValidationException;
import com.galenframework.testng.GalenTestNgTestBase;
import com.galenframework.utils.GalenUtils;
import ua.nure.yakunina.galen.core.Driver;
import ua.nure.yakunina.galen.core.DriverFactory;
import ua.nure.yakunina.galen.core.TestProperties;
import ua.nure.yakunina.galen.enums.Browsers;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;

public abstract class GalenTestBase extends GalenTestNgTestBase {

    public static final String MOBILE = "mobile";
    public static final String TABLET = "tablet";
    public static final String DESKTOP = "desktop";
    public static final String excludePatternStart = "http";
    public static final String excludeNull = "null,";
    public static final String excludeFolder = "specs/";
    public static final String excludeExtension = ".spec";
    protected Object[] args;
    protected Method method;
    protected final ThreadLocal<Boolean> testFailedFlag = new ThreadLocal<>();
    protected ThreadLocal<String> url = new ThreadLocal<>();
    protected Driver driverHelper;
    protected String browserName;
    Map<String, Dimension> configurations;
    private List<LayoutValidationException> errorsList = Collections.synchronizedList(new ArrayList<>());

    public GalenTestBase() {
        testFailedFlag.set(false);
        configurations = new HashMap<>();
        configurations.put(MOBILE, new Dimension(480, 800));
        configurations.put(TABLET, new Dimension(870, 1000));
        configurations.put(DESKTOP, new Dimension(1550, 1200));
        driverHelper = new Driver();
    }

    protected abstract void loadFullUrl();

    @BeforeMethod(alwaysRun = true)
    @Override
    public void initReport(Method method, Object[] arguments) {
        arguments = excludeUrlFromArgumentsIntoProperty(method, arguments);
        args = arguments;
        this.method = method;
    }

    public void resizeAndRefresh(String deviceName) {
        setWindowSize(deviceName);
        loadFullUrl();
        printActualSize(deviceName);
    }

    protected void setWindowSize(String deviceName) {
        Dimension dimension = configurations.get(deviceName);
        driverHelper.setWindowSize(dimension);
    }

    @Override
    public WebDriver createDriver(Object[] args) {
        checkBrowserName(args);
        System.out.println(browserName);
        Reporter.log(browserName);
        WebDriver driver = new DriverFactory(driverHelper).createDriver(Browsers.valueOf(browserName));
        return driver;
    }

    @Override
    public void checkLayout(String specPath, SectionFilter sectionFilter, Properties properties, Map<String, Object> vars) throws IOException {
        String deviceName = sectionFilter.getIncludedTags().get(0);
        String title = String.format("Check layout %s on %s (%dx%d). Browser: %s. \nURL: %s", specPath, deviceName, configurations.get(deviceName).getWidth(), configurations.get(deviceName).getHeight(), browserName, url.get());
        LayoutReport layoutReport = Galen.checkLayout(this.getDriver(), specPath, sectionFilter, properties, vars);
        this.getReport().layout(layoutReport, title);

        if (this.getReport().fetchStatistic().getTotal() == 0)
            Assert.fail("No verifications were performed. Please check the page URL.");

        if (layoutReport.errors() > 0) {
            testFailedFlag.set(true);
            errorsList.add(new LayoutValidationException(specPath, layoutReport, sectionFilter));
        }
    }


    public void checkLayout(String specName, String deviceName) {
        try {
            checkLayout(specName, asList(deviceName));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Can't find spec file " + specName);
        }
    }

    public void evaluateTestResults() {

        if (testFailedFlag.get() != null && testFailedFlag.get() == true) {
            System.out.println("There are the following LayoutValidationExceptions: ");
            for (int i = 0; i < errorsList.size(); i++) {
                System.out.println(errorsList.get(i).getMessage());
                Reporter.log(errorsList.get(i).getMessage());
            }
            Assert.fail("There are layout validation exceptions.");
        }
    }

    @AfterMethod()
    public void cleanErrorList(Method method, Object[] arguments) {
        if(!errorsList.isEmpty()){
            errorsList.clear();
        }
    }

    private void printActualSize(String deviceName) {
        java.awt.Dimension actual = GalenUtils.getViewportArea(getDriver());
        String viewport = String.format("Actual ViewportArea for %s: %s x %s.", deviceName, (int) actual.getWidth(), (int) actual.getHeight());
        System.out.println(viewport);
        Reporter.log(viewport);
    }

    private Object[] excludeUrlFromArgumentsIntoProperty(Method method, Object[] arguments) {
        Object[] temp = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] != null) {
                if (arguments[i].toString().contains(excludePatternStart)) {
                    url.set(arguments[i].toString());
                } else {
                    temp[i] = arguments[i];
                }
            }
        }
        return temp;
    }

    private String makeTestInfoNameShorter() {
        String empty = "";
        String curName = testInfo.get().getName();
        curName = curName.replace(excludeFolder, empty).replace(excludeExtension, empty)
                .concat(" " + browserName);
        if (curName.contains(excludeNull)) {
            curName = curName.replace(excludeNull, empty);
        }
        return curName;
    }

    private void checkBrowserName(Object[] args) {
        if (browserName == null) {
            if ((args.length == 1 || args.length == 3) && args[0] != null)
                browserName = args[0].toString();
            else
                browserName = TestProperties.getBrowser();
        }
    }

    protected void addArgumentToReport(String arg){
        Object[] temp = new Object[args.length + 1];
        for(int i = 0; i < args.length; i++){
            temp[i] = args[i];
        }
        temp[args.length] = arg;
        args = temp;
    }

    protected void reinitReport(){
        super.initReport(method, args);
        String newTestInfoName = makeTestInfoNameShorter();
        testInfo.get().setName(newTestInfoName);
    }
}