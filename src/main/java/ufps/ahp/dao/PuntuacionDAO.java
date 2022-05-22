package ufps.ahp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufps.ahp.model.Puntuacion;
import ufps.ahp.model.PuntuacionAlternativa;

import java.util.List;

public interface PuntuacionDAO extends JpaRepository<Puntuacion, Integer> {

    @Query("select p from Puntuacion p where p.decisor.email=:emailDecisor and p.puntuacionCriterio.problema.token=:tokenProblema")
    List<Puntuacion> puntuacionesDeUsuario(@Param("emailDecisor") String emailDecisor, @Param("tokenProblema")String tokenProblema);
}
