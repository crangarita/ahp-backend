package ufps.ahp.negocio;

import java.util.*;

public class PlayGround {

//    Calculo de peso de prioridades ====================================
    public static List<Object> calculoDePesos(float[][]matrizA){
        int x = matrizA.length;
        List<Object> procesoYMatriz = new ArrayList<>();

        List<Object> resultados = multiplicarMatrices(matrizA,matrizA);
        String proceso = "Matriz A: "+mostrarMatriz(matrizA);
        proceso+="\n"+"=================================================";

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

            if(i>0 || i>50){
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
    public static float[] calcularVectorPropio(float[]vector, float total){

        for (int i = 0; i < vector.length; i++) {
            vector[i] /=total;
            vector[i] = (float) redondearDecimales(vector[i],3);
        }
        return vector;
    }
    public static List<Object> calcularVectorNormal(float[][]matriz){
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
    public static List<Object> multiplicarMatrices(float[][]matrizA, float[][]matrizB){
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
    public static List<Object> multiplicarMatrizXVector(float[][]matrizA, float[]matrizB){
        List<Object> procesoYMatriz = new ArrayList<>();
        int x = matrizA.length;
        int y = matrizB.length;

        String proceso ="";

        float[][] axb = new float[x][y];
        float total=0f;

        for (int i=0; i < x ; i++) {
            for (int k = 0, contador=0;k<y; k++, contador++) {
                if(k%y==0){

                    axb[i][k]=total;
                    k=0;
                    total=0;
                }

                float valor1 = matrizA[i][k];
                float valor2 = matrizB[k];
                total+=valor1*valor2;
                proceso += valor1+" * "+valor2+" = "+valor1*valor2+"\n";


            }
        }

        procesoYMatriz.add(proceso);
        procesoYMatriz.add(axb);

        return procesoYMatriz;
    }
    public static String mostrarMatriz(float[][]matriz){
        String resultado = "";
        for (int i = 0; i < matriz.length; i++) {
            resultado+="\n";
            for (int j = 0; j < matriz.length; j++) {
                resultado+=("["+matriz[i][j]+"] ");
            }
        }
        return resultado;
    }
    public static String mostrarVector(float[]vector){
        String resultado = "";
        for (int i = 0; i < vector.length; i++) {
                resultado+=("["+vector[i]+"] "+"\n");
        }
        return resultado;
    }
    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }

//    Calculo de consistencia ====================================
    public static List<Object> calculoDeConsistencia(float[][]matrizA){
        int x = matrizA.length;
        List<Object> procesoYMatriz = new ArrayList<>();
        String proceso = "Matriz A: \n"+mostrarMatriz(matrizA);

        proceso+="\n\nNormalización de matriz:\n"+mostrarMatriz(normalizacionMatriz(matrizA));

//        proceso+="\n Vector suma filas:\n"+mostrarVector();

        procesoYMatriz.add(proceso);
        return  procesoYMatriz;
    }

    public static float[][]normalizacionMatriz(float[][]matriz){
        int x = matriz.length;
        float[]sumas = new float[x];

        for (int i = 0; i < x; i++) {
            float sumaVector = 0;
            for (int j = 0; j < x; j++) {
                sumaVector+=matriz[j][i];
            }
            sumas[i]= (float) redondearDecimales(sumaVector,4);
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                matriz[j][i] /=sumas[i];
            }
        }
        return matriz;
    }
    public static List<float[]> calculoVectorSumaPromedio(float[][]matriz){
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

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        Date manana = calendar.getTime();
//        System.out.println(manana);
//        System.out.println(manana.before(new Date()));
        int x = 3;
        int y = 3;
        float[][] matrizA = new float[x][y];

        matrizA[0][0]= 1;
        matrizA[0][1]= 4;
        matrizA[0][2]= 5;

        matrizA[1][0]= 0.25f;
        matrizA[1][1]= 1;
        matrizA[1][2]= 2;

        matrizA[2][0]= 0.2f;
        matrizA[2][1]= 0.5f;
        matrizA[2][2]= 1;
//        matrizA[0][0]= 1;
//        matrizA[0][1]= 1/4f;
//        matrizA[0][2]= 4;
//
//        matrizA[1][0]= 4;
//        matrizA[1][1]= 1;
//        matrizA[1][2]= 9;
//
//        matrizA[2][0]= 1/4f;
//        matrizA[2][1]= 1/9f;
//        matrizA[2][2]= 1;

//        matrizA[0][0]= 1;
//        matrizA[0][1]= 4;
//        matrizA[0][2]= 5;
//        matrizA[1][0]= 0.25f;
//        matrizA[1][1]= 1;
//        matrizA[1][2]= 2;
//        matrizA[2][0]= 0.20f;
//        matrizA[2][1]= 0.50f;
//        matrizA[2][2]= 1;


//        float[][] matrizB = new float[x][y];
//
//        matrizB[0][0]= 5;
//        matrizB[0][1]= 8;
//        matrizB[0][2]= 3;
//
//        matrizB[1][0]= 6;
//        matrizB[1][1]= 4;
//        matrizB[1][2]= 0;
//
//        matrizB[2][0]= 1;
//        matrizB[2][1]= 0;
//        matrizB[2][2]= 2;

        List<Object> resultados = calculoDePesos(matrizA);

        float []vectorPropio = (float[]) resultados.get(0);
        String proceso = (String) resultados.get(1);

//        System.out.println("Calculo de pesos:\n VECTOR PROPIO:\n" +mostrarVector(vectorPropio));
//        System.out.println("Proceso:\n"+proceso);
//
//        System.out.println(mostrarMatriz(matrizA));
//        System.out.println(mostrarVector(calculoVectorSumaPromedio(normalizacionMatriz(matrizA)).get(0)));
//        System.out.println(mostrarVector(calculoVectorSumaPromedio(normalizacionMatriz(matrizA)).get(1)));

        float[] matrizB = new float[x];

        matrizB[0]= 0.6806426f;
        matrizB[1]= 0.20141065f;
        matrizB[2]= 0.11794671f;
        System.out.println(mostrarMatriz(matrizA));
        System.out.println("=========================");

        System.out.println(mostrarVector(matrizB));
        System.out.println("=========================");
        System.out.println(mostrarMatriz((float[][])multiplicarMatrizXVector(matrizA,matrizB).get(1)));


    }
}
