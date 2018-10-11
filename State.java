import java.util.HashMap;
import java.util.HashSet;

public class State {

    private String name;
    private HashMap<String, String> outputMap = new HashMap<>();
    private HashMap<String, String> nextStateMap = new HashMap<>();
    private HashSet<String> inputSet = new HashSet<>();

    public State(String name, String input, String output, String next) {

        this.name = name;

        addMapping(input, output, next);
    }

    public void addMapping(String input, String output, String next) {

        if (!inputSet.contains(input)) {
            inputSet.add(input);
            outputMap.put(input, output);
            nextStateMap.put(input, next);
        } else {
            System.out.println("THIS DOESN'T MAKE SENSE");
            System.exit(0);
            //TODO Make program quit in a neat way
        }

    }

    public String getName() {
        return name;
    }

    public String getOutput(String input) {

        if (!fsminterpreter.getInputSet().contains(input)) {
            System.out.println("THIS DOESN'T MAKE SENSE");
            System.exit(0);
            //TODO Make program quit in a neat way
        }
        //System.out.println("Output: " + outputMap.get(input));
        return outputMap.get(input);

    }

    public String getNextState(String input) {

        if (!fsminterpreter.getInputSet().contains(input)) {
            System.out.println("THIS DOESN'T MAKE SENSE");
            System.exit(0);
            //TODO Make program quit in a neat way
        }
        //System.out.println("Next state: " + nextStateMap.get(input));
        return nextStateMap.get(input);
    }

}