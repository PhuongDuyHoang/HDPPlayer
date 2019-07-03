

package GUI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

public class Author extends JDialog{
    JPanel aboutP;
    JLabel l;
    JTextPane aboutTP;
 
    public Author(){
        setModal(true);
        setTitle("Author");
        setBounds(150, 150, 400, 380);
        setResizable(false);
        aboutP = new JPanel();
        add(aboutP);
        l = new JLabel();
  
        aboutP.add(l);
   
        aboutP.setBackground(Color.white);
        aboutP.setLayout(new FlowLayout(FlowLayout.CENTER));
        l.setIcon(new ImageIcon("images/logo.jpg"));
        
        aboutTP = new JTextPane();
        aboutTP.setEditable(false);
        aboutP.add(aboutTP);
        
        aboutTP.setText("Họ và tên sinh viên: Hoàng Duy Phương "+"\n"+ "MSSV: 16520965");
        aboutTP.setBackground(Color.pink);
        aboutTP.setForeground(Color.RED);
        TitledBorder titleBorder = new TitledBorder("HDPPlayer");
        aboutTP.setBorder(titleBorder);
    

      
    }
}
