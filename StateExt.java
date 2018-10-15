import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class StateExt {

    private String name;
    private HashMap<String, ArrayList<String>> outputMapRec = new HashMap<>();
    private HashMap<String, ArrayList<String>> nextStateMapRec = new HashMap<>();
    private HashSet<String> inputSet = new HashSet<>();
    private HashSet<String> nextStateSet = new HashSet<>();

    public StateExt(String name, String input, String output, String next) {

        this.name = name;

        addMapping(input, output, next);
    }

    public void addMapping(String input, String output, String next) {

        //if (!inputSet.contains(input)) {
            if (inputSet.contains(input)) {
                outputMapRec.get(input).add(output);
                nextStateMapRec.get(input).add(next);
                return;
            }

            inputSet.add(input);

            outputMapRec.put(input, new ArrayList<>());
            outputMapRec.get(input).add(output);

            nextStateMapRec.put(input, new ArrayList<>());
            nextStateMapRec.get(input).add(next);

            nextStateSet.add(next);
        /*} else {
            System.out.println("meep");
            System.out.println("Bad description");
            System.exit(0);
        }*/

    }

    public String getName() {
        return name;
    }

    /*public String getNextState(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return nextStateMap.get(input);
    }*/

    public ArrayList<String> getNextStateRec(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("beep");
            System.out.println("Bad input");
            System.exit(0);
        }
        return nextStateMapRec.get(input);
    }

    /*@Override
    public String getOutput(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        } else if (outputMap.get(input).endsWith("*")) {
            return outputMap.get(input) + "(...)";
        } else if (outputMap.get(input).endsWith("+")) {

        }
        return outputMap.get(input);

    }*/

    public ArrayList<String> getOutputRec(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("input: " + input);
            System.out.println("meep");
            System.out.println("Bad input");
            System.exit(0);
        }
        return outputMapRec.get(input);

    }

    public boolean checkNextState(StateExt state) {
        return nextStateSet.contains(state.getName()) && !state.getName().equals(name);
    }

    public void checkInputSet() {
        for (String input : fsminterpreterExt.getInputSet()) {
            if (!inputSet.contains(input)) {
                addMapping(input, "", name);
            }
        }
    }

}