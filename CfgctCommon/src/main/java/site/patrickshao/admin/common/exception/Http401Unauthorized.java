package site.patrickshao.admin.common.exception;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public class Http401Unauthorized extends HttpExceptionalResponse{
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public Http401Unauthorized() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public Http401Unauthorized(String message) {
        super(message);
    }
}
