package ar.com.codoAcodo.proyectoSkyFly.service;

import ar.com.codoAcodo.proyectoSkyFly.dto.response.RespUsuarioDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.informesPagosDto;
import ar.com.codoAcodo.proyectoSkyFly.entity.Reservas;
import ar.com.codoAcodo.proyectoSkyFly.entity.Usuarios;
import ar.com.codoAcodo.proyectoSkyFly.enums.PagoEstado;
import ar.com.codoAcodo.proyectoSkyFly.enums.UsuarioRol;
import ar.com.codoAcodo.proyectoSkyFly.exception.FechaErrorException;
import ar.com.codoAcodo.proyectoSkyFly.exception.ReservaNotFoundException;
import ar.com.codoAcodo.proyectoSkyFly.exception.UsuarioNotFoundException;
import ar.com.codoAcodo.proyectoSkyFly.repository.IReservasRepository;
import ar.com.codoAcodo.proyectoSkyFly.repository.IUsuariosRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
    ModelMapper mapper = new ModelMapper();
    @Autowired
    IUsuariosRepository usuariosRepository;

    @Autowired
    IReservasRepository reservasRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<RespUsuarioDto> verReservas(Long agenteId, Long clienteId) {

        chequeoDeTipoDeUsuarioYRol(agenteId,UsuarioRol.AGENTE);
        chequeoDeTipoDeUsuarioYRol(clienteId, UsuarioRol.CLIENTE);
        List<Reservas>listaReserva= reservasRepository.findAll();
        List<RespUsuarioDto> listaReservasDto = new ArrayList<>();

        for (Reservas r: listaReserva) {
            if (Objects.equals(r.getUsuarios().getUsuariosId(), clienteId)) {

                RespUsuarioDto respUsuarioDto = new RespUsuarioDto();
                respUsuarioDto.setReservaId(r.getReservasId());
                String fechaFormateada = formatter.format(r.getFechaReserva());
                respUsuarioDto.setFechaReserva(fechaFormateada);
                respUsuarioDto.setPrecioUnitario(r.getVuelos().getPrecio());
                respUsuarioDto.setMontoAPagar(r.getCostoTotal());
                respUsuarioDto.setCiudadOrigen(r.getVuelos().getCiudadOrigen());
                respUsuarioDto.setCiudadDestino(r.getVuelos().getCiudadDestino());
                respUsuarioDto.setNumeroVuelo(r.getVuelos().getNumeroVuelo());
                listaReservasDto.add(mapper.map(respUsuarioDto, RespUsuarioDto.class));

            }
        }

        if (listaReservasDto.isEmpty()){
            throw new UsuarioNotFoundException("El cliente seleccionado no tiene reservas");

        }

        return listaReservasDto;

    }

    @Override
    public informesPagosDto informeDiario(Long admindId, String fecha) {

        chequeoDeTipoDeUsuarioYRol(admindId, UsuarioRol.ADMIN);
        LocalDate fechaConsultada = validacionfecha(fecha);
        informesPagosDto informeDiario = new informesPagosDto();
        List<Reservas> listaReservas= reservasRepository.findAllByFechaReserva(fechaConsultada);

        if (!listaReservas.isEmpty()){
            Double montoTotal= (double) 0;
            int pendientes = 0;
            int pagadas=0;
            for(Reservas r: listaReservas){
                if(r.getPagos().getEstadoDePago().equals(PagoEstado.PENDIENTE)){
                    pendientes++;

                }else{
                    pagadas++;
                    montoTotal += r.getCostoTotal();
                }
            }

            informeDiario.setFecha(formatter.format(fechaConsultada));
            informeDiario.setCantidadTotalReservas(listaReservas.size());
            informeDiario.setCantidadReservasPagadas(pagadas);
            informeDiario.setCantidadReservasPendientes(pendientes);
            informeDiario.setMontoDeReservasPagadas(montoTotal);

        }else{
            throw new ReservaNotFoundException("No existen reservas para la fecha solicitada");
        }

        return informeDiario;
    }

    private LocalDate validacionfecha(String fecha) {

        LocalDate localDate;

        try{
            localDate = LocalDate.parse(fecha, formatter);

        }catch (Exception e){
            throw new FechaErrorException("La fecha solicitada no existe. Por favor ingrese una fecha válida.");
        }

        return localDate;
    }

    private void chequeoDeTipoDeUsuarioYRol(Long id, UsuarioRol usuarioRol) {

        Usuarios usu = usuariosRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException( usuarioRol.toString() + "  No existe!"   ));

        if(!(usu.getRol().equals(usuarioRol))){
            throw new UsuarioNotFoundException(id + " es un "+ usu.getRol()  +" -- Se requiere un -> " + usuarioRol);
        }
    }

}
