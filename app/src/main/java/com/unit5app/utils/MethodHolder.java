package com.unit5app.utils;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Stores a method to be called through Java Reflection.
 * @author Andrew
 * @version 3/8/16
 */
public class MethodHolder {

    private String methodName;
    private String className;
    private Class[] parameters;

    /**
     * Holds a Method to be used later by java reflection.
     * @param className the entire path of the class name, can be gotten by calling MyClass.class.getName(), or written as "com.unit5app.mypackage.myClass"
     * @param methodName the name of the method to hold
     * @param parameters parameters of the method. e.g. Class[] {String.class, Integer.class, etc..} If there are no parameters, set this to null.
     */
    public MethodHolder(String className, String methodName, Class... parameters) {
        this.methodName = methodName;
        this.className = className;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class[] getParameters() {
        return parameters;
    }

    public String getClassName() {
        return className;
    }

    public void callMethod() {
        try {
            Class c = Class.forName(className);
            Method m = c.getDeclaredMethod(methodName, parameters);
            Object o = m.invoke(null, (Class[]) null);
        } catch (Exception e) {
            Log.d("MethodHolder", e.getMessage(), e);
        }
    }
}
