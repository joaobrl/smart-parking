package br.com.smart_parking.infraestructure.persistence.postgresql.repository;

import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.infraestructure.persistence.postgresql.entity.TicketEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TicketRepositoryTest {

    @Autowired
    private TicketRepository repository;

    @Test
    void shouldFindTicketByLicensePlateAndStatus() {
        // arrange
        repository.save(ticket("ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN));

        // act
        boolean result = repository.existsByLicensePlateAndStatus("ABC1234", TicketStatus.OPEN);

        // assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenLicensePlateAndStatusDoNotMatch() {
        // arrange
        repository.save(ticket("ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN));

        // act
        boolean result = repository.existsByLicensePlateAndStatus("ABC1234", TicketStatus.CLOSED);

        // assert
        assertFalse(result);
    }

    @Test
    void shouldReturnAllTicketsWhenNoFiltersAreProvided() {
        // arrange
        repository.save(ticket("ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN));
        repository.save(ticket("XYZ9876", TypeVehicle.MOTO, TicketStatus.CLOSED));

        // act
        var result = repository.findByFiltrosOpcionais(null, null);

        // assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldFilterTicketsByStatus() {
        // arrange
        repository.save(ticket("ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN));
        repository.save(ticket("XYZ9876", TypeVehicle.MOTO, TicketStatus.CLOSED));

        // act
        var result = repository.findByFiltrosOpcionais(TicketStatus.OPEN, null);

        // assert
        assertEquals(1, result.size());
        assertEquals("ABC1234", result.getFirst().getLicensePlate());
    }

    @Test
    void shouldFilterTicketsByVehicleType() {
        // arrange
        repository.save(ticket("ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN));
        repository.save(ticket("XYZ9876", TypeVehicle.MOTO, TicketStatus.CLOSED));

        // act
        var result = repository.findByFiltrosOpcionais(null, TypeVehicle.MOTO);

        // assert
        assertEquals(1, result.size());
        assertEquals("XYZ9876", result.getFirst().getLicensePlate());
    }

    @Test
    void shouldFilterTicketsByStatusAndVehicleType() {
        // arrange
        repository.save(ticket("ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN));
        repository.save(ticket("XYZ9876", TypeVehicle.MOTO, TicketStatus.CLOSED));

        // act
        var result = repository.findByFiltrosOpcionais(TicketStatus.CLOSED, TypeVehicle.MOTO);

        // assert
        assertEquals(1, result.size());
        assertEquals("XYZ9876", result.getFirst().getLicensePlate());
    }

    private TicketEntity ticket(String licensePlate, TypeVehicle typeVehicle, TicketStatus status) {
        TicketEntity entity = new TicketEntity();
        entity.setId(UUID.randomUUID());
        entity.setLicensePlate(licensePlate);
        entity.setTypeVehicle(typeVehicle);
        entity.setStatus(status);
        return entity;
    }
}
