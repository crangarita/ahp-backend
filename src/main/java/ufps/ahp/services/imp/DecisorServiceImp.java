package ufps.ahp.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.dao.DecisorDAO;
import ufps.ahp.model.Decisor;
import ufps.ahp.services.DecisorService;

import java.util.List;

@Service
public class DecisorServiceImp implements DecisorService {

    @Autowired
    DecisorDAO decisorDAO;

    @Override
    @Transactional(readOnly = true)

    public List<Decisor> listar() {
        return decisorDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)

    public Decisor buscar(int idDecisor) {
        return decisorDAO.findById(idDecisor).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)

    public Decisor buscarPorEmail(String email) {
        return decisorDAO.findDecisorByEmail(email);
    }

    @Override
    @Transactional

    public void guardar(Decisor decisor) {
        decisorDAO.save(decisor);
    }

    @Override
    @Transactional
    public void eliminar(Decisor decisor) {
        decisorDAO.delete(decisor);
    }
}
