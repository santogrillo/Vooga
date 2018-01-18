package exceptions;

import java.util.ResourceBundle;


/**
 * Abstraction of the exception thrown when an error occurs. The names of subclasses are used to obtain the error
 * message associated with the particular exceptions from a properties file, which is displayed as an alert in the
 * frontend.
 *
 * @author Ben Schwennesen
 */
public class VoogaException extends Throwable {

	private final String BUNDLE_NAME = VoogaException.class.getSimpleName();

	/**
	 * Construct a Vooga exception that the frontend can catch and display an associated message
	 *
	 * @param exception the exception that was caught and led to this exception
	 */
	public VoogaException(Throwable exception) {
		super(exception);
	}

	/**
	 * Get the message associated with a particular Vooga exception.
	 *
	 * @return the appropriate message for the exception type, obtained through reflection on the exception name
	 */
	public String getMessage() {
		return ResourceBundle.getBundle(BUNDLE_NAME).getString(this.getClass().getName());
	}
	
}
