import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Resource extends MainGUIComponents implements Serializable {
    private String resource;
    private ResourceType resourceType;
    JTextField resourceText;
    JButton globe;
    JButton computer;
    JButton cross;

    public String getResource() {
        return resource;
    }

    public Resource(String resource, ResourceType resourceType) {
        this.resource = resource;
        this.resourceType = resourceType;
    }

    public Resource() { resourceType = ResourceType.LINK; }

    public void saveResource() {
        try {
            resource = resourceText.getText();
            if(globe.isVisible())
                resourceType = ResourceType.LINK;
            if(computer.isVisible())
                resourceType = ResourceType.FILE;
            if(cross.isVisible())
                resourceType = ResourceType.NONE;
        } catch (Exception ignore) {
        }
    }

    public JPanel createPanel() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = 1;
        g.fill = GridBagConstraints.BOTH;

        int offset;
        String textColor = "#C7D9D9";
        LineBorder blank = new LineBorder(Color.BLACK,0,false);

        JPanel jClass = new JPanel();
        jClass.setLayout(new GridBagLayout());
        jClass.setBackground(Color.decode("#7386D1"));
        jClass.setPreferredSize(new Dimension(200,65));

        resourceText = new JTextField(resource);
        resourceText.setBackground(Color.decode("#7386D1"));
        resourceText.setForeground(Color.decode(textColor));
        resourceText.setFont(new Font("Calibri",Font.PLAIN,24));
        resourceText.setBorder(blank);
        resourceText.setToolTipText("Type here to add a resource (Link)");

        JScrollPane resourceFieldScroll = new JScrollPane(resourceText);
        //resourceFieldScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        resourceFieldScroll.setBorder(new LineBorder(Color.decode(textColor), 1,false));
        resourceFieldScroll.setPreferredSize(new Dimension(100,100));

        globe = new JButton(globeIcon);
        globe.setBackground(Color.decode("#7386D1"));
        globe.setBorder(blank);
        globe.setBounds(100,100,65,65);
        offset = globe.getInsets().left;
        globe.setIcon(resizeIcon(globeIcon, globe.getWidth() - offset, globe.getHeight() - offset));
        globe.setToolTipText("This resource will open in the web browser (Click to Change)!");
        globe.setVisible(resourceType == ResourceType.LINK);
        globe.setEnabled(resourceType == ResourceType.LINK);

        computer = new JButton(computerIcon);
        computer.setBackground(Color.decode("#7386D1"));
        computer.setBorder(blank);
        computer.setBounds(100,100,65,65);
        offset = computer.getInsets().left;
        computer.setIcon(resizeIcon(computerIcon, computer.getWidth() - offset, computer.getHeight() - offset));
        computer.setToolTipText("This resource will run a file (Click to Change)!");
        computer.setVisible(resourceType == ResourceType.FILE);
        computer.setEnabled(resourceType == ResourceType.FILE);

        cross = new JButton(cancelIcon);
        cross.setBackground(Color.decode("#7386D1"));
        cross.setBorder(blank);
        cross.setBounds(100,100,65,65);
        offset = computer.getInsets().left;
        cross.setIcon(resizeIcon(cancelIcon, computer.getWidth() - offset, computer.getHeight() - offset));
        cross.setToolTipText("This resource will not run when called (Click to Change)!");
        cross.setVisible(resourceType == ResourceType.NONE);
        cross.setEnabled(resourceType == ResourceType.NONE);

        globe.addActionListener(e -> {
            computer.setVisible(true);
            computer.setEnabled(true);
            globe.setVisible(false);
            globe.setEnabled(false);
            jClass.validate();
            jClass.repaint();
        });

        computer.addActionListener(e -> {
            cross.setVisible(true);
            cross.setEnabled(true);
            computer.setVisible(false);
            computer.setEnabled(false);
            jClass.validate();
            jClass.repaint();
        });

        cross.addActionListener(e -> {
            globe.setVisible(true);
            globe.setEnabled(true);
            cross.setVisible(false);
            cross.setEnabled(false);
            jClass.validate();
            jClass.repaint();
        });

        JButton delete = new JButton(deleteIcon);
        delete.setBackground(Color.decode("#7386D1"));
        delete.setBorder(blank);
        delete.setBounds(100,100,60,60);
        offset = delete.getInsets().left;
        delete.setIcon(resizeIcon(deleteIcon, delete.getWidth() - offset, delete.getHeight() - offset));
        delete.setToolTipText("Click to delete this resource!");

        delete.addActionListener(e -> delete());

        g.weightx = 10;
        g.gridx = 1;
        jClass.add(resourceFieldScroll, g);
        g.weightx = 1;
        g.gridx = 2;
        jClass.add(globe, g);
        jClass.add(computer, g);
        jClass.add(cross, g);
        g.gridx = 3;
        jClass.add(delete, g);
        return jClass;
    }

    public void delete() {
        ArrayList<Resource> resources = Main.gui.editorGUI.getResources();
        resources.remove(this);
        Main.gui.editorGUI.addAllResources();
    }

    public static ArrayList<Resource> parseResources(String resource) {
        ArrayList<Resource> temp = new ArrayList<>();
        for(String s : resource.split(" ")) {
            String[] split = s.split("-");
            temp.add(new Resource(split[0], ResourceType.valueOf(split[1])));
        }
        return temp;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource1 = (Resource) o;
        return Objects.equals(resource, resource1.resource) &&
                Objects.equals(resourceText, resource1.resourceText);
    }

    @Override
    public String toString() {
        return resource + "-" + resourceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, resourceText);
    }
}
