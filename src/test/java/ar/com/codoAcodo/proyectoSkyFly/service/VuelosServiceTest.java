package ar.com.codoAcodo.proyectoSkyFly.service;

import ar.com.codoAcodo.proyectoSkyFly.dto.request.AsientosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.PagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.ReservaDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.VuelosDto;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoDescripcion;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoEstado;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoTipo;
import ar.com.codoAcodo.proyectoSkyFly.exception.AsientoNotFoundException;
import org.junit.jupiter.api.*;
import ar.com.codoAcodo.proyectoSkyFly.exception.VueloNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {"SCOPE = testing_conf"})
public class VuelosServiceTest {

    @Autowired
    IVuelosService vuelosService;

    @Test
    @DisplayName("Camino Feliz buscar vuelos...")
    void buscarVuelosOkTest(){

        //ARRANGE
        List<VuelosDto> expected = new ArrayList<>();
        expected.add(new VuelosDto(1L, "Avianca","Av180","Buenos Aires","Rio de Janeiro", LocalDateTime.parse("2023-06-29T18:00:00"),LocalDateTime.parse("2023-06-30T09:00:00"),400.00,false));
        expected.add(new VuelosDto(2L, "Aerolineas Argentinas","Ar200","Buenos Aires","Cancun",LocalDateTime.parse("2023-06-28T18:00:00"),LocalDateTime.parse("2023-06-29T21:30:00"),500.00,false));
        expected.add(new VuelosDto(3L, "Panam","Pan150","Buenos Aires","Madrid",LocalDateTime.parse("2023-06-27T18:00:00"),LocalDateTime.parse("2023-06-29T21:30:00"),900.00,true));

        //ACT
        List<VuelosDto> result = vuelosService.buscarVuelos();
        //ASSERT
        assertEquals(expected,result);
    }

    @Test
    @DisplayName("Camino Feliz ver asientos libres...")
    void verAsientosLibresOkTest(){

        //ARRANGE
        List<AsientosDto> expected = new ArrayList<>();
        expected.add(new AsientosDto(3L, 15L, AsientoDescripcion.PASILLO, AsientoTipo.PRIMERA, AsientoEstado.LIBRE));
        expected.add(new AsientosDto(3L, 17L, AsientoDescripcion.VENTANILLA, AsientoTipo.PRIMERA, AsientoEstado.LIBRE));
        expected.add(new AsientosDto(3L, 19L, AsientoDescripcion.PASILLO, AsientoTipo.PRIMERA, AsientoEstado.LIBRE));
        expected.add(new AsientosDto(3L, 20L, AsientoDescripcion.VENTANILLA, AsientoTipo.TURISTA, AsientoEstado.LIBRE));
        expected.add(new AsientosDto(3L, 22L, AsientoDescripcion.PASILLO, AsientoTipo.TURISTA, AsientoEstado.LIBRE));
        expected.add(new AsientosDto(3L, 24L, AsientoDescripcion.PASILLO, AsientoTipo.TURISTA, AsientoEstado.LIBRE));

        //ACT
        List<AsientosDto> result = vuelosService.verAsientosLibres(3L);
        //ASSERT
        assertEquals(expected,result);
    }

    @Test
    @DisplayName("Camino NO feliz asiento no libre...")
    void checkDisponibilidadAsientosThrowExTest(){
        //act and assert
        assertThrows(AsientoNotFoundException.class,()->{
            vuelosService.realizarReserva(new ReservaDto(1L,1L, Arrays.asList(22)));
        });
    }

    @Test
    @DisplayName("Camino NO feliz vuelo no encontrado")
    void checkExisteVueloThrowExTest(){
        //act and assert
        assertThrows(VueloNotFoundException.class,()->{
            vuelosService.pagarReserva(new PagosDto(10L, "TRANSFERENCIA"));
        });
    }

}