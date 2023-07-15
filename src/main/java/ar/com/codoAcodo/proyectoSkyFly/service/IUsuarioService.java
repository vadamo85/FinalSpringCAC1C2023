package ar.com.codoAcodo.proyectoSkyFly.service;

import ar.com.codoAcodo.proyectoSkyFly.dto.response.RespUsuarioDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.informesPagosDto;
import java.util.List;

public interface IUsuarioService {

    List<RespUsuarioDto> verReservas(Long agenteId, Long clienteId);

    informesPagosDto informeDiario(Long admindId, String fecha);

}
