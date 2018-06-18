package gala.gala_api.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Event")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Event implements Serializable {

    //This is highly recommended for classes that implement Serializable.
    //Keeping this class serializable because there's a chance its sent as JSON.
    //This value was randomly generated by the IDE.
	private static final long serialVersionUID = 6756471936322629004L;

  /**
   * An Events's unique Id.
   */
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(unique = true)
  private String id;

  @NotBlank
  private String name;

  //TODO Should this be @NotBlank?
  private String place;

  //TODO Should this be @NotBlank?
  private Date eventTime;

  @NotNull
  @ManyToOne
  @JoinColumn(name="account_id")
  private Account account;

  @NotNull
  private Integer capacity;

  /**
   * The date this Event entity was created at.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdAt;

  /**
   * The date this Event entity was last modified.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
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
   * Get the capacity for this event.
   */
  public Integer getCapacity() {
    return this.capacity;
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

  /**
   * Set the capacity for this Event.
   */
  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}