package com.yes.zookeeper;

/**
 * Created by р╤омя╚ on 2015/9/15.
 */
public class NetWorkClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }


}
