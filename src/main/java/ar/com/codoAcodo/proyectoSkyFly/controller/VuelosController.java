package ar.com.codoAcodo.proyectoSkyFly.controller;

import ar.com.codoAcodo.proyectoSkyFly.dto.request.PagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.ReservaDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.RespPagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.response.RespReservaDto;
import ar.com.codoAcodo.proyectoSkyFly.service.VuelosServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vuelos")
public class VuelosController {

    VuelosServiceImpl vuelosService;

    public VuelosController(VuelosServiceImpl vuelosService) {

        this.vuelosService = vuelosService;
    }

    @GetMapping("/buscarVuelos")
    public ResponseEntity<?> buscarVuelos(){
        return new ResponseEntity<>(vuelosService.buscarVuelos(), HttpStatus.OK);
    }

    @PostMapping("/reservas")
    public ResponseEntity<RespReservaDto> realizarReserva(@RequestBody ReservaDto reservaDto){
        return new ResponseEntity<>(vuelosService.realizarReserva(reservaDto), HttpStatus.OK);
    }

    @PostMapping("/pagarReserva")
    public ResponseEntity<RespPagosDto> pagarReserva(@RequestBody PagosDto pagosDto){

        return new ResponseEntity<>(vuelosService.pagarReserva(pagosDto), HttpStatus.OK);
    }

    @GetMapping("/verAsientos")
    public ResponseEntity<?> verAsientos(@RequestParam Long vuelosId){
        return new ResponseEntity<>(vuelosService.verAsientos(vuelosId), HttpStatus.OK);
    }

    @GetMapping("/verAsientosLibres")
    public ResponseEntity<?> verAsientosLibres(@RequestParam Long vuelosId){
        return new ResponseEntity<>(vuelosService.verAsientosLibres(vuelosId), HttpStatus.OK);
    }

}
