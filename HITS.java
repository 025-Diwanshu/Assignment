import java.util.*;

public class HITS {
    int [][] adj;
    int [][] trans;
    double [][] hubAndAuth; // hubAndAuth[0] denotes hub and hubAndAuth[1] denotes authority.
    HITS(){}
    HITS(List<List<Integer>> g,int n){
        adj = makeAdjacencyMatrix(g,n);
        trans = transposeOf(adj);
        hubAndAuth = new double[2][n];
    }
    double [][] calculateKTimes(int k){
        Arrays.fill(hubAndAuth[0],1);
        Arrays.fill(hubAndAuth[1],1);
        while (k-->0){
            hubAndAuth[1] = matrixMultiplication(trans,hubAndAuth[0]);
            normalize(hubAndAuth[1]);
            hubAndAuth[0] = matrixMultiplication(adj,hubAndAuth[1]);
            normalize(hubAndAuth[0]);
        }
        return hubAndAuth;
    }

    private void normalize(double[] a) {
        double normal = 0;
        for(double e : a){
            normal+=e*e;
        }
        normal = Math.sqrt(normal);
        for (int i = 0; i < a.length; i++) {
            a[i] /=normal;

        }
    }

    double[] matrixMultiplication(int [][] a, double[] b){
        int n = a.length;
        double [] c = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                sum+=a[i][j]*b[j];
            }
            c[i] = sum;
        }
        return c;
    }

    private int[][] transposeOf(int[][] adj) {
        int n = adj.length;
        int [][] a = new int[n][];
        for (int i = 0; i < n; i++) {
            a[i] = adj[i].clone();
        }
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                int temp = a[i][j];
                a[i][j] = a[j][i];
                a[j][i] = temp;
            }
        }
        return a;
    }

    private int[][] makeAdjacencyMatrix(List<List<Integer>> g,int n) {
        int [][] a= new int[n][n];
        for(int i=0;i<n;i++){
            for(int e : g.get(i)){
                a[i][e] = 1;
            }
        }
        return a;
    }
}