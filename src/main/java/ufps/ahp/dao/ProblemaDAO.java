package ufps.ahp.dao;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Decisor;
import ufps.ahp.model.Problema;

import java.util.List;

public interface ProblemaDAO extends JpaRepository<Problema, Integer> {
    @Query(value = "SELECT c FROM Problema p, Criterio c WHERE p.token = :token and p.token=c.problema.token")
    List<Criterio>  criteriorPorProblema(@Param("token") String token);

    @Query(value = "SELECT dp.decisor FROM DecisorProblema dp WHERE dp.decisor.email = :email and dp.problema.token=:token ")
    Decisor existeDecisor(@Param("email") String email, @Param("token") String token);

    @Query(value="select dp.decisor from DecisorProblema dp where dp.problema.idProblema=:token")
    List<Decisor> decisorPorProblema (@Param("token")String token);

    @Query(value="select p from Problema p where p.token=:token")
    Problema encontrarProblema (@Param("token")String token);

    Problema findProblemaByToken(String token);

    Problema findProblemaByIdProblema(int id);
}
