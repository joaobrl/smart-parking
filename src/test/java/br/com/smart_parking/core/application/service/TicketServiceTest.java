package br.com.smart_parking.core.application.service;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.core.mapper.TicketWebMapper;
import br.com.smart_parking.core.port.out.TicketPortOut;
import br.com.smart_parking.core.port.out.dto.TicketResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketPortOut portOut;

    @Mock
    private TicketWebMapper mapper;

    @InjectMocks
    private TicketService service;

    @Test
    void shouldCreateTicketWhenNoOpenTicketExists() {
        // arrange
        String licensePlate = "ABC1234";
        given(portOut.existsByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN)).willReturn(false);

        // act
        Ticket ticket = service.createTicket(licensePlate, "CARRO");

        // assert
        assertNotNull(ticket.getId());
        assertEquals(licensePlate, ticket.getLicensePlate());
        assertEquals(TypeVehicle.CARRO, ticket.getTypeVehicle());
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
        assertNotNull(ticket.getEntryTime());
        then(portOut).should().save(ticket);
    }

    @Test
    void shouldRejectCreationWhenOpenTicketAlreadyExists() {
        // arrange
        String licensePlate = "ABC1234";
        given(portOut.existsByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN)).willReturn(true);

        // act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createTicket(licensePlate, "CARRO")
        );

        // assert
        assertEquals("Já existe um ticket aberto para o veículo com a placa " + licensePlate, exception.getMessage());
        then(portOut).should().existsByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN);
        then(portOut).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldRejectCreationWhenVehicleTypeIsInvalid() {
        // arrange
        String licensePlate = "ABC1234";
        given(portOut.existsByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN)).willReturn(false);

        // act
        assertThrows(IllegalArgumentException.class, () -> service.createTicket(licensePlate, "INVALID"));

        // assert
        then(portOut).should().existsByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN);
        then(portOut).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldListMappedTicketsWhenTicketsExist() {
        // arrange
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        TicketResponse response = new TicketResponse(
                ticket.getId(), ticket.getLicensePlate(), ticket.getTypeVehicle(), ticket.getStatus(),
                ticket.getEntryTime().toString(), null, "0"
        );
        given(portOut.listTickets("OPEN", "CARRO")).willReturn(List.of(ticket));
        given(mapper.toResponse(ticket)).willReturn(response);

        // act
        List<TicketResponse> result = service.listTickets("OPEN", "CARRO");

        // assert
        assertEquals(List.of(response), result);
        then(mapper).should().toResponse(ticket);
    }

    @Test
    void shouldReturnEmptyListWhenNoTicketsExist() {
        // arrange
        given(portOut.listTickets(null, null)).willReturn(List.of());

        // act
        List<TicketResponse> result = service.listTickets(null, null);

        // assert
        assertTrue(result.isEmpty());
        then(mapper).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnMappedTicketWhenTicketIsFound() {
        // arrange
        UUID id = UUID.randomUUID();
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        TicketResponse response = new TicketResponse(
                ticket.getId(), ticket.getLicensePlate(), ticket.getTypeVehicle(), ticket.getStatus(),
                ticket.getEntryTime().toString(), null, "0"
        );
        given(portOut.findById(id)).willReturn(ticket);
        given(mapper.toResponse(ticket)).willReturn(response);

        // act
        TicketResponse result = service.findTicket(id);

        // assert
        assertSame(response, result);
        then(portOut).should().findById(id);
    }

    @Test
    void shouldCloseAndSaveTicketWhenTicketIsOpen() {
        // arrange
        UUID id = UUID.randomUUID();
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        TicketResponse response = new TicketResponse(
                ticket.getId(), ticket.getLicensePlate(), ticket.getTypeVehicle(), TicketStatus.CLOSED,
                ticket.getEntryTime().toString(), null, "0"
        );
        given(portOut.findById(id)).willReturn(ticket);
        given(mapper.toResponse(ticket)).willReturn(response);

        // act
        TicketResponse result = service.ticketCompleted(id);

        // assert
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
        assertSame(response, result);
        then(portOut).should().save(ticket);
    }

    @Test
    void shouldRejectClosingWhenTicketIsAlreadyClosed() {
        // arrange
        UUID id = UUID.randomUUID();
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        ticket.setStatus(TicketStatus.CLOSED);
        given(portOut.findById(id)).willReturn(ticket);

        // act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.ticketCompleted(id)
        );

        // assert
        assertEquals("Ticket com id " + id + " já foi finalizado", exception.getMessage());
        then(portOut).should().findById(id);
        then(portOut).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
    }
}
