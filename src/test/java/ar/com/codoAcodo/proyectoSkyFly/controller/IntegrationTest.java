package ar.com.codoAcodo.proyectoSkyFly.controller;

import ar.com.codoAcodo.proyectoSkyFly.dto.request.PagosDto;
import ar.com.codoAcodo.proyectoSkyFly.dto.request.ReservaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(properties = {"SCOPE = integration_test"})
public class IntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Camino feliz buscar vuelos")
    void buscarVuelosIntTestOk()throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("http://localhost:8080/vuelos/buscarVuelos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].aerolinea").value("Avianca"))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("camino feliz realizar reserva")
    public void realizarReservaIntTestOk() throws Exception {

        ReservaDto reservaDto = new ReservaDto(2L,1L, Arrays.asList(20,21));
        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String jsonPayload = writer.writeValueAsString(reservaDto);

        mockMvc.perform(post("http://localhost:8080/vuelos/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservaId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("La reserva se realizó con éxito"));
                }

    @Test
    @DisplayName("camino feliz pagar reserva")
    public void pagarReservaIntTestOk() throws Exception {

        PagosDto pagosDto = new PagosDto(1L,"TRANSFERENCIA");
        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String jsonPayload = writer.writeValueAsString(pagosDto);

        mockMvc.perform(post("http://localhost:8080/vuelos/pagarReserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("Su pago está confirmado. Usted pagó con " + pagosDto.getFormaDePago()));
    }

    @Test
    @DisplayName("Camino feliz ver asientos")
    public void verAsientosIntTestOk()throws Exception{

        mockMvc.perform(get("http://localhost:8080/vuelos/verAsientos")
                        .param("vuelosId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroDeAsiento").value(10));

    }

    @Test
    @DisplayName("Camino feliz ver asientos libres")
    public void verAsientosLibresIntTestOk()throws Exception{

        mockMvc.perform(get("http://localhost:8080/vuelos/verAsientosLibres")
                        .param("vuelosId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroDeAsiento").value(12));

    }

    @Test
    @DisplayName("Camino feliz ver reservas")
    public void verReservasIntTestOk() throws Exception {

        Long agenteId = 4L;
        Long clienteId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/verReservas")
                        .param("agenteId", String.valueOf(agenteId))
                        .param("clienteId", String.valueOf(clienteId))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroVuelo").value("Av180"));

    }

    @Test
    @DisplayName("Camino feliz informe diario")
    public void informeDiarioIntTestOk() throws Exception {

        Long adminId = 5L;
        String fecha = "29/06/2023";

        mockMvc.perform(get("/usuarios/informesPagos")
                        .param("adminId", adminId.toString())
                        .param("fecha", fecha))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.cantidadTotalReservas").value(2))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.cantidadReservasPendientes").value(1))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.cantidadReservasPagadas").value(1))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.montoDeReservasPagadas").value(400.00));
    }

}



