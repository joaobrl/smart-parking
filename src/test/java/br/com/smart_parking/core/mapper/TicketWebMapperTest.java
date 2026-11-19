package br.com.smart_parking.core.mapper;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.core.port.out.dto.TicketResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketWebMapperTest {

    private final TicketWebMapper mapper = Mappers.getMapper(TicketWebMapper.class);

    @Test
    void shouldMapAllDomainFieldsToResponse() {
        // arrange
        UUID id = UUID.randomUUID();
        LocalDateTime entryTime = LocalDateTime.of(2026, 1, 2, 3, 4);
        LocalDateTime exitTime = LocalDateTime.of(2026, 1, 2, 5, 6);
        Ticket ticket = new Ticket(
                id, "ABC1234", TypeVehicle.CARRO_ELETRICO, TicketStatus.CLOSED,
                entryTime, exitTime, new BigDecimal("25.50")
        );

        // act
        TicketResponse response = mapper.toResponse(ticket);

        // assert
        assertEquals(id, response.id());
        assertEquals("ABC1234", response.licensePlate());
        assertEquals(TypeVehicle.CARRO_ELETRICO, response.vehicleType());
        assertEquals(TicketStatus.CLOSED, response.status());
        assertEquals("2026-01-02T03:04:00", response.entryTime());
        assertEquals("2026-01-02T05:06:00", response.exitTime());
        assertEquals("25.50", response.price());
    }
}
