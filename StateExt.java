import java.util.HashMap;
import java.util.HashSet;

public class StateExt extends State{

    private String name;
    private HashMap<String, String> outputMap = new HashMap<>(); //String, String[] !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private HashMap<String, String[]> outputMapRec = new HashMap<>();
    private HashMap<String, String> nextStateMap = new HashMap<>();
    private HashMap<String, String[]> nextStateMapRec = new HashMap<>(); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private HashSet<String> inputSet = new HashSet<>();
    private HashSet<String> nextStateSet = new HashSet<>();

    public StateExt(String name, String input, String output, String next) {

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

    public String getNextState(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return nextStateMap.get(input);
    }

    public String[] getNextStateRec(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return nextStateMapRec.get(input);
    }

    @Override
    public String getOutput(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        } else if (outputMap.get(input).endsWith("*")) {
            return outputMap.get(input) + "(...)";
        } else if (outputMap.get(input).endsWith("+")) {

        }
        return outputMap.get(input);

    }

    public String[] getOutputRec(String input) {

        if (!fsminterpreterExt.getInputSet().contains(input)) {
            System.out.println("Bad input");
            System.exit(0);
        }
        return outputMapRec.get(input);

    }

    public boolean checkNextState(StateExt state) {
        return nextStateSet.contains(state.getName()) && !state.getName().equals(name);
    }

    @Override
    public void checkInputSet() {
        for (String input : fsminterpreterExt.getInputSet()) {
            if (!inputSet.contains(input)) {
                //TODO "Fill in the blanks"
                addMapping(input, "", name);
            }
        }
    }

}