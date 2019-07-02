

package Main;

import GUI.PlayerGUI;

public class Main {
    public static void main(String arg[]){
        PlayerGUI p = new PlayerGUI();
        p.setVisible(true);
         new Player(p);         
    }
}
