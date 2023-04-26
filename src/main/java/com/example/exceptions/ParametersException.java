package com.example.exceptions;

public class ParametersException extends Exception{
    private String variableName;
    private int minVal;
    private int maxVal;

    public ParametersException(String variableName, String message) {
        super(message);
        this.variableName = variableName;
    }

    public ParametersException(String variableName, String message, int minVal, int maxVal) {
        super(message);
        this.variableName = variableName;
        this.maxVal = maxVal;
        this.minVal = minVal;
    }

    public ParametersException(String variableName) {
        super("Parameter " + variableName + " should be between 0 and 255");
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    public int getMinVal() {
        return minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }
}
