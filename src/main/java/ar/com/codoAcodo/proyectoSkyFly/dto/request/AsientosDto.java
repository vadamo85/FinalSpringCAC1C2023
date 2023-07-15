package ar.com.codoAcodo.proyectoSkyFly.dto.request;

import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoDescripcion;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoEstado;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoTipo;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AsientosDto {

    private Long vuelosId;
    private Long numeroDeAsiento;
    private AsientoDescripcion descripcion;
    private AsientoTipo tipoDeAsiento;
    private AsientoEstado estadoAsiento;

}
