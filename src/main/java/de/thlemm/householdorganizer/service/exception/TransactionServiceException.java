package de.thlemm.householdorganizer.service.exception;

import java.io.Serial;

public class TransactionServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String msg;

    public TransactionServiceException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}