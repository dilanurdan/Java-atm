package gui;

import java.awt.BorderLayout;  // EKLENDİ
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;  // EKLENDİ
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;  // EKLENDİ

import service.MusteriIslemleri;
import util.DosyaYardimcisi;

public class DigerIslemlerPenceresi extends JFrame {

    private String dil;
    private String kullaniciAdi;
    private String aktifKullaniciAdi;

    public DigerIslemlerPenceresi(String dil, String kullaniciAdi) {
        this.dil = dil;
        this.kullaniciAdi = kullaniciAdi;
        this.aktifKullaniciAdi = kullaniciAdi;

        setTitle(dil.equals("English") ? "Other Operations" : "Diğer İşlemler");
        setSize(300, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        setLayout(new BorderLayout());

        // Butonları koyacağımız panel
        JPanel butonPaneli = new JPanel(new GridLayout(3, 1, 0, 10));
        butonPaneli.setBorder(new EmptyBorder(20, 20, 20, 25));
        butonPaneli.setBackground(Color.LIGHT_GRAY);

        URL logoURL = getClass().getClassLoader().getResource("blockatm.png");
        ImageIcon icon = new ImageIcon(logoURL);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        RoundedButton blokeKaldirBtn = new RoundedButton(dil.equals("English") ? "Unblock Account" : "Bloke Kaldır");
        blokeKaldirBtn.setIcon(scaledIcon);
        blokeKaldirBtn.setFocusPainted(false);
        blokeKaldirBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        blokeKaldirBtn.setVerticalTextPosition(SwingConstants.CENTER);

        RoundedButton qrKodBtn = new RoundedButton(dil.equals("English") ? "QR Code Operations" : "QR Kod İşlemleri");
        RoundedButton sifreDegistirBtn = new RoundedButton(dil.equals("English") ? "Change Password" : "Şifre Değiştir");

        RoundedButton[] butonlar = {blokeKaldirBtn, qrKodBtn, sifreDegistirBtn};
        for (RoundedButton b : butonlar) {
            b.setBackground(Color.ORANGE);
            b.setForeground(Color.BLACK);
            b.setPreferredSize(new Dimension(200, 40));
            butonPaneli.add(b);
        }

        add(butonPaneli, BorderLayout.NORTH);  // Buton panelini pencerenin üstüne ekle
        

        // Bloke kaldırma işlemi
        blokeKaldirBtn.addActionListener(e -> {
            if (DosyaYardimcisi.kullaniciBlokeMi(aktifKullaniciAdi)) {
                DosyaYardimcisi.blokeKaldir(aktifKullaniciAdi);
                JOptionPane.showMessageDialog(this, 
                    dil.equals("English") ? "Block removed." : "Bloke kaldırıldı.");
            } else {
                JOptionPane.showMessageDialog(this, 
                    dil.equals("English") ? "No block present." : "Bloke yok.");
            }
        });
        // QR kod işlemleri
        qrKodBtn.addActionListener(e -> {
            new QRKodIslemleriPenceresi(dil, kullaniciAdi).setVisible(true);
        });

        // Şifre değiştirme işlemi
        sifreDegistirBtn.addActionListener(e -> {
            String eskiSifre = JOptionPane.showInputDialog(
                this,
                dil.equals("English") ? "Enter your old password:" : "Eski şifrenizi girin:"
            );
            String yeniSifre = JOptionPane.showInputDialog(
                this,
                dil.equals("English") ? "Enter your new password:" : "Yeni şifrenizi girin:"
            );
            boolean sonuc = new MusteriIslemleri().sifreDegistir(kullaniciAdi, eskiSifre, yeniSifre);

            if (sonuc) {
                JOptionPane.showMessageDialog(this,
                    dil.equals("English") ? "Password changed successfully." : "Şifre başarıyla değiştirildi.");
            } else {
                JOptionPane.showMessageDialog(this,
                    dil.equals("English") ? "Incorrect old password." : "Eski şifre yanlış.");
            }
        });

        setVisible(true);
    }
}

