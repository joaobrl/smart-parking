package br.com.smart_parking.api.rest;

import br.com.smart_parking.core.port.in.dto.TicketRequest;
import br.com.smart_parking.core.port.out.dto.TicketResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RequestMapping("/smart-parking/tickets")
public interface TicketController {

    @PostMapping("/register")
    public ResponseEntity<TicketResponse> registerTicket(@Valid @RequestBody TicketRequest ticketRequest, UriComponentsBuilder uri);

    @GetMapping("/list")
    public ResponseEntity<List<TicketResponse>> listTickets(
            @RequestParam (required = false) String status,
            @RequestParam (required = false) String vehicleType);

    @GetMapping("/find/{id}")
    public ResponseEntity<TicketResponse> findTicket(@PathVariable UUID id);

    @PatchMapping("/update/{id}")
    public ResponseEntity<TicketResponse> completeTicket(@PathVariable UUID id);
}
