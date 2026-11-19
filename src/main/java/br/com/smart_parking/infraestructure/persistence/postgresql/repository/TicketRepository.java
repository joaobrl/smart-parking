package br.com.smart_parking.infraestructure.persistence.postgresql.repository;

import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.infraestructure.persistence.postgresql.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    boolean existsByLicensePlateAndStatus(String licensePlate, TicketStatus status);

    @Query("SELECT t FROM TicketEntity t WHERE (:status IS NULL OR t.status = :status) AND (:vehicleType IS NULL OR t.typeVehicle = :vehicleType)")
    List<TicketEntity> findByFiltrosOpcionais(@Param("status") TicketStatus status,
                                              @Param("vehicleType") TypeVehicle vehicleType);
}
