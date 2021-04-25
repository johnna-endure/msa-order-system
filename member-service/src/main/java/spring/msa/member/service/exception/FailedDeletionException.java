package spring.msa.member.service.exception;

public class FailedDeletionException extends RuntimeException {
    public FailedDeletionException(Throwable cause) {
        super(cause);
    }
}
