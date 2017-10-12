package ua.nure.yakunina.galen.core;

import ua.nure.yakunina.galen.enums.Browsers;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import ua.nure.yakunina.galen.enums.Constants;

import java.util.concurrent.TimeUnit;

public class DriverFactory {

    private Driver _driverHelper;

    public DriverFactory(Driver driverHelper){
        _driverHelper = driverHelper;
    }

    public synchronized WebDriver createDriver(final Browsers browserName) {
        DesiredCapabilities caps;
        WebDriver newDriver;
        try {
            switch (browserName) {
                case firefox:
                    FirefoxProfile profile = getFirefoxProfile();
                    caps = DesiredCapabilities.firefox();
                    caps = setCommonCapabilities(caps);
                    caps.setCapability(FirefoxDriver.PROFILE, profile);
                    newDriver = new FirefoxDriver(caps);
                    System.out.println("  Browser:  " + Browsers.firefox);
                    break;
                case chrome:
                    caps = setChromeCapabilities();
                    caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                    newDriver = new ChromeDriver(caps);
                    System.out.println("  Browser:  " + Browsers.chrome);
                    break;
                case internetExplorer:
                    System.setProperty("webdriver.ie.driver", "src/main/resources/drivers/IEDriverServer.exe");
                    caps = DesiredCapabilities.internetExplorer();
                    caps = setCommonCapabilities(caps);
                    caps.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                    caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                    caps.setCapability("nativeEvents", false);
                    caps.setCapability("ignoreZoomSetting", true);
                    newDriver = new InternetExplorerDriver(caps);
                    System.out.println("  Browser:  " + Browsers.internetExplorer);
                    break;
                default:
                    throw new RuntimeException(browserName.toString() + " is not supported browser");
            }
            newDriver.manage().timeouts().pageLoadTimeout(Constants.TimeOuts.PAGE_LOAD_TIMEOUT, TimeUnit.MILLISECONDS);
            newDriver.manage().timeouts().implicitlyWait(Constants.TimeOuts.WAITER_IMPLICIT_TIMEOUT, TimeUnit.MILLISECONDS);
            newDriver.manage().timeouts().setScriptTimeout(Constants.TimeOuts.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            _driverHelper.setDriver(newDriver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to created driver", e);
        }
        return newDriver;
    }

    private static FirefoxProfile getFirefoxProfile() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setEnableNativeEvents(false);
        profile.setPreference("webdriver_assume_untrusted_issuer", false);
        profile.setPreference("datareporting.healthreport.service.enabled", false);
        profile.setPreference("geo.prompt.testing", true);
        profile.setPreference("geo.prompt.testing.allow", true);
        profile.setPreference("browser.cache.disk.enable", false);
        profile.setPreference("dom.max_chrome_script_run_time", 0);
        profile.setPreference("dom.max_script_run_time", 0);
        profile.setPreference("devtools.selfxss.count", 100);
        return profile;
    }

    private static DesiredCapabilities setChromeCapabilities() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities = setCommonCapabilities(capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setCommonCapabilities(DesiredCapabilities capabilities) {
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        return capabilities;
    }

}
