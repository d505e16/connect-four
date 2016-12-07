package Client;

import java.util.ArrayList;
import java.util.HashMap;

public class Master {
    private static ArrayList<HashMap<String, String>> workers = new ArrayList<>();

    public static void main(String[] args) {
        getWorkers(args);

        for (HashMap worker : workers) {
            worker.get("address");
            worker.get("port");
        }

        workers.size();


    }

    private static void getWorkers(String[] args) {
        for (String arg : args) {
            HashMap<String, String> map = new HashMap<>();
            String[] parts = arg.split(":");
            map.put("address", parts[0]);
            map.put("port", parts[1]);
            workers.add(map);
        }
    }
}
