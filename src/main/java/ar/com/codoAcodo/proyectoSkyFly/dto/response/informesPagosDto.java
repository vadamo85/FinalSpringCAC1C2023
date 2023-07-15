package ar.com.codoAcodo.proyectoSkyFly.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class informesPagosDto {

    private String fecha;
    private int cantidadTotalReservas;
    private int cantidadReservasPendientes;
    private int cantidadReservasPagadas;
    private Double montoDeReservasPagadas;

}
