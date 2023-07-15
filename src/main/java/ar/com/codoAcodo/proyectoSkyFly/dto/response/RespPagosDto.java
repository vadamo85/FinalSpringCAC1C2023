package ar.com.codoAcodo.proyectoSkyFly.dto.response;

import ar.com.codoAcodo.proyectoSkyFly.dto.request.PagosDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RespPagosDto{

    private PagosDto pagos;
    private String mensaje;
    private String aereolinea;
    private String numeroVuelo;
    private String ciudadOrigen;
    private String ciudadDestino;
    private double totalAPagar;

}
