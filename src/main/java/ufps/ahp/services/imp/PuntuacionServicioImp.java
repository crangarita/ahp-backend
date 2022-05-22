package ufps.ahp.services.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufps.ahp.dao.PuntuacionDAO;
import ufps.ahp.model.*;
import ufps.ahp.negocio.PlayGround;
import ufps.ahp.services.PuntuacionServicio;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class PuntuacionServicioImp implements PuntuacionServicio {

    @Autowired
    PuntuacionDAO puntuacionDAO;

    @Autowired
    PlayGround playGround;


    @Override
    public List<Puntuacion> listar() {
        return puntuacionDAO.findAll();
    }

    @Override
    public List<Puntuacion> puntuacionDecisor(String emailDecisor, String token) {
        return puntuacionDAO.puntuacionesDeUsuario(emailDecisor,token);
    }

    @Override
    public Puntuacion buscar(int idPuntuacion) {
        return puntuacionDAO.findById(idPuntuacion).orElse(null);
    }

    @Override
    public void guardar(Puntuacion ct) {
        puntuacionDAO.save(ct);
    }

    @Override
    public void eliminar(Puntuacion a) {
        puntuacionDAO.delete(a);
    }

    @Override
    public List<Object> calcularMatriz(String emailDecisor, String token, Problema p) {

        List<Object> resultados = new ArrayList<>();

        List<Puntuacion> puntuacionesDeProblema = puntuacionDAO.puntuacionesDeUsuario(emailDecisor,token);
        List<Criterio> criterios = (List<Criterio>) p.criterioCollection();

        int totalCriterios =p.criterioCollection().size()+1;

        Object[][] matrizPareada = new Object[totalCriterios][totalCriterios];
        float[][] matrizPareadaDefault = new float[totalCriterios-1][totalCriterios-1];

        for (int i = 0; i < matrizPareada.length; i++) {

            if(i==0){
                matrizPareada[0][0] = "";
            }else{
                Criterio nombreCriterio = criterios.get(i-1);
                matrizPareada[0][i] = nombreCriterio;
                matrizPareada[i][0] = nombreCriterio;
            }

        }

        for (int i = 1; i < totalCriterios; i++) {
            for (int j = 1; j < totalCriterios; j++) {
                if(i == j){
                    matrizPareada[i][j] = 1;
                    matrizPareadaDefault[i-1][j-1]=1;
                }else{
                    if(j>i){
                        Criterio criterio1 = (Criterio) matrizPareada[i][0];
                        Criterio criterio2 = (Criterio) matrizPareada[0][j];
                        Puntuacion puntuacion = buscarPuntuacion(puntuacionesDeProblema, criterio1,criterio2);

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

        for (int i = 0; i < prioridades.length; i++) {
            prioridadesCompletas[i][0]= matrizPareada[i+1][0];
            prioridadesCompletas[i][1]=prioridades[i];
        }
        resultados.add(matrizPareada);
        resultados.add(matrizPareadaDefault);
        resultados.add(prioridadesCompletas);
        resultados.add(resultados2.get(1));

        return resultados;
    }

    @Override
    public List<Object> totalizarMatriz(String token, Problema p) {
        List<DecisorProblema> decisores =(List<DecisorProblema>)(p.decisorProblemas());

        List<Object> resultados = new ArrayList<>();
        List<Criterio> criterios = (List<Criterio>) p.criterioCollection();
        int totalCriterios =p.criterioCollection().size()+1;

        Object[][] matrizPareada = new Object[totalCriterios][totalCriterios]; // Matriz de tipo object para guardar tanto string (como los nombres de los criterios) y float de los valores de la matriz
        float[][] matrizPareadaDefault = new float[totalCriterios-1][totalCriterios-1]; // Matriz unicamente numerica de punto flotante, le resto -1 porque esta no tiene la fila ni columna de nomrbes de criterios

        //Se llena la matriz con los nombres de los criterios
        for (int i = 0; i < matrizPareada.length; i++) {

            if(i==0){
                matrizPareada[0][0] = "";
            }else{
                Criterio nombreCriterio = criterios.get(i-1);
                matrizPareada[0][i] = nombreCriterio;
                matrizPareada[i][0] = nombreCriterio;
            }

        }

        // Recorro por cada decisor las puntuaciones realizadas para totalizar en una sola matriz pareada
        for(int x = 0; x < decisores.size(); x++) {

            Decisor decisor = decisores.get(x).getDecisor();

            List<Puntuacion> puntuacionesDeProblema = puntuacionDAO.puntuacionesDeUsuario(decisor.getEmail(),token);
            for (int i = 1; i < totalCriterios; i++) {
                for (int j = 1; j < totalCriterios; j++) {
                    if(i == j){
                        matrizPareada[i][j] = 1;
                        matrizPareadaDefault[i-1][j-1]=1;
                    }else{
                        if(j>i){
                            Criterio criterio1 = (Criterio) matrizPareada[i][0];
                            Criterio criterio2 = (Criterio) matrizPareada[0][j];
                            Puntuacion puntuacion = buscarPuntuacion(puntuacionesDeProblema, criterio1,criterio2);

                            matrizPareada[i][j]=puntuacion.getValor();
                            matrizPareadaDefault[i-1][j-1]+=puntuacion.getValor();
                            if(puntuacion.getValor()<1){
                                matrizPareada[j][i]=(float)1/puntuacion.getValor();
                                matrizPareadaDefault[j-1][i-1]+=(float)1/puntuacion.getValor();
                            }else{
                                matrizPareada[j][i]=1+"/"+puntuacion.getValor();
                                matrizPareadaDefault[j-1][i-1]+=(float)1/puntuacion.getValor();
                            }

                        }else{
                            matrizPareada[i][j]+="";

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

        resultados.add(matrizPareadaDefault);
        resultados.add(prioridadesCompletas);
        resultados.add(resultados2.get(1));

        return resultados;
    }

    @Override
    public List<Object> vectorPropio(String tokenProblema) {

        return null;
    }


    public Puntuacion buscarPuntuacion(List<Puntuacion> puntuaciones, Criterio c1, Criterio c2){

        for (int i = 0; i < puntuaciones.size(); i++) {
            Criterio criterio1 = puntuaciones.get(i).getPuntuacionCriterio().getCriterio1Id();
            Criterio criterio2 = puntuaciones.get(i).getPuntuacionCriterio().getCriterio2Id();

            if(criterio1.getIdCriterio()==c1.getIdCriterio() && criterio2.getIdCriterio() == c2.getIdCriterio()
                    ||
               criterio1.getIdCriterio()==c2.getIdCriterio() && criterio1.getIdCriterio() == c1.getIdCriterio()
            ){
                return puntuaciones.get(i);
            }

        }
    return null;
    }
}
