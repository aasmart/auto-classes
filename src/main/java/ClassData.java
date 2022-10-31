import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ClassData {
    static File data = new File("C:\\Users\\alexa\\IdeaProjects\\AutoClasses\\classData.csv");
    public static ArrayList<Class> read() {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            String line;
            int lineNumber = 0;
            BufferedReader br = new BufferedReader(new FileReader(data));
            while((line = br.readLine()) != null) {
                try {
                    if (lineNumber > 0) {
                        String[] lineData = line.split(",");
                        classes.add(new Class(lineData[0],
                                new ArrayList<>(Schedule.parseSchedules(lineData[1])),
                                new ArrayList<>(Resource.parseResources(lineData[2]))));
                    }
                    lineNumber++;
                } catch (Exception ignore) {}
            }

        } catch(Exception e) {
            System.out.println("Error reading data!");
        }

        return classes;
    }

    public static void write(ArrayList<Class> classes) {
        try {
            PrintWriter p = new PrintWriter(data);
            if(data.createNewFile()) {
                System.out.println("File Created!");
            } else {
                System.out.println("File Exists!");
            }
            p.println("Name,Schedule,Resources");
            for(Class c : classes) {
                p.println(c.getCSV());
            }
            p.close();
        } catch(Exception e) {
            System.out.println("Error writing data!");
        }
    }
}
