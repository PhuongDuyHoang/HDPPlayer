package GUI;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class VolumeChangeProcess implements ChangeListener{

    Main.Player player;
    public VolumeChangeProcess(Main.Player player){
        this.player = player;
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        float f = PlayerGUI.volume.getValue();
        player.setVolumn(f/100);
    }

}
