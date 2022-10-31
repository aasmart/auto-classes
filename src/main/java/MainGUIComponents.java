import javax.swing.*;
import java.awt.*;

public abstract class MainGUIComponents {
    ImageIcon deleteIcon = new ImageIcon("C:\\Users\\Alexander\\IdeaProjects\\AutoClasses\\xbutton.png");
    ImageIcon globeIcon = new ImageIcon("C:\\Users\\Alexander\\IdeaProjects\\AutoClasses\\globe.png");
    ImageIcon computerIcon = new ImageIcon("C:\\Users\\Alexander\\IdeaProjects\\AutoClasses\\computer.png");
    ImageIcon cancelIcon = new ImageIcon("C:\\Users\\Alexander\\IdeaProjects\\AutoClasses\\cancelIcon.png");
    public Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
