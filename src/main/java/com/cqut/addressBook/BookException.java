package com.cqut.addressBook;

import java.io.PrintWriter;

/**
 * @author Augenstern
 * @date 2021/12/6
 */
public class BookException extends RuntimeException {
    public BookException(String message) {
        super(message);
    }

    public BookException(String message, Throwable cause) {
        super(message, cause);
    }

    public void error(PrintWriter writer) {
        writer.print(getMessage());
        this.printStackTrace(writer);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }
}
