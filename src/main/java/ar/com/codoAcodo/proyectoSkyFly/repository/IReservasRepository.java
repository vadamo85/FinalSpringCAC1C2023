package ar.com.codoAcodo.proyectoSkyFly.repository;

import ar.com.codoAcodo.proyectoSkyFly.entity.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface IReservasRepository extends JpaRepository<Reservas,Long> {
    List<Reservas> findAllByFechaReserva(LocalDate fecha);

}
