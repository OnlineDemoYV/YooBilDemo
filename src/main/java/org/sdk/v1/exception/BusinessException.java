package org.sdk.v1.exception;

/**
 * @author YooBil
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String msg) {
        super(msg);
    }
    
    public BusinessException(Throwable ex) {
        super(ex);
    }
    
    public BusinessException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
}