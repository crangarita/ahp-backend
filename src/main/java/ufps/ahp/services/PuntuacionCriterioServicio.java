package ufps.ahp.services;

import ufps.ahp.model.Criterio;
import ufps.ahp.model.PuntuacionAlternativaCriterio;
import ufps.ahp.model.PuntuacionCriterio;

import java.util.List;

public interface PuntuacionCriterioServicio {
    public List<PuntuacionCriterio> listar();
    public void agregarCriteriosPuntuacion(int idProblema);
    public void agregarCriteriosPuntuacionIndividual(int criterio, List<Criterio>criterios);
    public List<PuntuacionCriterio> obtenerParesCriterios(String idProblema);
    public List<PuntuacionAlternativaCriterio> obtenerParesAlternativa(String idProblema);
    public PuntuacionCriterio buscar(int idPuntuacionCriterio);
    public void guardar(PuntuacionCriterio ct);
    public void eliminar(PuntuacionCriterio a);
}
