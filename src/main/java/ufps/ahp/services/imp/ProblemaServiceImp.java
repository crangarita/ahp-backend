package ufps.ahp.services.imp;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.dao.ProblemaDAO;
import ufps.ahp.model.Alternativa;
import ufps.ahp.model.Criterio;
import ufps.ahp.model.Decisor;
import ufps.ahp.model.Problema;
import ufps.ahp.services.ProblemaService;

import java.util.List;
@Service
@Slf4j
public class ProblemaServiceImp implements ProblemaService {

    @Autowired
    ProblemaDAO problemaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Problema> listar() {
        return problemaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeDecisor(String email, String idProblema) {
        return problemaDAO.existeDecisor(email,idProblema)!=null;
    }

    @Override
    @Transactional(readOnly = true)
    public Problema buscar(String idProblema) {
        log.info(idProblema);
        return problemaDAO.getById(idProblema);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Criterio> criteriosPorProblema(String idProblema) {
        return problemaDAO.criteriorPorProblema(idProblema);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Decisor> decisoresPorProblema(String idProblema) {
        return problemaDAO.decisorPorProblema(idProblema);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alternativa> alternativasPorProblema(String idProblema) {
        return null;
    }

    @Override
    @Transactional
    public void guardar(Problema p) {
        problemaDAO.save(p);
    }

    @Override
    @Transactional
    public void eliminar(Problema p) {
        problemaDAO.delete(p);
    }
}
