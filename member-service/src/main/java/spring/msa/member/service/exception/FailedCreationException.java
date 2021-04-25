package spring.msa.member.service.exception;

public class FailedCreationException extends RuntimeException{
    public FailedCreationException(String message, Throwable cause) {
        super(message, cause);
    }
    public FailedCreationException(Throwable cause) {
        super(cause);
    }
    public FailedCreationException(String message) {
        super(message);
    }

}
