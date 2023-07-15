package ar.com.codoAcodo.proyectoSkyFly.entity;

import ar.com.codoAcodo.proyectoSkyFly.enums.UsuarioRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long usuariosId;
    private String nombre;
    private String apellido;
    private String dni;
    private Date fechaNac;
    private String email;
    @Enumerated(value = EnumType.STRING)
    private UsuarioRol rol;

    @OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, fetch = FetchType.EAGER)//el cascade se encarga de definir que operaciones realizadas sobre un objeto voy a propagar sobre un objeto relacionado, en este caso items.
    private List<Reservas> reservas;

}
