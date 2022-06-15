package ufps.ahp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.model.PuntuacionAlternativaCriterio;
import ufps.ahp.model.PuntuacionCriterio;

import java.util.List;

public interface PuntuacionCriterioDAO extends JpaRepository<PuntuacionCriterio, Integer> {
    @Transactional
    @Modifying
    @Query(value =
                    "insert " +
                            "into puntuacion_criterio (criterio1_id, criterio2_id, problema) " +
                            "SELECT c1.id_criterio, c2.id_criterio, c1.problema " +
                            "from criterio c1, criterio c2 " +
                            "where c1.problema = c2.problema and c1.id_criterio<c2.id_criterio and c1.problema =:idProblema and c2.problema=:idProblema" ,
            nativeQuery = true)
    void llenarPuntuacionCriterio(@Param("idProblema") int idProblema);

    @Transactional
    @Modifying
    @Query(value ="select pc from PuntuacionCriterio pc where pc.problema.token=:id and pc.criterio1Id<>pc.criterio2Id order by pc.criterio1Id.idCriterio asc"
            )
    List<PuntuacionCriterio> obtenerPares(@Param("id") String id );

    @Transactional
    @Modifying
    @Query(value ="select pac from PuntuacionAlternativaCriterio pac where pac.problema.token=:id and pac.alternativa1.idAlternativa<>pac.alternativa2.idAlternativa order by pac.criterio.idCriterio, pac.alternativa1.idAlternativa asc"
    )
    List<PuntuacionAlternativaCriterio> obtenerParesAlternativas(@Param("id") String id );
}
