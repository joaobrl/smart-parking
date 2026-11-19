package br.com.smart_parking.api.rest;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.port.in.TicketPortIn;
import br.com.smart_parking.core.port.in.dto.TicketRequest;
import br.com.smart_parking.core.port.out.dto.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TicketControllerImpl implements TicketController {

    private final TicketPortIn portIn;

    @Override
    public ResponseEntity<TicketResponse> registerTicket(TicketRequest ticketRequest, UriComponentsBuilder uri) {
        Ticket ticket = portIn.createTicket(ticketRequest.licensePlate(), ticketRequest.vehicleType());

        TicketResponse response = new TicketResponse(
                ticket.getId(),
                ticket.getLicensePlate(),
                ticket.getTypeVehicle(),
                ticket.getStatus(),
                ticket.getEntryTime() != null ? ticket.getEntryTime().toString() : null,
                null,
                ticket.getPrice() != null ? ticket.getPrice().toString() : null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<TicketResponse>> listTickets(String status, String vehicleType) {
        var list = portIn.listTickets(status, vehicleType);
        return ResponseEntity.ok().body(list);
    }

    @Override
    public ResponseEntity<TicketResponse> findTicket(UUID id) {
        var ticket = portIn.findTicket(id);
        return ResponseEntity.ok().body(ticket);
    }

    @Override
    public ResponseEntity<TicketResponse> completeTicket(UUID id) {
        var ticket = portIn.ticketCompleted(id);
        return ResponseEntity.ok().body(ticket);
    }
}
