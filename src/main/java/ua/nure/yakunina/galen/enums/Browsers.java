package ua.nure.yakunina.galen.enums;

public enum Browsers {
    chrome("chrome"),
    firefox("firefox"),
    internetExplorer("internetExplorer");

    private final String browserName;

    Browsers(final String text) {
        this.browserName = text;
    }

    @Override
    public String toString() {
        return this.browserName;
    }
}
