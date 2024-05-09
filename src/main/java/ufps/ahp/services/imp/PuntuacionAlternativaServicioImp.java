package ufps.ahp.services.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufps.ahp.dao.PuntuacionAlternativaCriterioDAO;
import ufps.ahp.dao.PuntuacionAlternativaDAO;
import ufps.ahp.dao.PuntuacionDAO;
import ufps.ahp.model.*;
import ufps.ahp.negocio.PlayGround;
import ufps.ahp.services.DecisorService;
import ufps.ahp.services.PuntuacionAlternativaCriterioServicio;
import ufps.ahp.services.PuntuacionAlternativaServicio;
import ufps.ahp.services.PuntuacionServicio;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class PuntuacionAlternativaServicioImp implements PuntuacionAlternativaServicio {

    @Autowired
    PuntuacionAlternativaDAO puntuacionAlternativaDAO;
    
    @Autowired
    PuntuacionDAO puntuacionDAO;

    @Autowired
    PuntuacionServicio puntuacionServicio;

    @Autowired
    PlayGround playGround;

    @Autowired
    DecisorService decisorService;

    @Override
    @Transactional(readOnly = true)
    public List<PuntuacionAlternativa> listar() {
        return puntuacionAlternativaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PuntuacionAlternativa buscar(int idPuntuacionAlternativa) {
        return puntuacionAlternativaDAO.findById(idPuntuacionAlternativa).orElse(null);
    }

    @Override
    public PuntuacionAlternativa buscarPuntuacionDecisorProblema(int idPuntuacionAlternativa, String email) {
        return puntuacionAlternativaDAO.puntuacionUsuario(email,idPuntuacionAlternativa);
    }

    @Override
    public List<Object> calcularMatriz(String emailDecisor, String token, Problema p) {

        List<Object> resultados = new ArrayList<>();
        Criterio idCriterioMasImportante = (Criterio)(puntuacionServicio.calcularMatriz(emailDecisor,token,p).get(4));

        List<PuntuacionAlternativa> puntuacionesDeProblema =
                puntuacionAlternativaDAO.puntuacionesAlternativasDeCriterioMayor(
                        emailDecisor,token,idCriterioMasImportante.getIdCriterio()); //Obtiene los puntajes
        List<Criterio> criterios = (List<Criterio>) p.criterioCollection();
        List<Alternativa>alternativas  = (List<Alternativa>) p.alternativaCollection();

        int totalAlternativas =p.alternativaCollection().size()+1;

        Object[][] matrizPareada = new Object[totalAlternativas][totalAlternativas];
        float[][] matrizPareadaDefault = new float[totalAlternativas-1][totalAlternativas-1];

        for (int i = 0; i < matrizPareada.length; i++) {
            if(i==0){
                matrizPareada[0][0] = "";
            }else{
                Alternativa nombreAlternativa = alternativas.get(i-1);
                matrizPareada[0][i] = nombreAlternativa;
                matrizPareada[i][0] = nombreAlternativa;
            }

        }

        for (int i = 1; i < totalAlternativas; i++) {
            for (int j = 1; j < totalAlternativas; j++) {
                if(i == j){
                    matrizPareada[i][j] = 1;
                    matrizPareadaDefault[i-1][j-1]=1;
                }else{
                    if(j>i){
                        Alternativa alternativa1 = (Alternativa) matrizPareada[i][0];
                        Alternativa alternativa2 = (Alternativa) matrizPareada[0][j];
                        PuntuacionAlternativa puntuacion = buscarPuntuacion(puntuacionesDeProblema, alternativa1,alternativa2);
                        matrizPareada[i][j]=puntuacion.getValor();
                        matrizPareadaDefault[i-1][j-1]=puntuacion.getValor();
                        if(puntuacion.getValor()<1){
                            matrizPareada[j][i]=(float)1/puntuacion.getValor();
                            matrizPareadaDefault[j-1][i-1]=(float)1/puntuacion.getValor();
                        }else{
                            matrizPareada[j][i]=1+"/"+puntuacion.getValor();
                            matrizPareadaDefault[j-1][i-1]=(float)1/puntuacion.getValor();
                        }

                    }else{
                        matrizPareada[i][j]+="";

                    }
                }
            }

        }

        List<Object> resultados2 = playGround.calculoDePesos(matrizPareadaDefault);
        float[]prioridades = (float[]) resultados2.get(0);

        Object[][]prioridadesCompletas = new Object[prioridades.length][2];

        float mayor = prioridades[0];
        Alternativa ganador = null;
        ganador = (Alternativa) matrizPareada[1][0];

        for (int i = 0; i < prioridades.length; i++) {
            if(mayor<prioridades[i]){
                mayor = prioridades[i];
                ganador= (Alternativa) matrizPareada[i+1][0];
            }
        }

        for (int i = 0; i < prioridades.length; i++) {
            prioridadesCompletas[i][0]= matrizPareada[i+1][0];
            prioridadesCompletas[i][1]=prioridades[i];
        }
        resultados.add(matrizPareada);
        resultados.add(matrizPareadaDefault);
        resultados.add(prioridadesCompletas);
        resultados.add(resultados2.get(1));
        resultados.add(decisorService.buscarPorEmail(emailDecisor));
        resultados.add(ganador);


        return resultados;
    }

    @Override
    public List<Object> totalizarMatriz(String token, Problema p) {

        if(this.puntuacionAlternativaDAO.puntuacionesDeProblema(p.getToken()).size()==0){
            return null;
        }

        List<Object> resultados = new ArrayList<>();
        List<Criterio> criterios = (List<Criterio>) p.criterioCollection();
        List<Alternativa>alternativas  = (List<Alternativa>) p.alternativaCollection();
        
        List<DecisorProblema> decisoresQ =(List<DecisorProblema>)(p.decisorProblemas());
        
        List<DecisorProblema> decisores =new ArrayList();
        
        
        for(int x = 0; x < decisoresQ.size(); x++) {
            Decisor decisor = decisoresQ.get(x).getDecisor();
            String emailDecisor = decisor.getEmail();
            
            List<Puntuacion> puntuaciones = puntuacionDAO.puntuacionesDeUsuario(emailDecisor, token);
            
            if (!puntuaciones.isEmpty()) {
            	decisores.add(decisoresQ.get(x));
            }

        } 

        int totalAlternativas =p.alternativaCollection().size()+1;

        Object[][] matrizPareada = new Object[totalAlternativas][totalAlternativas];
        float[][] matrizPareadaDefault = new float[totalAlternativas-1][totalAlternativas-1];

        for (int i = 0; i < matrizPareada.length; i++) {
            if(i==0){
                matrizPareada[0][0] = "";
            }else{
                Alternativa nombreAlternativa = alternativas.get(i-1);
                matrizPareada[0][i] = nombreAlternativa;
                matrizPareada[i][0] = nombreAlternativa;
            }

        }

        List<Object> estadisticasDecisor = new ArrayList<>();


        for(int x = 0; x < decisores.size(); x++) {
            Decisor decisor = decisores.get(x).getDecisor();
            String emailDecisor = decisor.getEmail();
            Criterio idCriterioMasImportante = (Criterio) (puntuacionServicio.calcularMatriz(emailDecisor, token, p).get(4));


            List<PuntuacionAlternativa> puntuacionesDeProblema =
                    puntuacionAlternativaDAO.puntuacionesAlternativasDeCriterioMayor(
                            emailDecisor, token, idCriterioMasImportante.getIdCriterio()); //Obtiene los puntajes

            if (puntuacionesDeProblema.size() > 0) {
            estadisticasDecisor.add(this.calcularMatriz(decisor.getEmail(), p.getToken(), p)); // pos [2] // pos [5] info decisor
            for (int i = 1; i < totalAlternativas; i++) {
                for (int j = 1; j < totalAlternativas; j++) {
                    if (i == j) {
                        matrizPareada[i][j] = 1;
                        matrizPareadaDefault[i - 1][j - 1] = 1;
                    } else {
                        if (j > i) {
                            Alternativa alternativa1 = (Alternativa) matrizPareada[i][0];
                            Alternativa alternativa2 = (Alternativa) matrizPareada[0][j];

                            PuntuacionAlternativa puntuacion = buscarPuntuacion(puntuacionesDeProblema, alternativa1, alternativa2);

                            matrizPareadaDefault[i - 1][j - 1] += puntuacion.getValor();
                            matrizPareada[i][j] = matrizPareadaDefault[i - 1][j - 1];
                            if (puntuacion.getValor() < 1) {
                                matrizPareadaDefault[j - 1][i - 1] += (float) 1 / puntuacion.getValor();
                                matrizPareada[j][i] = matrizPareadaDefault[j - 1][i - 1];
                            } else {
                                matrizPareadaDefault[j - 1][i - 1] += (float) 1 / puntuacion.getValor();
                                matrizPareada[j][i] = matrizPareadaDefault[j - 1][i - 1];
                            }

                        } else {
                            matrizPareada[i][j] += "";

                        }
                    }
                }

            }

        }
        }
        List<Object> resultados2 = playGround.calculoDePesos(matrizPareadaDefault);
        float[]prioridades = (float[]) resultados2.get(0);

        Object[][]prioridadesCompletas = new Object[prioridades.length][2];


        for (int i = 0; i < prioridades.length; i++) {
            prioridadesCompletas[i][0]= matrizPareada[i+1][0];
            prioridadesCompletas[i][1]=prioridades[i];
        }
        float mayor = prioridades[0];
        Alternativa ganador = null;
        ganador = (Alternativa) matrizPareada[1][0];
        for (int i = 0; i < prioridades.length; i++) {
            if(mayor<prioridades[i]){
                mayor = prioridades[i];
                ganador= (Alternativa) matrizPareada[i+1][0];
            }
        }

        resultados.add(matrizPareada);
        resultados.add(matrizPareadaDefault);
        resultados.add(prioridadesCompletas);
        resultados.add(resultados2.get(1));
        resultados.add(ganador);
        resultados.add(estadisticasDecisor);

        return resultados;
    }

    @Override
    @Transactional
    public void guardar(PuntuacionAlternativa ct) {
        puntuacionAlternativaDAO.save(ct);
    }

    @Override
    @Transactional
    public void eliminar(PuntuacionAlternativa a) {
        puntuacionAlternativaDAO.delete(a);
    }

    @Override
    public List<PuntuacionAlternativa> puntuacionesDecisor(String token, String emailDecisor) {
        return puntuacionAlternativaDAO.puntuacionesDeUsuario(emailDecisor,token);
    }

    public PuntuacionAlternativa buscarPuntuacion(List<PuntuacionAlternativa> puntuaciones, Alternativa c1, Alternativa c2){

        for (int i = 0; i < puntuaciones.size(); i++) {
            Alternativa alternativa1 = puntuaciones.get(i).getPuntuacionAlternativaCriterio().getAlternativa1();
            Alternativa alternativa2 = puntuaciones.get(i).getPuntuacionAlternativaCriterio().getAlternativa2();
            if(alternativa1.getIdAlternativa()==c1.getIdAlternativa() && alternativa2.getIdAlternativa()== c2.getIdAlternativa()
                    ||
                    alternativa1.getIdAlternativa()==c2.getIdAlternativa() && alternativa2.getIdAlternativa() == c1.getIdAlternativa()
            ){
                return puntuaciones.get(i);
            }

        }
        return null;
    }
}