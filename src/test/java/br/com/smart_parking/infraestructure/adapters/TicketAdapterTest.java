package br.com.smart_parking.infraestructure.adapters;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.infraestructure.mapper.TicketMapper;
import br.com.smart_parking.infraestructure.persistence.postgresql.entity.TicketEntity;
import br.com.smart_parking.infraestructure.persistence.postgresql.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TicketAdapterTest {

    @Mock
    private TicketRepository repository;

    @Mock
    private TicketMapper mapper;

    @InjectMocks
    private TicketAdapter adapter;

    @Test
    void shouldDelegateExistsCheckToRepository() {
        // arrange
        given(repository.existsByLicensePlateAndStatus("ABC1234", TicketStatus.OPEN)).willReturn(true);

        // act
        boolean result = adapter.existsByLicensePlateAndStatus("ABC1234", TicketStatus.OPEN);

        // assert
        assertTrue(result);
        then(repository).should().existsByLicensePlateAndStatus("ABC1234", TicketStatus.OPEN);
    }

    @Test
    void shouldMapAndSaveTicket() {
        // arrange
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        TicketEntity entity = new TicketEntity();
        given(mapper.toEntity(ticket)).willReturn(entity);

        // act
        adapter.save(ticket);

        // assert
        then(mapper).should().toEntity(ticket);
        then(repository).should().save(entity);
    }

    @Test
    void shouldPropagateRepositoryFailureWhenSavingTicket() {
        // arrange
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        TicketEntity entity = new TicketEntity();
        IllegalStateException failure = new IllegalStateException("repository unavailable");
        given(mapper.toEntity(ticket)).willReturn(entity);
        given(repository.save(entity)).willThrow(failure);

        // act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> adapter.save(ticket));

        // assert
        assertSame(failure, exception);
    }

    @Test
    void shouldReturnMappedTicketsWhenRepositoryReturnsEntities() {
        // arrange
        TicketEntity entity = new TicketEntity();
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        given(repository.findByFiltrosOpcionais(TicketStatus.OPEN, TypeVehicle.CARRO)).willReturn(List.of(entity));
        given(mapper.toDomain(entity)).willReturn(ticket);

        // act
        List<Ticket> result = adapter.listTickets("OPEN", "CARRO");

        // assert
        assertEquals(List.of(ticket), result);
        then(mapper).should().toDomain(entity);
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryReturnsNoEntities() {
        // arrange
        given(repository.findByFiltrosOpcionais(null, null)).willReturn(List.of());

        // act
        List<Ticket> result = adapter.listTickets(null, null);

        // assert
        assertTrue(result.isEmpty());
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve aplicar o status quando o tipo de veículo não é informado")
    void shouldFilterByStatusWhenVehicleTypeIsNull() {
        // arrange
        given(repository.findByFiltrosOpcionais(TicketStatus.OPEN, null)).willReturn(List.of());

        // act
        List<Ticket> result = adapter.listTickets("OPEN", null);

        // assert
        assertTrue(result.isEmpty());
        then(repository).should().findByFiltrosOpcionais(TicketStatus.OPEN, null);
    }

    @Test
    @DisplayName("deve aplicar o tipo de veículo quando o status não é informado")
    void shouldFilterByVehicleTypeWhenStatusIsNull() {
        // arrange
        given(repository.findByFiltrosOpcionais(null, TypeVehicle.MOTO)).willReturn(List.of());

        // act
        List<Ticket> result = adapter.listTickets(null, "MOTO");

        // assert
        assertTrue(result.isEmpty());
        then(repository).should().findByFiltrosOpcionais(null, TypeVehicle.MOTO);
    }

    @Test
    @DisplayName("deve rejeitar status inválido")
    void shouldThrowWhenStatusIsInvalid() {
        // arrange
        String status = "INVALID";

        // act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.listTickets(status, null));

        // assert
        assertTrue(exception.getMessage().contains(status));
        then(repository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("deve rejeitar tipo de veículo inválido")
    void shouldThrowWhenVehicleTypeIsInvalid() {
        // arrange
        String vehicleType = "INVALID";

        // act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.listTickets(null, vehicleType));

        // assert
        assertTrue(exception.getMessage().contains(vehicleType));
        then(repository).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnMappedTicketWhenRepositoryFindsId() {
        // arrange
        UUID id = UUID.randomUUID();
        TicketEntity entity = new TicketEntity();
        Ticket ticket = new Ticket("ABC1234", TypeVehicle.CARRO);
        given(repository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(ticket);

        // act
        Ticket result = adapter.findById(id);

        // assert
        assertSame(ticket, result);
        then(mapper).should().toDomain(entity);
    }

    @Test
    void shouldThrowWhenRepositoryDoesNotFindId() {
        // arrange
        UUID id = UUID.randomUUID();
        given(repository.findById(id)).willReturn(Optional.empty());

        // act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adapter.findById(id));

        // assert
        assertEquals("Ticket com id " + id + " não encontrado", exception.getMessage());
        then(mapper).shouldHaveNoInteractions();
    }

}
