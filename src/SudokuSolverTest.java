import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SudokuSolverTest {
    @DataProvider(name = "testCases")
    public static Object[][] testCases() {
        return new Object[][] {
            {
                new char[][] {
                    {'.','.','9','7','4','8','.','.','.'},
                    {'7','.','.','.','.','.','.','.','.'},
                    {'.','2','.','1','.','9','.','.','.'},
                    
                    {'.','.','7','.','.','.','2','4','.'},
                    {'.','6','4','.','1','.','5','9','.'},
                    {'.','9','8','.','.','.','3','.','.'},
                    
                    {'.','.','.','8','.','3','.','2','.'},
                    {'.','.','.','.','.','.','.','.','6'},
                    {'.','.','.','2','7','5','9','.','.'}}
            },
            {
                new char[][] {
                    {'.','.','.','2','.','.','.','6','3'},
                    {'3','.','.','.','.','5','4','.','1'},
                    {'.','.','1','.','.','3','9','8','.'},
                    
                    {'.','.','.','.','.','.','.','9','.'},
                    {'.','.','.','5','3','8','.','.','.'},
                    {'.','3','.','.','.','.','.','.','.'},
                    
                    {'.','2','6','3','.','.','5','.','.'},
                    {'5','.','3','7','.','.','.','.','8'},
                    {'4','7','.','.','.','1','.','.','.'}}
            },
            {
                // The Hardest-Ever Sudoku
                // https://abcnews.go.com/blogs/headlines/2012/06/can-you-solve-the-hardest-ever-sudoku
                new char[][] {
                    {'8','.','.','.','.','.','.','.','.'},
                    {'.','.','3','6','.','.','.','.','.'},
                    {'.','7','.','.','9','.','2','.','.'},
                    
                    {'.','5','.','.','.','7','.','.','.'},
                    {'.','.','.','.','4','5','7','.','.'},
                    {'.','.','.','1','.','.','.','3','.'},
                    
                    {'.','.','1','.','.','.','.','6','8'},
                    {'.','.','8','5','.','.','.','1','.'},
                    {'.','9','.','.','.','.','4','.','.'}}
            }
        };
    }

    @Test(dataProvider = "testCases")
    public void testSudoku(char[][] board) {
        SudokuSolver sudoku = new SudokuSolver(board);
        long start = System.currentTimeMillis();
        sudoku.solve();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Solved in " + (((double)timeElapsed)/1000) + " s");
        sudoku.print();
        boolean validationResult = sudoku.validate();
        Assert.assertTrue(validationResult, null);
    }
}
