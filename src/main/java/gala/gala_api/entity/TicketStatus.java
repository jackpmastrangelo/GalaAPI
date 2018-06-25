package gala.gala_api.entity;

/**
 * This enumeration represents the possible statuses of a ticket.
 */
public enum TicketStatus {
  /**
   * Ticket has been created, granted and not validated. Still able to be validated.
   */
  ACTIVE,
  /** //TODO What will set the tickets to voided? Is it necessary to change this if the event is in the past or just check the time?
   * Ticket is unable to be used but has not been validated (i.e. Event is in past)
   */
  VOIDED,
  /**
   * Ticket has been validated at event and cannot be validated again.
   */
  VALIDATED
}