package ufps.ahp.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ufps.ahp.dao.PuntuacionDAO;
import ufps.ahp.services.PuntuacionServicio;

import java.util.*;

@Component
public class PlayGround {

//    Calculo de peso de prioridades ====================================
    public  List<Object> calculoDePesos(float[][]matrizA){
        int x = matrizA.length;
        List<Object> procesoYMatriz = new ArrayList<>();

        List<Object> resultados = multiplicarMatrices(matrizA,matrizA);
        String proceso = "=============== CALCULO DE PESOS ===============" +
                "\nMatriz A: "+mostrarMatriz(matrizA);
        proceso+="\n"+"";

        float[][]matrizMultiplicada = (float[][])resultados.get(1) ;

        List<float[]> vectoresPropios = new ArrayList<>();
        int i = 0;
        while (true){
            proceso+="\n\n ITERACIÓN: ["+((i+1)+"]");
            proceso+="\n\n"+"Matriz AxA: "+mostrarMatriz(matrizMultiplicada);
            List<Object> resultadosVector = calcularVectorNormal(matrizMultiplicada);
            float []vectorNormal = (float[]) resultadosVector.get(1);
            float total = (float) resultadosVector.get(0);
            proceso+="\n\n VECTOR NORMAL:\n"+mostrarVector(vectorNormal);
            proceso+="\n"+"TOTAL: "+total;

            proceso+="\n"+"VECTOR PROPIO:";
            float[]vectorPropio = calcularVectorPropio(vectorNormal,total);
            proceso+="\n"+mostrarVector(vectorPropio);
            vectoresPropios.add(vectorPropio);

            if(i>0){
                float[]anterior = vectoresPropios.get(i-1);
                if(Arrays.equals(vectorPropio, anterior)){
                    proceso+="\n"+"**************************************";
                    proceso+="\n"+"RESULTADO FINAL:\n"+mostrarVector(vectorPropio);
                    procesoYMatriz.add(vectorPropio);
                    break;
                }
            }
            i++;
            matrizMultiplicada = (float[][]) multiplicarMatrices(matrizMultiplicada,matrizMultiplicada).get(1);
            proceso+="\n"+"=================================================";
        }

        procesoYMatriz.add(proceso);

        return  procesoYMatriz;
    }
    public  float[] calcularVectorPropio(float[]vector, float total){

        for (int i = 0; i < vector.length; i++) {
            vector[i] /=total;
            vector[i] = (float) redondearDecimales(vector[i],3);
        }
        return vector;
    }
    public  List<Object> calcularVectorNormal(float[][]matriz){
        List<Object> totalYVector = new ArrayList<>();

        int x = matriz.length;
        float resultado = 0;
        float []vectorNormal = new float[x];

        for (int i = 0; i < x; i++) {
            float total = 0;
            for (int j = 0; j < x; j++) {
                total+=matriz[i][j];
            }
            vectorNormal[i]= (float) redondearDecimales(total,3);
        }

        for (int i = 0; i < vectorNormal.length; i++) {
            resultado+=vectorNormal[i];
        }

        totalYVector.add(resultado);
        totalYVector.add(vectorNormal);

    return totalYVector;
    }
    public  List<Object> multiplicarMatrices(float[][]matrizA, float[][]matrizB){
        List<Object> procesoYMatriz = new ArrayList<>();
        int x = matrizA.length;
        int y = matrizB.length;

        String proceso ="";

        float[][] axb = new float[x][y];
        float total=0f;

        for (int i=0; i < x ; i++) {
            for (int k = 0, contador=0, z=0; contador <= x*x; k++, contador++) {

                if((k)%x==0){
                    axb[i][z]=total;
                    if(k>0){
                        proceso+="TOTAL:"+total+"\n=============================\n";
                        z++;
                    }
                    k=0;
                    total=0;
                    if(contador==x*x)
                        break;
                }
                float valor1 = matrizA[i][k];
                float valor2 = matrizB[k][z];
                total+=valor1*valor2;
                proceso += valor1+" * "+valor2+" = "+valor1*valor2+"\n";


            }
        }

        procesoYMatriz.add(proceso);
        procesoYMatriz.add(axb);

        return procesoYMatriz;
    }
    public  List<Object> multiplicarMatrizXVector(float[][]matrizA, float[]matrizB){
        List<Object> procesoYMatriz = new ArrayList<>();
        int x = matrizA.length;
        int y = matrizB.length;

        String proceso ="";

        float[] axb = new float[x];
        float total=0f;

        for (int i=0; i < x ; i++) {
            for (int k = 0, contador=0;k<y&&contador<y*y; k++, contador++) {

                if((k%y==0)){
                    k=0;
                    total=0;
                }

                float valor1 = matrizA[i][k];
                float valor2 = matrizB[k];
                total+=valor1*valor2;
                proceso += valor1+" * "+valor2+" = "+valor1*valor2+"\n";
                if(k+1 == y){
                    axb[i]=total;
                }

            }
        }

        procesoYMatriz.add(proceso);
        procesoYMatriz.add(axb);

        return procesoYMatriz;
    }
    public  String mostrarMatriz(float[][]matriz){
        String resultado = "";
        for (int i = 0; i < matriz.length; i++) {
            resultado+="\n";
            for (int j = 0; j < matriz.length; j++) {
                resultado+=("["+matriz[i][j]+"] ");
            }
        }
        return resultado;
    }
    public  String mostrarVector(float[]vector){
        String resultado = "";
        for (int i = 0; i < vector.length; i++) {
                resultado+=("["+vector[i]+"] "+"\n");
        }
        return resultado;
    }
    public  double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }

//    Calculo de consistencia ====================================
    public  List<Object> calculoDeConsistencia(float[][]matrizA){
        int x = matrizA.length;

        List<Object> procesoYConsistencia = new ArrayList<>();
        String proceso = "============== CALCULO DE CONSISTENCIA============== " +
                "\nMatriz A: \n"+mostrarMatriz(matrizA);

        float[][]matrizNormalizada = normalizacionMatriz(matrizA);
        proceso+="\n\nNormalización de matriz:\n"+mostrarMatriz(matrizNormalizada);
        List<float[]>vectorSumapromedio = calculoVectorSumaPromedio(matrizNormalizada);

        float []vectorSumaFilas = vectorSumapromedio.get(0);
        float []vectorPromedio = vectorSumapromedio.get(1);

        float []vectorFilaTotal = (float[]) multiplicarMatrizXVector(matrizA,vectorPromedio).get(1);
        float []vectorCociente = calcularVectorCociente(vectorFilaTotal,vectorPromedio);
        float promedio = calcularPromedio(vectorCociente);
        float ci = (promedio-x)/2;
        float ica = (1.98f *(x-2))/x; // indice de consistencia aleatoria x=cantidad de criterios
        float radioConsistencia = ci/ica;

        proceso+="\n\n Vector Suma filas: \n"+mostrarVector(vectorSumaFilas);
        proceso+="\n\n Vector Promedio: \n"+mostrarVector(vectorPromedio);
        proceso+="\n\n Vector Fila total: \n"+mostrarVector(vectorFilaTotal);
        proceso+="\n\n Vector cociente:\n\n"+mostrarVector(vectorCociente);
        proceso+="\n\n λ Max:"+promedio;
        proceso+="\n\n INDICE DE CONSISTENCIA: ["+ci+"]";
        proceso+="\n\n RADIO DE CONSISTENCIA: ["+radioConsistencia+"]" ;
        if(radioConsistencia<=0.10f)
            proceso+="\n\n ✔✔✔ CONSISTENCIA RAZONABLE ✔✔✔";
        if(radioConsistencia>0.10f)
            proceso+="\n\nXXXX PRESENTA INCONSISTENCIA XXXX";
        proceso+="\n\n =================================================";

        procesoYConsistencia.add(proceso);
        procesoYConsistencia.add(radioConsistencia);
        return  procesoYConsistencia;
    }
    public  float[][]normalizacionMatriz(float[][]matriz){
        int x = matriz.length;
        float[]sumas = new float[x];
        float[][]matrizNormalizada=new float[x][x];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                matrizNormalizada[i][j]=matriz[i][j];
            }
        }

        for (int i = 0; i < x; i++) {
            float sumaVector = 0;
            for (int j = 0; j < x; j++) {
                sumaVector+=matriz[j][i];
            }
            sumas[i]= (float) redondearDecimales(sumaVector,4);
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                matrizNormalizada[j][i] /=sumas[i];
            }
        }
        return matrizNormalizada;
    }
    public  List<float[]> calculoVectorSumaPromedio(float[][]matriz){
        int x = matriz.length;
        List<float[]> vectorSumaPromedio = new ArrayList<>();

        float[]vectorSumas = new float[x];
        float[]vectorPromedios = new float[x];

        for (int i = 0; i < x; i++) {
            float total=0;
            for (int j = 0; j < x; j++) {
                total+=matriz[i][j];
            }
            vectorSumas[i]=total;
            vectorPromedios[i] = vectorSumas[i]/x;

        }
        vectorSumaPromedio.add(vectorSumas);
        vectorSumaPromedio.add(vectorPromedios);


        return vectorSumaPromedio;
    }
    public  float[]calcularVectorCociente(float[]vectorSumaFilas, float[]vectorPromedio){
        float[]vectorCociente = new float[vectorPromedio.length];
        for (int i = 0; i < vectorPromedio.length; i++) {
            float v1=vectorSumaFilas[i];
            float v2=vectorPromedio[i];
            vectorCociente[i]=(float)(v1)/(v2);
        }
        return vectorCociente;
    }
    private  float calcularPromedio(float[] vector) {
        float total=0;

        for (int i = 0; i < vector.length; i++) {
            total+=vector[i];
        }
        return total/vector.length;
    }

    private  String agregarEspacios(int total){
        String totalEspacios = "";

        for (int i = 0; i < total; i++) {
            totalEspacios+=" ";
        }
        return totalEspacios;
    }

//    public  void main(String[] args) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR,1);
//        Date manana = calendar.getTime();
////        System.out.println(manana);
////        System.out.println(manana.before(new Date()));
////        int x = 3;
//////        int y = 3;
//////        float[][] matrizA = new float[x][y];
//////
//////        matrizA[0][0]= 1;
//////        matrizA[0][1]= 4;
//////        matrizA[0][2]= 5;
//////
//////        matrizA[1][0]= 0.25f;
//////        matrizA[1][1]= 1;
//////        matrizA[1][2]= 2;
//////
//////        matrizA[2][0]= 0.2f;
//////        matrizA[2][1]= 0.5f;
//////        matrizA[2][2]= 1;
//////
//////        List<Object> resultados = calculoDeConsistencia(matrizA);
//////
//////        System.out.println(resultados.get(0));
//////
//////        List<Object> resultados2 = calculoDePesos(matrizA);
//////
//////        System.out.println(resultados2.get(1));
//
//        int totalCriterios =6;
//
//        Object[][] matrizPareada = new Object[totalCriterios+1][totalCriterios+1];
//
//        for (int i = 0; i < matrizPareada.length; i++) {
//
//            if(i==0){
//                matrizPareada[0][0] = "";
//            }else{
//
//                matrizPareada[0][i] = agregarEspacios(8)+"C"+i;
//                matrizPareada[i][0] = "C"+i;
//            }
//
//        }
//
//        for (int i = 1; i < totalCriterios+1; i++) {
//            for (int j = 1; j < totalCriterios+1; j++) {
//                if(i == j){
//                    matrizPareada[i][j] = 1;
//                }else{
//                    if(j>i){
//                        System.out.println(matrizPareada[0][j]+"----"+matrizPareada[i][0]);
//                        matrizPareada[i][j]="X";
//
//                    }else{
//                        matrizPareada[i][j]="#";
//
//                    }
//                }
//            }
//
//        }
//
//
//        for (int i = 0; i < totalCriterios+1; i++) {
//            System.out.println("");
//            for (int j = 0; j < totalCriterios+1; j++) {
//                System.out.print(matrizPareada[i][j].toString()+" ");
//            }
//        }
//
//
//        System.out.println();
//
//
//
//    }
}
