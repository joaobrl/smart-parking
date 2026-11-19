package br.com.smart_parking.infraestructure.mapper;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.infraestructure.persistence.postgresql.entity.TicketEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketMapperTest {

    private final TicketMapper mapper = new TicketMapper();

    @Test
    void shouldMapAllDomainFieldsToEntity() {
        // arrange
        UUID id = UUID.randomUUID();
        LocalDateTime entryTime = LocalDateTime.of(2026, 1, 2, 3, 4);
        LocalDateTime exitTime = LocalDateTime.of(2026, 1, 2, 5, 6);
        Ticket ticket = new Ticket(
                id, "ABC1234", TypeVehicle.CARRO, TicketStatus.CLOSED,
                entryTime, exitTime, new BigDecimal("25.50")
        );

        // act
        TicketEntity entity = mapper.toEntity(ticket);

        // assert
        assertEquals(id, entity.getId());
        assertEquals("ABC1234", entity.getLicensePlate());
        assertEquals(TypeVehicle.CARRO, entity.getTypeVehicle());
        assertEquals(TicketStatus.CLOSED, entity.getStatus());
        assertEquals(entryTime, entity.getEntryTime());
        assertEquals(exitTime, entity.getExitTime());
        assertEquals(new BigDecimal("25.50"), entity.getPrice());
    }

    @Test
    void shouldMapAllEntityFieldsToDomain() {
        // arrange
        UUID id = UUID.randomUUID();
        LocalDateTime entryTime = LocalDateTime.of(2026, 1, 2, 3, 4);
        LocalDateTime exitTime = LocalDateTime.of(2026, 1, 2, 5, 6);
        TicketEntity entity = new TicketEntity();
        entity.setId(id);
        entity.setLicensePlate("ABC1234");
        entity.setTypeVehicle(TypeVehicle.CARRO);
        entity.setStatus(TicketStatus.CLOSED);
        entity.setEntryTime(entryTime);
        entity.setExitTime(exitTime);
        entity.setPrice(new BigDecimal("25.50"));

        // act
        Ticket ticket = mapper.toDomain(entity);

        // assert
        assertEquals(id, ticket.getId());
        assertEquals("ABC1234", ticket.getLicensePlate());
        assertEquals(TypeVehicle.CARRO, ticket.getTypeVehicle());
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
        assertEquals(entryTime, ticket.getEntryTime());
        assertEquals(exitTime, ticket.getExitTime());
        assertEquals(new BigDecimal("25.50"), ticket.getPrice());
    }
}
