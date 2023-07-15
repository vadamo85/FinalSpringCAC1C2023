package ar.com.codoAcodo.proyectoSkyFly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Setter
@Getter
@Entity
@Table(name = "reservas")
public class Reservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservasId;

//    private String categoria;

    @Column(name = "fecha_reserva", columnDefinition = "TIMESTAMP")
    private LocalDate fechaReserva;
    private Double costoTotal;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuarios_id", nullable = false)//JoinColumn me genera la FK en la entidad reservas
    private Usuarios usuarios;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "vuelos_id", referencedColumnName = "vuelosId")//JoinColumn me genera la FK en la entidad reservas
    private Vuelos vuelos;

    @OneToOne(mappedBy = "reservas")
    private Pagos pagos;

}
