package br.com.smart_parking.core.mapper;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.port.out.dto.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketWebMapper {

    @Mapping(source = "typeVehicle", target = "vehicleType")
    TicketResponse toResponse(Ticket domain);
}
