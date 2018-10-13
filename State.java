import java.util.HashMap;
import java.util.HashSet;

public class State {

    private String name;
    private HashMap<String, String> outputMap = new HashMap<>();
    private HashMap<String, String> nextStateMap = new HashMap<>();
    private HashSet<String> inputSet = new HashSet<>();
    private HashSet<String> nextStateSet = new HashSet<>();

    public State(String name, String input, String output, String next) {

        this.name = name;

        addMapping(input, output, next);
    }

    public void addMapping(String input, String output, String next) {

        if (!inputSet.contains(input)) {
            inputSet.add(input);
            outputMap.put(input, output);
            nextStateMap.put(input, next);
            nextStateSet.add(next);
        } else {
            System.out.println("Bad description");
            System.exit(0);
        }

    }

    public String getName() {
        return name;
    }

    public String getOutput(String input) {

        if (!fsminterpreter.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }

        return outputMap.get(input);
    }

    public String getNextState(String input) {

        if (!fsminterpreter.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return nextStateMap.get(input);
    }

    public void checkInputSet() {
        for (String input : fsminterpreter.getInputSet()) {
            if (!inputSet.contains(input)) {
                System.out.println("Bad description");
                System.exit(0);
            }
        }
    }

    public boolean checkNextState(State state) {
        return nextStateSet.contains(state.getName()) && !state.getName().equals(name);
    }

}