package ua.nure.yakunina.galen.core;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ua.nure.yakunina.galen.enums.Constants.TimeOuts.DEFAULT_TIMEOUT;
import static ua.nure.yakunina.galen.enums.Constants.TimeOuts.WAITER_IMPLICIT_TIMEOUT;


public class Driver {
    private final ThreadLocal<WebDriver> driverInstance = new InheritableThreadLocal<>();
    private final ThreadLocal<Actions> actionsNavigators = new InheritableThreadLocal<Actions>();

    private boolean logNotFoundElement = true;


    public synchronized WebDriver setDriver(WebDriver driver) throws Exception {
        if (driver == null) {
            driverInstance.remove();
            return null;
        } else {
            System.out.println("[Trying to create a new driver");
            driverInstance.set(driver);
            return get();
        }
    }

    public WebDriver get() {
        return driverInstance.get();
    }

    public  Actions getNavigator() {
        if (actionsNavigators.get() == null) {
            actionsNavigators.set(new Actions(get()));
        }
        return actionsNavigators.get();
    }

    public void setWindowSize(Dimension dim) {
        get().manage().window().setSize(dim);
    }

    public static void delay(final Integer timeoutInMilliSeconds) {
        try {
            java.lang.Thread.sleep(timeoutInMilliSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates new waiter object using specified waiter time out and specified waiter sleep delay
     *
     * @param timeoutInMilliSeconds waiter time out in milli seconds, default value Constants.TimeOuts.DEFAULT_TIMEOUT
     * @param sleepInMilliSeconds   waiter sleep delay in milli seconds,
     *                              default value Constants.TimeOuts.WAITER_SLEEP_DELAY
     * @return new waiter object
     */
    public FluentWait getWaiter(final Integer timeoutInMilliSeconds, final Integer sleepInMilliSeconds) {
        FluentWait wait = new FluentWait(get(), new SystemClock(), Sleeper.SYSTEM_SLEEPER);
        wait.ignoring(NotFoundException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.withTimeout(timeoutInMilliSeconds, TimeUnit.MILLISECONDS);
        wait.pollingEvery(sleepInMilliSeconds, TimeUnit.MILLISECONDS);
        return wait;
    }

    /**
     * Creates new waiter object using specified waiter time out and specified waiter sleep delay
     *
     * @param timeoutInMilliSeconds waiter time out in milli seconds, default value Constants.TimeOuts.DEFAULT_TIMEOUT
     * @return new waiter object
     */
    public FluentWait getWaiter(final Integer timeoutInMilliSeconds) {
        return getWaiter(timeoutInMilliSeconds, WAITER_IMPLICIT_TIMEOUT);
    }

    public FluentWait getWaiter() {
        return getWaiter(DEFAULT_TIMEOUT, WAITER_IMPLICIT_TIMEOUT);
    }

    public Alert waitForAlert(final Integer timeoutInMilliSeconds) {
        try {
            return (Alert) getWaiter(timeoutInMilliSeconds).until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            return null;
        }
    }

    public Alert waitForAlert() {
        return waitForAlert(DEFAULT_TIMEOUT);
    }

    public Boolean waitForConditionBoolean(ExpectedCondition<Boolean> condition, final Integer timeoutInMilliSeconds) {
        return (Boolean) getWaiter(timeoutInMilliSeconds).until(condition);
    }

    public Boolean waitForConditionBoolean(ExpectedCondition<Boolean> condition) {
        return (Boolean) getWaiter().until(condition);
    }

    public WebElement waitForVisibilityOfElement(final By by, final Integer timeoutInMilliSeconds) {
        return (WebElement) getWaiter(timeoutInMilliSeconds).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebElement waitForVisibilityOfElement(final By by) {
        return waitForVisibilityOfElement(by, DEFAULT_TIMEOUT);
    }

    public WebElement waitForVisibilityOfElement(final WebElement element, final Integer timeoutInMilliSeconds) {
        return (WebElement) getWaiter(timeoutInMilliSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForVisibilityOfElement(final WebElement element) {
        return waitForVisibilityOfElement(element, DEFAULT_TIMEOUT);
    }

    public WebElement waitForElementToBeClickable(final By by, final Integer timeoutInMilliSeconds) {
        return (WebElement) getWaiter(timeoutInMilliSeconds).until(ExpectedConditions.elementToBeClickable(by));
    }

    public WebElement waitForElementToBeClickable(final By by) {
        return (WebElement) getWaiter().until(ExpectedConditions.elementToBeClickable(by));
    }

    public WebElement waitForElementToBeClickable(final WebElement element) {
        return (WebElement) getWaiter().until(ExpectedConditions.elementToBeClickable(element));
    }

    public void wait(int timeOut) {
        delay(timeOut);
    }

    public void click(WebElement el) {
        try {
            el.click();
        } catch (Exception e) {
            this.jsScrollIntoView(el);
            this.jsClick(el);
        }
    }

    public void clickOn(WebElement el) {
        click(el);
    }

    public void type(final WebElement element, final String text) {
        int count = 5;
        while (!((String) executeScript("return arguments[0].value", element)).equalsIgnoreCase(text) && count-- > 0) {
            element.clear();
            element.sendKeys(text);
            delay(200);
        }
    }

    public void setValueToInput(final WebElement element, final String text) {
        executeScript("arguments[0].value='" + text + "'", element);
    }

    public List<WebElement> findVisibleElements(By by) {
        List<WebElement> webElements = new ArrayList<>();
        for (WebElement we : get().findElements(by)) {
            if (we.isDisplayed()) {
                webElements.add(we);
            }
        }
        return webElements;
    }

    public List<WebElement> findVisibleElements(WebElement element, By locator) {
        List<WebElement> webElements = new ArrayList<>();
        for (WebElement we : get().findElements(locator)) {
            if (we.isDisplayed()) {
                webElements.add(we);
            }
        }
        return webElements;
    }

    public Boolean isElementDisplayed(By by) {
        return isElementDisplayed(by, 0);
    }

    public Boolean isElementDisplayed(By by, final Integer timeoutInMilliSeconds) {
        get().manage().timeouts().implicitlyWait(timeoutInMilliSeconds, TimeUnit.MILLISECONDS);
        List<WebElement> list = get().findElements(by);
        get().manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        return !list.isEmpty() && list.get(0).isDisplayed();
    }

    public Boolean isElementClickable(By by, final Integer timeoutInMilliSeconds) {
        get().manage().timeouts().implicitlyWait(timeoutInMilliSeconds, TimeUnit.MILLISECONDS);
        List<WebElement> list = get().findElements(by);
        get().manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        return !list.isEmpty() && list.get(0).isDisplayed() && list.get(0).isEnabled();
    }

    public Boolean isElementClickable(WebElement element, final Integer timeoutInMilliSeconds) {
        return element.isDisplayed() && element.isEnabled();
    }

    public WebElement findElement(By by) {
        try {
            return get().findElement(by);
        } catch (NoSuchElementException e) {
            if (logNotFoundElement)
                System.out.println(String.format("Could not find element using locator: %s", by.toString()));
            throw e;
        }
    }

    public WebElement findElementById(String using) {
        try {
            return get().findElement(By.id(using));
        } catch (NoSuchElementException e) {
            if (logNotFoundElement)
                System.out.println(String.format("Could not find element using locator: %s", using));
            throw e;
        }
    }

    public WebElement findElementByClassName(String using) {
        try {
            return get().findElement(By.className(using));
        } catch (NoSuchElementException e) {
            if (logNotFoundElement)
                System.out.println(String.format("Could not find element using locator: %s", using));
            throw e;
        }
    }

    public WebElement findElementByCssSelector(String using) {
        try {
            return get().findElement(By.cssSelector(using));
        } catch (NoSuchElementException e) {
            if (logNotFoundElement)
                System.out.println(String.format("Could not find element using locator: %s", using));
            throw e;
        }
    }

    public List<WebElement> findElementsByCssSelector(String using) {
        try {
            return get().findElements(By.cssSelector(using));
        } catch (NoSuchElementException e) {
            if (logNotFoundElement)
                System.out.println(String.format("Could not find element using locator: %s", using));
            throw e;
        }
    }

    public WebElement findElementByXPath(String using) {
        try {
            return get().findElement(By.xpath(using));
        } catch (NoSuchElementException e) {
            if (logNotFoundElement)
                System.out.println(String.format("Could not find element using locator: %s", using));
            throw e;
        }
    }

    public List<WebElement> findElementsByXPath(String using) {
        try {
            return get().findElements(By.xpath(using));
        } catch (NoSuchElementException e) {
            if (logNotFoundElement)
                System.out.println(String.format("Could not find element using locator: %s", using));
            throw e;
        }
    }

    public Object executeScript(String script, Object... args) {
        JavascriptExecutor js;
        if (get() instanceof JavascriptExecutor) {
            js = (JavascriptExecutor) get();
            script = script.replaceAll("\"", "\\\"");
            return js.executeScript(script, args);
        } else
            throw new UnsupportedOperationException("You must be using an underlying instance of WebDriver that supports executing javascript");
    }

    public void jsScrollIntoView(final WebElement webElement) {
        executeScript("arguments[0].scrollIntoView(true);", webElement);
        executeScript("window.scrollTo(0," + webElement.getLocation().y + ")");
    }

    public void jsClick(final WebElement webElement) {
        final String JAVA_SCRIPT_CLICK = "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(evObj);";
        executeScript(JAVA_SCRIPT_CLICK, webElement);
    }
}
