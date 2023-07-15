package ar.com.codoAcodo.proyectoSkyFly.dto.response;

import ar.com.codoAcodo.proyectoSkyFly.dto.request.ReservaDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RespReservaDto {

    private Long reservaId;
    private String mensaje;
    private String fechaReserva;
    private Double precioUnitario;
    private Double montoAPagar;
    private ReservaDto datos_de_reserva;

}
