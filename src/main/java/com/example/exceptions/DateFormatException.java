package com.example.exceptions;

public class DateFormatException extends Exception{
    private String date;
    public DateFormatException() {
        super();
    }

    public DateFormatException(String date) {
        super();
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
