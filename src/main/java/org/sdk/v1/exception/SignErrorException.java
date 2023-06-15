package org.sdk.v1.exception;

/**
 * @author YooBil
 */
public class SignErrorException extends RuntimeException {
    
    public SignErrorException(String msg) {
        super(msg);
    }
    
    public SignErrorException(Throwable ex) {
        super(ex);
    }
    
    public SignErrorException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
}