import minimax.Board;

public class Main {

    public static void main(String[] args) {
        try {
            //Board board = new Board("3333302111131200000122224544555556666664");
            Board board = new Board("565656565601230123012301230123012344444");

            //board.print();

            board.minimax(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
