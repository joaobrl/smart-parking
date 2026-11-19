package br.com.smart_parking.api.rest;

import br.com.smart_parking.core.domain.Ticket;
import br.com.smart_parking.core.domain.enums.TicketStatus;
import br.com.smart_parking.core.domain.enums.TypeVehicle;
import br.com.smart_parking.core.port.in.TicketPortIn;
import br.com.smart_parking.core.port.out.dto.TicketResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketControllerImpl.class)
class TicketControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketPortIn portIn;

    @Test
    void shouldRegisterTicketAndReturnCreatedResponse() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        LocalDateTime entryTime = LocalDateTime.of(2026, 1, 2, 3, 4);
        Ticket ticket = new Ticket(
                id, "ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN,
                entryTime, null, BigDecimal.ZERO
        );
        given(portIn.createTicket("ABC1234", "CARRO")).willReturn(ticket);

        // act
        var result = mockMvc.perform(post("/smart-parking/tickets/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"licensePlate\":\"ABC1234\",\"vehicleType\":\"CARRO\"}"));

        // assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.licensePlate").value("ABC1234"))
                .andExpect(jsonPath("$.vehicleType").value("CARRO"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.entryTime").value(entryTime.toString()))
                .andExpect(jsonPath("$.exitTime").doesNotExist())
                .andExpect(jsonPath("$.price").value("0"));
        then(portIn).should().createTicket("ABC1234", "CARRO");
    }

    @Test
    void shouldRegisterTicketWithNullOptionalFields() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        Ticket ticket = new Ticket(id, "ABC1234", TypeVehicle.MOTO, TicketStatus.OPEN, null, null, null);
        given(portIn.createTicket("ABC1234", "MOTO")).willReturn(ticket);

        // act
        var result = mockMvc.perform(post("/smart-parking/tickets/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"licensePlate\":\"ABC1234\",\"vehicleType\":\"MOTO\"}"));

        // assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.entryTime").doesNotExist())
                .andExpect(jsonPath("$.exitTime").doesNotExist())
                .andExpect(jsonPath("$.price").doesNotExist());
    }

    @Test
    void shouldListTicketsWithProvidedFilters() throws Exception {
        // arrange
        TicketResponse response = new TicketResponse(
                UUID.randomUUID(), "ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN,
                "2026-01-02T03:04", null, "0"
        );
        given(portIn.listTickets("OPEN", "CARRO")).willReturn(List.of(response));

        // act
        var result = mockMvc.perform(get("/smart-parking/tickets/list")
                .param("status", "OPEN")
                .param("vehicleType", "CARRO"));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licensePlate").value("ABC1234"))
                .andExpect(jsonPath("$[0].vehicleType").value("CARRO"));
        then(portIn).should().listTickets("OPEN", "CARRO");
    }

    @Test
    void shouldReturnEmptyListWhenNoTicketsMatchFilters() throws Exception {
        // arrange
        given(portIn.listTickets(null, null)).willReturn(List.of());

        // act
        var result = mockMvc.perform(get("/smart-parking/tickets/list"));

        // assert
        result.andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldFindTicketById() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        TicketResponse response = new TicketResponse(
                id, "ABC1234", TypeVehicle.CARRO, TicketStatus.OPEN,
                "2026-01-02T03:04", null, "0"
        );
        given(portIn.findTicket(id)).willReturn(response);

        // act
        var result = mockMvc.perform(get("/smart-parking/tickets/find/{id}", id));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.licensePlate").value("ABC1234"));
        then(portIn).should().findTicket(id);
    }

    @Test
    void shouldCompleteTicketById() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        TicketResponse response = new TicketResponse(
                id, "ABC1234", TypeVehicle.CARRO, TicketStatus.CLOSED,
                "2026-01-02T03:04", "2026-01-02T05:06", "25.50"
        );
        given(portIn.ticketCompleted(id)).willReturn(response);

        // act
        var result = mockMvc.perform(patch("/smart-parking/tickets/update/{id}", id));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"))
                .andExpect(jsonPath("$.exitTime").value("2026-01-02T05:06"))
                .andExpect(jsonPath("$.price").value("25.50"));
        then(portIn).should().ticketCompleted(id);
    }
}
