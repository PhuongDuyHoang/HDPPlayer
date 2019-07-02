

package GUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;


public class EventProcess implements ActionListener{

    PlayerGUI pFrame;
    JFileChooser cs;
    Main.Player player;
    PlayList playList;
    

    public EventProcess(PlayerGUI pF){
        this.pFrame = pF;
        this.player = pFrame.player;        
    }  

    public void actionPerformed(ActionEvent e) {
 
          
        if(e.getActionCommand().equalsIgnoreCase("Open Files")){
            player.openFile();            
        }        
        if(e.getActionCommand().equalsIgnoreCase("Pause")){
            player.pause();            
                
        }
        if(e.getActionCommand().equalsIgnoreCase("Play")){
            player.play();     
                     
        }
        if(e.getActionCommand().equalsIgnoreCase("Stop")){
            player.stop();
        }
        if(e.getActionCommand().equalsIgnoreCase("Shuffle")){
            player.shuffle();
        }
         
        if((e.getActionCommand().equalsIgnoreCase("Previous"))){
            player.previous();
        }
        if(e.getActionCommand().equalsIgnoreCase("Next")){
            player.next();
            PlayerGUI.normal.setSelected(true);
        }
         if(e.getActionCommand().equalsIgnoreCase("First")){
            player.first();
        }
        if(e.getActionCommand().equalsIgnoreCase("Last")){
            player.last();
        }
        if(e.getActionCommand().equalsIgnoreCase("Exit")){
            player.quit();
        }
        if(e.getActionCommand().equalsIgnoreCase("Slow")){
            player.setSlow();
        }
        if(e.getActionCommand().equalsIgnoreCase("Normal")){
            player.setNormal();
        }
        if(e.getActionCommand().equalsIgnoreCase("Fast")){
            player.setFast();
        }
        if(e.getActionCommand().equalsIgnoreCase("About")){
            new Author().setVisible(true);
        }
        if(e.getActionCommand().equalsIgnoreCase("Save NowPlaying List")){
            player.saveNPL();
        }
        if(e.getActionCommand().equalsIgnoreCase("Open PlayList")){
            player.openPlayList();        
        } 
        
    }

}