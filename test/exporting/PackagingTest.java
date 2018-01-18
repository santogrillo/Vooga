package exporting;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Tests the packager.
 *
 * @author Ben Schwennesen
 */
public class PackagingTest {

    private static void testJarCreation(Packager packager) {
        try {
            Properties properties = new Properties();
            InputStream in = new FileInputStream("resources/Export.properties");
            properties.load(in);
            properties.setProperty("gameFile", "AAAA.voog");
            properties.store(new FileOutputStream("resources/Export.properties"), "");
            packager.generateJar("AAAA");
            // test the JAR manually with a launch
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testPathConversion(Packager packager) throws Exception {
        String test = "C:\\some\\windows\\path";
        String expected = "C:/some/windows/path";
        Method convertPath = packager.getClass().getDeclaredMethod("convertPathToJarFormat", String.class);
        convertPath.setAccessible(true);
        String result = (String) convertPath.invoke(packager, test);
        assert result.equals(expected);
    }

    public static void main(String[] args) {
        Packager packager = new Packager();
        try{
            testJarCreation(packager);
            // testPathConversion(packager);
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
    }

}
