package gala.gala_api.entity;

/**
 * This enumeration represents the possible statuses of a ticket.
 */
public enum TicketStatus {
    /**
     * Ticket has been created, granted and not validated. Still able to be validated.
     */
    ACTIVE, 
    /**
     * Ticket is unable to be used but has not been validated (ie. Event is in past)
     */
    INACTIVE,
    /**
     * Ticket has been validated at event and cannot be validated again.
     */
    VALIDATED; 
}