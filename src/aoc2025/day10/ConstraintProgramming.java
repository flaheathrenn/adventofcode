package aoc2025.day10;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class ConstraintProgramming {
    static void main(String[] args) {
        /*
        //        Vector v = new Vector(new int[] { 2, 5, 8 });
//        Matrix m = new Matrix(new int[][] {
//                { 0, 1, 0 },
//                { 1, 0, 1 },
//                { 1, 1, 0 }
//        });
//        System.out.println(v.multiply(m));
//
//        Vector v1 = new Vector(new int[] { 13, 10, 5 });
//        System.out.println(v1.multiply(m.inverse()));
         */
        Model model = new Model("test");
        IntVar a = model.intVar("a", 0, 13, false);
        IntVar b = model.intVar("b", 0, 13, false);
        IntVar c = model.intVar("c", 0, 13, false);

//        D.add(E).eq(Y.add(r[0].mul(10))).post();

        b.add(c).eq(13).post();
        a.add(c).eq(10).post();
        b.eq(5).post();

        model.setObjective(false, a.add(b).add(c).intVar());

        Solver s = model.getSolver();
        s.solve();
        System.out.println(s.getBestSolutionValue().intValue());

    }
}
