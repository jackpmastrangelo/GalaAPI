package gala.gala_api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="Account", uniqueConstraints = @UniqueConstraint(columnNames = { "email" }))
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Account implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(unique = true)
  @JsonIgnore
  private String id;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  @NotBlank
  @JsonIgnore
  private String email;

  @NotBlank
  @JsonIgnore
  private String password;

  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date updatedAt;

  public String getId() {
      return id;
  }

  public void setId(String id) {
      this.id = id;
  }

  public String getFirstName() {
      return firstName;
  }

  public void setFirstName(String firstName) {
      this.firstName = firstName;
  }

  public String getLastName() {
      return lastName;
  }

  public void setLastName(String lastName) {
      this.lastName = lastName;
  }

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
  }

  public Date getCreatedAt() {
      return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
      return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Account account = (Account) o;

    if (id != null ? !id.equals(account.id) : account.id != null) return false;
    if (firstName != null ? !firstName.equals(account.firstName) : account.firstName != null)
      return false;
    if (lastName != null ? !lastName.equals(account.lastName) : account.lastName != null)
      return false;
    if (email != null ? !email.equals(account.email) : account.email != null) return false;
    if (password != null ? !password.equals(account.password) : account.password != null)
      return false;
    if (createdAt != null ? !createdAt.equals(account.createdAt) : account.createdAt != null)
      return false;
    return updatedAt != null ? updatedAt.equals(account.updatedAt) : account.updatedAt == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Account{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}