package com.unit5app.utils;

/**
 * Stores a method to be called through Java Reflection.
 * @author Andrew
 * @version 3/8/16
 */
public class MethodHolder {

    private String methodName;
    private String classPath;
    private Class[] parameters;

    /**
     * Holds a Method to be used later by java reflection.
     * @param classPath may be Class name and not the entire path
     * @param methodName the name of the method to hold
     * @param parameters parameters of the method. e.g. String.class, Integer.class, etc..
     */
    public MethodHolder(String classPath, String methodName, Class... parameters) {
        this.methodName = methodName;
        this.classPath = classPath;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class[] getParameters() {
        return parameters;
    }

    public String getObjectString() {
        return classPath;
    }
}
