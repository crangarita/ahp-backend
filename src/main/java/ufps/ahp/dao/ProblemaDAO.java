package ufps.ahp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Decisor;
import ufps.ahp.model.Problema;

import java.util.List;

public interface ProblemaDAO extends JpaRepository<Problema, String> {
    @Query(value = "SELECT c FROM Problema p, Criterio c WHERE p.idProblema = :id and p.idProblema=c.problema")
    List<Criterio>  criteriorPorProblema(@Param("id") String id);

    @Query(value = "SELECT d FROM Decisor d, Problema p WHERE d.email = :email and p.idProblema=:idProblema ")
    Decisor existeDecisor(@Param("email") String email, @Param("idProblema") String idProblema);

    @Query(value="select dp.decisor from DecisorProblema dp where dp.problema=:idProblema")
    List<Decisor> decisorPorProblema (@Param("idProblema")String idProblema);
}
