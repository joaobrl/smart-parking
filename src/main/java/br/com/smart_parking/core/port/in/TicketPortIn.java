package br.com.smart_parking.core.port.in;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.port.out.dto.TicketResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface TicketPortIn {
    Ticket createTicket(String licensePlate, String vehicleType);

    List<TicketResponse> listTickets(String status, String vehicleType);

    TicketResponse findTicket(UUID id);

    TicketResponse ticketCompleted(UUID id);
}
