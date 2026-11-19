package br.com.smart_parking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import br.com.smart_parking.core.port.out.TicketPortOut;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:smart_parking_test",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.datasource.driver-class-name=org.h2.Driver"
})
class SmartParkingApplicationTests {

	@MockitoBean
	private TicketPortOut ticketPortOut;

	@Test
	void contextLoads() {
	}

}
