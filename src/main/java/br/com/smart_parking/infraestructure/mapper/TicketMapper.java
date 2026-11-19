package br.com.smart_parking.infraestructure.mapper;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.infraestructure.persistence.postgresql.entity.TicketEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketEntity toEntity(Ticket ticket) {
        var entity = new TicketEntity();
        entity.setId(ticket.getId());
        entity.setLicensePlate(ticket.getLicensePlate());
        entity.setTypeVehicle(ticket.getTypeVehicle());
        entity.setStatus(ticket.getStatus());
        entity.setEntryTime(ticket.getEntryTime());
        entity.setExitTime(ticket.getExitTime());
        entity.setPrice(ticket.getPrice());

        return entity;
    }

    public Ticket toDomain(TicketEntity ticketEntity) {
        var ticket = new Ticket();
        ticket.setId(ticketEntity.getId());
        ticket.setLicensePlate(ticketEntity.getLicensePlate());
        ticket.setTypeVehicle(ticketEntity.getTypeVehicle());
        ticket.setStatus(ticketEntity.getStatus());
        ticket.setEntryTime(ticketEntity.getEntryTime());
        ticket.setExitTime(ticketEntity.getExitTime());
        ticket.setPrice(ticketEntity.getPrice());

        return ticket;
    }
}
