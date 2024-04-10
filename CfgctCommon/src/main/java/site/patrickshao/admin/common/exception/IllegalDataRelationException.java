package site.patrickshao.admin.common.exception;

public class IllegalDataRelationException extends RuntimeException {
    public IllegalDataRelationException() {
    }

    public IllegalDataRelationException(String message) {
        super(message);
    }

    public IllegalDataRelationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDataRelationException(Throwable cause) {
        super(cause);
    }
}
