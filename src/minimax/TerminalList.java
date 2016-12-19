package minimax;

import java.util.ArrayList;

public class TerminalList {
    public ArrayList<Terminal> arrayList = new ArrayList<>();

    public Terminal get(int i) {
        return arrayList.get(i);
    }

    public void add(int col, int value) {
        arrayList.add(new Terminal(col, value));
    }

    public int min() {
        int minValue = Integer.MIN_VALUE;

        for (Terminal t : this.arrayList) {
            if (t.value < minValue) minValue = t.value;
        }

        return minValue;
    }

    public int max() {
        int maxValue = Integer.MAX_VALUE;

        for (Terminal t : this.arrayList) {
            if (t.value > maxValue) maxValue = t.value;
        }

        return maxValue;
    }
}
