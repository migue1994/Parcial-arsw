package edu.eci.arsw.exams.moneylaunderingapi.service;

public class MoneyException extends Exception {

    private static final long serialVersionUID = 1L;

    public MoneyException( String message ) {
        super(message);
    }

    public MoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}