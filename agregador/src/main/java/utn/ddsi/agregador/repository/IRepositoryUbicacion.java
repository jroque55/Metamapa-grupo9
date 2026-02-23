package utn.ddsi.agregador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.ddsi.agregador.domain.hecho.Ubicacion;

public interface IRepositoryUbicacion extends JpaRepository<Ubicacion, Long> {

}
