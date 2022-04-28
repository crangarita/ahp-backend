package ufps.ahp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufps.ahp.model.Decisor;
import ufps.ahp.model.DecisorProblema;

import java.util.List;

public interface DecisorDAO extends JpaRepository<Decisor, Integer> {
    Decisor findDecisorByEmail(String email);

    @Query("select dp from DecisorProblema dp where dp.problema.usuario.email=:email")
    List<DecisorProblema> decisoresDeUsuario(@Param("email") String email);

    @Query("select dp.decisor from DecisorProblema dp where dp.problema.token=:token and dp.decisor.email=:email")
    Decisor buscarDecisorProblema(@Param("token") String token, @Param("email") String email);
}
