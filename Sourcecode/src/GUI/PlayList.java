

package GUI;

import Main.Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class PlayList extends JTable{
    public DefaultTableModel model;
    public JPopupMenu rightMenu;
    Vector column, row;
    JFileChooser cs;
    String name = "PlayList";    
    File nowPlayFile = null;
    int current = 0;
    public static List<File> fileList;
    Player player;
    JMenuItem playI, removeI, clearI, openfile;
    int ro = 0;

    public PlayList(Player p){
        player = p;
        fileList = new LinkedList<File>();
        column = new Vector();
        row = new Vector();        
        column.addElement(this.getPlayListName());
        model = new DefaultTableModel() {
            @Override
			public boolean isCellEditable(int r, int col) {
				return false;
			}
		};
        model.addColumn(column);
        setModel(model);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    ro = rowAtPoint(e.getPoint());
                    if(ro != -1) {
                        setCurrent(ro);
                        setRowSelectionInterval(ro, ro);
                        if(player != null)
                        player.stop();
                        player.play();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
                ro = rowAtPoint(e.getPoint());
                setRowSelectionInterval(ro, ro);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
                ro = rowAtPoint(e.getPoint());
                setRowSelectionInterval(ro, ro);
            }

            private void showPopup(MouseEvent e) {
              if (e.isPopupTrigger()) {
                rightMenu.show(e.getComponent(), e.getX(), e.getY());
              }
            }
        });

        rightMenu = new JPopupMenu();

        playI = new JMenuItem("Play");
        removeI = new JMenuItem("Remove");
        clearI = new JMenuItem("Clear All");
        openfile = new JMenuItem("Open Containing Folder");

        rightMenu.add(playI);
        rightMenu.add(removeI);
        rightMenu.addSeparator();
        rightMenu.add(clearI);
        rightMenu.addSeparator();
        rightMenu.add(openfile);

        add(rightMenu);

        playI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setCurrent(ro);
                if(player != null)
                player.stop();
                player.play();
            }
        });

        removeI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(ro == getCurrent()){                    
                    if(fileList.size() > 1){
                player.next();
                        model.removeRow(ro);
                        fileList.remove(ro);                                              
                    }else{
                        player.stop();
                        model.removeRow(ro);
                        fileList.remove(ro);
                    }
                }else{
                    model.removeRow(ro);
                    fileList.remove(ro);
                }
               
                
            }
        });

        clearI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                reset();
                PlayerGUI.timeL.setText("00:00:00 / 00:00:00");
            }
        });

        openfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                File f = fileList.get(ro);
                ProcessBuilder p = null;
                if(getOS().contains("Win")){
                    p = new ProcessBuilder("explorer.exe", f.getParent());
                }else if(getOS().contains("Linux")){
                    p = new ProcessBuilder("nautilus", f.getParent());
                }                
                try {
                    p.start();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Message", JOptionPane.ERROR_MESSAGE, null);
                }
            }
        });

    }

    public String getOS(){
        String os = System.getProperty("os.name");
        return os;
    }

    public int getPlayListSize(){
        return fileList.size();
    }


    public void reset(){
        while(model.getRowCount() != 0){
            for (int i = 0; i < model.getRowCount(); i++) {
                model.removeRow(i);
                player.stop();
                PlayList.fileList = null;
            }
        }        
        
    }

    public void setCurrent(int index){
        this.current = index;
    }

    public int getCurrent(){
        return this.current;
    } 



    public void addToTable(File file){
        row = new Vector();
        row.addElement(file.getName());
        model.addRow(row);
    }

    public void clearAll(){
        
    }

    public String getPlayListName(){
        return this.name;
    }

    public void setPlayListName(String name){
        this.name = name;
    }

    public File getFile(int index){
        return fileList.get(index);
    } 

}

