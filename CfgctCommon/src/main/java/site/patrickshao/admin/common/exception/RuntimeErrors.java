package site.patrickshao.admin.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static site.patrickshao.admin.common.constants.ExceptionMessages.*;
import static site.patrickshao.admin.common.constants.SignConstants.*;

public final class RuntimeErrors extends Error{
    private static final Logger logger = LoggerFactory.getLogger(RuntimeError.class);
    private static final String DEFAULT_MESSAGE = RUNTIME_ERROR_OCCURRED_HEADER + RUNTIME_ERROR_OCCURRED_EN
            + NEW_LINE + RUNTIME_ERROR_OCCURRED_CH + NEW_LINE;

    public static <T> T emit() {
        throw new RuntimeError(DEFAULT_MESSAGE);
    }

    public static <T> T emit(String message) {
        logger.error( MESSAGE_T + message);
        throw new RuntimeError( DEFAULT_MESSAGE + MESSAGE_T + message);
    }
    public static <T> T emit(Throwable cause) {
        logger.error( CAUSE_T + cause.getClass().getSimpleName() + COLON + SPACE + cause.getMessage());
        throw new RuntimeError(DEFAULT_MESSAGE + CAUSE_T + cause.getClass().getSimpleName()
                + COLON + SPACE + cause.getMessage(), cause);
    }

    public static <T> T emit(String message, Throwable cause) {
        logger.error( CAUSE_T + cause.getClass().getSimpleName() + COLON + SPACE + cause.getMessage()
                + NEW_LINE + NEW_LINE + MESSAGE_T + message);
        throw new RuntimeError(DEFAULT_MESSAGE   + CAUSE_T + cause.getClass().getSimpleName()
                + COLON + SPACE  + cause.getMessage() + NEW_LINE + NEW_LINE + MESSAGE_T + message, cause);
    }

    public static void onConditionEmit(boolean condition) {
        throw new RuntimeError(DEFAULT_MESSAGE);
    }

    public static void onConditionEmit(boolean condition, String message) {
        logger.error( MESSAGE_T + message);
        throw new RuntimeError( DEFAULT_MESSAGE + MESSAGE_T + message);
    }

    private RuntimeErrors() {
    }


    public static class RuntimeError extends Error {
        public RuntimeError() {
            super();
        }

        public RuntimeError(String message) {
            super(message);
        }

        public RuntimeError(String message, Throwable cause) {
            super(message, cause);
        }

        public RuntimeError(Throwable cause) {
            super(cause);
        }

        protected RuntimeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
