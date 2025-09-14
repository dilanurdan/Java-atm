package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

public class GirisEkrani extends JFrame {

    private JTextField kullaniciAlani;
    private JPasswordField sifreAlani;
    private JLabel kayanYazi;
    JComboBox<String> dilSecimi;

    // Hatalı girişleri takip etmek için sayaç
    private HashMap<String, Integer> hataliGirisSayaci = new HashMap<>();
    private ImageIcon showIcon;
    private ImageIcon hideIcon;

    public GirisEkrani() {
        setUndecorated(true); // Opaklık için önce çağrılmalı
        setOpacity(0.0f);     // Başlangıçta şeffaf

        setTitle("ATM Giriş Ekranı");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.GRAY);

        // Kayan yazı paneli
        JPanel kayanYaziPanel = new JPanel();
        kayanYaziPanel.setBounds(0, 10, 400, 30);
        kayanYaziPanel.setBackground(Color.WHITE);
        kayanYaziPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        add(kayanYaziPanel);

        kayanYazi = new JLabel("Hoşgeldiniz! Kartınızı takınız...");
        kayanYazi.setForeground(Color.BLACK);
        kayanYaziPanel.add(kayanYazi);

        kayanYaziAnimasyon();

        JLabel dilLabel = new JLabel("Dil Seçiniz :");
        dilLabel.setBounds(158, 40, 150, 25);
        add(dilLabel);

        dilSecimi = new JComboBox<>(new String[]{"Türkçe", "English"});
        dilSecimi.setBounds(275, 40, 120, 25);
        dilSecimi.setBackground(Color.ORANGE);
        add(dilSecimi);
        
        
        // Logo
        URL logoURL = getClass().getClassLoader().getResource("atmgirisi.png");
        if (logoURL != null) {
            ImageIcon logoIcon = new ImageIcon(logoURL);
            Image resized = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(resized));

            
            int logoGenislik = 40;
            int logoYukseklik = 40;

            // 400x300 pencere için:
            int x = 400 - logoGenislik-3;
            int y = 300 - logoYukseklik-2;

            logoLabel.setBounds(x, y, logoGenislik, logoYukseklik);
            add(logoLabel);
        }


        JLabel kullaniciLabel = new JLabel("Kullanıcı Adı:");
        kullaniciLabel.setBounds(50, 70, 100, 30);
        kullaniciLabel.setForeground(Color.BLACK);
        add(kullaniciLabel);

        kullaniciAlani = new JTextField();
        kullaniciAlani.setBounds(150, 70, 150, 30);
        add(kullaniciAlani);

        JLabel sifreLabel = new JLabel("Şifre:");
        sifreLabel.setBounds(50, 110, 100, 30);
        sifreLabel.setForeground(Color.BLACK);
        add(sifreLabel);

        sifreAlani = new JPasswordField();
        sifreAlani.setBounds(150, 110, 150, 30);
        sifreAlani.setEchoChar('*');
        add(sifreAlani);
        
        
        JButton gosterGizleBtn = new JButton();
        gosterGizleBtn.setBounds(310, 110, 40, 30);
        gosterGizleBtn.setBackground(new Color(255, 200, 0));
        gosterGizleBtn.setBorderPainted(false);
        gosterGizleBtn.setFocusPainted(false);
        add(gosterGizleBtn);

        // Yeni ikonlar
        URL openURL = getClass().getClassLoader().getResource("icons8-eye-16.png");
        URL closedURL = getClass().getClassLoader().getResource("icons8-eye-16.png");

        if (openURL != null && closedURL != null) {
            showIcon = new ImageIcon(openURL);
            hideIcon = new ImageIcon(closedURL);
        } else {
            System.err.println("icon veya iconlar bulunamadı!");
        }

        gosterGizleBtn.setIcon(showIcon);

        gosterGizleBtn.addActionListener(e -> {
            if (sifreAlani.getEchoChar() != (char) 0) {
                sifreAlani.setEchoChar((char) 0);
                gosterGizleBtn.setIcon(hideIcon);
            } else {
                sifreAlani.setEchoChar('*');
                gosterGizleBtn.setIcon(showIcon);
            }
        });

        
   

        JButton girisButonu = new JButton("Giriş Yap");
        girisButonu.setBounds(150, 160, 150, 40);
        girisButonu.setBackground(Color.ORANGE);
        girisButonu.setForeground(Color.BLACK);
        add(girisButonu);
        sifreAlani.addActionListener(e -> girisButonu.doClick());

        dilSecimi.addActionListener(e -> {
            String secilenDil = (String) dilSecimi.getSelectedItem();
            if (secilenDil.equals("English")) {
                dilLabel.setText("Select Language:");
                kullaniciLabel.setText("Username:");
                sifreLabel.setText("Password:");
                girisButonu.setText("Login");
                kayanYazi.setText("Welcome! Insert your card...");
            } else {
                dilLabel.setText("Dil Seçiniz:");
                kullaniciLabel.setText("Kullanıcı Adı:");
                sifreLabel.setText("Şifre:");
                girisButonu.setText("Giriş Yap");
                kayanYazi.setText("Hoşgeldiniz! Kartınızı takınız...");
            }
        });

        girisButonu.addActionListener(e -> {
            String secilenDil = (String) dilSecimi.getSelectedItem();
            String kullanici = kullaniciAlani.getText().trim();
            String sifre = new String(sifreAlani.getPassword());

            if (kullanici.isEmpty() || sifre.isEmpty()) {
                JOptionPane.showMessageDialog(this, secilenDil.equals("English") ?
                        "Username and password cannot be empty" :
                        "Kullanıcı adı ve şifre boş olamaz");
                return;
            }

            if (util.DosyaYardimcisi.kullaniciDogrula(kullanici, sifre)) {
            	util.DosyaYardimcisi.islemKaydiYaz(kullanici, "Giriş başarılı");
            	// Doğru giriş yapıldıysa, hatalı giriş sayaç sıfırlanır
                hataliGirisSayaci.remove(kullanici);

                // Blokeli mi kontrol et (ama girişe izin verilir)
                if (util.DosyaYardimcisi.kullaniciBlokeMi(kullanici)) {
                    JOptionPane.showMessageDialog(this, secilenDil.equals("English") ?
                            "Warning: This user is currently blocked!" :
                            "Uyarı: Bu kullanıcı şu anda bloke edilmiş durumda!");
                }

                double bakiye = util.DosyaIslemleri.bakiyeOku(kullanici);
                this.setVisible(false);
                AnaMenu anaMenu = new AnaMenu(secilenDil, kullanici, bakiye);

                if (anaMenu.isUndecorated()) {
                    anaMenu.setOpacity(0.0f);
                    anaMenu.setVisible(true);
                    new Thread(() -> {
                        try {
                            for (float i = 0.0f; i <= 1.0f; i += 0.05f) {
                                Thread.sleep(30);
                                Window win = anaMenu;
                                win.setOpacity(Math.min(i, 1.0f));
                            }
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                } else {
                    anaMenu.setVisible(true);
                }

            } else {
                // Hatalı giriş yapıldıysa sayaç artırılır
                int sayac = hataliGirisSayaci.getOrDefault(kullanici, 0) + 1;
                hataliGirisSayaci.put(kullanici, sayac);
                
                util.DosyaYardimcisi.islemKaydiYaz(kullanici, "Hatalı giriş denemesi (" + sayac + ". kez)");

                if (sayac >= 3) {
                    util.DosyaYardimcisi.blokeEkle(kullanici);
                    JOptionPane.showMessageDialog(this,
                    	    secilenDil.equals("English") ?
                    	        "User blocked after 3 failed attempts!" :
                    	        "3 hatalı girişten sonra kullanıcı bloke edildi!",
                    	    secilenDil.equals("English") ? "User Blocked" : "Kullanıcı Bloke",
                    	    JOptionPane.ERROR_MESSAGE);

                } else {
                	JOptionPane.showMessageDialog(this,
                		    secilenDil.equals("English") ?
                		        "Login failed. Attempt " + sayac + " of 3." :
                		        "Giriş başarısız. " + sayac + ". deneme.",
                		    secilenDil.equals("English") ? "Error" : "Hata",
                		    JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        Timer fadeInTimer = new Timer(30, new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.05f;
                    setOpacity(Math.min(opacity, 1.0f));
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        fadeInTimer.start();

        setVisible(true);
    }

    private void kayanYaziAnimasyon() {
        Timer timer = new Timer(30, new ActionListener() {
            int x = 400;

            @Override
            public void actionPerformed(ActionEvent e) {
                kayanYazi.setBounds(x--, 8, kayanYazi.getPreferredSize().width, kayanYazi.getPreferredSize().height);
                if (x < -200) {
                    x = 400;
                }
            }
        });
        timer.start();
    }

    public void islemKaydiYaz(String islem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            String kullaniciAdi = null;
            writer.write(kullaniciAdi + " - " + islem + " - " + java.time.LocalDateTime.now());
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "İşlem kaydı yazılırken hata oluştu.");
        }
    }

    public static void main(String[] args) {
        new GirisEkrani();
    }
}
