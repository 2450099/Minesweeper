import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * Shows an image.
 * To use, 
 * Image image = new Image("filename.jpg");
 */

public class Image {

    private JLabel lbl;

    /** 
     * show the image in the given file
     */
    public Image(String file)
    {
        try {
            BufferedImage img=ImageIO.read(new File(file));
            ImageIcon icon=new ImageIcon(img);
            JFrame frame=new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(500,500);
            lbl=new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * changes the image to a new one
     */
    public void setImage(String file) {
        try {
            BufferedImage img=ImageIO.read(new File(file));
            ImageIcon icon=new ImageIcon(img);

            lbl.setIcon(icon);
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}