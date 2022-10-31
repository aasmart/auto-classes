import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class ClassEditorGUI {
    public final String mainColor = "#1A1D2E";
    public final String textColor = "#C7D9D9";
    String textBoxTemp = "";
    private final ArrayList<Schedule> schedulesTemp;
    private final ArrayList<Resource> resourcesTemp;
    private final LineBorder blankBorder = new LineBorder(Color.black,0,false);
    private JFrame popUp;
    private int editPos = -1;
    JFrame classCreator;
    JPanel scheduleScrollArea;
    JPanel resourceScrollArea;
    Class tempEdit;

    public ClassEditorGUI(String textBoxTemp, ArrayList<Schedule> schedulesTemp, ArrayList<Resource> resourcesTemp, int editPos) {
        this.textBoxTemp = textBoxTemp;
        this.schedulesTemp = schedulesTemp;
        this.resourcesTemp = resourcesTemp;
        if(editPos == -1)
            return;
        tempEdit = (Class) Main.deepCopy(Main.gui.getClasses().get(editPos));
        this.editPos = editPos;
        classEditor();
    }

    public ClassEditorGUI() {
        schedulesTemp = new ArrayList<>();
        resourcesTemp = new ArrayList<>();
        classEditor();
    }

    public JPanel padding(String color) {
        JPanel padding = new JPanel();
        padding.setBackground(Color.decode(color));
        return padding;
    }

    public void addAllSchedules() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        scheduleScrollArea.removeAll();
        JPanel temp = new JPanel();
        temp.setBackground(Color.decode("#5867A1"));
        scheduleScrollArea.add(temp, gbc);
        for(int i = schedulesTemp.size()-1; i >= 0; i--) {
            gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0,0,3,0);
            schedulesTemp.get(i).saveSchedule();
            scheduleScrollArea.add(schedulesTemp.get(i).createPanel(), gbc, 0);
        }
        classCreator.revalidate();
        classCreator.repaint();
    }

    public void addAllResources() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        resourceScrollArea.removeAll();
        JPanel temp = new JPanel();
        temp.setBackground(Color.decode("#5867A1"));
        resourceScrollArea.add(temp, gbc);
        for(int i = resourcesTemp.size()-1; i >= 0; i--) {
            gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0,0,3,0);
            resourcesTemp.get(i).saveResource();
            resourceScrollArea.add(resourcesTemp.get(i).createPanel(), gbc, 0);
        }
        classCreator.revalidate();
        classCreator.repaint();
    }

    public void classEditor() {
        // Main JFrame
        classCreator = new JFrame("Class Editor");
        classCreator.setLayout(new BorderLayout());
        classCreator.setSize(1800,900);
        classCreator.setLocationRelativeTo(null);
        classCreator.getContentPane().setBackground(Color.decode(mainColor));

        //Upper Panel
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setBackground(Color.decode("#0D0F17"));

        //Upper panel text
        JLabel upperText = new JLabel("Class Editor",SwingConstants.CENTER);
        upperText.setBackground(Color.decode(mainColor));
        upperText.setForeground(Color.decode(textColor));
        upperText.setFont(new Font("Calibri",Font.BOLD,48));

        upperPanel.add(upperText,BorderLayout.CENTER);

        //Lower Panel
        JPanel lowerPanel = new JPanel();
        lowerPanel.setBackground(Color.decode(mainColor));
        lowerPanel.setLayout(new FlowLayout());

        //Lower Panel Buttons
        JButton finish = new JButton("Finish");
        createButton(finish);

        JButton cancel = new JButton("Cancel");
        createButton(cancel);

        cancel.addActionListener(e -> {
            if(popUp != null)
                popUp.dispose();
            try {
                Main.gui.getClasses().set(editPos, new Class(tempEdit));
            } catch (Exception ignored) {
            }
            Main.gui.addAllClasses();
            classCreator.dispose();
            Main.gui.setVisible(true);
            Main.gui.setEnabled(true);
        });

        lowerPanel.add(cancel, BorderLayout.SOUTH);
        lowerPanel.add(finish, BorderLayout.SOUTH);

        //Creation area panel
        JPanel createArea = new JPanel();
        createArea.setBorder(new LineBorder(Color.decode("#333B5C"),10,false));
        createArea.setBackground(Color.decode("#475280"));
        createArea.setLayout(new GridLayout(1,3));

        //Name Creation Panel
        JPanel namePanel = new JPanel(new GridLayout(2,1));
        namePanel.setBackground(Color.decode("#475280"));

        //Name Label & Field
        JLabel nameLabel = new JLabel("Class Name",SwingConstants.CENTER);
        nameLabel.setFont(new Font("Calibri",Font.PLAIN,48));
        nameLabel.setForeground(Color.decode(textColor));

        JTextField nameField = new JTextField(textBoxTemp);
        nameField.setBackground(Color.decode("#5867A1"));
        nameField.setForeground(Color.decode(textColor));
        nameField.setFont(new Font("Calibri",Font.PLAIN,48));
        nameField.setBorder(new LineBorder(Color.decode("#475280"),8,false));
        nameField.setHorizontalAlignment(JTextField.CENTER);

        JScrollPane nameFieldScroll = new JScrollPane(nameField);
        nameFieldScroll.setBorder(blankBorder);

        namePanel.add(nameLabel);
        namePanel.add(nameFieldScroll);

        // Schedule Creation Panel
        JPanel schedulePanel = new JPanel(new GridLayout(2,1));
        schedulePanel.setBackground(Color.decode("#475280"));

        // Schedule Name & Main Panel
        JLabel scheduleLabel = new JLabel("Schedule", SwingConstants.CENTER);
        scheduleLabel.setFont(new Font("Calibri",Font.PLAIN,48));
        scheduleLabel.setForeground(Color.decode(textColor));

        JPanel scheduleField = new JPanel(new BorderLayout());
        scheduleField.setBackground(Color.decode("#6879BD"));
        scheduleField.setBorder(new LineBorder(Color.decode("#475280"),8,false));

        // JPanel containing schedules/scrollbar
        scheduleScrollArea = new JPanel(new GridBagLayout());
        scheduleScrollArea.setBackground(Color.decode("#5867A1"));

        JScrollPane temp = new JScrollPane(scheduleScrollArea);
        temp.setBorder(blankBorder);
        addAllSchedules();

        // Add Button
        JButton add = createAddButton();
        add.setToolTipText("Click to add a new schedule!");

        add.addActionListener(e -> {
            schedulesTemp.add(new Schedule());
            addAllSchedules();
        });

        scheduleField.add(temp);
        scheduleField.add(add, BorderLayout.SOUTH);

        schedulePanel.add(scheduleLabel);
        schedulePanel.add(scheduleField);

        // Resources Creation Panel
        JPanel resourcesPanel = new JPanel(new GridLayout(2,1));
        resourcesPanel.setBackground(Color.decode("#475280"));

        // Resources Name & Main Panel
        JLabel resourcesLabel = new JLabel("Class Resources", SwingConstants.CENTER);
        resourcesLabel.setFont(new Font("Calibri",Font.PLAIN,48));
        resourcesLabel.setForeground(Color.decode(textColor));

        JPanel resourceField = new JPanel(new BorderLayout());
        resourceField.setBackground(Color.decode("#6879BD"));
        resourceField.setBorder(new LineBorder(Color.decode("#475280"),8,false));

        // JPanel containing resources/scrollbar
        resourceScrollArea = new JPanel(new GridBagLayout());
        resourceScrollArea.setBackground(Color.decode("#5867A1"));

        temp = new JScrollPane(resourceScrollArea);
        temp.setBorder(blankBorder);

        addAllResources();

        // Add Button
        add = createAddButton();
        add.setToolTipText("Click to add a new resource!");

        add.addActionListener(e -> {
            resourcesTemp.add(new Resource());
            addAllResources();
        });

        resourceField.add(temp);
        resourceField.add(add, BorderLayout.SOUTH);

        resourcesPanel.add(resourcesLabel);
        resourcesPanel.add(resourceField);
        //-----------------------------------------------------

        createArea.add(namePanel);
        createArea.add(schedulePanel);
        createArea.add(resourcesPanel);

        classCreator.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if(popUp != null)
                    popUp.dispose();
                try {
                    Main.gui.getClasses().set(editPos, tempEdit);
                } catch (Exception ignore) {}
                Main.gui.addAllClasses();
                classCreator.dispose();
                Main.gui.setVisible(true);
                Main.gui.setEnabled(true);
            }
        });

        finish.addActionListener(e -> {
            schedulesTemp.forEach(Schedule::saveSchedule);
            resourcesTemp.forEach(Resource::saveResource);
            for(int i = resourcesTemp.size()-1; i >= 0; i--) {
                if(resourcesTemp.get(i).getResource().trim().length() == 0)
                    resourcesTemp.remove(i);
            }
            if(nameField.getText().trim().length() == 0 || getSchedules().size() == 0 || getResources().size() == 0) {
                if(popUp != null) {
                    popUp.dispose();
                }
                popUp = createPopUp("Error!","To Create You Must Have:\n A Name\n At least one scheduled time\n At least one resource",500,500);
                popUp.setVisible(true);
                return;
            }

            if(editPos > -1) {
                Main.gui.addNewClass(nameField.getText(),schedulesTemp,resourcesTemp, editPos);
                classCreator.dispose();
                Main.gui.setVisible(true);
                Main.gui.setEnabled(true);
                Main.checkAnyClasses();
                editPos = -1;
            } else {
                Main.gui.addNewClass(nameField.getText(), schedulesTemp, resourcesTemp);
                classCreator.dispose();
                Main.gui.setVisible(true);
                Main.gui.setEnabled(true);
                Main.checkAnyClasses();
            }
        });

        classCreator.add(lowerPanel,BorderLayout.SOUTH);
        classCreator.add(padding(mainColor),BorderLayout.WEST);
        classCreator.add(padding(mainColor),BorderLayout.EAST);
        classCreator.add(createArea,BorderLayout.CENTER);
        classCreator.add(upperPanel,BorderLayout.NORTH);
        classCreator.setVisible(true);

    }

    public JFrame createPopUp(String title, String popupText, int width, int height) {
        JFrame popUp = new JFrame(title);
        popUp.setLayout(new BorderLayout());
        popUp.setSize(width,height);
        popUp.getContentPane().setBackground(Color.decode(mainColor));
        popUp.setLocationRelativeTo(null);

        // Upper Panel
            JPanel upperPanel = new JPanel();
            upperPanel.setLayout(new BorderLayout());
            upperPanel.setBackground(Color.decode("#0D0F17"));

            // Upper panel text
                JLabel upperText = new JLabel(title,SwingConstants.CENTER);
                upperText.setBackground(Color.decode(mainColor));
                upperText.setForeground(Color.decode("#EE2E2F"));
                upperText.setFont(new Font("Calibri",Font.BOLD,48));

                upperPanel.add(upperText,BorderLayout.CENTER);

        // Popup Text
        JTextArea textPane = new JTextArea(popupText);
        textPane.setEditable(false);
        textPane.setBackground(Color.decode("#5867A1"));
        textPane.setForeground(Color.decode(textColor));
        textPane.setBorder(new LineBorder(Color.decode("#333B5C"),5,false));
        textPane.setFont(new Font("Calibri",Font.BOLD,30));
        textPane.setLineWrap(true);

        JScrollPane textPaneScroll = new JScrollPane(textPane);
        textPaneScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        textPaneScroll.setBorder(blankBorder);


        // Accept Button/Panel
        JPanel acceptPanel = new JPanel(new BorderLayout());
        acceptPanel.setBackground(Color.decode(mainColor));

        JButton accept = new JButton("Okay");
        createButton(accept);
        acceptPanel.add(accept, BorderLayout.CENTER);
        acceptPanel.setBorder(new LineBorder(Color.decode(mainColor),10,false));

        accept.addActionListener(e -> popUp.dispose());

        popUp.add(upperPanel, BorderLayout.NORTH);
        popUp.add(padding(mainColor),BorderLayout.WEST);
        popUp.add(padding(mainColor),BorderLayout.EAST);
        popUp.add(textPane,BorderLayout.CENTER);
        popUp.add(acceptPanel, BorderLayout.SOUTH);

        return popUp;
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

    private void createButton(JButton b) {
        b.setPreferredSize(new Dimension(300,75));
        b.setBackground(Color.decode("#4D517A"));
        b.setForeground(Color.decode(textColor));
        b.setFont(new Font("Calibri",Font.PLAIN,35));
        b.setBorder(new LineBorder(Color.decode("#434669"),4,false));
    }

    public ArrayList<Schedule> getSchedules() {
        return schedulesTemp;
    }

    public ArrayList<Resource> getResources() {
        return resourcesTemp;
    }
}
