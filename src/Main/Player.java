
package Main;

import GUI.PlayList;
import GUI.PlayerGUI;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Player implements Runnable, ControllerListener {

    public PlayList playList;
    public static javax.media.Player player;
    PlayerGUI playerGUI;
    Thread playThread = null;
    JFileChooser cs;
    File file = null;
    public static boolean isPlaying = false;

    public Player(PlayerGUI playergui) {
        this.playerGUI = playergui;
    }

    public void setVolumn(float volume) {
        try{
            player.getGainControl().setLevel(volume);
        }catch(Exception e){

        }
    }

    public void openFile() {
        cs = new JFileChooser();
        File[] list = null;
        cs.setFileFilter(new FileNameExtensionFilter("Audio File (*.mp3, *.wav)", "mp3", "wav"));
        cs.setMultiSelectionEnabled(true);
        if (cs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            PlayerGUI.playList.reset();
            removePlayer();
            PlayList.fileList = new LinkedList<File>();
            list = cs.getSelectedFiles();
            for (int i = 0; i < list.length; i++) {
                PlayList.fileList.add(list[i]);
                PlayerGUI.playList.addToTable(list[i]);
            }
            PlayerGUI.playList.setCurrent(0);
            PlayerGUI.playList.setRowSelectionInterval(0, 0);
        }
        play();
        isPlaying = true;
    }


    public void removePlayer() {
        if (player != null) {
            player.stop();
            player.removeController(player);
            player.close();
            player = null;
        }
    }

    public void openPlayList() {
        File list = null;
        BufferedReader reader;
        String line;
        File f;
        cs = new JFileChooser();
        cs.setFileFilter(new FileNameExtensionFilter("PlayList File (*.m3u)", "m3u"));
        cs.setMultiSelectionEnabled(false);
        if (cs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            removePlayer();
            try {
                PlayerGUI.playList.reset();
                PlayList.fileList = new LinkedList<File>();
                list = cs.getSelectedFile();
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(list), "UTF-8"));
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    if(line.startsWith("#")){
                        
                    }else{
                        f = new File(line);
                        PlayList.fileList.add(f);
                        PlayerGUI.playList.addToTable(f);
                        reader.readLine();
                    }                    
                }

                PlayerGUI.playList.setCurrent(0);
                PlayerGUI.playList.setRowSelectionInterval(0, 0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Mesage", JOptionPane.ERROR_MESSAGE, null);
            }
        }
        play();
        isPlaying = true;
    }

    public void saveNPL() {
        cs = new JFileChooser();
        cs.setFileFilter(new FileNameExtensionFilter("PlayList File (*.m3u)", "m3u"));
        BufferedWriter writer;
        if (cs.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = cs.getSelectedFile();
                int r = 5;
                if(f.exists()){
                    r = JOptionPane.showConfirmDialog(playerGUI, "File "+ f.getName() + " already exist! Overwrite?", "Question ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                }
                if(!f.exists() || r == JOptionPane.YES_OPTION){
                    if (f.getName().endsWith("m3u")) {
                        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
                    } else {
                        f = new File(f.getPath() + ".m3u");
                        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
                    }

                    try {
                        writer.write("#EXTM3U");
                        writer.newLine();
                        for (int i = 0; i < PlayList.fileList.size(); i++) {
                            writer.write("#EXTINF:0,"+PlayList.fileList.get(i).getPath());
                            writer.newLine();
                            writer.write(PlayList.fileList.get(i).getPath());
                            writer.newLine();
                            writer.newLine();
                        }
                        writer.close();
                    } catch (IOException ex) {
                        errorMessage(ex.getMessage());
                    }
                }else{
                    JOptionPane.showMessageDialog(playerGUI, "No overwite. No file selected.", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (UnsupportedEncodingException ex) {
                errorMessage(ex.getMessage());
            } catch (FileNotFoundException ex) {
                errorMessage(ex.getMessage());
            }
        }
    }

    public void play() {
        URL fileToPlay;
        File filePlay = null;
        try {
            int position = PlayerGUI.playList.getCurrent();//vt bh hien tai
            filePlay = PlayerGUI.playList.getFile(position); 
            file = new File(filePlay.getPath());//chon file
            if (player == null) {//bat bai hat
                try {
                    fileToPlay = new URL("file:///" + file.getPath());
                    player = Manager.createPlayer(fileToPlay);
                    player.addControllerListener(this);
                    PlayerGUI.progress.setString("Realizing...");
                   player.realize();
                    isPlaying = true;
                    
                } catch (MalformedURLException ex) {
                    errorMessage(ex.getMessage());
                } catch (NoPlayerException ex) {
                    errorMessage(ex.getMessage());
                } catch (IOException ex) {
                    errorMessage(ex.getMessage());
                }
            } else 
            if (player != null) {
                if (!isPlaying) {
                    try {
                        player.start();
                        isPlaying = true;
                    } catch (Exception e) {
                        errorMessage(e.getMessage());
                    }
                    PlayerGUI.progress.setString("Playing : " + file.getName());
                } else {
                }
            } 
            if(isPlaying){
                playerGUI.playB.setIcon(new ImageIcon("images/pause.jpg"));
                playerGUI.playB.setActionCommand("Pause");
            
            }
            
            
        } catch (Exception e) {
            
        }
    }

    public void errorMessage(String mess) {
        JOptionPane.showMessageDialog(null, mess, "Error", JOptionPane.ERROR_MESSAGE, null);
    }

    public void run() {        

        while (playThread != null) {
            if (player != null) {
                javax.media.Time time = player.getMediaTime();
                PlayerGUI.progress.setValue((int) time.getSeconds());
                PlayerGUI.timeL.setText(timeToString(player.getMediaTime().getSeconds()) + " / " + timeToString(player.getDuration().getSeconds()));
                float f = PlayerGUI.volume.getValue();
                setVolumn(f/100);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public void setSlow() {
        player.stop();
        player.setRate(0.8f);
        if(isPlaying){
            player.start();
        }
        
    }

    public void setNormal() {
        player.stop();
        player.setRate(1f);
        if(isPlaying){
            player.start();
        }
    }

    public void setFast() {
        player.stop();
        player.setRate(1.5f);
        if(isPlaying){
            player.start();
        }
    }

    public void pause() {
        if (isPlaying) {
            try {
                player.stop();
                PlayerGUI.progress.setString("Pause");
                isPlaying = false;                
                playerGUI.playB.setIcon(new ImageIcon("images/play.jpg"));
                playerGUI.playB.setActionCommand("Play");
            
            } catch (Exception e) {
            }
        } else {
        }

    }

    public void stop() {
        if (player != null) {
            player.removeControllerListener(this);
            player.stop();
            player.close();
            player = null;
        }
        if (playThread != null) {
            playThread = null;
        }

        playerGUI.playB.setIcon(new ImageIcon("images/play.jpg"));
        playerGUI.playB.setActionCommand("Play");
       
        PlayerGUI.progress.setValue(0);
        PlayerGUI.progress.setString("");
        PlayerGUI.timeL.setText("00:00:00 / 00:00:00");
    }

    public void exit() {
        if (player != null) {
            if (player != null) {
                player.removeControllerListener(this);
                player.stop();
                player.close();
                player = null;
            }
            System.exit(0);
        }else{
            removePlayer();
            System.exit(0);
        }
    }

    public void quit(){
        int i = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "JPlayer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (i == JOptionPane.NO_OPTION) {
                    playerGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                } else if (i == JOptionPane.YES_OPTION) {
                    exit();
                }
    }

    public void next() {
        try{
            int position = PlayerGUI.playList.getCurrent();

            if (position == PlayerGUI.playList.getRowCount() - 1) {
                position = 0;
                PlayerGUI.playList.setCurrent(position);
                stop();
            } else if (position < PlayerGUI.playList.getRowCount()) {
                position++;
                stop();
            }
            PlayerGUI.playList.setCurrent(position);
            PlayerGUI.playList.setRowSelectionInterval(position, position);
            if (player != null) {
                stop();
            }
            GUI.PlayerGUI.normal.setSelected(true);

            play();
            
        }catch (Exception e){
            
        }

    }

    public void previous() {
        try {
            int position = PlayerGUI.playList.getCurrent();
            if (position == 0) {
                position = PlayerGUI.playList.getRowCount() - 1;
                stop();
            } else if (position > 0) {
                position--;
                stop();
            }
            PlayerGUI.playList.setCurrent(position);
            PlayerGUI.playList.setRowSelectionInterval(position, position);
            if (player != null) {
                stop();
            }
            GUI.PlayerGUI.normal.setSelected(true);
            play();
        }catch(Exception e){
            
        }
    }
    public void shuffle() {
        try{
            int position = PlayerGUI.playList.getCurrent();

            if (position == PlayerGUI.playList.getRowCount() - 1) {
                position = 0;
                PlayerGUI.playList.setCurrent(position);
                stop();
            } else if (position < PlayerGUI.playList.getRowCount()) {
           
          position=(int)(Math.random()*(PlayerGUI.playList.getRowCount()-1));
           
                stop();
            }
            PlayerGUI.playList.setCurrent(position);
            PlayerGUI.playList.setRowSelectionInterval(position, position);
            if (player != null) {
                stop();
            }
            GUI.PlayerGUI.normal.setSelected(true);

            play();
            
        }catch (Exception e){
            
        }

    }
     public void first() {
        try{
            int position = PlayerGUI.playList.getCurrent();

            if (position !=0) {
                position = 0;
                PlayerGUI.playList.setCurrent(position);
                stop();
            } 
            PlayerGUI.playList.setCurrent(position);
            PlayerGUI.playList.setRowSelectionInterval(position, position);
            if (player != null) {
                stop();
            }
            GUI.PlayerGUI.normal.setSelected(true);

            play();
            
        }catch (Exception e){
            
        }

    }
      public void last() {
        try{
            int position = PlayerGUI.playList.getCurrent();

            if (position != PlayerGUI.playList.getRowCount()-1 ) {
                position = PlayerGUI.playList.getRowCount()-1;
                PlayerGUI.playList.setCurrent(position);
                stop();
            }
            PlayerGUI.playList.setCurrent(position);
            PlayerGUI.playList.setRowSelectionInterval(position, position);
            if (player != null) {
                stop();
            }
            GUI.PlayerGUI.normal.setSelected(true);

            play();
            
        }catch (Exception e){
            
        }

    }
 

    public String timeToString(double d) {
        int hour = (int) (d / 3600);
        int scR = (int) (d - hour * 3600);
        int minu = (int) (scR / 60);
        scR = (int) (scR - minu * 60);
        String h = "", m = "", s = "";
        if (hour < 10) {
            h = "0" + hour;
        } else {
            h = "" + hour;
        }
        if (minu < 10) {
            m = "0" + minu;
        } else {
            m = "" + minu;
        }
        if (scR < 10) {
            s = "0" + scR;
        } else {
            s = "" + scR;
        }


        return h + ":" + m + ":" + s;
    }

    public void controllerUpdate(ControllerEvent ce) {
        if (ce instanceof RealizeCompleteEvent) {
            player.prefetch();
            PlayerGUI.progress.setString("Prefetching...");
        }
        if (ce instanceof PrefetchCompleteEvent) {
            Time time = player.getDuration();
            PlayerGUI.progress.setMaximum((int) time.getSeconds());
            PlayerGUI.progress.setString("Playing : " + file.getName());
            playThread = new Thread(this);
            playThread.start();
            player.start();
            isPlaying = true;
        }

        if (ce instanceof EndOfMediaEvent) {
            player.removeControllerListener(this);
            player.stop();
            player.close();
            player = null;
            if (playThread != null) {
                playThread = null;
            }
            PlayerGUI.progress.setValue(0);
            int position = PlayerGUI.playList.getCurrent();
         if(position == (PlayerGUI.playList.getPlayListSize() - 1)){
                if(PlayerGUI.repeatMI.isSelected()){
                    next();
                }else{
                    stop();
                }
            } else{ next(); 
            } 
         }
            
        } }       
    

 
    

