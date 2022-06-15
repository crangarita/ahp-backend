package ufps.ahp.services;

import ufps.ahp.model.Problema;
import ufps.ahp.model.Puntuacion;
import ufps.ahp.model.PuntuacionCriterio;

import java.util.List;

public interface PuntuacionServicio {
    public List<Puntuacion> listar();
    public List<Puntuacion>puntuacionDecisor(String emailDecisor, String token);
    public Puntuacion buscar(int idPuntuacion);
    public Puntuacion buscarPuntuacionDecisorProblema(int puntuacionCriterio, String email);
    public void guardar(Puntuacion ct);
    public void eliminar(Puntuacion a);
    public List<Object> calcularMatriz(String emailDecisor, String token, Problema problema);
    public List<Object> totalizarMatriz(String token, Problema problema);
    public List<Object> vectorPropio(String tokenProblema);
    public List<Puntuacion> puntuacionesDecisor(String tokenProblema, String emailDecisor);

}
