package com.yes.zookeeper;

/**
 * Created by Ò¶ÏÍÑ« on 2015/9/14.
 */
public class Test {

    public static void main(String[] args) {


        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        String testZkGroup = "com.yes.zookeeper.TestZkGroup";
        try {
            Class classTest = classLoader.loadClass(testZkGroup);
            System.out.println(classTest);
            System.out.println(classTest.getMethods());
            System.out.println(classLoader.getParent());
            System.out.println(String[].class);
            System.out.println(TestZkGroup.class);
            System.out.println(Class.forName(testZkGroup));
            System.out.println(classLoader);
            System.out.println(classLoader.loadClass(testZkGroup));
            TestZkGroup testZkGroup1 = (TestZkGroup) classLoader.loadClass(testZkGroup).newInstance();
            System.out.println(testZkGroup1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
