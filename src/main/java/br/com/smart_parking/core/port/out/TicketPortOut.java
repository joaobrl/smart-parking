package br.com.smart_parking.core.port.out;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;

import java.util.List;
import java.util.UUID;

public interface TicketPortOut {

    boolean existsByLicensePlateAndStatus(String licensePlate, TicketStatus ticketStatus);

    void save(Ticket ticket);

    List<Ticket> listTickets(String status, String vehicleType);

    Ticket findById(UUID id);
}
