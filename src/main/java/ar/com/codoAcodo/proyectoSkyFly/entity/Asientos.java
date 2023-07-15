package ar.com.codoAcodo.proyectoSkyFly.entity;

import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoDescripcion;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoEstado;
import ar.com.codoAcodo.proyectoSkyFly.enums.AsientoTipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "asientos")
public class Asientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long asientosId;

    @Column(name = "numero_asiento")
    private Long numeroDeAsiento;

    @Enumerated(value = EnumType.ORDINAL)
    private AsientoEstado estadoAsiento;
    @Enumerated(value = EnumType.ORDINAL)
    private AsientoTipo tipoDeAsiento;
    @Enumerated(value = EnumType.ORDINAL)
    private AsientoDescripcion descripcion;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vuelos_id", nullable = false)
    private Vuelos vuelos;

}
