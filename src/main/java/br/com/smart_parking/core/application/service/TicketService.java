package br.com.smart_parking.core.application.service;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.core.mapper.TicketWebMapper;
import br.com.smart_parking.core.port.in.TicketPortIn;
import br.com.smart_parking.core.port.out.TicketPortOut;
import br.com.smart_parking.core.port.out.dto.TicketResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService implements TicketPortIn {

    private final TicketPortOut portOut;

    private final TicketWebMapper mapper;

    @Override
    public Ticket createTicket(String licensePlate, String vehicleType) {
        if (portOut.existsByLicensePlateAndStatus(licensePlate, TicketStatus.OPEN)) {
            throw new IllegalArgumentException("Já existe um ticket aberto para o veículo com a placa " + licensePlate);
        }
        var ticket = new Ticket(licensePlate, TypeVehicle.valueOf(vehicleType));
        portOut.save(ticket);

        return ticket;
    }

    @Override
    public List<TicketResponse> listTickets(String status, String vehicleType) {
        var tickets = portOut.listTickets(status, vehicleType);
        return tickets.stream().map(mapper::toResponse).toList();
    }

    @Override
    public TicketResponse findTicket(UUID id) {
        var ticket = portOut.findById(id);
        return mapper.toResponse(ticket);
    }

    @Override
    public TicketResponse ticketCompleted(UUID id) {
        var ticket = portOut.findById(id);
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalArgumentException("Ticket com id " + id + " já foi finalizado");
        }

        ticket.setStatus(TicketStatus.CLOSED);
        portOut.save(ticket);
        return mapper.toResponse(ticket);
    }
}
