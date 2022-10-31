import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class ClassesGUI extends JFrame {
    private final String mainColor = "#1A1D2E";
    private final String textColor = "#C7D9D9";
    private final ArrayList<Class> classes;
    private final LineBorder blankBorder = new LineBorder(Color.black,0,false);
    ClassEditorGUI editorGUI;
    JPanel classPanel;

    public ClassesGUI(ArrayList<Class> classes) {
        this.classes = classes;

        setTitle("Class Manager");
        setLayout(new BorderLayout());
        int mainWidth = 1000;
        int mainHeight = 1000;
        setSize(mainWidth, mainHeight);
        setLocationRelativeTo(null);
        setBackground(Color.decode(mainColor));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UIManager.put("ToolTip.background", Color.decode(mainColor));
        UIManager.put("ToolTip.foreground", Color.decode(textColor));
        UIManager.put("ScrollBar.background", Color.decode(mainColor));
        UIManager.put("ScrollBar.thumb", Color.RED);

        add(upper(), BorderLayout.NORTH);
        add(roster(), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);
        add(padding(mainColor),BorderLayout.WEST);
        add(padding(mainColor),BorderLayout.EAST);

        setVisible(true);
    }

    public JPanel padding(String color) {
        JPanel padding = new JPanel();
        padding.setBackground(Color.decode(color));
        return padding;
    }

    public JPanel upper() {
        JPanel upper = new JPanel();
        upper.setBackground(Color.decode(mainColor));
        upper.setLayout(new BorderLayout());

        JLabel upperText = new JLabel("Class Manager",SwingConstants.CENTER);
        upperText.setFont(new Font("Calibri",Font.BOLD,40));
        upperText.setForeground(Color.decode(textColor));

        upper.add(upperText,BorderLayout.NORTH);

        return upper;
    }

    public void addAllClasses() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        classPanel.removeAll();
        JPanel temp = new JPanel();
        temp.setBackground(Color.decode("#475280"));
        classPanel.add(temp, gbc);
        for(int i = classes.size()-1; i >= 0; i--) {
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(0,0,5,0);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            classPanel.add(classes.get(i).createPanel(), gbc, 0);
        }
        validate();
        repaint();
    }

    public JPanel roster() {
        JPanel roster = new JPanel();
        roster.setBackground(Color.decode(mainColor));
        roster.setLayout(new BorderLayout());
        roster.setBorder(new LineBorder(Color.decode("#333B5C"),10,false));

        JButton add = createAddButton();
        add.setToolTipText("Click to add a new class!");

        classPanel = new JPanel(new GridBagLayout());
        classPanel.setBackground(Color.decode("#475280"));

        addAllClasses();

        add.addActionListener(e -> {
            editorGUI = new ClassEditorGUI();
            setEnabled(false);
        });

        JScrollPane temp = new JScrollPane(classPanel);
        temp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        temp.setBorder(blankBorder);

        roster.add(add, BorderLayout.SOUTH);
        roster.add(temp);

        return roster;
    }

    public JPanel buttons() {
        final int buttonWidth = 315;
        final int buttonHeight = 100;
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.decode("#384063"));
        buttons.setLayout(new FlowLayout());
        buttons.setBorder(new LineBorder(Color.decode(mainColor),10,false));

        LineBorder buttonBorder = new LineBorder(Color.decode("#434669"),4,false);

        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        save.setFont(new Font("Calibri",Font.PLAIN,35));
        save.setBorder(buttonBorder);
        save.setBackground(Color.decode("#4D517A"));
        save.setForeground(Color.decode(textColor));

        JButton cancel = new JButton("Exit");
        cancel.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        cancel.setFont(new Font("Calibri",Font.PLAIN,35));
        cancel.setBorder(buttonBorder);
        cancel.setBackground(Color.decode("#4D517A"));
        cancel.setForeground(Color.decode(textColor));

        JButton start = new JButton("Start");
        start.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        start.setFont(new Font("Calibri",Font.PLAIN,35));
        start.setBorder(buttonBorder);
        start.setBackground(Color.decode("#4D517A"));
        start.setForeground(Color.decode(textColor));

        JButton stop = new JButton("Stop");
        stop.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
        stop.setFont(new Font("Calibri",Font.PLAIN,35));
        stop.setBorder(buttonBorder);
        stop.setBackground(Color.decode("#4D517A"));
        stop.setForeground(Color.decode(textColor));
        stop.setVisible(false);
        stop.setEnabled(false);

        save.addActionListener(e -> ClassData.write(classes));

        cancel.addActionListener(e -> System.exit(0));

        start.addActionListener(e -> {
            Main.startUp();
            Main.checkAnyClasses();
            start.setEnabled(false);
            start.setVisible(false);
            stop.setVisible(true);
            stop.setEnabled(true);
            repaint();
            revalidate();
        });

        stop.addActionListener(e -> {
            Main.scheduleHandle.cancel(true);
            stop.setEnabled(false);
            stop.setVisible(false);
            start.setVisible(true);
            start.setEnabled(true);
            repaint();
            revalidate();
        });

        buttons.add(save);
        buttons.add(cancel);
        buttons.add(start);
        buttons.add(stop);

        return buttons;
    }

    public void addNewClass(String name, ArrayList<Schedule> schedules, ArrayList<Resource> resources) {
        Class c = new Class(
                name,
                new ArrayList<>(schedules),
                new ArrayList<>(resources));
        classes.add(c);
        addAllClasses();
    }

    public void addNewClass(String name, ArrayList<Schedule> schedules, ArrayList<Resource> resources, int index) {
        Class c = new Class(
                name,
                new ArrayList<>(schedules),
                new ArrayList<>(resources));
        classes.set(index,c);
        addAllClasses();
    }

    private JButton createAddButton() {
        JButton add = new JButton("+");
        add.setBackground(Color.decode("#4D517A"));
        add.setForeground(Color.decode(textColor));
        add.setBorder(new LineBorder(Color.decode("#434669"),5,false));
        add.setPreferredSize(new Dimension(100,50));
        add.setFont(new Font("Calibri",Font.PLAIN,35));
        return add;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }
}