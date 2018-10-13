import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

public class ReadFile {

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
