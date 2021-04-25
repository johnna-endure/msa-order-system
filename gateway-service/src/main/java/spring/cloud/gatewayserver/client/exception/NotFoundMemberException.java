package spring.cloud.gatewayserver.client.exception;

public class NotFoundMemberException extends RuntimeException{
    public NotFoundMemberException(String message, Throwable e) {
        super(message, e);
    }
}
