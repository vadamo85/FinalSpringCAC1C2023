package ar.com.codoAcodo.proyectoSkyFly.dto.request;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VuelosDto {

    private String aerolinea;
    private String numeroVuelo;
    private String ciudadOrigen;
    private String ciudadDestino;
    private LocalDateTime partida;
    private LocalDateTime arribo;
    private Double precio;
    private Boolean conexion;

}
