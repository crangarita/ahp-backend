package ufps.ahp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.model.Puntuacion;
import ufps.ahp.model.PuntuacionAlternativa;

import java.util.List;

public interface PuntuacionAlternativaDAO extends JpaRepository<PuntuacionAlternativa, Integer> {

    @Query("select p from PuntuacionAlternativa p where p.decisor.email=:emailDecisor and p.puntuacionAlternativaCriterio.problema.token=:tokenProblema")
    List<PuntuacionAlternativa> puntuacionesDeUsuario(@Param("emailDecisor") String emailDecisor, @Param("tokenProblema")String tokenProblema);

    @Query("select p from PuntuacionAlternativa p where p.decisor.email=:emailDecisor and p.puntuacionAlternativaCriterio.problema.token=:tokenProblema and p.puntuacionAlternativaCriterio.criterio.idCriterio=:idCriterio")
    List<PuntuacionAlternativa> puntuacionesAlternativasDeCriterioMayor(@Param("emailDecisor") String emailDecisor, @Param("tokenProblema")String tokenProblema, @Param("idCriterio") int idCriterio);

    @Query("select p from PuntuacionAlternativa p where p.puntuacionAlternativaCriterio.problema.token=:tokenProblema")
    List<PuntuacionAlternativa> puntuacionesDeProblema(@Param("tokenProblema")String tokenProblema);

    @Query("select p from PuntuacionAlternativa p where p.decisor.email=:emailDecisor and p.puntuacionAlternativaCriterio.idPuntuacionAltCrit=:idPuntuacionAlternativaCrit")
    PuntuacionAlternativa puntuacionUsuario(@Param("emailDecisor") String emailDecisor, @Param("idPuntuacionAlternativaCrit")int idPuntuacionAlternativaCrit);

}
