package ar.com.codoAcodo.proyectoSkyFly.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ReservaDto {

    private Long usuarioId;
    private Long vueloId;
    private List<Integer> listaAsientos;

}
