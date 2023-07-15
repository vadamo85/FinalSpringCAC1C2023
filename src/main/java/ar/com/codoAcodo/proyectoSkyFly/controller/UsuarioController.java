package ar.com.codoAcodo.proyectoSkyFly.controller;

import ar.com.codoAcodo.proyectoSkyFly.dto.response.informesPagosDto;
import ar.com.codoAcodo.proyectoSkyFly.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioServiceImpl usuarioService;

    @GetMapping("/verReservas")
    public ResponseEntity<?> verReservas(@RequestParam Long agenteId, @RequestParam Long clienteId){
        return new ResponseEntity<>(usuarioService.verReservas(agenteId, clienteId), HttpStatus.OK);
    }

    @GetMapping("/informesPagos")
    public ResponseEntity<informesPagosDto> generarInformeVentasDiario(@RequestParam("adminId") Long adminId, @RequestParam("fecha") String fecha) {
        return new ResponseEntity<>(usuarioService.informeDiario(adminId, fecha), HttpStatus.OK);
    }

}