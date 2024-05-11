package sbu.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatrixMultiplication {

    // You are allowed to change all code in the BlockMultiplier class
    public static class BlockMultiplier implements Runnable
    {
        private final List<List<Integer>> tempMatrixProduct = new ArrayList<>();
        private final List<List<Integer>> matrix_A ;
        private final List<List<Integer>> matrix_B ;

        public BlockMultiplier( List<List<Integer>> matrix_A, List<List<Integer>> matrix_B) {
            this.matrix_A = matrix_A;
            this.matrix_B = matrix_B;
        }

        @Override
        public void run() {
            for (int i = 0; i < matrix_A.size(); i++) {
                List<Integer> row = new ArrayList<>();
                tempMatrixProduct.add(row);
            }
            int sum;
            for (int i = 0; i < matrix_A.size(); i++) {
                for (int j = 0; j < matrix_B.getFirst().size(); j++) {
                    sum = 0;
                    for (int k = 0; k < matrix_B.size(); k++) {
                        sum += matrix_A.get(i).get(k) * matrix_B.get(k).get(j);
                    }
                    tempMatrixProduct.get(i).add(sum);
                }
            }
        }
    }

    /*
    Matrix A is of the form p x q
    Matrix B is of the form q x r
    both p and r are even numbers
    */
    public static List<List<Integer>> ParallelizeMatMul(List<List<Integer>> matrix_A, List<List<Integer>> matrix_B)
    {
        Scanner scanner = new Scanner(System.in);
        int rows = matrix_A.size()/2 ;
        int columns = matrix_B.size()/2 ;

        List<List<Integer>> SubMatrix1 = getSubmatrix(matrix_A, 0, 0, rows, matrix_A.getFirst().size());
        List<List<Integer>> SubMatrix2 = getSubmatrix(matrix_A, rows, 0, matrix_A.size(), matrix_A.getFirst().size());
        List<List<Integer>> SubMatrix3 = getSubmatrix(matrix_B, 0, 0, rows, matrix_B.getFirst().size());
        List<List<Integer>> SubMatrix4 = getSubmatrix(matrix_B, 0, columns, rows, matrix_B.getFirst().size());

        BlockMultiplier b1 = new BlockMultiplier(SubMatrix1 , SubMatrix3);
        BlockMultiplier b2 = new BlockMultiplier(SubMatrix1 , SubMatrix4);
        BlockMultiplier b3 = new BlockMultiplier(SubMatrix2 , SubMatrix3);
        BlockMultiplier b4 = new BlockMultiplier(SubMatrix2 , SubMatrix4);

        Thread t1 = new Thread(b1);
        Thread t2 = new Thread(b2);
        Thread t3 = new Thread(b3);
        Thread t4 = new Thread(b4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < matrix_A.size(); i++) {
            List<Integer> line = new ArrayList<>();
            if (i < rows) {
                line.addAll(b1.tempMatrixProduct.get(i));
                line.addAll(b2.tempMatrixProduct.get(i));
            } else {
                line.addAll(b3.tempMatrixProduct.get(i - rows));
                line.addAll(b4.tempMatrixProduct.get(i - rows));
            }
            result.add(line);
        }

        return result;
    }

    private static List<List<Integer>> getSubmatrix(List<List<Integer>> matrix, int startRow, int startCol, int endRow, int endCol) {
        List<List<Integer>> submatrix = new ArrayList<>();
        for (int i = startRow; i < endRow; i++) {
            List<Integer> row = new ArrayList<>(matrix.get(i).subList(startCol, endCol));
            submatrix.add(row);
        }
        return submatrix;
    }

    public static List<List<Integer>> addMatrices(List<List<Integer>> matrix1, List<List<Integer>> matrix2) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < matrix1.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < matrix1.get(i).size(); j++) {
                row.add(matrix1.get(i).get(j) + matrix2.get(i).get(j));
            }
            result.add(row);
        }

        return result;
    }

    public static void main(String[] args) {
        List<List<Integer>> tempMatrixProduct = new ArrayList<>();
        List<List<Integer>> matrix_A = new ArrayList<>();
        List<List<Integer>> matrix_B = new ArrayList<>();
        List<Integer> l1 = new ArrayList<>();
        l1.add(1);l1.add(1);l1.add(1);l1.add(1);
        matrix_A.add(l1);matrix_A.add(l1);
        List<Integer> l2 = new ArrayList<>();
        l2.add(1);l2.add(1);
        matrix_B.add(l2);matrix_B.add(l2);matrix_B.add(l2);matrix_B.add(l2);
        tempMatrixProduct = ParallelizeMatMul(matrix_A , matrix_B);
        for(List<Integer> i : tempMatrixProduct) {
            for (int j : i)
                System.out.print(j + " ");
            System.out.println();
        }
    }
}
