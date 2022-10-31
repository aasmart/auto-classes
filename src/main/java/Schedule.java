import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Schedule extends MainGUIComponents implements Serializable {
    private String day;
    private String startTime;
    // JFrame Stuff
    JComboBox<String> dayPanel;
    JComboBox<String> minutesPanel;
    JComboBox<String> hourPanel;


    public Schedule() {
        day = "Monday";
        startTime = "1:00";
    }

    public Schedule(String day, String startTime) {
        this.day = day;
        this.startTime = startTime;
    }

    public static ArrayList<Schedule> parseSchedules(String schedules) {
        ArrayList<Schedule> temp = new ArrayList<>();
        for(String s : schedules.split(" ")) {
            String[] split = s.split("-");
            temp.add(new Schedule(split[0],split[1]));
        }
        return temp;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void saveSchedule() {
        try {
            this.day = (String) dayPanel.getSelectedItem();
            this.startTime = hourPanel.getSelectedItem() + ":" + minutesPanel.getSelectedItem();
        } catch(Exception ignore) {
        }
    }

    public JPanel createPanel() {
        EmptyBorder blank = new EmptyBorder(0,0,0,0);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 1;
        g.gridy = 0;
        g.weightx = 5;
        g.fill = GridBagConstraints.BOTH;

        final String[] daysOfWeek = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        //final

        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        listRenderer.setBorder(blank);

        int offset;
        String textColor = "#C7D9D9";


        JPanel jClass = new JPanel();
        jClass.setLayout(new GridBagLayout());
        jClass.setBackground(Color.decode("#7386D1"));
        jClass.setPreferredSize(new Dimension(200,65));

        JButton delete = new JButton(deleteIcon);
        delete.setBackground(Color.decode("#7386D1"));
        delete.setFont(new Font("Calibri",Font.BOLD,48));
        delete.setBorder(blank);
        delete.setBounds(100,100,60,60);
        offset = delete.getInsets().left;
        delete.setIcon(resizeIcon(deleteIcon, delete.getWidth() - offset, delete.getHeight() - offset));
        delete.setToolTipText("Click to delete this schedule!");

        delete.addActionListener(e -> delete());

        dayPanel = new JComboBox<>(daysOfWeek);
        dayPanel.setSelectedItem(day);
        dayPanel.setForeground(Color.decode(textColor));
        dayPanel.setBackground(Color.decode("#7386D1"));
        dayPanel.setFont(new Font("Calibri",Font.BOLD,24));
        dayPanel.setToolTipText("Day of the week");
        dayPanel.setRenderer(listRenderer);

        hourPanel = new JComboBox<>();
        for(int i = 1; i <= 24; i++) {
            hourPanel.addItem((Integer.toString(i)));
        }
        hourPanel.setSelectedItem(startTime.substring(0,startTime.indexOf(":")));
        hourPanel.setForeground(Color.decode(textColor));
        hourPanel.setBackground(Color.decode("#7386D1"));
        hourPanel.setFont(new Font("Calibri",Font.BOLD,24));
        hourPanel.setToolTipText("Hour of the day (Military Time)");
        hourPanel.setRenderer(listRenderer);

        minutesPanel = new JComboBox<>();
        for(int i = 0; i < 60; i++) {
            if(i < 10) {
                minutesPanel.addItem("0"+ i);
            } else {
                minutesPanel.addItem(Integer.toString(i));
            }

        }
        minutesPanel.setSelectedItem(startTime.substring(startTime.indexOf(":")+1));
        minutesPanel.setForeground(Color.decode(textColor));
        minutesPanel.setBackground(Color.decode("#7386D1"));
        minutesPanel.setFont(new Font("Calibri",Font.BOLD,24));
        minutesPanel.setToolTipText("Minute of the day");
        minutesPanel.setRenderer(listRenderer);

        jClass.add(dayPanel,g);
        g.gridx = 2;
        g.weightx = 2;
        jClass.add(hourPanel,g);
        g.gridx = 3;
        jClass.add(minutesPanel,g);
        g.gridx = 4;
        g.weightx = 1;
        jClass.add(delete,g);
        return jClass;
    }

    public String determineDistance(String time) {
        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int objectDay;
        switch (day) {
            case "Monday":
                objectDay = 2;
                break;
            case "Tuesday":
                objectDay = 3;
                break;
            case "Wednesday":
                objectDay = 4;
                break;
            case "Thursday":
                objectDay = 5;
                break;
            case "Friday":
                objectDay = 6;
                break;
            case "Saturday":
                objectDay = 7;
                break;
            case "Sunday":
                objectDay = 1;
                break;
            default:
                objectDay = 0;
        }

        int currentTime =
                ((objectDay < currentDay ? 7 + objectDay - currentDay : objectDay - currentDay) * 24
                + Integer.parseInt(startTime.substring(0,startTime.indexOf(":")))
                - Integer.parseInt(time.substring(0, time.indexOf(":")))) * 60
                + Integer.parseInt(startTime.substring(startTime.indexOf(":")+1))
                - Integer.parseInt(time.substring(time.indexOf(":")+1));

        int hour = currentTime / 60;
        double tempMinutes = (currentTime / 60.0) % 1 * 60;
        if(tempMinutes % 1 >= 0.5) {
            tempMinutes = Math.ceil(tempMinutes);
        }
        int minutes = (int)tempMinutes;
        return hour + ":" + (minutes < 10 ? "0" : "") + minutes;
    }

    public void delete() {
        ArrayList<Schedule> schedules = Main.gui.editorGUI.getSchedules();
        schedules.remove(this);
        Main.gui.editorGUI.addAllSchedules();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(day, schedule.day) &&
                Objects.equals(startTime, schedule.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startTime, dayPanel, minutesPanel, hourPanel);
    }

    public String toString() {
        return day+"-"+startTime;
    }
}
