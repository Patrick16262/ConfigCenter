package site.patrickshao.admin.common.exception.http;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public class HttpExceptionalResponse extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public HttpExceptionalResponse() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public HttpExceptionalResponse(String message) {
        super(message);
    }

    public static void onConditionThrow(boolean condition, HttpExceptionalResponse e) {
        throw e;
    }
}
