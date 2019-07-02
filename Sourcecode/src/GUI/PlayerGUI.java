

package GUI;

import Main.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.Time;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

public class PlayerGUI extends javax.swing.JFrame {

    Toolkit kit;
    int screenHeight, screenWidth;
    JMenu fileM, playM, helpM;
    JMenuItem openFilesMI, exitMI, saveNPL, openPL, changeBackground;
    public JMenuItem  setSpeedMI;
    JMenuItem aboutMI;
    public static JRadioButtonMenuItem slow, normal, fast;
    public static JCheckBoxMenuItem repeatMI;
    JPanel mainPanel, toolBarPanel, buttonPanel, progressPanel;
    JScrollPane playListPanel;
    public static PlayList playList;
    public static JButton playB, stopB, nextB, preB,shuffleB,lastB,firstB;
    public static JProgressBar progress;
    JLabel mainL;
    public static JLabel timeL;
    public static JSlider volume;
    ButtonGroup group;
    Main.Player player;
    Player p = new Player(this);
    JButton okB;
    JLabel vol;
    String background;
    JMenuItem[] listMenuItem = null;
    LookAndFeelInfo[] listSkin;

    public PlayerGUI() {
        player = new Main.Player(this);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent arg0) {
                int i = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "JPlayer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (i == JOptionPane.NO_OPTION) {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                } else if (i == JOptionPane.YES_OPTION) {
                    player.exit();
                }
            }
        });

        kit = Toolkit.getDefaultToolkit();
        Image icon = kit.getImage("images/icon.png");
        setIconImage(icon);
        setVisible(true);
        setMinimumSize(new Dimension(770, 550));
        setMaximumSize(new Dimension(1024, 768));

        setTitle("HDPPlayer");
        setBounds(100, 100, 980, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(createMenuBar(), BorderLayout.NORTH);
        mainPanel = new JPanel();
        mainL = new JLabel(new ImageIcon("images/1.jpg"));
        background = "1";
        mainPanel.add(mainL);

        add(mainPanel, BorderLayout.CENTER);
        playList = new PlayList(player);
        playList.setVisible(true);
        playListPanel = new JScrollPane(playList);
        playListPanel.setPreferredSize(new Dimension(400, 350));
        add(playListPanel, BorderLayout.EAST);

        toolBarPanel = new JPanel();
        add(toolBarPanel, BorderLayout.SOUTH);

        progress = new JProgressBar();
        progress.setPreferredSize(new Dimension(700, 20));
        progress.setBackground(Color.BLUE);
        progress.setForeground(Color.BLUE);
        progress.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        progress.setStringPainted(true);
        progress.setString("");
        progress.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        progress.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (Player.isPlaying) {
                    progress.setValue((int) e.getX() * 100 / 700);
                    System.out.println(e.getX() * 100 / 700);
                    double totalTime = Player.player.getDuration().getSeconds();
                    double timeToSet = (e.getX() * 100 / 700) * totalTime / 100;
                    Player.player.stop();
                    Player.player.setMediaTime(new Time(timeToSet));
                    Player.player.start();
                }

            }
        });
        progressPanel = new JPanel(new BorderLayout());
        progressPanel.add(progress, BorderLayout.CENTER);
        progressPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        toolBarPanel.add(progressPanel);
        toolBarPanel.add(buttonPanel);



        preB = new JButton(new ImageIcon("images/pre.jpg"));
        preB.setActionCommand("Previous");
        preB.setToolTipText("Previous song ");
        preB.addActionListener(new EventProcess(this));
        preB.setBackground(Color.white);


        playB = new JButton(new ImageIcon("images/play.jpg"));
        playB.setActionCommand("Play");
        playB.setToolTipText("Play/Pause ");
        playB.addActionListener(new EventProcess(this));
        playB.setBackground(Color.white);

        stopB = new JButton(new ImageIcon("images/stop.jpg"));
        stopB.setActionCommand("Stop");
        stopB.setToolTipText("Stop ");
        stopB.addActionListener(new EventProcess(this));
        stopB.setBackground(Color.white);

        nextB = new JButton(new ImageIcon("images/next.jpg"));
        nextB.setActionCommand("Next");
        nextB.setToolTipText("Next song ");
        nextB.addActionListener(new EventProcess(this));
        nextB.setBackground(Color.white);
        
        
        
        shuffleB = new JButton(new ImageIcon("images/shuffle.png"));
       shuffleB .setActionCommand("Shuffle");
        stopB.setToolTipText("Shuffle song");
        shuffleB .addActionListener(new EventProcess(this));
        shuffleB .setBackground(Color.white);
        
         firstB = new JButton(new ImageIcon("images/first.png"));
        firstB.setActionCommand("First");
        firstB.setToolTipText("First song ");
       firstB.addActionListener(new EventProcess(this));
        firstB.setBackground(Color.white);
        
         lastB= new JButton(new ImageIcon("images/last.png"));
         lastB.setActionCommand("Last");
           lastB.setToolTipText("Last song ");
          lastB.addActionListener(new EventProcess(this));
           lastB.setBackground(Color.white);
        
        timeL = new JLabel("00:00:00 / 00:00:00");
        vol = new JLabel("Volume ");
        vol.setToolTipText("Volume");
        vol.setIcon(new ImageIcon("images/audio.png"));

        volume = new JSlider(0, 100, 50);
        volume.setPreferredSize(new Dimension(100, 15));
        volume.addChangeListener(new VolumeChangeProcess(player));


        buttonPanel.setPreferredSize(new Dimension(1000, 110));
        buttonPanel.add(timeL, FlowLayout.LEFT);
        buttonPanel.add(firstB);
        buttonPanel.add(preB);
        buttonPanel.add(playB);
        buttonPanel.add(stopB);
        buttonPanel.add(nextB);
        
        buttonPanel.add(lastB);
        buttonPanel.add(shuffleB);
        buttonPanel.add(vol);
        buttonPanel.add(volume);
      
    }

    public JMenuBar createMenuBar() {
        JMenuBar MP3PlayerMenuBar = new JMenuBar();

        MP3PlayerMenuBar.add(createFileMenu());

        MP3PlayerMenuBar.add(createPlayMenu());

        MP3PlayerMenuBar.add(createMenuHelp());
        return MP3PlayerMenuBar;
    }

    public JMenu createFileMenu() {
        fileM = new JMenu("File");
        fileM.setMnemonic(KeyEvent.VK_F); 

        openFilesMI = new JMenuItem("Open File...", new ImageIcon("images/openfile.png"));
        openFilesMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openFilesMI.setMnemonic(KeyEvent.VK_O);
        openFilesMI.addActionListener(new EventProcess(this));
        openFilesMI.setActionCommand("Open Files");
        fileM.add(openFilesMI);

        openPL = new JMenuItem("Open PlayList...", new ImageIcon("images/openplaylist.png"));
        openPL.setAccelerator(KeyStroke.getKeyStroke("control shift O"));
        openPL.setMnemonic(KeyEvent.VK_L);
        openPL.addActionListener(new EventProcess(this));
        openPL.setActionCommand("Open PlayList");
        fileM.add(openPL);

        fileM.addSeparator();

        saveNPL = new JMenuItem("Save NowPlaying List...", new ImageIcon("images/save.png"));
        saveNPL.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveNPL.setMnemonic(KeyEvent.VK_S);
        saveNPL.addActionListener(new EventProcess(this));
        saveNPL.setActionCommand("Save NowPlaying List");
        fileM.add(saveNPL);

        fileM.addSeparator();

        exitMI = new JMenuItem("Exit", new ImageIcon("images/exit.png"));
        exitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitMI.setMnemonic(KeyEvent.VK_X);
        exitMI.addActionListener(new EventProcess(this));
        exitMI.setActionCommand("Exit");
        fileM.add(exitMI);

        return fileM;
    }

    public JMenu createPlayMenu() {
        playM = new JMenu("Player");
        playM.setMnemonic(KeyEvent.VK_P);

       

        playM.addSeparator();

        repeatMI = new JCheckBoxMenuItem("Repeat", true);
        repeatMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        repeatMI.setMnemonic(KeyEvent.VK_R);
        repeatMI.addActionListener(new EventProcess(this));
        repeatMI.setActionCommand("Repeat");
        playM.add(repeatMI);

        setSpeedMI = new JMenu("Set play speed!");
        setSpeedMI.setMnemonic(KeyEvent.VK_S);
        setSpeedMI.addActionListener(new EventProcess(this));
        setSpeedMI.setActionCommand("set speed");
        playM.add(setSpeedMI);

        slow = new JRadioButtonMenuItem("Slow", false);
        slow.setActionCommand("slow");
        normal = new JRadioButtonMenuItem("Normal", true);
        normal.setActionCommand("normal");
        fast = new JRadioButtonMenuItem("Fast", false);
        fast.setActionCommand("fast");

        slow.addActionListener(new EventProcess(this));
        normal.addActionListener(new EventProcess(this));
        fast.addActionListener(new EventProcess(this));

        group = new ButtonGroup();
        group.add(slow);
        group.add(normal);
        group.add(fast);
        setSpeedMI.add(slow);
        setSpeedMI.add(normal);
        setSpeedMI.add(fast);


        changeBackground = new JMenuItem("Change Background");
        changeBackground.setMnemonic(KeyEvent.VK_B);
        changeBackground.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (background.equalsIgnoreCase("1")) {
                    mainL.setIcon(new ImageIcon("images/2.jpg"));
                    background = "2";
                } else if (background.equalsIgnoreCase("2")) {
                    mainL.setIcon(new ImageIcon("images/3.jpg"));
                    background = "3";
                }
                else if (background.equalsIgnoreCase("3")) {
                    mainL.setIcon(new ImageIcon("images/4.jpg"));
                    background = "4";
                }
                 else if (background.equalsIgnoreCase("4")) {
                    mainL.setIcon(new ImageIcon("images/5.jpg"));
                    background = "5";
                }
                else if (background.equalsIgnoreCase("5")) {
                    mainL.setIcon(new ImageIcon("images/1.jp"));
                    background = "1";
                }
            }
            
        });
        
        playM.add(changeBackground);


        return playM;
    }

    public JMenu createMenuHelp() {
        helpM = new JMenu("Help");
        helpM.setMnemonic(KeyEvent.VK_H);

        aboutMI = new JMenuItem("About...", new ImageIcon("images/author.png"));
        aboutMI.setActionCommand("about");
        aboutMI.setMnemonic(KeyEvent.VK_A);
        aboutMI.setAccelerator(KeyStroke.getKeyStroke("F1"));
        aboutMI.addActionListener(new EventProcess(this));
        helpM.add(aboutMI);

        return helpM;
    }
}
