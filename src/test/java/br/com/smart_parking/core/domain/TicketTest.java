package br.com.smart_parking.core.domain;

import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void shouldInitializeNewTicketWithOpenStatusAndZeroPrice() {
        // arrange
        String licensePlate = "ABC1234";

        // act
        Ticket ticket = new Ticket(licensePlate, TypeVehicle.MOTO);

        // assert
        assertNotNull(ticket.getId());
        assertEquals(licensePlate, ticket.getLicensePlate());
        assertEquals(TypeVehicle.MOTO, ticket.getTypeVehicle());
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
        assertNotNull(ticket.getEntryTime());
        assertNull(ticket.getExitTime());
        assertEquals(BigDecimal.ZERO, ticket.getPrice());
    }
}
