package ua.nure.yakunina.galen.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class TestProperties {
    protected static final ThreadLocal<Properties> instance = new ThreadLocal<>();

    synchronized protected static Properties loadProperties(final String... fileName) {
        Properties props = new Properties();
        InputStream input = null;

        try {
            props.load(input = new FileInputStream(fileName.length > 0 ? fileName[0] : "src/main/resources/testing.properties"));
            return props;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (input != null) input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getProperty(final Property key) {
        return getProperty(key.toString());
    }

    public static String getProperty(final String key) {
        synchronized (instance) {
            Properties props = instance.get();
            if (props == null)
                instance.set(props = loadProperties());
            return props.getProperty(key);
        }
    }

    public static String setProperty(final Property key, final String value) {
        return setProperty(key.toString(), value);
    }

    public static String setProperty(final String key, final String value) {
        synchronized (instance) {
            Properties props = instance.get();
            if (props == null)
                instance.set(props = loadProperties());
            return (String) props.setProperty(key, value);
        }
    }

    public static String getBrowser() {
        return getProperty(Property.BROWSER);
    }

    public enum Property {
        BROWSER("browser"),

        TEST_DATA_FILE("test_data_file_path"),
        EXCEL_REGRESSION_SHEET_NAME("regression_sheet_name"),
        EXCEL_SINGLE_RUN_SHEET_NAME("single_run_sheet_name");

        private final String propertyName;

        Property(final String text) {
            this.propertyName = text;
        }

        @Override
        public String toString() {
            return this.propertyName;
        }
    }
}
