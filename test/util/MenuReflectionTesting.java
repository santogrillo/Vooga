package util;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

public class MenuReflectionTesting {

    public static abstract class Dummy {

        public abstract void doNothing();

    }

    public static class Thing extends Dummy {

        public void doNothing() {}

        public void doSomething() { System.out.println("hey there"); }

    }

    public static class OtherThing extends Dummy {

        public void doNothing() {}

        public void doSomething() { System.out.println("hello"); }

    }

    public static void main(String[] args) {
        InputStream in = MenuReflectionTesting.class.getResourceAsStream("MenuTest.properties");
        Dummy other = new OtherThing();
        Dummy thing = new Thing();
        Properties properties = new Properties();
        try {
            properties.load(in);
            for (String key : properties.stringPropertyNames()) {
                Method method = other.getClass().getDeclaredMethod(properties.getProperty(key));
                method.invoke(other);
                method = thing.getClass().getDeclaredMethod(properties.getProperty(key));
                method.invoke(thing);
                // it works!!!!!!!
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
