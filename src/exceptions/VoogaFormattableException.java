package exceptions;

/**
 * Abstract exception representing exceptions caused by a string-representable .
 *
 * @author Ben Schwennesen
 */
public abstract class VoogaFormattableException extends VoogaException {

    private final String EXCEPTION_CAUSE;

    /**
     * Construct a syntax caused exception.
     *
     * @param exceptionCauseToken - the user-entered token causing the error
     */
    public VoogaFormattableException(Throwable exceptionCause, String exceptionCauseToken) {
        super(exceptionCause);
        EXCEPTION_CAUSE = exceptionCauseToken;
    }

    @Override
    public String getMessage() {
        return String.format(super.getMessage(), EXCEPTION_CAUSE);
    }
}
