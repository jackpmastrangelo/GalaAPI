package gala.gala_api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

/**
 * This class represents a Ticket enitity in the Gala API.
 */
@Entity
@Table (name = "Ticket")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Ticket implements Serializable {
    
    /**
     * A Ticket's unique Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    /**
     * The Id of the event associated with this Ticket.
     */
    @NonNull
    private Long eventId;

    /**
     * The email associated with the creation and sending of this Ticket.
     */
    @NotBlank
    private String email;

    /**
     * The status of this ticket.
     */
    @NotBlank
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    /**
     * Get the unique Id of a Ticket.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Get the id for the event associated with this Ticket.
     */
    public Long getEventId() {
        return this.eventId;
    }

    /**
     * Get the email associated with this Ticket.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Get this Ticket's status.
     */
    public TicketStatus getStatus() {
        return this.status;
    }

}