package ar.com.codoAcodo.proyectoSkyFly.service;

import ar.com.codoAcodo.proyectoSkyFly.exception.UsuarioNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@TestPropertySource(properties = {"SCOPE = testing_conf"})
public class UsuarioServiceTest {

    @Autowired
    IUsuarioService usuarioService;


    @Test
    @DisplayName("Camino NO feliz usuario no válido")
    void chequeoDeTipoDeUsuarioYRolThrowExTest(){
        //act and assert
        assertThrows(UsuarioNotFoundException.class,()->{
            usuarioService.verReservas(6L,6L);
        });
    }

}