package ar.utn.ba.ddsi.apiadmi.models.repository;


import ar.utn.ba.ddsi.apiadmi.models.entities.condiciones.InterfaceCondicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICriteriosRepository extends JpaRepository<InterfaceCondicion,Long> {

    @Modifying
    @Query("DELETE FROM InterfaceCondicion c WHERE c.id IN :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);

}
