package project_game_oop;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MainMenu extends JPanel {
    private static final long serialVersionUID = 1L;

    private Clip backgroundSound;

    public MainMenu(JFrame frame) {
        setLayout(null); // ใช้ Absolute Positioning

        // เพิ่มรูปภาพพื้นหลัง
        ImageIcon backgroundImageIcon = new ImageIcon("backgroundmenu.jpg");
        Image backgroundImage = backgroundImageIcon.getImage().getScaledInstance(FlappyBirdGame.WIDTH, FlappyBirdGame.HEIGHT, Image.SCALE_SMOOTH);
        backgroundImageIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(backgroundImageIcon);
        backgroundLabel.setBounds(0, 0, FlappyBirdGame.WIDTH, FlappyBirdGame.HEIGHT);
        add(backgroundLabel);

        JButton playButton = new JButton("เริ่มเกม");
        playButton.setForeground(new Color(0, 0, 0));
        playButton.setBackground(new Color(255, 255, 255));
        playButton.setFont(new Font("CooperThin", Font.PLAIN, 25));
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setOpaque(true);

        // เพิ่มเอฟเฟกต์เมื่อนำเมาส์มาชี้
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playButton.setFont(new Font("CooperThin", Font.PLAIN, 25)); // คืนค่าขนาดอักษรเดิม
                playButton.setPreferredSize(new Dimension(300, 60)); // เพิ่มขนาดปุ่ม
                playButton.setBackground(new Color(150, 150, 150)); // เปลี่ยนสีพื้นหลังเมื่อโฮเวอร์
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playButton.setFont(new Font("CooperThin", Font.PLAIN, 25)); // คืนค่าขนาดอักษรเดิม
                playButton.setPreferredSize(new Dimension(200, 50)); // คืนค่าขนาดปุ่มเดิม
                playButton.setBackground(new Color(255, 255, 255)); // เปลี่ยนสีพื้นหลังเมื่อออกจากปุ่ม
            }
        });

        playButton.setBounds((FlappyBirdGame.WIDTH - 200) / 2, FlappyBirdGame.HEIGHT / 2, 200, 50);

        playSoundBackgroundHome();

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(MainMenu.this);
                FlappyBirdGame game = new FlappyBirdGame();
                frame.add(game);
                frame.revalidate();
                frame.repaint();
                game.requestFocusInWindow();

                game.playBackgroundSound();
                stopSoundBackgroundHome();
            }
        });

        add(playButton);
    }


    // เมทอดสำหรับโหลดและเล่นเสียงพื้นหลัง
    private void playSoundBackgroundHome() {
        try {
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(new File("SoundBackgroundHome.wav"));
            backgroundSound = AudioSystem.getClip();
            backgroundSound.open(backgroundStream);
            backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundSound.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopSoundBackgroundHome() {
        if (backgroundSound != null && backgroundSound.isRunning()) {
            backgroundSound.stop();
        }
    }
}
