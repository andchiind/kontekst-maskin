import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class fsminterpreter {

    private static State currentState;
    private static HashSet<State> states = new HashSet<>();
    private static ConcurrentLinkedQueue<String> inputs = new ConcurrentLinkedQueue<>();
    private static HashSet<String> inputSet = new HashSet<>();

    private static void readInstructions(String fileName) {

        String fileContent = ReadFile.readFile(fileName);

        String[] fileLine = fileContent.split(",");

        int n = 0;

        for (String line : fileLine) {

            String[] elements = line.split(" ");

            boolean exists = false;

            for (State state : states) {
                if (state.getName().equals(elements[0])) {
                    state.addMapping(elements[1], elements[2], elements[3]);
                    exists = true;
                }
            }

            if (!exists) {
                states.add(new State(elements[0], elements[1], elements[2], elements[3]));
                if (n == 0) {
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    currentState = (State)states.toArray()[0];
                    n++;
                }
            }
        }
    }

    public static HashSet<String> getInputSet() {
        return inputSet;
    }

    public static void main(String[] args) {

        readInstructions(args[0]);

        inputSet.addAll(Arrays.asList(args).subList(1, args.length));

        inputs.addAll(inputSet);

        while (!inputs.isEmpty()) {

            int size = inputs.size();

            System.out.print(currentState.getOutput(inputs.peek()));

            for (State state : states) {
                if (state.getName().equals(currentState.getNextState(inputs.peek()))) {
                    currentState = state;
                    inputs.poll();
                }
            }

            if (size == inputs.size()) {
                System.out.println("SHIT AIN'T GOOD"); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
                //TODO make this proper and pretty
                System.exit(0);
            }
        }

    }
}
