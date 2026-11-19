package br.com.smart_parking.core.port.out.dto;

import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;

import java.util.UUID;

public record TicketResponse(
        UUID id,
        String licensePlate,
        TypeVehicle vehicleType,
        TicketStatus status,
        String entryTime,
        String exitTime,
        String price
) {
}
