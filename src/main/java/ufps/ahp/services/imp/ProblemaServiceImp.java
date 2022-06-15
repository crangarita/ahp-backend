package ufps.ahp.services.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.dao.ProblemaDAO;
import ufps.ahp.model.*;
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
    public Decisor existeDecisor(String email, String token) {
        return problemaDAO.existeDecisor(email, token);
    }

    @Override
    @Transactional(readOnly = true)
    public Problema buscar(String token) {
        return problemaDAO.encontrarProblema(token);
    }

    @Override
    @Transactional(readOnly = true)
    public Problema buscarPorId(int id) {
        return problemaDAO.findProblemaByIdProblema(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Criterio> criteriosPorProblema(String token) {
        return problemaDAO.criteriorPorProblema(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Decisor> decisoresPorProblema(String token) {
        return problemaDAO.decisorPorProblema(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alternativa> alternativasPorProblema(String token) {
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
