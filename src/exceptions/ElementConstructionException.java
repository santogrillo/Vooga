package exceptions;

/**
 * Exception cause by a failure to generate a defined game element via the game element factory.
 *
 * @author Ben Schwennesen
 */
public class ElementConstructionException extends VoogaFormattableException {

    /**
     * Construct an exception caused by failure to generate a game element from its defined properties.
     *
     * @param exceptionCause       the exception that caused this exception to be thrown
     * @param problemParameterName the parameter that caused a generation error
     */
    public ElementConstructionException(Throwable exceptionCause, String problemParameterName) {
        super(exceptionCause, problemParameterName);
    }
}
