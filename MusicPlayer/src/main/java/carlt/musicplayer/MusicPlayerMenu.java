/*
 * Click nbfs:// nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package carlt.musicplayer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Carlt
 */

// Extends the JPanel class.
public final class MusicPlayerMenu extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // Swing UI attributes.
    private JFrame newFrame;
    private JPanel newPanel;
    private JLabel titleOnScreen;
    private JTable songList;
    
    // Declares object of type DefaultTableModel.
    private DefaultTableModel tableModel;
    // Map attribute (keys of type String, values of type Song).
    private transient Map<String, Song> songs;
    
    // Album/Track cover attributes.
    private transient Image gnxAlbumCoverImage;
    private transient Image fmTrackCoverImage;
    private transient Image puTrackCoverImage;
    private transient Image tcAlbumCoverImage;
    private transient Image fatdAlbumCoverImage;
    private transient Image shAlbumCoverImage;
    private transient Image clbAlbumCoverImage;
    private transient Image dldtAlbumCoverImage;
    private transient Image srTrackCoverImage;
    private transient Image ngTrackCoverImage;
    private transient Image ptTrackCoverImage;
    private transient Image gutsAlbumCoverImage;
    private transient Image smithereensAlbumCoverImage;
    private transient Image ilaydownmylifeforyouAlbumCoverImage;
    private transient Image utopiaAlbumCoverImage;
    private transient Image lyTrackCoverImage;
    private transient Image timelessCoverImage;
    private transient Image mwmTrackCoverImage;
   
    // Constructor - takes the initialised songs map from main.
    public MusicPlayerMenu(Map<String, Song> songs) {
        this.songs = songs;
        
        // Method calls
        loadAlbumCovers();
        // Assigns songs with keys and values and adds them to the songs map.
        initialiseSongs();
        // Creates the main JFrame.
        newFrame = createMainFrame();
        // Initialises a JPanel with border layout.
        newPanel = new JPanel(new BorderLayout());
        // Initialises a DefaultTableModel object.
        tableModel = new DefaultTableModel();
        
        // Method that creates a JTable of song choices and displays it to the UI.
        setUpSongChoices();
        
        // Title properties
        titleOnScreen = new JLabel("Java Tunes");
        // Padding
        titleOnScreen.setBorder(new EmptyBorder(15, 15, 15, 15));
        titleOnScreen.setOpaque(true);
        titleOnScreen.setBackground(Color.BLACK);
        titleOnScreen.setForeground(Color.WHITE);
        titleOnScreen.setHorizontalAlignment(SwingConstants.CENTER);
        titleOnScreen.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        
        newPanel.setBackground(Color.BLACK);
        
        // Adds scroll pane to JTable.
        JScrollPane scrollPane = new JScrollPane(songList);
        // Customises scroll pane properties.
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollPane.getHorizontalScrollBar().setBackground(Color.BLACK);
        
        // Adds the scroll pane and the title label to the JPanel.
        newPanel.add(scrollPane);
        newPanel.add(titleOnScreen, BorderLayout.NORTH);
        // The main frame adds the JPanel.
        newFrame.add(newPanel, BorderLayout.CENTER);
        newFrame.setVisible(true);
    }
    
    // Method that creates the main JFrame and returns it.
    private JFrame createMainFrame() {
        newFrame = new JFrame("Java Tunes");
        // try, catch to set the icon image of the JFrame.
        try {
            newFrame.setIconImage(ImageIO.read(new File("appicon.png")));
        } catch (IOException e) {
            System.out.println(e);
        }
        
        // Assigns JFrame properties needed for display
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(700, 615);
        newFrame.setResizable(false);
        newFrame.setLayout(new BorderLayout());
        return newFrame;
    }
    
    // Method that creates and configures a JTable with song choices.
    private void setUpSongChoices() {
        // 2D array that stores song data
        // Album/Track cover image path, Song Title, Artist, Album, Duration.
        String[][] songData = {
            {"Assets/SongCovers/family_matters_icon.jpg", "Family Matters", "Drake", "Family Matters", "7:36"},
            {"Assets/SongCovers/pushups_cover.png", "Push Ups", "Drake", "Push Ups", "3:52"},
            {"Assets/SongCovers/take_care_cover.jpg", "Over My Dead Body", "Drake", "Take Care", "4:33"},
            {"Assets/SongCovers/fatd_cover.png", "Virginia Beach", "Drake", "For All The Dogs", "4:11"},
            {"Assets/SongCovers/scary_hours_cover.png", "Red Button", "Drake", "For All The Dogs Scary Hours Edition", "2:40"},
            {"Assets/SongCovers/fatd_cover.png", "First Person Shooter", "Drake", "For All The Dogs", "4:07"},
            {"Assets/SongCovers/dldt_cover.png", "Toosie Slide", "Drake", "Dark Lane Demo Tapes", "4:07"},
            {"Assets/SongCovers/clb_cover.png", "Fair Trade", "Drake, Travis Scott", "Certified Lover Boy", "4:51"},
            {"Assets/SongCovers/utopia_cover.jpeg", "MELTDOWN", "Travis Scott, Drake", "UTOPIA", "4:06"},
            {"Assets/SongCovers/search_rescue_cover.png", "Search & Rescue", "Drake", "Search & Rescue", "4:32"},
            {"Assets/SongCovers/no_guidance_cover.png", "No Guidance", "Chris Brown, Drake", "No Guidance", "4:21"},
            {"Assets/SongCovers/portantonio_cover.jpeg", "Port Antonio", "J Cole", "Port Antonio", "5:16"},
            {"Assets/SongCovers/guts_cover.png", "Vampire", "Olivia Rodrigo", "GUTS", "3:39"},
            {"Assets/SongCovers/gnx_cover.png", "Luther", "Kendrick Lamar, SZA", "GNX", "2:58"},
            {"Assets/SongCovers/gnx_cover.png", "squabble up", "Kendrick Lamar", "GNX", "2:38"},
            {"Assets/SongCovers/money_without_me_cover.png", "money without me", "Kendrick Lamar", "Faith", "3:17"},
            {"Assets/SongCovers/smithereens_cover.png", "Die For You", "Joji", "Smithereens", "3:31"},
            {"Assets/SongCovers/ilaydownmylifeforyou.png", "either on or off the drugs", "JPEGMAFIA", "I LAY DOWN MY LIFE FOR YOU", "2:21"},
            {"Assets/SongCovers/Lose_you_cover.jpg", "Lose You", "Playboi Carti, The Weeknd", "I AM MUSIC", "3:22"},
            {"Assets/SongCovers/timeless_cover.png", "Timeless", "The Weeknd, Playboi Carti", "Timeless", "4:16"}
        };
        
        String[] columnNames = {"Song Cover", "Song Name", "Artist(s)", "Album", "Track Duration"};
        
        // adds the song data and the column names to the default table model object.
        tableModel = new DefaultTableModel(songData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Restricts the user from editing the data stored in the cells.
                return false;
            }
        };
        
        // Adds the table model to a JTable object.
        songList = new JTable(tableModel);
        
        // Gets the first column of the JTable (the album/track cover column) and sets the cell renderer to a new instance of the image renderer class.
        // The image renderer class enables the display of the album/track cover images.
        songList.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
        
        // Custom cell renderer for JTable.
        songList.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable songList, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // Calls superclass - creates the default component for rendering a cell.
                Component component = super.getTableCellRendererComponent(songList, value, isSelected, hasFocus, row, column);
                
                // Customises cell appearance to remain the same on click/hover.
                if (isSelected){
                    component.setBackground(Color.BLACK);
                    component.setForeground(Color.WHITE);
                }
                else{
                    component.setBackground(Color.BLACK);
                    component.setForeground(Color.WHITE);
                }
                return component;
            }
        });
        
        // Customises JTable properties.
        songList.setSelectionBackground(Color.BLACK);
        songList.setSelectionForeground(Color.WHITE);

        songList.setRowHeight(100);
        songList.setGridColor(Color.BLACK);
        songList.setShowGrid(true);
        songList.setBackground(Color.BLACK);

        songList.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        songList.setForeground(Color.WHITE);
        Font headerFont = new Font("Comic Sans MS", Font.BOLD, 14);
        
        // Creates a new default table cell renderer object used for the JTable header.
        final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        // Centers header.
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        // Removes the header border.
        renderer.setBorder(null);
        
        songList.getTableHeader().setDefaultRenderer(renderer);
        
        // JTable header properties are customised.
        songList.getTableHeader().setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.WHITE));
        songList.getTableHeader().setBackground(Color.BLACK);
        songList.getTableHeader().setForeground(Color.WHITE);
        songList.getTableHeader().setFont(headerFont);
        songList.setFillsViewportHeight(true);
        
        // A mouse adapter object is added to the JTable as a mouse listener.
        songList.addMouseListener(new MouseAdapter() {
            @Override
            // Method that is called when the mouse is clicked
            public void mouseClicked(MouseEvent e) {
                // Checks if the mouse event occurred inside the JTable.
                if (e.getSource() == songList) {
                    // Retrieves row, song name and selected song.
                    int row = songList.rowAtPoint(e.getPoint());
                    String songName = (String) songList.getValueAt(row, 1);
                    Song selectedSong = songs.get(songName);
                    if (selectedSong != null) {
                        // Creates a new mini player frame to accommodate the miniplayer where the songs are played from.
                        JFrame newMiniPlayerFrame = new JFrame("MiniPlayer");
                        newMiniPlayerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        newMiniPlayerFrame.setSize(500, 500);
                        newMiniPlayerFrame.setResizable(false);
                        
                        // Creates a new miniplayerpanel object and passes the created frame, the selectedSong and the songs map in.
                        MiniPlayerPanel newMiniPlayer = new MiniPlayerPanel(newMiniPlayerFrame, selectedSong, songs);
                        newMiniPlayer.loadSong(selectedSong);

                        newMiniPlayerFrame.setVisible(true);
                        // Disposes of the music player menu frame.
                        newFrame.dispose();
                    } 
                    else {
                        System.out.println("No song found for: " + songName);
                    }
                }
            }
        });
    }
    
    // Loads the album/track cover images.
    private void loadAlbumCovers() {
        try {
            gnxAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/gnx_cover.png"));
            fmTrackCoverImage = ImageIO.read(new File("Assets/SongCovers/family_matters_icon.jpg"));
            puTrackCoverImage = ImageIO.read(new File("Assets/SongCovers/pushups_cover.png"));
            tcAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/take_care_cover.jpg"));
            fatdAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/fatd_cover.png"));
            shAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/scary_hours_cover.png"));
            clbAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/clb_cover.png"));
            dldtAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/dldt_cover.png"));
            srTrackCoverImage = ImageIO.read(new File("Assets/SongCovers/search_rescue_cover.png"));
            ngTrackCoverImage = ImageIO.read(new File("Assets/SongCovers/no_guidance_cover.png"));
            ptTrackCoverImage = ImageIO.read(new File("Assets/SongCovers/portantonio_cover.jpeg"));
            gutsAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/guts_cover.png"));
            smithereensAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/smithereens_cover.png"));
            ilaydownmylifeforyouAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/ilaydownmylifeforyou.png"));
            utopiaAlbumCoverImage = ImageIO.read(new File("Assets/SongCovers/utopia_cover.jpeg"));
            lyTrackCoverImage = ImageIO.read(new File("Assets/SongCovers/Lose_you_cover.jpg"));
            timelessCoverImage = ImageIO.read(new File("Assets/SongCovers/timeless_cover.png"));
            mwmTrackCoverImage = ImageIO.read(new File("Assets/SongCovers/money_without_me_cover.png"));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    // Initialises the songs by adding them to the songs map, assigning them a key and a value.
    // The key being the song name and the value being a new song object with the required song attributes.
    // Required song attributes are: Song Title, Artist, file path of sound file, album/track cover.
    public void initialiseSongs() {
        songs.put("Luther", new Song("Luther", "Kendrick Lamar, SZA", "Assets/Tracks/luther.wav", gnxAlbumCoverImage));
        songs.put("squabble up", new Song("squabble up", "Kendrick Lamar", "Assets/Tracks/squabbleup.wav", gnxAlbumCoverImage));
        songs.put("money without me", new Song("money without me", "Kendrick Lamar", "Assets/Tracks/money_without_me.wav", mwmTrackCoverImage));
        songs.put("Family Matters", new Song("Family Matters", "Drake", "Assets/Tracks/familymatters.wav", fmTrackCoverImage));
        songs.put("Push Ups", new Song("Push Ups", "Drake", "Assets/Tracks/push_ups.wav", puTrackCoverImage));
        songs.put("Over My Dead Body", new Song("Over My Dead Body", "Drake", "Assets/Tracks/overmydeadbody.wav", tcAlbumCoverImage));
        songs.put("Virginia Beach", new Song("Virginia Beach", "Drake", "Assets/Tracks/virginiabeach.wav", fatdAlbumCoverImage));
        songs.put("First Person Shooter", new Song("First Person Shooter","Drake, JCole","Assets/Tracks/firstpersonshooter.wav",fatdAlbumCoverImage));
        songs.put("Red Button", new Song("Red Button", "Drake", "Assets/Tracks/red_button.wav", shAlbumCoverImage));
        songs.put("Fair Trade", new Song("Fair Trade", "Drake, Travis Scott", "Assets/Tracks/fairtrade.wav", clbAlbumCoverImage));
        songs.put("MELTDOWN", new Song("MELTDOWN","Travis Scott, Drake" ,"Assets/Tracks/meltdown.wav" ,utopiaAlbumCoverImage));
        songs.put("Toosie Slide", new Song("Toosie Slide", "Drake", "Assets/Tracks/toosieslide.wav", dldtAlbumCoverImage));
        songs.put("Search & Rescue", new Song("Search & Rescue", "Drake", "Assets/Tracks/searchandrescue.wav", srTrackCoverImage));
        songs.put("No Guidance", new Song("No Guidance", "Chris Brown, Drake", "Assets/Tracks/no_guidance.wav", ngTrackCoverImage));
        songs.put("Port Antonio", new Song("Port Antonio", "J Cole", "Assets/Tracks/portantonio.wav", ptTrackCoverImage));
        songs.put("Vampire", new Song("Vampire", "Olivia Rodrigo", "Assets/Tracks/vampire.wav", gutsAlbumCoverImage));
        songs.put("Die For You", new Song("Die For You","Joji" ,"Assets/Tracks/die_for_you.wav", smithereensAlbumCoverImage));
        songs.put("either on or off the drugs", new Song("either on or off the drugs", "JPEGMAFIA", "Assets/Tracks/eitheronoroffthedrugs.wav", ilaydownmylifeforyouAlbumCoverImage));
        songs.put("Lose You", new Song("Lose You", "Playboi Carti, The Weeknd", "Assets/Tracks/lose_you_carti.wav", lyTrackCoverImage));
        songs.put("Timeless", new Song("Timeless", "The Weeknd, Playboi Carti", "Assets/Tracks/timeless.wav", timelessCoverImage));
    }

}
