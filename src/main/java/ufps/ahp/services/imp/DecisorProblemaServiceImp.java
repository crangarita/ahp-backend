package ufps.ahp.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.dao.DecisorProblemaDAO;
import ufps.ahp.model.DecisorProblema;
import ufps.ahp.services.DecisorProblemaService;

import java.util.List;

@Service
public class DecisorProblemaServiceImp implements DecisorProblemaService {

    @Autowired
    DecisorProblemaDAO DecisorProblemaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<DecisorProblema> listar() {
        return DecisorProblemaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DecisorProblema buscar(int idDecisorProblema) {
        return DecisorProblemaDAO.findById(idDecisorProblema).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(DecisorProblema ct) {
        DecisorProblemaDAO.save(ct);
    }

    @Override
    @Transactional
    public void eliminar(DecisorProblema a) {
        DecisorProblemaDAO.delete(a);
    }
}
