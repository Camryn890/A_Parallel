package serialAbelianSandpile;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class GridCompute extends RecursiveTask<Boolean> {

    public int hi;
    public int lo;
    public int columns;
    public int rows;
    private int[][] updateGrid;
    //static final int Cut_Off = 32;

    private int[][] grid;


    //Constructor for initializing the GridCompute task
    GridCompute(int lo, int hi, int[][] updateGrid, int[][]grid){

        this.lo = lo;
        this.hi = hi;
        this.updateGrid = updateGrid;
        this.grid = grid;
        columns = grid[0].length;
        rows = grid.length;

    }

    // Computes the updated grid values in parallel
    @Override
    protected Boolean compute() {

        boolean change = false;

        if (hi - lo < rows/12) {
            for (int i = lo; i < hi; i++) {
                for (int j = 1; j < columns - 1; j++) {
                    updateGrid[i][j] = (grid[i][j] % 4) +
                            (grid[i - 1][j] / 4) +
                            grid[i + 1][j] / 4 +
                            grid[i][j - 1] / 4 +
                            grid[i][j + 1] / 4;
                    if (!change && grid[i][j] != updateGrid[i][j]) {
                        change = true;
                    }
                }
            }
            return change;

        } else {

            GridCompute left = new GridCompute(lo, (hi + lo) / 2, updateGrid, grid);

            GridCompute right = new GridCompute( (lo + hi) / 2, hi, updateGrid, grid);

            left.fork();

            boolean l = right.compute();

            boolean r = left.join();

            if (l || r) {
                change = true;
            }
            return change;
        }
    }

}
