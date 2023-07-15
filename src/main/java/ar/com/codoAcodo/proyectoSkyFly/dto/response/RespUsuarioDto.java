package ar.com.codoAcodo.proyectoSkyFly.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RespUsuarioDto {

    private Long reservaId;
    private String fechaReserva;
    private String numeroVuelo;
    private Double precioUnitario;
    private Double montoAPagar;
    private String ciudadOrigen;
    private String ciudadDestino;

}
