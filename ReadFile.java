import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class contains a method which reads the content for a given file and returns it.
 */
public class ReadFile {

    /**
     * A static method which reads the content of the file with the filepath given in the parameter line by line, adds
     * a comma at the end of each line, and then returns the content as a String.
     * @param name The filepath of the file that is to be read
     * @return the content of the file, with each line seperated by a comma
     */
    public static String readFile(String name) {

        String content = null;

        try {

            File file = new File(name);

            if (file.exists() && file.isFile()) {

                BufferedReader reader = new BufferedReader(new FileReader(file));

                StringBuilder builder = new StringBuilder();

                while (reader.ready()) {
                    builder.append(reader.readLine() + ",");
                }

                reader.close();

                content = builder.toString();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

}
