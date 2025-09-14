package gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Musteri;
import service.MusteriIslemleri;

public class DilSecimEkrani extends JFrame {

    public DilSecimEkrani(Musteri musteri, MusteriIslemleri islemler) {
        setTitle("Dil Seçimi");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Lütfen dil seçiniz / Please select language:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        panel.add(label);

        JButton turkceButon = new JButton("Türkçe");
        turkceButon.setBackground(Color.ORANGE);
        turkceButon.setForeground(Color.BLACK);

        JButton ingilizceButon = new JButton("English");
        ingilizceButon.setBackground(Color.ORANGE);
        ingilizceButon.setForeground(Color.BLACK);

        panel.add(turkceButon);
        panel.add(ingilizceButon);

        add(panel);
        setVisible(true);
            
            turkceButon.addActionListener(e -> {
                dispose();

                String kullaniciAdi = musteri.getAdSoyad(); 
                double bakiye = islemler.bakiyeGetir(kullaniciAdi);

                System.out.println("Kullanıcı: " + kullaniciAdi + " - Bakiye: " + bakiye);

                new AnaMenu("Türkçe", kullaniciAdi, bakiye);
            });

           

        ingilizceButon.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "English language support is not yet implemented.");
        });
    }
}

