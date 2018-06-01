package gala.gala_api.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public enum TicketStatus {
    ACTIVE,
    INACTIVE,
    VALIDATED;
}