package gala.gala_api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

/**
 * This class represents a Ticket enitity in the Gala API.
 */
@Entity
@Table(name = "Ticket")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Ticket implements Serializable {

    //This is highly reccommended for classes that implement Serializable.
    //Keeping this class serializable because theres a chance its sent as JSON.
    //This value was randomly generated by the IDE.
    private static final long serialVersionUID = -8423698635559761439L;

	  /**
     * A Ticket's unique Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    /**
     * The {@link Event} associated with this Ticket.
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

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
     * The date this Ticket entity was created.
     */
    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    /**
     * The date this Ticket entity was last modified.
     */
    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    /**
     * Get the unique Id of a Ticket.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Get the event associated with this Ticket.
     */
    public Event getEvent() {
        return this.event;
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

    /**
     * Get the date this Ticket entity was created at.
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Get the date this Ticket entity was last modified.
     */
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Set the unique Id of a Ticket.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Set the id for the event associated with this Ticket.
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Set the email associated with this Ticket.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set this Ticket's status.
     */
    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    /**
     * Set the date this Ticket entity was created at.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Set the date this Ticket entity was last modified.
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}