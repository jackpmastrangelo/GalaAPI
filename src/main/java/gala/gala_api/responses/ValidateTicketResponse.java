package gala.gala_api.responses;

public class ValidateTicketResponse {

    /**
     * Indicates whether the call to validateTicketResponse was successful.
     */
    private boolean success;
    
    /**
     * Message for more information, should contain message to display if
     * validation is unsuccessful.
     */
    private String message;

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}