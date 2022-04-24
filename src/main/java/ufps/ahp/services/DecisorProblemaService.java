package ufps.ahp.services;

import ufps.ahp.model.DecisorProblema;

import java.util.List;

public interface DecisorProblemaService {
    public List<DecisorProblema> listar();
    public DecisorProblema buscar(int idDecisorProblema);
    public void guardar(DecisorProblema ct);
    public void eliminar(DecisorProblema a);
}
