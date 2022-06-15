package ufps.ahp.services;

import org.springframework.beans.factory.parsing.Problem;
import ufps.ahp.model.Problema;
import ufps.ahp.model.Puntuacion;
import ufps.ahp.model.PuntuacionAlternativa;
import ufps.ahp.model.PuntuacionAlternativaCriterio;

import java.util.List;

public interface PuntuacionAlternativaServicio {
    public List<PuntuacionAlternativa> listar();
    public PuntuacionAlternativa buscar(int idPuntuacionAlternativa);
    public PuntuacionAlternativa buscarPuntuacionDecisorProblema(int idPuntuacionAlternativa, String email);
    public List<Object> calcularMatriz(String email, String token, Problema problema);
    public List<Object> totalizarMatriz(String token, Problema problema);
    public void guardar(PuntuacionAlternativa ct);
    public void eliminar(PuntuacionAlternativa a);
    List<PuntuacionAlternativa> puntuacionesDecisor(String token, String emailDecisor);
}
