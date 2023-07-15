package ar.com.codoAcodo.proyectoSkyFly.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagosDto {

    private Long reservaId;
    private String formaDePago;

}
