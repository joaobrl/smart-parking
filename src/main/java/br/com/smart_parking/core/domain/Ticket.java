package br.com.smart_parking.core.domain;

import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.core.port.in.dto.TicketRequest;
import lombok.*;

import java.math.BigDecimal;
import java.net.ProtocolFamily;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    private UUID id;

    private String licensePlate;

    private TypeVehicle typeVehicle;

    private TicketStatus status;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private BigDecimal price;

    public Ticket(String licensePlate, TypeVehicle typeVehicle) {
        this.id = UUID.randomUUID();
        this.licensePlate = licensePlate;
        this.typeVehicle = typeVehicle;
        this.status = TicketStatus.OPEN;
        this.entryTime = LocalDateTime.now();
        this.price = BigDecimal.ZERO;
    }

}
