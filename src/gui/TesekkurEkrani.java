package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class TesekkurEkrani extends JFrame {
    public TesekkurEkrani(String dil) {
        setTitle(dil.equals("English") ? "Goodbye" : "Güle Güle");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel anaPanel = new JPanel(new BorderLayout());
        anaPanel.setBackground(Color.LIGHT_GRAY);

        JLabel mesaj = new JLabel(
            dil.equals("English") ? "Thank you, have a nice day!" : "Teşekkürler, iyi günler!",
            SwingConstants.CENTER
        );
        mesaj.setFont(new Font("Arial", Font.BOLD, 20));
        mesaj.setForeground(Color.BLACK);
        anaPanel.add(mesaj, BorderLayout.CENTER);

        JPanel butonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        butonPanel.setBackground(Color.LIGHT_GRAY);
        butonPanel.setBorder(new EmptyBorder(10, 0, 30, 0));

        JButton tekrarGirisButton = new JButton(dil.equals("English") ? "Log in Again" : "Tekrar Giriş Yap");
        tekrarGirisButton.setBackground(Color.ORANGE);
        tekrarGirisButton.setForeground(Color.BLACK);
        tekrarGirisButton.setFont(new Font("Arial", Font.BOLD, 16));
        tekrarGirisButton.setFocusPainted(false);
        tekrarGirisButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        tekrarGirisButton.setPreferredSize(new Dimension(160, 40));
        
        tekrarGirisButton.addActionListener(e -> {
            if (dil.equals("English")) {
                playSound("/welcome.wav"); 
            } else {
                playSound("/hosgeldiniz.wav");
            }
            playSoundParallel("/ding-47489.wav");
            
            dispose();
            new GirisEkrani();
        });
            
      
        butonPanel.add(tekrarGirisButton);
        anaPanel.add(butonPanel, BorderLayout.SOUTH);
        add(anaPanel);
        setVisible(true);

        
        playSound("/ding-36029.wav");

        Timer timer = new Timer(6000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Ses oynatma metodu
    private void playSound(String soundFileName) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResource(soundFileName)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Ses çalınamadı: " + e.getMessage());
        }
    }
    private void playSoundParallel(String soundFileName) {
        new Thread(() -> {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(soundFileName));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } catch (Exception e) {
                System.out.println("Arka plan sesi çalınamadı: " + e.getMessage());
            }
        }).start();
    }
}


