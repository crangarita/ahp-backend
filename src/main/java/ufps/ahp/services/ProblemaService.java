package ufps.ahp.services;

import ufps.ahp.model.*;

import java.util.List;

public interface ProblemaService {
    public List<Problema> listar();
    public Decisor existeDecisor(String email, String token);
    public Problema buscar(String token);
    public Problema buscarPorId(int id);
    public List<Criterio> criteriosPorProblema(String token);
    public List<Decisor> decisoresPorProblema(String token);
    public List<Alternativa> alternativasPorProblema(String token);
    public void guardar(Problema p);
    public void eliminar(Problema p);

}
