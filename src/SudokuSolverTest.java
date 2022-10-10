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
            }
        };
    }

    @Test(dataProvider = "testCases")
    public void testSudoku(char[][] board) {
        SudokuSolver sudoku = new SudokuSolver(board);
        sudoku.solve();
        sudoku.print();
        boolean validationResult = sudoku.validate();
        Assert.assertTrue(validationResult, null);
    }
}
