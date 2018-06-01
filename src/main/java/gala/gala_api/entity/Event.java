package gala.gala_api.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "Event")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Event implements Serializable {

    //This is highly reccommended for classes that implement Serializable.
    //Keeping this class serializable because theres a chance its sent as JSON.
    //This value was randomly generated by the IDE.
	private static final long serialVersionUID = 6756471936322629004L;

	/**
     * An Events's unique Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    //TODO Should this be @NotBlank?
    private String place;

    //TODO Should this be @NotBlank?
    private Date eventTime;

    @NonNull
    private Long accountId;

    /**
     * The date this Event entity was created at.
     */
    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    /**
     * The date this Event entity was last modified.
     */
    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    /**
     * Get the date this Event entity was created at.
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Get the date this Event entity was last modified.
     */
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Get the place the event will be held at.
     */
    public String getPlace() {
        return this.place;
    }

    /**
     * Get the datetime the event will be held at.
     */
    public Date getEventTime() {
        return this.eventTime;
    }

    /**
     * Get the id of the account that owns this event.
     */
    public Long getAccountId() {
        return this.accountId;
    }

    /**
     * Set the place the event will be held at.
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Set the datetime the event will be held at.
     */
    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Set the id of the account that owns this event.
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * Set the date this Event entity was created at.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Set the date this Event entity was last modified.
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}