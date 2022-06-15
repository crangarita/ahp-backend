package ufps.ahp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.model.PuntuacionAlternativaCriterio;

public interface PuntuacionAlternativaCriterioDAO extends JpaRepository<PuntuacionAlternativaCriterio, Integer> {
    @Transactional
    @Modifying
    @Query(value =
            "insert " +
                    "into puntuacion_alternativa_criterio(alternativa_1, alternativa_2, problema,criterio) " +
                    "SELECT a1.id_alternativa, a2.id_alternativa, a1.problema, :idCriterio " +
                    "from alternativa a1, alternativa a2 " +
                    "where a1.problema = a2.problema and a1.id_alternativa<a2.id_alternativa and a1.problema =:idProblema and a2.problema=:idProblema" ,
            nativeQuery = true)
    void llenarPuntuacionAlternativa(@Param("idProblema") int idProblema, @Param("idCriterio")int idCriterio);
}
