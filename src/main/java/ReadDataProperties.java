import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadDataProperties {
    private static ReadDataProperties instance;
    private static final Object lock = new Object();
    private static String url;

    /**
     * Return a Singleton instance
     * @return
     */
    public static ReadDataProperties getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new ReadDataProperties();
                instance.loadProperties();
            }
        }
        return instance;
    }

    /**
     * Load the data properties file
     */
    private void loadProperties() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream("src/main/resources/data.properties"));
        } catch (IOException e) {
            System.out.println("Properties file not found");
        }

        url = properties.getProperty("url");
    }

    /**
     * Return the url value in the data properties file
     * @return
     */
    public String getUrl() {
        return url;
    }
}
