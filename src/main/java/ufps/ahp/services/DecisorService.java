package ufps.ahp.services;

import ufps.ahp.model.Decisor;
import ufps.ahp.model.DecisorProblema;

import java.util.List;
import java.util.Optional;

public interface DecisorService{

    public List<Decisor> listar();
    public List<DecisorProblema> listarDecisoresDeUsuario(String email);
    public Optional<Decisor> buscarDecisorProblema (String token, String email);
    public Decisor buscar(int idDecisor);
    public Decisor buscarPorEmail(String email);
    public void guardar(Decisor decisor);
    public void eliminar(Decisor decisor);

}
