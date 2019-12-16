import java.io.*;  
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class KMeans {
   public static double[] getTrainingCentroid(String[][] data, int start, int size) {
      double sum1 = 0;
      double sum2 = 0;
      double sum3 = 0;
      double sum4 = 0;
      for(int i = start; i < start + size; i++) {
         sum1 += Double.parseDouble(data[i][1]);
         sum2 += Double.parseDouble(data[i][2]);
         sum3 += Double.parseDouble(data[i][3]);
         sum4 += Double.parseDouble(data[i][4]);
      }
      
      double[] result = new double[4];
      result[0] = sum1 / size;
      result[1] = sum2 / size;
      result[2] = sum3 / size;
      result[3] = sum4 / size;
      
      return result;
   }
   
   public static void assignTestDataToCentroid(String[][] data, int start, int size, double[] centroidSetosa, double[] centroidVersicolor, double[] centroidVirginica, Set<Integer> setosaSet, Set<Integer> versicolorSet, Set<Integer> virginicaSet) {
      for(int i = start; i < start + size; i++) {
         double x1 = Double.parseDouble(data[i][1]);
         double x2 = Double.parseDouble(data[i][2]);
         double x3 = Double.parseDouble(data[i][3]);
         double x4 = Double.parseDouble(data[i][4]);
         double distanceFromCentroidSetosa = euclideanDistanceFourDimensions(x1, x2, x3, x4, centroidSetosa[0], centroidSetosa[1], centroidSetosa[2], centroidSetosa[3]);
         double distanceFromCentroidVersicolor = euclideanDistanceFourDimensions(x1, x2, x3, x4, centroidVersicolor[0], centroidVersicolor[1], centroidVersicolor[2], centroidVersicolor[3]);
         double distanceFromCentroidVirginica = euclideanDistanceFourDimensions(x1, x2, x3, x4, centroidVirginica[0], centroidVirginica[1], centroidVirginica[2], centroidVirginica[3]);
         if(distanceFromCentroidSetosa < distanceFromCentroidVersicolor) {
            if(distanceFromCentroidSetosa < distanceFromCentroidVirginica) {
               setosaSet.add(Integer.parseInt(data[i][0]));
            } else {
               virginicaSet.add(Integer.parseInt(data[i][0]));
            }
         } else {
            if(distanceFromCentroidVersicolor < distanceFromCentroidVirginica) {
               versicolorSet.add(Integer.parseInt(data[i][0]));
            } else {
               virginicaSet.add(Integer.parseInt(data[i][0]));
            }
         }
      }
   }
   
   public static double euclideanDistanceFourDimensions(double x1, double x2, double x3, double x4, double y1, double y2, double y3, double y4) {
      double z1 = (x1 - y1) * (x1 - y1);
      double z2 = (x2 - y2) * (x2 - y2);
      double z3 = (x3 - y3) * (x3 - y3);
      double z4 = (x4 - y4) * (x4 - y4);
      double sum = z1 + z2 + z3 + z4;
      double result = Math.sqrt(sum);
      return result;
   }
   
   public static double[] getTestCentroid(String[][] data, Set<Integer> set) {
      double sum1 = 0;
      double sum2 = 0;
      double sum3 = 0;
      double sum4 = 0;
      for(Integer id: set) {
         sum1 += Double.parseDouble(data[id - 1][1]);
         sum2 += Double.parseDouble(data[id - 1][2]);
         sum3 += Double.parseDouble(data[id - 1][3]);
         sum4 += Double.parseDouble(data[id - 1][4]);
      }
      double avg1 = sum1 / set.size();
      double avg2 = sum2 / set.size();
      double avg3 = sum3 / set.size();
      double avg4 = sum4 / set.size();
      double[] result = new double[4];
      result[0] = avg1;
      result[1] = avg2;
      result[2] = avg3;
      result[3] = avg4;
      return result;
   }
   
   public static double[] getAccuracy(String[][] data, Set<Integer> set1, Set<Integer> set2, Set<Integer> set3, int size) {
      String setosa = "Iris-setosa";
      String versicolor = "Iris-versicolor";
      String virginica = "Iris-virginica";
      double sum1 = 0;
      for(Integer id: set1) {
         if(data[id - 1][5].equals(setosa))
            sum1++;
      }
      double sum2 = 0;
      for(Integer id: set2) {
         if(data[id - 1][5].equals(versicolor))
            sum2++;
      }
      double sum3 = 0;
      for(Integer id: set3) {
         if(data[id - 1][5].equals(virginica))
            sum3++;
      }
      double avg1 = sum1 / size;
      double avg2 = sum2 / size;
      double avg3 = sum3 / size;
      double[] result = new double[3];
      result[0] = avg1 * 100;
      result[1] = avg2 * 100;
      result[2] = avg3 * 100;
      return result;
   }
   
   public static void main(String[] args) throws Exception {
      String fileLocation = "C:\\Iris.csv";
      
      Scanner scan = new Scanner(new File(fileLocation));
      int dataRows = 150;
      int dataColumns = 6;
      // populate array data with csv data
      // data(id, sepal-length, sepal-width, petal-length, petal-width, species)
      String[][] data = new String[dataRows][dataColumns];
      String scannedLine = "";
      int i = 0;
      while(scan.hasNextLine()) {
         scannedLine = scan.nextLine();
         String[] parsedLine = scannedLine.split(",");
         for(int p = 0; p < parsedLine.length; p++) {
            data[i][p] = parsedLine[p];
         }
         i++;
      }
      scan.close();
      
      int trainSize = 30;
      int train1Start = 0;
      int train2Start = 50;
      int train3Start = 100;      
      int testSize = 20;
      int test1Start = 30;
      int test2Start = 80;
      int test3Start = 130;
      // centroids derived from training data set  [sepalLength, sepalWidth, petalLength, petalWidth]
      double[] centroidSetosa = getTrainingCentroid(data, train1Start, trainSize);    // setosa
      double[] centroidVersicolor = getTrainingCentroid(data, train2Start, trainSize);    // versicolor
      double[] centroidVirginica = getTrainingCentroid(data, train3Start, trainSize);    // virginica
      
      Set<Integer> initialSetosaSet = new HashSet<>();
      Set<Integer> initialVersicolorSet = new HashSet<>();
      Set<Integer> initialVirginicaSet = new HashSet<>();
      Set<Integer> setosaSet = new HashSet<>();
      Set<Integer> versicolorSet = new HashSet<>();
      Set<Integer> virginicaSet = new HashSet<>();
      // assign test data set to each centroid set ie. setosa, versicolor, virginica sets
      assignTestDataToCentroid(data, test1Start, testSize, centroidSetosa, centroidVersicolor, centroidVirginica, initialSetosaSet, initialVersicolorSet, initialVirginicaSet);
      assignTestDataToCentroid(data, test2Start, testSize, centroidSetosa, centroidVersicolor, centroidVirginica, initialSetosaSet, initialVersicolorSet, initialVirginicaSet);
      assignTestDataToCentroid(data, test3Start, testSize, centroidSetosa, centroidVersicolor, centroidVirginica, initialSetosaSet, initialVersicolorSet, initialVirginicaSet);
     
      boolean isMembershipUnchanged = false;
      while(!isMembershipUnchanged) {
         // recalculate centroids
         centroidSetosa = getTestCentroid(data, initialSetosaSet);
         centroidVersicolor = getTestCentroid(data, initialVersicolorSet);
         centroidVirginica = getTestCentroid(data, initialVirginicaSet);
         // reassign test data set to each centroid set ie. setosa, versicolor, virginica sets
         assignTestDataToCentroid(data, test1Start, testSize, centroidSetosa, centroidVersicolor, centroidVirginica, setosaSet, versicolorSet, virginicaSet);
         assignTestDataToCentroid(data, test2Start, testSize, centroidSetosa, centroidVersicolor, centroidVirginica, setosaSet, versicolorSet, virginicaSet);
         assignTestDataToCentroid(data, test3Start, testSize, centroidSetosa, centroidVersicolor, centroidVirginica, setosaSet, versicolorSet, virginicaSet);
         
         // if no point changes cluster membership
         if(initialSetosaSet.equals(setosaSet) && initialVersicolorSet.equals(versicolorSet) && initialVirginicaSet.equals(virginicaSet)) {
            isMembershipUnchanged = true;
         } else {
            initialSetosaSet = setosaSet;
            initialVersicolorSet = versicolorSet;
            initialVirginicaSet = virginicaSet;
            setosaSet = new HashSet<>();
            versicolorSet = new HashSet<>();
            virginicaSet = new HashSet<>();
         }
      }
      // calculate accuracy
      double[] accuracy = getAccuracy(data, setosaSet, versicolorSet, virginicaSet, testSize);
      System.out.println("Setosa accuracy = " + accuracy[0] + "%");
      System.out.println("Versicolor accuracy = " + accuracy[1] + "%");
      System.out.println("Virginica accuracy = " + accuracy[2] + "%");
   }
}
