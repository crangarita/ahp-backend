package ufps.ahp.services.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.dao.AlternativaDAO;
import ufps.ahp.dao.CriterioDAO;
import ufps.ahp.dao.ProblemaDAO;
import ufps.ahp.dao.PuntuacionAlternativaCriterioDAO;
import ufps.ahp.model.Alternativa;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Problema;
import ufps.ahp.model.PuntuacionAlternativaCriterio;
import ufps.ahp.services.PuntuacionAlternativaCriterioServicio;

import java.util.List;
@Service
@Slf4j
class PuntuacionAlternativaCriterioServiceImp implements PuntuacionAlternativaCriterioServicio {

    @Autowired
    PuntuacionAlternativaCriterioDAO PuntuacionAlternativaCriterioDAO;

    @Autowired
    CriterioDAO criterioDAO;

    @Autowired
    AlternativaDAO alternativaDAO;

    @Autowired
    ProblemaDAO problemaDAO;



    @Override
    @Transactional(readOnly = true)
    public List<PuntuacionAlternativaCriterio> listar() {
        return PuntuacionAlternativaCriterioDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PuntuacionAlternativaCriterio buscar(int idPuntuacionAlternativaCriterio) {
        return PuntuacionAlternativaCriterioDAO.getById(idPuntuacionAlternativaCriterio);
    }

    @Override
    @Transactional
    public void llenarPuntuacionAlternativa(int idProblema) {
        List<Criterio> criteriosDeProblema = criterioDAO.criteriosPorProblema(idProblema);
        for(Criterio c: criteriosDeProblema){
            PuntuacionAlternativaCriterioDAO.llenarPuntuacionAlternativa(idProblema,c.getIdCriterio());
        }

    }

    @Override
    public void llenarPuntuacionAlternativaIndividual(int idAlternativa, List<Alternativa> alternativasAgregar) {
        Alternativa alternativa = alternativaDAO.getById(idAlternativa);
        Problema problema = problemaDAO.encontrarProblema(alternativa.getProblema().getToken());
        List<Alternativa> alternativas = (List<Alternativa>) problema.alternativaCollection();
        List<Criterio> criterios = (List<Criterio>) problema.criterioCollection();

        alternativas.addAll(alternativasAgregar);

        for (int j = 0; j<criterios.size(); j++) {
            Criterio ci = criterios.get(j);
            for (int i = 0; i<alternativas.size(); i++) {
                Alternativa ai = alternativas.get(i);
                if(ai.getIdAlternativa()!=idAlternativa){
                    PuntuacionAlternativaCriterioDAO.save(new PuntuacionAlternativaCriterio(ai,alternativa,0,problema, ci));
                }
            }
        }




    }

    @Override
    @Transactional
    public void guardar(PuntuacionAlternativaCriterio p) {
        PuntuacionAlternativaCriterioDAO.save(p);
    }

    @Override
    @Transactional
    public void eliminar(PuntuacionAlternativaCriterio p) {
        PuntuacionAlternativaCriterioDAO.delete(p);
    }
}