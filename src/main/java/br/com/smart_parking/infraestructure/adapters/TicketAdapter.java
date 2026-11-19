package br.com.smart_parking.infraestructure.adapters;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.core.port.out.TicketPortOut;
import br.com.smart_parking.infraestructure.mapper.TicketMapper;
import br.com.smart_parking.infraestructure.persistence.postgresql.entity.TicketEntity;
import br.com.smart_parking.infraestructure.persistence.postgresql.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TicketAdapter implements TicketPortOut {

    private final TicketRepository repository;
    private final TicketMapper mapper;

    @Override
    public boolean existsByLicensePlateAndStatus(String licensePlate, TicketStatus status) {
        return repository.existsByLicensePlateAndStatus(licensePlate, status);
    }

    @Override
    public void save(Ticket ticket) {
        repository.save(mapper.toEntity(ticket));
    }

    @Override
    public List<Ticket> listTickets(String status, String vehicleType) {
        TicketStatus ticketStatus = status == null ? null : TicketStatus.valueOf(status);
        TypeVehicle typeVehicle = vehicleType == null ? null : TypeVehicle.valueOf(vehicleType);
        List<TicketEntity> entities = repository.findByFiltrosOpcionais(ticketStatus, typeVehicle);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Ticket findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Ticket com id " + id + " não encontrado"));
    }
}
