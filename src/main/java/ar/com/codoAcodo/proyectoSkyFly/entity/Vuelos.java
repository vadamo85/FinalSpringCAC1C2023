package ar.com.codoAcodo.proyectoSkyFly.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vuelos")
public class Vuelos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vuelosId;
    private String aerolinea;
    private String numeroVuelo;
    private String ciudadOrigen;
    private String ciudadDestino;
    private LocalDateTime partida;
    private LocalDateTime arribo;
    private Double precio;
    private Boolean conexion;

    @OneToMany(mappedBy = "vuelos",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Reservas> reservas;//Vuelo tiene como atributo una entidad de tipo Reservas, que va a estar relacionada mediante el mappedBy con una entidad que se llama Vuelos

    @OneToMany(mappedBy = "vuelos", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Asientos> asientos;

}
