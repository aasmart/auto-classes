import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

public class Class extends MainGUIComponents implements Serializable {
    private final String className;
    private final ArrayList<Schedule> schedules;
    private final ArrayList<Resource> resources;
    private String timeUntilStart = "00:00";

    public Class(String className, ArrayList<Schedule> schedules, ArrayList<Resource> resources) {
        this.className = className;
        this.schedules = schedules;
        this.resources = resources;
    }

    public Class(Class c) {
        this.className = c.className;
        this.schedules = c.schedules;
        this.resources = c.resources;
        this.timeUntilStart = c.timeUntilStart;
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void run() {
        Desktop desktop = java.awt.Desktop.getDesktop();
        // Resources
        for(Resource r : resources) {
            try {
                if(!r.getResource().equals("none")) {
                    if(r.getResourceType() == ResourceType.LINK) {
                        URI zoomURL = new URI(r.getResource());
                        desktop.browse(zoomURL);
                    } else if(r.getResourceType() == ResourceType.FILE){
                        Main.runtime.exec(r.getResource());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error Opening:" + r.getResource());
            }
        }
    }

    public String getCSV() {
        StringBuilder schedule = new StringBuilder();
        StringBuilder resource = new StringBuilder();
        for(Schedule s : schedules) {
            schedule.append(s.toString()).append(" ");
        }
        for(Resource r : resources) {
            resource.append(r.toString()).append(" ");
        }

        return className + "," + schedule.toString() + "," + resource.toString();
    }

    public JPanel createPanel() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 1;
        g.gridy = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.BOTH;

        int offset;
        String textColor = "#C7D9D9";
        LineBorder blank = new LineBorder(Color.BLACK,0,false);

        JPanel jClass = new JPanel(new GridBagLayout());
        jClass.setBackground(Color.decode("#7386D1"));
        jClass.setPreferredSize(new Dimension(300,104));

        JPanel moveButtons = new JPanel(new GridLayout(2,1));
        moveButtons.setBackground(Color.decode("#7386D1"));
        moveButtons.setBorder(blank);

        JButton moveUp = new JButton("^");
        moveUp.setForeground(Color.decode(textColor));
        moveUp.setBackground(Color.decode("#6879BD"));
        moveUp.setFont(new Font("Calibri",Font.BOLD,44));
        moveUp.setBorder(blank);

        JButton moveDown = new JButton("v");
        moveDown.setForeground(Color.decode(textColor));
        moveDown.setBackground(Color.decode("#6879BD"));
        moveDown.setFont(new Font("Calibri",Font.BOLD,40));
        moveDown.setBorder(blank);

        moveUp.addActionListener(e -> swap(false));

        moveDown.addActionListener(e -> swap(true));

        moveButtons.add(moveUp);
        moveButtons.add(moveDown);

        JButton delete = new JButton(deleteIcon);
        delete.setForeground(Color.RED);
        delete.setBackground(Color.decode("#7386D1"));
        delete.setFont(new Font("Calibri",Font.BOLD,48));
        delete.setBorder(blank);
        delete.setBounds(100,100,60,60);
        offset = delete.getInsets().left;
        delete.setIcon(resizeIcon(deleteIcon, delete.getWidth() - offset, delete.getHeight() - offset));
        delete.setToolTipText("Click to delete this class!");

        delete.addActionListener(e -> delete());

        JLabel classNameLabel = new JLabel(" " + getClassName(),SwingConstants.CENTER);
        classNameLabel.setForeground(Color.decode(textColor));
        classNameLabel.setBackground(Color.decode("#7386D1"));
        classNameLabel.setFont(new Font("Calibri",Font.BOLD,40));
        classNameLabel.setToolTipText("Class Name!");
        classNameLabel.setOpaque(true);

        JScrollPane classNameScroll = new JScrollPane(classNameLabel);
        classNameScroll.setBackground(Color.decode("#7386D1"));
        classNameScroll.setBorder(blank);

        ImageIcon editIcon = new ImageIcon("C:\\Users\\alexa\\IdeaProjects\\AutoClasses\\editpencil.png");
        JButton editIconButton = new JButton(editIcon);
        editIconButton.setBackground(Color.decode("#7386D1"));
        editIconButton.setBorder(blank);
        editIconButton.setBounds(100,100,60,60);
        offset = editIconButton.getInsets().left;
        editIconButton.setIcon(resizeIcon(editIcon, editIconButton.getWidth() - offset, editIconButton.getHeight() - offset));
        editIconButton.setToolTipText("Click to edit this class!");

        editIconButton.addActionListener(e -> edit());

        JLabel startTimeLabel = new JLabel(timeUntilStart,SwingConstants.CENTER);
        startTimeLabel.setForeground(Color.decode(textColor));
        startTimeLabel.setBackground(Color.decode("#7386D1"));
        startTimeLabel.setFont(new Font("Calibri",Font.BOLD,40));
        startTimeLabel.setToolTipText("Time until class starts (Will not run unless started)!");
        startTimeLabel.setOpaque(true);

        JScrollPane startTimeScroll = new JScrollPane(startTimeLabel);
        startTimeScroll.setBackground(Color.decode("#7386D1"));
        startTimeScroll.setBorder(blank);

        jClass.add(moveButtons, g);
        g.weightx = 9;
        g.gridx = 2;
        jClass.add(classNameScroll,g);
        g.weightx = 3;
        g.gridx = 3;
        jClass.add(startTimeScroll, g);
        g.weightx = 1;
        g.gridx = 4;
        jClass.add(editIconButton, g);
        g.gridx = 5;
        jClass.add(delete, g);
        return jClass;
    }

    public void swap(boolean down) {
        ArrayList<Class> classes = Main.gui.getClasses();
        int otherPos = down ? 1 : -1;
        int myPos = classes.indexOf(this);
        try {
            Class temp = classes.get(myPos);
            classes.set(myPos, classes.get(myPos + otherPos));
            classes.set(myPos + otherPos, temp);
        } catch (Exception ignore) {}
        Main.gui.addAllClasses();
    }

    public void delete() {
        ArrayList<Class> classes = Main.gui.getClasses();
        classes.remove(this);
        Main.gui.addAllClasses();
    }

    public void edit() {
        ArrayList<Class> classes = Main.gui.getClasses();
        Main.gui.editorGUI = new ClassEditorGUI(className, schedules, resources, classes.indexOf(this));
        Main.gui.setEnabled(false);
        Main.gui.setVisible(false);
    }

    public void setTime(String timeUntilStart) {
        this.timeUntilStart = timeUntilStart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class aClass = (Class) o;
        return Objects.equals(className, aClass.className) &&
                Objects.equals(schedules, aClass.schedules) &&
                Objects.equals(resources, aClass.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, schedules, resources);
    }

    @Override
    public String toString() {
        return "Class.Class{" +
                "className='" + className + '\'' +
                ", schedules=" + schedules +
                ", resources=" + resources +
                ", timeUntilStart='" + timeUntilStart + '\'' +
                '}';
    }
}
