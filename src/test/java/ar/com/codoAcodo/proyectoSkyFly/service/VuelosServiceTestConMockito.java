package ar.com.codoAcodo.proyectoSkyFly.service;

import ar.com.codoAcodo.proyectoSkyFly.dto.request.PagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.RespPagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.informesPagosDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class VuelosServiceTestConMockito {

    @Mock
    private VuelosServiceImpl vuelosService;

    @Mock
    private UsuarioServiceImpl usuarioService;

    @Test
    @DisplayName("Camino feliz pagar reserva")

    public void pagarReservaIntTestOk(){

        // Arrange
        PagosDto pagosDto = new PagosDto(1L,"TRANSFERENCIA");
        RespPagosDto expected = new RespPagosDto();
        expected.setAereolinea("Avianca");
        expected.setNumeroVuelo("Av180");
        expected.setCiudadOrigen("Buenos Aires");
        expected.setCiudadDestino("Rio de Janeiro");
        expected.setTotalAPagar(400.00);
        expected.setPagos(pagosDto);
        expected.setMensaje("Su pago con " + pagosDto.getFormaDePago() + " ha sido aceptada");

        when(vuelosService.pagarReserva(any(PagosDto.class))).thenReturn(expected);

        // Act
        RespPagosDto act = vuelosService.pagarReserva(pagosDto);

        // Assert
        assertEquals(expected,act);
    }

    @Test
    @DisplayName("Camino feliz ver reserva")

    public void confeccionDeRespPagoDto(){
        // Arrange
        PagosDto pagosDto = new PagosDto(1L,"TRANSFERENCIA");
        RespPagosDto expected = new RespPagosDto();

        expected.setPagos(pagosDto);
        expected.setMensaje("Su pago ha sido confirmado. Usted pagó con " + pagosDto.getFormaDePago());
        expected.setAereolinea("Avianca");
        expected.setNumeroVuelo("Av180");
        expected.setCiudadOrigen("Buenos Aires");
        expected.setCiudadDestino("Rio de Janeiro");
        expected.setTotalAPagar(400.00);


        when(vuelosService.pagarReserva(any(PagosDto.class))).thenReturn(expected);

        // Act
        RespPagosDto act = vuelosService.pagarReserva(pagosDto);

        // Assert
        assertEquals(expected,act);
    }

    @Test
    @DisplayName("Camino feliz informe diario")

    public void informeDiarioTestOk(){
        // Arrange
        informesPagosDto expected = new informesPagosDto();
        Long adminId = 5L;
        String fecha = "29/06/2023";

        expected.setFecha(fecha);
        expected.setCantidadTotalReservas(5);
        expected.setCantidadReservasPendientes(2);
        expected.setCantidadReservasPagadas(3);
        expected.setMontoDeReservasPagadas(5000.0);

        when(usuarioService.informeDiario(5L, "29/06/2023")).thenReturn(expected);;

        // Act
        informesPagosDto act = usuarioService.informeDiario(5L, fecha);

        // Assert
        assertEquals(expected,act);
    }

}
