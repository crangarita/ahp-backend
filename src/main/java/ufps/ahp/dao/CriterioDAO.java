package ufps.ahp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Puntuacion;

import java.util.List;

public interface CriterioDAO extends JpaRepository<Criterio, Integer> {
    @Query("select c from Criterio c where c.problema.idProblema =:idProblema")
    List<Criterio> criteriosPorProblema( @Param("idProblema")int idProblema);
}
