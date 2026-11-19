package br.com.smart_parking.core.port.in.dto;

public record TicketRequest(

    String licensePlate,
    String vehicleType

) {

}
