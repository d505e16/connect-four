package minimax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Board {
    private final Integer ROW = 6;
    private final Integer COL = 7;
    private String boardString = "";
    private ArrayList<ArrayList<Character>> board = new ArrayList<>(COL);

    public Board(String boardString) throws Exception {
        String[] boardStringArray = boardString.split("");

        // prepopulate board
        for (int i = 0; i < COL; i++) {
            this.board.add(new ArrayList<>(ROW));
        }

        // iterate through board string
        for (String move : boardStringArray) {
            if (!Objects.equals(move, ""))
                this.addPiece(Integer.parseInt(move), this.boardString.length() % 2 == 0 ? 'X' : 'O');
        }
    }

    private void addPiece(int col, Character player) throws Exception {
        if (col < COL) {
            if (this.board.get(col).size() < ROW) {
                this.board.get(col).add(player);
                this.boardString = this.boardString.concat(Integer.toString(col));
            } else {
                throw new Exception("Column " + col + " is full, board string is invalid");
            }
        } else {
            throw new Exception("Column is out of bounds");
        }
    }

    public int minimax(int depth) throws Exception {
        TerminalList terminals = new TerminalList();

        for (int i = 0; i < COL; i++) {
            if (this.board.get(i).size() < ROW) {
                Board board = new Board(this.boardString.concat(Integer.toString(i)));
                Character player = board.boardString.length() % 2 == 1 ? 'X' : 'O';

                //board.print();
                //System.out.println(depth);

                if (board.isTerminal(i)) {
                    terminals.add(i, depth % 2 == 0 ? 100 : -100);
                } else if (depth != 5) {
                    terminals.add(i, board.minimax(depth + 1));
                }
            }
        }

        if (depth % 2 == 0) {
            return terminals.max();
        } else {
            return terminals.min();
        }
    }

    private boolean isTerminal(int colIndex) {
        return isHorizontalTerminal(colIndex)
                || isVerticalTerminal(colIndex)
                || isLTRDiagonalTerminal(colIndex)
                || isRTLDiagonalTerminal(colIndex);
    }

    private boolean isHorizontalTerminal(int colIndex) {
        int connected = 1;
        int rowIndex = this.board.get(colIndex).size() - 1;
        Character player = this.board.get(colIndex).get(rowIndex);

        for (int i = colIndex + 1; i < COL; i++) {
            if (rowIndex < this.board.get(i).size() && this.board.get(i).get(rowIndex) == player) {
                connected++;
            } else {
                break;
            }
        }

        for (int i = colIndex - 1; i >= 0; i--) {
            if (rowIndex < this.board.get(i).size() && this.board.get(i).get(rowIndex) == player) {
                connected++;
            } else {
                break;
            }
        }

        return connected >= 4;
    }

    private boolean isVerticalTerminal(int colIndex) {
        int connected = 1;
        int rowIndex = this.board.get(colIndex).size() - 1;
        Character player = this.board.get(colIndex).get(rowIndex);

        for (int i = rowIndex - 1; i >= 0; i--) {
            if (this.board.get(colIndex).get(i) == player) {
                connected++;
            } else {
                break;
            }
        }

        return connected >= 4;
    }

    private boolean isLTRDiagonalTerminal(int colIndex) {
        int connected = 1;
        int rowIndex = this.board.get(colIndex).size() - 1;
        Character player = this.board.get(colIndex).get(rowIndex);

        for (int i = colIndex + 1, j = rowIndex + 1; i < COL && j < ROW; i++, j++) {
            if (j < this.board.get(i).size() && this.board.get(i).get(j) == player) {
                connected++;
            } else {
                break;
            }
        }

        for (int i = colIndex - 1, j = rowIndex - 1; i >= 0 && j >= 0; i--, j--) {
            if (j < this.board.get(i).size() && this.board.get(i).get(j) == player) {
                connected++;
            } else {
                break;
            }
        }

        return connected >= 4;
    }

    private boolean isRTLDiagonalTerminal(int colIndex) {
        int connected = 1;
        int rowIndex = this.board.get(colIndex).size() - 1;
        Character player = this.board.get(colIndex).get(rowIndex);

        for (int i = colIndex - 1, j = rowIndex + 1; i >= 0 && j < ROW; i--, j++) {
            if (j < this.board.get(i).size() && this.board.get(i).get(j) == player) {
                connected++;
            } else {
                break;
            }
        }

        for (int i = colIndex + 1, j = rowIndex - 1; i < COL && j >= 0; i++, j--) {
            if (j < this.board.get(i).size() && this.board.get(i).get(j) == player) {
                connected++;
            } else {
                break;
            }
        }

        return connected >= 4;
    }

//    private double terminalValue(int depth) { // skal bare være +/-100 - kan laves til int
//        if (depth % 2 == 0) {
//            return 1;
//        } else {
//            return -1;
//        }
//    }

    public void print() {
        System.out.println(" _______________________________________________________");
        for (int i = ROW - 1; i >= 0; i--) {
            System.out.print("|");
            for (int j = 0; j < COL; j++) {
                if (i < this.board.get(j).size()) {
                    System.out.print("   " + this.board.get(j).get(i) + "   |");
                } else {
                    System.out.print("       |");
                }
            }
            System.out.println();
            System.out.println("|_______|_______|_______|_______|_______|_______|_______|");
        }
        System.out.println("    0       1       2       3       4       5       6");
    }
}
