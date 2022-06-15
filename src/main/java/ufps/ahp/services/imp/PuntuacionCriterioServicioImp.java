package ufps.ahp.services.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.dao.CriterioDAO;
import ufps.ahp.dao.ProblemaDAO;
import ufps.ahp.dao.PuntuacionAlternativaCriterioDAO;
import ufps.ahp.dao.PuntuacionCriterioDAO;
import ufps.ahp.model.*;
import ufps.ahp.services.PuntuacionAlternativaCriterioServicio;
import ufps.ahp.services.PuntuacionCriterioServicio;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class PuntuacionCriterioServicioImp implements PuntuacionCriterioServicio {

    @Autowired
    PuntuacionCriterioDAO puntuacionCriterioDAO;

    @Autowired
    PuntuacionAlternativaCriterioDAO puntuacionAlternativaCriterioDAO;

    @Autowired
    CriterioDAO criterioDAO;

    @Autowired
    ProblemaDAO problemaDAO;


    @Override
    @Transactional(readOnly = true)
    public List<PuntuacionCriterio> listar() {
        return puntuacionCriterioDAO.findAll();
    }

    @Override
    @Transactional
    public void agregarCriteriosPuntuacion(int idProblema) {
        puntuacionCriterioDAO.llenarPuntuacionCriterio(idProblema);
    }

    @Override
    @Transactional
    public void agregarCriteriosPuntuacionIndividual(int criterio, List<Criterio> criterios1) {
        Criterio criterio1 = criterioDAO.getById(criterio);
        Problema problema = problemaDAO.encontrarProblema(criterio1.problema().getToken());

        List<Criterio> criterios = (List<Criterio>) problema.criterioCollection();
        List<PuntuacionCriterio> puntuacionCriterios =puntuacionCriterioDAO.obtenerPares(problema.getToken());
        criterios.addAll(criterios1);

        for (PuntuacionCriterio puntuacionCriterio: puntuacionCriterios) {
            log.info(puntuacionCriterio.getIdPuntuacionDecisor()+" puntuacionCrit");
        }
        log.info("===============");

        for (int i = 0; i<criterios.size(); i++) {
            Criterio crit = criterios.get(i);
            if(crit.getIdCriterio()!=criterio1.getIdCriterio() && !noEstan(crit.getIdCriterio(),criterio1.getIdCriterio(), puntuacionCriterios)){
                puntuacionCriterioDAO.save(new PuntuacionCriterio(crit,criterio1,0,problema));
            }
        }
    }

    private boolean noEstan(Integer idCriterio, Integer idCriterio1, List<PuntuacionCriterio> criterios) {
        for (int i = 0; i < criterios.size(); i++) {
            PuntuacionCriterio puntuacionCriterio = criterios.get(i);
            int c1 = puntuacionCriterio.getCriterio1Id().getIdCriterio();
            int c2 = puntuacionCriterio.getCriterio2Id().getIdCriterio();

            if(idCriterio==c1 && idCriterio1==c2 || idCriterio==c2 && idCriterio1==c1){
                return true;
            }
        }
    return false;
    }


    @Override
    @Transactional(readOnly = true)

    public List<PuntuacionCriterio> obtenerParesCriterios(String idProblema) {
        return puntuacionCriterioDAO.obtenerPares(idProblema);
    }

    @Override
    public List<PuntuacionAlternativaCriterio> obtenerParesAlternativa(String idProblema) {
        return puntuacionCriterioDAO.obtenerParesAlternativas(idProblema);
    }

    @Override
    @Transactional(readOnly = true)
    public PuntuacionCriterio buscar(int idPuntuacionCriterio) {
        return puntuacionCriterioDAO.findById(idPuntuacionCriterio).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(PuntuacionCriterio ct) {
        puntuacionCriterioDAO.save(ct);
    }

    @Override
    @Transactional
    public void eliminar(PuntuacionCriterio a) {
        puntuacionCriterioDAO.delete(a);
    }
}
