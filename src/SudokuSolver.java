import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SudokuSolver implements Cloneable {

    class Cell implements Comparable<Cell> {
        String key;
        boolean solved = false;
        Set<Character> values = new TreeSet<>();
        Set<Cell> horizontalCells = new TreeSet<>();
        Set<Cell> verticalCells = new TreeSet<>();
        Set<Cell> boxedCells = new TreeSet<>();

        void setValue(Character value) {
            this.values.clear();
            this.values.add(value);
        }

        public String toString() {
            return MessageFormat.format("{0}: [{1}], {2}", key, values, solved);
        }

        @Override
        public int compareTo(Cell cell) {
            return this.key.compareTo(cell.key) + (values.equals(cell.values) ? 0 : 1);
        }

        public void solve() {
            if (values.size() > 1) {
                Character uniqueC = null;
                for (Character c : values) {
                    // is `c` a unique option in horizontalCells
                    boolean isUnique = true;
                    for (Cell cell : horizontalCells) {
                        if (cell.values.contains(c)) {
                            isUnique = false;
                            break;
                        }
                    }
                    if (!isUnique) {
                        isUnique = true;
                        // is `c` a unique option in verticalCells
                        for (Cell cell : verticalCells) {
                            if (cell.values.contains(c)) {
                                isUnique = false;
                                break;
                            }
                        }
                        if (!isUnique) {
                            isUnique = true;
                            // is `c` a unique option in boxedCells
                            for (Cell cell : boxedCells) {
                                if (cell.values.contains(c)) {
                                    isUnique = false;
                                    break;
                                }
                            }
                            if (!isUnique) {
                                // no other option to check
                            } else {
                                uniqueC = c; // It's unique in boxedCells.
                            }
                        } else {
                            uniqueC = c; // It's unique in verticalCells.
                        }
                    } else {
                        uniqueC = c; // It's unique in verticalCells.
                    }
                }
                if (uniqueC != null) {
                    values.clear();
                    values.add(uniqueC);
                }
            }

            if (values.size() == 1) {
                solved = true;
                Character c = values.iterator().next();
                for (Cell cell : horizontalCells) {
                    cell.values.remove(c);
                }
                for (Cell cell : verticalCells) {
                    cell.values.remove(c);
                }
                for (Cell cell : boxedCells) {
                    cell.values.remove(c);
                }
            }
        }
    }

    Map<String, Cell> cells;
    char[][] board;
    int solvedCount = 0;
    Set<String> triedKeys;
    final static Set<Character> NINE_SET = new HashSet<>();
    static {
        for (int i = 1; i < 10; i++) {
            NINE_SET.add((char) ('0' + i));
        }
    }

    Cell getCell(String key) {
        return cells.get(key);
    }

    java.util.Collection<Cell> getCells() {
        return cells.values();
    }

    public SudokuSolver(char[][] board) {

        if (null == board || board.length != 9) {
            for (int i = 0; i < 9; i++) {
                if (board[i].length != 9) {
                    throw new IllegalArgumentException("Invalid Suduku");
                }
                for (int j = 0; j < 9; j++) {
                    if (!(NINE_SET.contains(board[i][j]) || board[i][j] == '.')) {
                        throw new IllegalArgumentException("Invalid Suduku");
                    }
                }
            }
        }
        this.board = board;
        this.triedKeys = new TreeSet<>();
        this.cells = new TreeMap<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String key = i + "-" + j;
                Cell cell = new Cell();
                cells.put(key, cell);
                cell.key = key;
                if (board[i][j] == '.') {
                    cell.values.addAll(NINE_SET);
                } else {
                    cell.values.add(board[i][j]);
                }
            }
        }
        initCells();
    }

    void initCells() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String key = i + "-" + j;
                Cell cell = cells.get(key);
                // horizontal
                for (int k = 0; k < 9; k++) {
                    int curX = i, curY = k;
                    if (!(i == curX && j == curY)) {
                        String hKey = curX + "-" + curY;
                        cells.get(hKey).horizontalCells.add(cell);
                    }
                }
                // vertical
                for (int k = 0; k < 9; k++) {
                    int curX = k, curY = j;
                    if (!(i == curX && j == curY)) {
                        String vKey = curX + "-" + curY;
                        cells.get(vKey).verticalCells.add(cell);
                    }
                }
                // boxed
                int boxX = (i / 3) * 3, boxY = (j / 3) * 3;
                for (int m = 0; m < 3; m++) {
                    for (int k = 0; k < 3; k++) {
                        int curX = boxX + m, curY = boxY + k;
                        if (!(i == curX && j == curY)) {
                            String bKey = curX + "-" + curY;
                            cells.get(bKey).boxedCells.add(cell);
                        }
                    }
                }
            }
        }
    }

    public boolean validate() {
        // horizontal
        for (int i = 0; i < 9; i++) {
            Set<Character> hSet = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                String hKey = i + "-" + j;
                Cell hCell = cells.get(hKey);
                if (hCell.values.size() == 1) {
                    hSet.addAll(hCell.values);
                }
            }
            if (!hSet.equals(NINE_SET)) {
                return false;
            }
        }
        // horizontal
        for (int i = 0; i < 9; i++) {
            Set<Character> vSet = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                String vKey = j + "-" + i;
                Cell vCell = cells.get(vKey);
                if (vCell.values.size() == 1) {
                    vSet.addAll(vCell.values);
                }
            }
            if (!vSet.equals(NINE_SET)) {
                return false;
            }
        }
        // boxed
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Set<Character> bSet = new HashSet<>();
                int boxX = i * 3, boxY = j * 3;
                for (int m = 0; m < 3; m++) {
                    for (int k = 0; k < 3; k++) {
                        int curX = boxX + m, curY = boxY + k;
                        String bKey = curX + "-" + curY;
                        Cell bCell = cells.get(bKey);
                        if (bCell.values.size() == 1) {
                            bSet.addAll(bCell.values);
                        }
                    }
                }
                if (!bSet.equals(NINE_SET)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void print() {
        System.out.print(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i = 0; i < 9; i++) {
            if (i != 0 && i % 3 == 0) {
                sb.append('\n');
            }
            for (int j = 0; j < 9; j++) {
                if (j != 0) {
                    sb.append(',');
                    sb.append(' ');
                }
                if (j != 0 && j % 3 == 0) {
                    sb.append(' ');
                }
                Set<Character> values = cells.get(i + "-" + j).values;
                if (values.size() == 1) {
                    sb.append(values.iterator().next());
                } else {
                    sb.append('(');
                    Iterator<Character> it = values.iterator();
                    while (it.hasNext()) {
                        sb.append(it.next());
                        if (it.hasNext()) {
                            sb.append(',');
                        }
                    }
                    sb.append(')');
                }
            }
            sb.append('\n');
        }
        sb.append('\n');
        return sb.toString();
    }

    boolean isSolved() {
        return this.solvedCount == 81;
    }

    public SudokuSolver clone() {
        SudokuSolver newSudoku = new SudokuSolver(this.board);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String key = i + "-" + j;
                Cell newCell = newSudoku.getCell(key);
                Cell cell = this.getCell(key);
                newCell.values.clear();
                newCell.values.addAll(cell.values);
                newCell.solved = cell.solved;
            }
        }
        newSudoku.solvedCount = this.solvedCount;
        newSudoku.triedKeys.addAll(this.triedKeys);
        return newSudoku;
    }

    void solve() {
        int preSolvedCount = solvedCount;
        while (!isSolved()) {
            for (Cell cell : cells.values()) {
                if (!cell.solved) {
                    cell.solve();
                    if (cell.solved) {
                        solvedCount++;
                    }
                }
            }
            if (preSolvedCount == solvedCount) {
                // this.print();
                // System.out.println("Sovled cells count: " + this.solvedCount);
                // need to do trying
                if (!dfs(this)) {
                    break;
                }
            }
            preSolvedCount = solvedCount;
        }
    }

    /**
     * 
     * @param sudoku
     * @return true if there is a valid solution for this `sudoku`
     */
    boolean dfs(SudokuSolver sudoku) {
        Cell tryCell = null;
        for (Cell cell : cells.values()) {
            if (cell.values.isEmpty()) {
                // This search does not lead to a solution.
                // sudoku.print();
                return false;
            }
            if (tryCell == null && cell.values.size() > 1) {
                if (!triedKeys.contains(cell.key)) {
                    tryCell = cell;
                    break;
                } else {
                    // System.out.println("tried before");
                }
            }
        }
        if (tryCell == null) {
            // This case might never happen.
            throw new IllegalArgumentException("Unable to solve this game. Please check your input.");
        }

        Character validTryValue = null;

        System.out.println("Backtracking " + tryCell.key + " ...");
        sudoku.triedKeys.add(tryCell.key);
        for (Character tryC : tryCell.values) {
            validTryValue = tryC;

            SudokuSolver newSudoku = this.clone();
            Cell newTryCell = newSudoku.getCell(tryCell.key);
            newTryCell.setValue(tryC);

            newSudoku.solve();

            if (newSudoku.isSolved()) {
                this.updateResultFrom(newSudoku);
                break;
            } else {
                validTryValue = null;
                continue;
            }
        }
        if (validTryValue != null) {
            System.out.println("Backtracking " + tryCell.key + " now solved with " + validTryValue);
            return true;
        } else {
            // This search does not lead to a solution.
            // This case might never happen.
            return false;
        }
    }

    private void updateResultFrom(SudokuSolver newSudoku) {
        if (newSudoku.isSolved()) {
            this.solvedCount = newSudoku.solvedCount;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String key = i + "-" + j;
                    Character c = newSudoku.getCell(key).values.iterator().next();
                    this.board[i][j] = c;
                    Cell cell = this.getCell(key);
                    cell.setValue(c);
                    cell.solved = true;
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid Sudoku result");
        }
    }
}
