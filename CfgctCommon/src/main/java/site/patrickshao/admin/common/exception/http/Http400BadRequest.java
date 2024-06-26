package site.patrickshao.admin.common.exception.http;

/**
 * Illegal request exception
 * IllegalArgumentException can be seen as Http400BadRequest
 * The default message foe IllegalArgumentException is
 * "Certain arguments are invalid;
 * you risk initializing fields that shouldn't be initialized and must remain null."
 *
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public class Http400BadRequest extends HttpExceptionalResponse {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public Http400BadRequest() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public Http400BadRequest(String message) {
        super(message);
    }
}
