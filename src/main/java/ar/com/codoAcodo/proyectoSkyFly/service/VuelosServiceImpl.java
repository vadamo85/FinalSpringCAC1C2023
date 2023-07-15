package ar.com.codoAcodo.proyectoSkyFly.service;

import ar.com.codoAcodo.proyectoSkyFly.dto.request.AsientosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.PagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.ReservaDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.VuelosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.RespPagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.RespReservaDto;
import ar.com.codoAcodo.proyectoSkyFly.entity.*;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoEstado;
import ar.com.codoAcodo.proyectoSkyFly.enums.FormaDePago;
import ar.com.codoAcodo.proyectoSkyFly.enums.PagoEstado;
import ar.com.codoAcodo.proyectoSkyFly.enums.UsuarioRol;
import ar.com.codoAcodo.proyectoSkyFly.exception.AsientoNotFoundException;
import ar.com.codoAcodo.proyectoSkyFly.exception.PagoNotFoundException;
import ar.com.codoAcodo.proyectoSkyFly.exception.UsuarioNotFoundException;
import ar.com.codoAcodo.proyectoSkyFly.exception.VueloNotFoundException;
import ar.com.codoAcodo.proyectoSkyFly.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VuelosServiceImpl implements IVuelosService {

    @Autowired
    IVuelosRepository vuelosRepository;
    @Autowired
    IReservasRepository reservasRepository;
    @Autowired
    IUsuariosRepository usuariosRepository;
    @Autowired
    IAsientosRepository asientosRepository;
    @Autowired
    IPagosRepository pagosRepository;
    ModelMapper mapper = new ModelMapper();//creamos un ModelMapper(debemos tener la dependencia en el pom).La clase ModelMapper nos permite transformar un objeto relacional en un objeto java

    Usuarios usuario;
    Vuelos vuelo;
    Reservas reserva;

    @Override
    public List<VuelosDto> buscarVuelos() {

        List<Vuelos> vuelosEnt = vuelosRepository.findAll();//creamos una lista de vuelos,que es una clase del tipo entidad, por ende es una lista de entidades. la vamos a crear por medio del findAll. buscamos en el repositorio mediante el findAll todos los vuelos entidades
        List<VuelosDto> vuelosDto = new ArrayList<>();
        vuelosEnt
                .forEach(c-> vuelosDto.add(mapper.map(c,VuelosDto.class)));
        return vuelosDto;

    }

    @Override
    public RespReservaDto realizarReserva(ReservaDto reservaDto) {

        usuario = checkExisteYRol(reservaDto.getUsuarioId());
        vuelo = checkExisteVuelo(reservaDto.getVueloId());
        checkListaVacia(reservaDto.getListaAsientos());
        checkNumerosDeAsientos(reservaDto.getListaAsientos());
        List<Asientos> asientos = checkExistenAsientosEnBBDD(reservaDto.getListaAsientos(),vuelo);
        checkDisponibilidadAsientos(asientos);
        int cantidadDeAsientos = asientos.size();
        Reservas reserva = guardaReserva(cantidadDeAsientos, vuelo, usuario);

        //cambio el estado del asiento que de libre a vendido
        cambiaEstadoDelAsientoaVendido(asientos);

        //Se genera un registro en PAGOS con el estado "Pendiente" d
        generaPagoPendiente(reserva);

        // Obtener la hora actual formateada
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHoraFormateada = fechaHoraActual.format(formatter);

        return new RespReservaDto(reserva.getReservasId(),
                 "La reserva se realizó con éxito", fechaHoraFormateada,
                 vuelo.getPrecio(), reserva.getCostoTotal(),reservaDto);

    }

    @Override
    public RespPagosDto pagarReserva(PagosDto pagosDto) {

        checkFormasDePago(pagosDto.getFormaDePago());
        RespPagosDto respPagosDto = confeccionDeRespPagoDto(pagosDto);
        return mapper.map(respPagosDto, RespPagosDto.class);

    }

    ////////// Metodos de Apoyo al Service /////////////////////

    private void checkListaVacia(List<Integer> listaAsientos) {

        if(listaAsientos == null || listaAsientos.isEmpty()){
            throw new RuntimeException("Lista de asientos vacía");
        }
    }

    private void checkDisponibilidadAsientos(List<Asientos> asientos) {

        List<Asientos> asientosOcupados = asientos.stream().
                filter(a ->a.getEstadoAsiento().equals(AsientoEstado.VENDIDO)).toList();

        if(!asientosOcupados.isEmpty()){
            StringBuilder numAsientosOcupados = new StringBuilder();

            asientosOcupados.forEach(
                    a -> numAsientosOcupados.append(a.getNumeroDeAsiento()).append("/"));

            throw new AsientoNotFoundException("Asiento/s " + numAsientosOcupados + " no está/n libre/s ");
        }

    }

    private List<Asientos> checkExistenAsientosEnBBDD(List<Integer> listaAsientos, Vuelos vueloSelect) {

        List<Asientos> asientosDeBBDD = asientosRepository.findAll();
        List<Asientos> listaAsientosOk =new ArrayList<>();

        for(Integer numAsiento: listaAsientos){
            Optional<Asientos> oAsiento = asientosDeBBDD.stream()
                    .filter(a-> a.getNumeroDeAsiento() == numAsiento.longValue())
                    .filter(a-> a.getVuelos().equals(vueloSelect))
                    .findFirst();

            listaAsientosOk.add(oAsiento.orElseGet(() ->
                    oAsiento.orElseThrow(() ->
                            new AsientoNotFoundException("Asiento " + numAsiento +  " no encontrado en vuelo "+vueloSelect.getNumeroVuelo()))));

        }

        return listaAsientosOk;

    }

    private void checkNumerosDeAsientos(List<Integer> listaAsientos) {

        // Revisa si los numeros de asientos son mayores a cero, o si hay un numero repetido
        boolean listaOK = listaAsientos.stream()
             .allMatch(numero -> numero > 0
             && listaAsientos.stream().filter(n -> n.equals(numero)).count() == 1);
       if (!listaOK) {
           throw new RuntimeException("Hay un error con los números de asientos, por favor verificar");
       }
    }

    private void generaPagoPendiente(Reservas reserva) {

        Pagos pago = new Pagos();
        pago.setPagosId(null);
        pago.setEstadoDePago(PagoEstado.PENDIENTE);
        pago.setReservas(reserva);
        try {
            pagosRepository.save(pago);
        }catch (Exception e){
            throw new RuntimeException("Error al guardar pagos");
        }

    }

    private Reservas guardaReserva(int cantAsientos, Vuelos vueloReserva, Usuarios usuarioReserva) {

        Reservas reserva = new Reservas();
        reserva.setReservasId(null);
        reserva.setCostoTotal(vueloReserva.getPrecio()* cantAsientos);
        reserva.setVuelos(vueloReserva);
        reserva.setUsuarios(usuarioReserva);
        reserva.setFechaReserva(LocalDate.now());

        try {
            reservasRepository.save(reserva);
        }catch (Exception e){
            throw new RuntimeException("Error al guardar reserva");
        }
        return reserva;

    }

    private void cambiaEstadoDelAsientoaVendido(List<Asientos> asientosList) {

        for (Asientos a : asientosList) {
            a.setEstadoAsiento(AsientoEstado.VENDIDO);
            try {
                asientosRepository.save(a);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar asientos");
            }
        }

    }

    private Usuarios checkExisteYRol(Long id){

        Usuarios usu = usuariosRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException("Usuario no existente"));

        boolean esCliente = usu.getRol().equals(UsuarioRol.CLIENTE);

        if (!esCliente){throw new RuntimeException("No es un rol válido");}

        return usu;

    }

    private Vuelos checkExisteVuelo(Long id){

        return vuelosRepository.findById(id)
                .orElseThrow(() -> new VueloNotFoundException("Vuelo no encontrado"));
    }

    private Reservas checkExisteReserva(Long id){

        return reservasRepository.findById(id)
                .orElseThrow(() -> new VueloNotFoundException("Reserva no encontrada"));
    }



    private void checkFormasDePago(String formaDePago) {

        boolean esFormaDePagoPermitida = Arrays.stream(FormaDePago.values())
               .anyMatch(valor -> valor.name()
               .equals(formaDePago.toUpperCase()));
       if(!esFormaDePagoPermitida) throw new RuntimeException("Forma de pago no admitida");

    }

    private RespPagosDto confeccionDeRespPagoDto(PagosDto pagosDto) {

        RespPagosDto respPagosDto = new RespPagosDto();
        reserva = checkExisteReserva(pagosDto.getReservaId());
        Pagos pagoAConfirmar = pagosRepository.findById
                        (reserva.getPagos().getPagosId())
                .orElseThrow(()->new PagoNotFoundException("Pago no encontrado"));

        if (pagoAConfirmar.getEstadoDePago().equals(PagoEstado.PENDIENTE)){
            respPagosDto.setMensaje("Su pago está confirmado. Usted pagó con " + pagosDto.getFormaDePago());
            respPagosDto.setAereolinea(pagoAConfirmar.getReservas().getVuelos().getAerolinea());
            respPagosDto.setNumeroVuelo(pagoAConfirmar.getReservas().getVuelos().getNumeroVuelo());
            respPagosDto.setCiudadOrigen(pagoAConfirmar.getReservas().getVuelos().getCiudadOrigen());
            respPagosDto.setCiudadDestino(pagoAConfirmar.getReservas().getVuelos().getCiudadDestino());
            respPagosDto.setPagos(pagosDto);
            respPagosDto.setTotalAPagar(pagoAConfirmar.getReservas().getCostoTotal());
            pagoAConfirmar.setEstadoDePago(PagoEstado.CONFIRMADO);
            pagosRepository.save(pagoAConfirmar);

        }
        else{

            throw new PagoNotFoundException("La reserva se encuentra abonada");
        }
        return respPagosDto;

    }

    @Override
    public List<AsientosDto> verAsientos(Long vuelosId) {

        vuelo = checkExisteVuelo(vuelosId);
        List<Asientos> asientos = asientosRepository.findAll();
         // Filtra los asientos en base a la ID pasada como parámetro

        return asientos.stream()
                .filter(asiento -> asiento.getVuelos().getVuelosId().equals(vuelosId))
                .map(asiento -> {
                    AsientosDto asientoDto = new AsientosDto();
                    asientoDto.setVuelosId(asiento.getVuelos().getVuelosId());
                    asientoDto.setNumeroDeAsiento(asiento.getNumeroDeAsiento());
                    asientoDto.setDescripcion(asiento.getDescripcion());
                    asientoDto.setTipoDeAsiento(asiento.getTipoDeAsiento());
                    asientoDto.setEstadoAsiento(asiento.getEstadoAsiento());
                    return asientoDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AsientosDto> verAsientosLibres(Long vuelosId) {

        vuelo = checkExisteVuelo(vuelosId);
        List<Asientos> asientosEnt = asientosRepository.findAll();
        List<AsientosDto> asiDto = new ArrayList<>();

        //  asientosEnt.forEach(c-> asiDto.add(mapper.map(c,AsientosDto.class)));
        asientosEnt.forEach(c -> {if (Objects.equals(c.getVuelos().getVuelosId(), vuelosId) && c.getEstadoAsiento().equals(AsientoEstado.LIBRE)) {
            asiDto.add(mapper.map(c, AsientosDto.class));
        }

        });
        return asiDto;
    }

}
