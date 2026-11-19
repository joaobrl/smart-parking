package br.com.smart_parking.infraestructure.persistence.postgresql.entity;

import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Ticket")
@Getter
@Setter
public class TicketEntity {

    @Id
    private UUID id;

    private String licensePlate;

    private TypeVehicle typeVehicle;

    private TicketStatus status;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private BigDecimal price;
}
