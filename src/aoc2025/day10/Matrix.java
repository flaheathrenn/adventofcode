package aoc2025.day10;

import java.util.Arrays;

public record Matrix(int[][] elements) {
    static void main() {
        int[][] tm = new int[][] {
                new int[] { 1, 2, 3 },
                new int[] { 0, 1, 4 },
                new int[] { 5, 6, 0 }
        };
        System.out.println(new Matrix(tm).determinant());
        for (int[] row : new Matrix(tm).inverse().elements) {
            System.out.println(Arrays.toString(row));
        };
    }

    public Matrix(Vector[] vectors) {
        int[][] elements = new int[vectors.length][vectors[0].elements().length];
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < vectors[i].elements().length; j++) {
                elements[i][j] = vectors[i].elements()[j];
            }
        }
       this(elements);
    }

    public Matrix minorOf(int x, int y) {
        int[][] minorElements = new int[this.elements.length - 1][this.elements[0].length - 1];
        for (int i = 0; i < this.elements.length; i++) {
            if (i == x) {
                continue;
            }
            for (int j = 0; j < this.elements[0].length; j++) {
                if (j == y) {
                    continue;
                }
                int ix = i >= x ? i-1 : i;
                int jy = j >= y ? j-1 : j;
                minorElements[ix][jy] = elements[i][j];
            }
        }
        return new Matrix(minorElements);
    }

    public int determinant() {
        if (this.elements.length == 2 && this.elements[0].length == 2) {
            return elements[0][0] * elements[1][1] - elements[1][0] * elements[0][1];
        }
        if (this.elements.length != this.elements[0].length) {
            throw new IllegalStateException();
        }
        int determinant = 0;
        for (int j = 0; j < elements.length; j++) {
            determinant += (j % 2 == 0 ? 1 : -1) * elements[0][j] * this.minorOf(0, j).determinant();
        }
        return determinant;
    }

    private Matrix cofactorMatrix() {
        int[][] cofactorMatrix = new int[this.elements.length][this.elements[0].length];
        for (int i = 0; i < this.elements.length; i++) {
            for (int j = 0; j < this.elements[0].length; j++) {
                Matrix minor = this.minorOf(i, j);
                int minorDet = minor.determinant();
                cofactorMatrix[i][j] = ((i + j) % 2 == 0 ? 1 : - 1) * minorDet;
            }
        }
        return new Matrix(cofactorMatrix);
    }

    Matrix transpose() {
        int[][] transposal = new int[this.elements[0].length][this.elements.length];
        for (int i = 0; i < this.elements.length; i++) {
            for (int j = 0; j < this.elements[0].length; j++) {
                transposal[j][i] = this.elements[i][j];
            }
        }
        return new Matrix(transposal);
    }

    private Matrix adjoint() {
        return this.cofactorMatrix().transpose();
    }

    Matrix inverse() {
        int detRec = 1 / this.determinant();
        Matrix adj = this.adjoint();
        int[][] newElements = new int[adj.elements.length][adj.elements[0].length];
        for (int i = 0; i < adj.elements.length; i++) {
            for (int j = 0; j < adj.elements.length; j++) {
                newElements[i][j] = adj.elements[i][j] * detRec;
            }
        }
        return new Matrix(newElements);
    }
}
