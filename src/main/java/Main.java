import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    static ArrayList<Class> classes;
    public static ClassesGUI gui;
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    static ScheduledFuture<?> scheduleHandle;
    static Runtime runtime;

    public static void checkAnyClasses() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        Date d = new Date();
        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();
        String lowestTime = "";

        String dayOfWeek = format.format(d);
        for (Class c : classes) {
            for (Schedule s : c.getSchedules()) {
                if (s.getDay().equals(dayOfWeek) && Integer.parseInt(s.getStartTime().split(":")[0]) == hour && Integer.parseInt(s.getStartTime().split(":")[1]) == minute)
                    c.run();

                String tempTime = s.determineDistance(hour + ":" + minute);
                int tempTimeLength = tempTime.substring(0, tempTime.indexOf(":")).length();
                if (!tempTime.contains("-")) {
                    if (lowestTime.equals("")) {
                        lowestTime = tempTime;
                    } else if (lowestTime.substring(0, lowestTime.indexOf(":")).length() > tempTimeLength) {
                        lowestTime = tempTime;
                    } else if (lowestTime.substring(0, lowestTime.indexOf(":")).length() == tempTimeLength && lowestTime.compareTo(tempTime) > 0) {
                        lowestTime = tempTime;
                    }
                }
            }
            if (lowestTime.equals("0:00"))
                lowestTime = "NOW";
            c.setTime(lowestTime);
            lowestTime = "";
        }

        gui.addAllClasses();
    }

    public static void startUp() {
        // Deals with setting up the system for checking times
        checkAnyClasses();

        // Creates the Runnable that starts the schedule to check if any classes can be run every minute
        Runnable initializeMainTimers = () -> {
            Runnable classesCheck = Main::checkAnyClasses;
            scheduler.scheduleWithFixedDelay(classesCheck, 0, 1, TimeUnit.MINUTES);
        };

        // Calculates the time it will until the next minute (milliseconds)
        int startDelay = (60000-(LocalTime.now().getSecond() * 1000 + LocalTime.now().getNano() / 1000_000));
        // Create the scheduler
        scheduleHandle =  scheduler.schedule(initializeMainTimers, startDelay, TimeUnit.MILLISECONDS);

    }

    public static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            return objInputStream.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        runtime = Runtime.getRuntime();
        classes = new ArrayList<>();
        classes = ClassData.read();
        gui = new ClassesGUI(classes);
        checkAnyClasses();
    }
}
