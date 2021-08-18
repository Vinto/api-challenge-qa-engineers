import org.testng.annotations.DataProvider;

public class DataProviderTestData {
    /**
     * @return
     */
    @DataProvider(name = "usernameDetails")
    public static Object[][] usernameDetails() {
        return new Object[][]{
                {"Delphine"},
        };
    }
}
