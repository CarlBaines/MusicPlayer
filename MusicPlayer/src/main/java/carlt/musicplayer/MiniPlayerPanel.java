/*
 * Click nbfs:// nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package carlt.musicplayer;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Carlt
 */

// Extends the JPanel class and implements the MouseListener and ActionListener interfaces.
public class MiniPlayerPanel extends JPanel implements MouseListener, ActionListener {

    private static final long serialVersionUID = 1L;
    
    // Swing UI Properties
    private JFrame newFrame;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel buttonSubPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel southSubPanel;
    private JLabel albumCoverLabel;
    private JLabel songTitle;
    private JLabel artistLabel;
    private JButton rewindButton;
    private JButton forwardButton;
    private JButton playButton;
    private JButton pauseButton;
    private JProgressBar songProgressBar;
    private JLabel currentTimeLabel;
    private JLabel totalTimeLabel;
    private JSlider volumeSlider;
    private JLabel volumeLabel;
    // Default volume (-30Db).
    private float currentVolume = -30.0f;
    
    // Song properties.
    private transient Clip audioClip;
    private String filepath;
    private boolean isPlaying = false;
    private long currentFrame = 0;
    private transient Song currentSong;
    
    private transient Image albumCoverImage;
    private transient Image resizedAlbumCoverImage;

    private Timer progressTimer;
    
    // Constructor
    // Takes the miniplayerpanel frame passed through from the musicplayermenu class, the selected song and the map of songs.
    public MiniPlayerPanel(JFrame frame, Song selectedSong, Map<String, Song> songs) {
        this.newFrame = frame;
        
        // Adds a window listener to the frame
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Method that stops playback of the song.
                cleanupAudioResources();
            }
        });
        
        // Volume label is initialised and customised.
        volumeLabel = new JLabel("🔊");
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        
        // Volume slider is initialised and customised.
        volumeSlider = new JSlider(JSlider.HORIZONTAL, -60, 6, -30);
        volumeSlider.setBackground(Color.BLACK);
        volumeSlider.setBorder(null);
        // Sets the ui of the volume slider to an overrided ui.
        volumeSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(volumeSlider) {
            // Customises the appearance of the track (the line which the thumb of the JSlider moves over).
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                // Track set to the colour white
                g2d.setColor(Color.WHITE);
                // Checks if slider is horizontal
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    // draws the track.
                    int trackHeight = 2;
                    int yCenter = trackRect.y + (trackRect.height / 2);
                    g2d.fillRect(trackRect.x, yCenter - (trackHeight / 2), 
                               trackRect.width, trackHeight);
                }
            }

            @Override
            public void paintFocus(Graphics g) {
            }

            public void paintBorder(Graphics g) {
            }
        });
        // Sets slider size
        volumeSlider.setPreferredSize(new Dimension(80, 20));
        // Adds a change listener to the volume slider
        // When the thumb of the JSlider is moved, it calls the updateVolume method which changes the volume of the track.
        volumeSlider.addChangeListener(e -> updateVolume());
        
        // Creates main JPanel.
        mainPanel = new JPanel(new BorderLayout(0, 5));
        mainPanel.setBackground(Color.BLACK);
        
        // Method calls
        setupImageLabel();
        setupButtonPanel();
        
        // If selected song is not null
        if (selectedSong != null) {
            // loads the selected song
            loadSong(selectedSong);
            // sets the title of the JFrame to be the song title followed by the song artist.
            newFrame.setTitle(selectedSong.getTitle() + " - " + selectedSong.getArtist());
        } else {
            System.out.println("Error: Selected song is null");
        }
        
        // Creates and customises top control panel.
        JPanel topControlPanel = new JPanel(new BorderLayout(5, 0));
        topControlPanel.setBackground(Color.BLACK);
        
        // Creates and customises volume panel.
        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        volumePanel.setBackground(Color.BLACK);
        volumePanel.setBorder(null);
        volumePanel.add(volumeLabel);
        volumePanel.add(volumeSlider);
        
        // Adds the volumePanel (the volume slider and label) to the top control panel.
        topControlPanel.add(volumePanel, BorderLayout.EAST);
        
        // Method call which sets up the back button.
        // The songs map and the topControlPanel is passed into it.
        setupBackButton(songs, topControlPanel);
        
        // Creates and customises north panel.
        northPanel = new JPanel(new BorderLayout(0, 2));
        northPanel.setBackground(Color.BLACK);
        northPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        northPanel.add(topControlPanel, BorderLayout.NORTH);
        northPanel.add(albumCoverLabel, BorderLayout.CENTER);
        
        // Creates and customises title panel.
        JPanel titlePanel = new JPanel(new BorderLayout(0, 2));
        titlePanel.setBackground(Color.BLACK);
        titlePanel.add(songTitle, BorderLayout.NORTH);
        titlePanel.add(artistLabel, BorderLayout.CENTER);
        
        northPanel.add(titlePanel, BorderLayout.SOUTH);
        
        // Creates and customises south and south sub panels.
        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout(0, 5));
        southPanel.setBackground(Color.BLACK);
        
        southSubPanel = new JPanel(new BorderLayout());
        southSubPanel.setBackground(Color.BLACK);
        southSubPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(southSubPanel, BorderLayout.CENTER);
        
        // Adds the north and south panels to the mainPanel.
        mainPanel.add(northPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Adds the main JPanel to the JFrame.
        newFrame.add(mainPanel);
        newFrame.setVisible(true);
    }
    
    // Method that sets up the image label which displays the album/track cover image in the centre of the miniplayer.
    private void setupImageLabel() {
        // Creates a new image icon object with the pathname of where all the track cover images are stored.
        albumCoverImage = new ImageIcon("Assets/SongCovers/").getImage();
        
        // Sets the album cover image dimensions.
        final int ALBUM_COVER_SIZE = 290;
        albumCoverImage = albumCoverImage.getScaledInstance(
                ALBUM_COVER_SIZE,
                ALBUM_COVER_SIZE,
                Image.SCALE_SMOOTH
        );
        
        // Creates and customises the album cover label.
        // A mouse listener is added.
        albumCoverLabel = new JLabel(new ImageIcon(albumCoverImage));
        albumCoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        albumCoverLabel.setPreferredSize(new Dimension(ALBUM_COVER_SIZE, ALBUM_COVER_SIZE));
        albumCoverLabel.setBackground(Color.BLACK);
        albumCoverLabel.setOpaque(true);
        albumCoverLabel.addMouseListener(this);
        
        // Default songTitle and artistLabel properties are created.
        songTitle = new JLabel("Family Matters");
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        songTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        songTitle.setForeground(Color.WHITE);
        songTitle.setBackground(Color.BLACK);

        artistLabel = new JLabel("Drake");
        artistLabel.setHorizontalAlignment(SwingConstants.CENTER);
        artistLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        artistLabel.setForeground(new Color(188, 188, 188));
        artistLabel.setBackground(Color.BLACK);
    }
    
    // Method that creates the buttonpanels that display the pause/play buttons, rewind/fast forward buttons.
    private void setupButtonPanel() {
        // Creates and customises the main button panel and sub panel.
        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.BLACK);
        
        buttonSubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonSubPanel.setBackground(Color.BLACK);
        buttonSubPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        // Creates and customises the rewind button JButton.
        // Adds an action listener to the button.
        rewindButton = new JButton("⏪");
        rewindButton.setBackground(Color.WHITE);
        rewindButton.setForeground(Color.BLACK);
        rewindButton.setContentAreaFilled(true);
        rewindButton.setFocusPainted(false);
        rewindButton.addActionListener(this);
        
        // Creates and customises the play button JButton.
        // Adds an action listener to the button.
        playButton = new JButton("   ▶   ");
        playButton.setBackground(Color.WHITE);
        playButton.setForeground(Color.BLACK);
        playButton.setContentAreaFilled(true);
        playButton.setFocusPainted(false);
        playButton.addActionListener(this);
        
        // Creates and customises the pause button JButton.
        // Adds an action listener to the button.
        pauseButton = new JButton("   ❚❚   ");
        pauseButton.setBackground(Color.WHITE);
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setContentAreaFilled(true);
        pauseButton.setFocusPainted(false);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(this);
        
        // Creates and customises the fast forward button JButton.
        // Adds an action listener to the button.
        forwardButton = new JButton("⏩");
        forwardButton.setBackground(Color.WHITE);
        forwardButton.setForeground(Color.BLACK);
        forwardButton.setContentAreaFilled(true);
        forwardButton.setFocusPainted(false);
        forwardButton.addActionListener(this);
        
        // Adds all of the buttons to the subpanel.
        buttonSubPanel.add(rewindButton);
        buttonSubPanel.add(playButton);
        buttonSubPanel.add(pauseButton);
        buttonSubPanel.add(forwardButton);
        
        // The subpanel is added to the buttonpanel.
        buttonPanel.add(buttonSubPanel, BorderLayout.CENTER);
        
        // Creates and customises a progress bar JPanel.
        JPanel progressBarPanel = new JPanel(new BorderLayout());
        progressBarPanel.setBackground(Color.BLACK);
        progressBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        // Creates and customises a progress container JPanel.
        JPanel progressContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        progressContainer.setBackground(Color.BLACK);
        
        // Creates and customises the current time label and adds it to the progress container JPanel.
        currentTimeLabel = new JLabel("0:00");
        currentTimeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        currentTimeLabel.setForeground(Color.WHITE);
        progressContainer.add(currentTimeLabel);
        
        // Creates and customises the song progress JProgressBar and adds it to the progress container.
        songProgressBar = new JProgressBar();
        songProgressBar.setPreferredSize(new Dimension(200, 15));
        songProgressBar.setBackground(Color.LIGHT_GRAY);
        songProgressBar.setForeground(Color.WHITE);
        progressContainer.add(songProgressBar);
        
        // Creates and customises the total time label and adds it to the progress container.
        totalTimeLabel = new JLabel("0:00");
        totalTimeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        totalTimeLabel.setForeground(Color.WHITE);
        progressContainer.add(totalTimeLabel);
        
        // Adds the progess container to the progress bar panel.
        progressBarPanel.add(progressContainer, BorderLayout.CENTER);
        // Adds the progress bar panel to the button panel.
        buttonPanel.add(progressBarPanel, BorderLayout.SOUTH);
    }
    
    private JButton backButton;
    // Method that adds the back button to the miniplayer UI.
    private void setupBackButton(Map<String, Song> songs, JPanel topControlPanel) {
        // Creates and customises the back button JButton.
        backButton = new JButton("<--");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        
        // Adds an action listener to the back button.
        backButton.addActionListener(e -> {
            backButton.setForeground(Color.BLACK);
            // cleans up audio resources - stops current track from playing.
            cleanupAudioResources();
            newFrame.dispose();
            // Returns to the music player menu.
            new MusicPlayerMenu(songs);
        });
        // The back button is added to the top control panel.
        topControlPanel.add(backButton, BorderLayout.WEST);
    }
    
    // Method responsible for audio playback.
    private void playMusic(String location, Boolean pauseClicked, Boolean rewindClicked, Boolean fastForwardClicked) {
        try {
            // If audioClip is not null and if the audio line is open
            if (audioClip != null && audioClip.isOpen()) {
                // Checks whether the audio line is running.
                if (audioClip.isRunning()) {
                    // Checks if pause button has been clicked.
                    if (pauseClicked) {
                        // Sets currentFrame to the paused frame position in the audio clip and stops playback of the audio clip.
                        currentFrame = audioClip.getFramePosition();
                        audioClip.stop();
                        isPlaying = false;
                        return;
                    }
                }
                
                float frameRate = audioClip.getFormat().getFrameRate();
                // framesPer30Seconds variable used in fast forward and rewinding logic.
                int framesPer30Seconds = (int) (frameRate * 30);
                
                // If rewind button has been clicked
                if (rewindClicked) {
                    // Rewind by 30 frames.
                    currentFrame -= framesPer30Seconds;
                    // If the current frame becomes smaller than zero, reset the current frame to be zero for the start of the song.
                    if (currentFrame < 0) {
                        currentFrame = 0;
                    }
                    // Sets the frame position of the audio clip to the currentFrame.
                    // Plays the audio clip from the rewinded frame.
                    audioClip.setFramePosition((int) currentFrame);
                    audioClip.start();
                    isPlaying = true;
                    return;
                }
                
                // If fast forward button has been clicked
                if (fastForwardClicked) {
                    // Fast forward by 30 frames.
                    currentFrame += framesPer30Seconds;
                    // If the currentFrame becomes larger than the total length of the audio clip, the current frame is set to the end of the song.
                    if (currentFrame > audioClip.getFrameLength()) {
                        currentFrame = audioClip.getFrameLength();
                    }
                    // Sets the frame position of the audio clip to the currentFrame.
                    // Plays the audio clip from the rewinded frame.
                    audioClip.setFramePosition((int) currentFrame);
                    audioClip.start();
                    isPlaying = true;
                    return;
                }
                
                // Checks if the audio clip is not currently playing and if the pause button has not been clicked.
                // Playback should continue.
                if (!audioClip.isRunning() && !pauseClicked) {
                    // Sets the audio clip to resume playback from the current frame.
                    audioClip.setFramePosition((int) currentFrame);
                    audioClip.start();
                    isPlaying = true;
                    return;
                }
            }
            
            // If audio clip is null and the audio clip is not open
            if (audioClip == null || !audioClip.isOpen()) {
                // A new file object with the path of the music file is created.
                File musicPath = new File(location);
                // Checks if the musicPath exists.
                if (musicPath.exists()) {
                    // A new audio input stream object is created which obtains an audio input stream from the filepath.
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    // Gets and opens the audio clip.
                    audioClip = AudioSystem.getClip();
                    audioClip.open(audioInput);
                    // Adjusts the volume to be the default currentVolume field.
                    // .MASTER_GAIN control adjusts the volume of the audio clip.
                    FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
                    // Sets the volume.
                    gainControl.setValue(currentVolume);
                    // Starts the audio clip and sets the currentFrame to zero.
                    audioClip.start();
                    isPlaying = true;
                    currentFrame = 0;
                    // Method call to start progess bar.
                    startProgressBarUpdates();
                } else {
                    System.out.println("Song file cannot be located: " + location);
                }
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println(e);
        }
    }
    
    // Method responsible for updating the JProgressBar in realtime with the current frame of the audio clip.
    private void startProgressBarUpdates() {
        if (progressTimer != null) {
            progressTimer.stop();
        }
        
        // Creates a new timer object, adds a delay and adds an action listener.
        progressTimer = new Timer(100, new ActionListener() {
            // Method that checks if an action is performed - action listener interface method.
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the audio clip is not null and the audio clip is open
                if (audioClip != null && audioClip.isOpen()) {
                    // Retrieves the current position and the total length of the audio clip.
                    long currentPosition = audioClip.getMicrosecondPosition();
                    long totalLength = audioClip.getMicrosecondLength();
                    // Sets min and max values of the progress bar.
                    songProgressBar.setMinimum(0);
                    songProgressBar.setMaximum(100);
                    // Calculates the current progess of the audio clip in relation to the progess bar.
                    // Sets the value of the JProgressBar to be the current progress.
                    int progress = (int)((currentPosition * 100.0) / totalLength);
                    songProgressBar.setValue(progress);
                    
                    // Changes the text of the current and total time labels which are either side of the progress bar.
                    currentTimeLabel.setText(formatTime(currentPosition));
                    totalTimeLabel.setText(formatTime(totalLength));
                    
                    // If the current position is higher than the total length of the audio clip.
                    if (currentPosition >= totalLength) {
                        // Configures, resets the UI elements as well as stopping the playback of the audio clip and setting the current frame to zero.
                        progressTimer.stop();
                        songProgressBar.setValue(0);
                        currentTimeLabel.setText("0:00");
                        playButton.setVisible(true);
                        pauseButton.setVisible(false);
                        buttonPanel.revalidate();
                        buttonPanel.repaint();
                        isPlaying = false;
                        audioClip.stop();
                        currentFrame = 0;
                    }
                }
            }
        });
        progressTimer.start();
    }
    
    // Converts a duration given in microseconds into a human-readable time format.
    private String formatTime(long microseconds) {
        // Converts microseconds to seconds.
        long seconds = microseconds / 1_000_000;
        // Calculates minutes.
        long minutes = seconds / 60;
        // Calculates the remaining seconds.
        seconds %= 60;
        // Formats the time.
        return String.format("%d:%02d", minutes, seconds);
    }
    
    // Method which loads the selected song, determining the filepath for the sound file, the title of the song, the artist.
    // Also responsible for loading and configuring the album/track cover images.
    public void loadSong(Song song) {
        // Assigns the song object to the currentSong field.
        this.currentSong = song;
        // filepath object gets the file path of the song object.
        this.filepath = song.getFilePath();
        // Sets the song title and artist labels to the associated title and artist of the song object using getters.
        songTitle.setText(song.getTitle());
        artistLabel.setText(song.getArtist());
        
        final int ALBUM_COVER_SIZE = 290;
        // Gets the album cover image from the song object.
        albumCoverImage = song.getAlbumCover();
        if (albumCoverImage == null) {
            System.err.println("Album cover missing for song: " + song.getTitle());
            albumCoverImage = new ImageIcon(getClass().getResource("/default_cover.jpeg")).getImage();
        }
        
        // Resizes the album cover image by the final ALBUM_COVER_SIZE variable set above.
        albumCoverImage = albumCoverImage.getScaledInstance(
                ALBUM_COVER_SIZE,
                ALBUM_COVER_SIZE,
                Image.SCALE_SMOOTH
        );
        // Sets the icon of the album cover label to be the album cover image icon.
        albumCoverLabel.setIcon(new ImageIcon(albumCoverImage));
        // Sets the album cover image as the icon image of the JFrame.
        newFrame.setIconImage(albumCoverImage);
    }
    
    // Method that cleans audio resources
    // Use: To stop audio playback when the user closes the miniplayer.
    private void cleanupAudioResources() {
        // If the progressTimer is not null
        if (progressTimer != null) {
            // Stop the progess timer.
            progressTimer.stop();
        }
        // If the audio clip is not equal to null
        if (audioClip != null) {
            // Stops and closes the audio clip
            // Sets the audio to null.
            audioClip.stop();
            audioClip.close();
            audioClip = null;
        }
        isPlaying = false;
        currentFrame = 0;
    }
    
    // Method that is responsible for updating the volume when the user adjusts the volume bar.
    private void updateVolume() {
        // If the audio clip is not equal to null and the audio clip is open.
        if (audioClip != null && audioClip.isOpen()) {
            try {
                // Creates a new FloatControl object which gains control of the audio clip with MASTER_GAIN control, which allows volume adjustment of the audio clip.
                FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
                // Gets the currentVolume from the volume slider.
                currentVolume = volumeSlider.getValue();
                // Sets the volume of the FloatControl objct to the currentVolume from the volume slider.
                gainControl.setValue(currentVolume);
                
                // Changes the volume icon dependant on the volume selected on the volume slider.
                if (currentVolume <= -60) {
                    volumeLabel.setText("🔇");
                } else if (currentVolume < -30) {
                    volumeLabel.setText("🔈");
                } else if (currentVolume < -10) {
                    volumeLabel.setText("🔉");
                } else {
                    volumeLabel.setText("🔊");
                }
            } catch (Exception e) {
                System.out.println("Error updating volume: " + e.getMessage());
            }
        }
    }
    
    // Abstract Methods
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
    
    // Method that checks if the mouse has hovered over a UI element.
    @Override
    public void mouseEntered(MouseEvent e) {
        // If the mouse entered the album cover label
        if (e.getSource() == albumCoverLabel) {
            // Resizes the album cover image.
            resizedAlbumCoverImage = albumCoverImage.getScaledInstance(
                    albumCoverImage.getWidth(null) + 2,
                    albumCoverImage.getHeight(null) + 2,
                    Image.SCALE_SMOOTH
            );
            albumCoverLabel.setIcon(new ImageIcon(resizedAlbumCoverImage));
            albumCoverLabel.repaint();
        }
    }
    
    // Method that checks if the mouse has left a UI element after it has been hovered over.
    @Override
    public void mouseExited(MouseEvent e) {
        // If the mouse has left the album cover label.
        if (e.getSource() == albumCoverLabel) {
            // Sets the album cover label back to the original album cover image.
            albumCoverLabel.setIcon(new ImageIcon(albumCoverImage));
            // Updates the UI.
            albumCoverLabel.repaint();
        }
    }
    
    // Action listener abstract method.
    @Override
    public void actionPerformed(ActionEvent e) {
        // If the play button was clicked
        if (e.getSource() == playButton) {
            // Calls the playMusic method passing in the filePath, if the pause button has been clicked, if the rewind button and the fast forward button has been clicked.
            playMusic(filepath, false, false, false);
            // Displays pause button and updates UI.
            playButton.setVisible(false);
            pauseButton.setVisible(true);
            buttonPanel.revalidate();
            buttonPanel.repaint();
        // If the pause button has been clicked
        } else if (e.getSource() == pauseButton) {
            // Vice versa
            playMusic(filepath, true, false, false);
            playButton.setVisible(true);
            pauseButton.setVisible(false);
            buttonPanel.revalidate();
            buttonPanel.repaint();
        // If the rewind button has been clicked
        } else if (e.getSource() == rewindButton) {
            // playMusic method is called passing the rewindClicked parameter in as True.
            playMusic(filepath, false, true, false);
        // If the fast forward button has been clicked
        } else if (e.getSource() == forwardButton) {
            // playMusic method is called passing the fastForwardClicked parameter in as True.
            playMusic(filepath, false, false, true);
        }
    }
}
