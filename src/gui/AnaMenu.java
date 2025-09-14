package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import util.DosyaIslemleri;

public class AnaMenu extends JFrame {
    private final String dil;
    private final String kullaniciAdi;
    private double bakiye;
    private ScheduledExecutorService zamanlayici = Executors.newSingleThreadScheduledExecutor();

    public AnaMenu(String dil, String kullaniciAdi, double bakiye) {
        this.dil = dil;
        this.kullaniciAdi = kullaniciAdi;
        this.bakiye = DosyaIslemleri.bakiyeOku(kullaniciAdi);

        setTitle(dil.equals("English") ? "Main Menu - " + kullaniciAdi : "Ana Menü - " + kullaniciAdi);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel anaPanel = new JPanel();
        anaPanel.setLayout(new BoxLayout(anaPanel, BoxLayout.Y_AXIS));
        anaPanel.setBackground(Color.WHITE);

        // Logo
        URL logoURL = getClass().getClassLoader().getResource("lotus.png");
        if (logoURL != null) {
            ImageIcon logoIcon = new ImageIcon(logoURL);
            Image resized = logoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(resized));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            anaPanel.add(logoLabel);
        }

        // Kayan yazı
        JLabel hosgeldin = new JLabel();
        hosgeldin.setFont(new Font("Segoe UI", Font.BOLD, 20));
        hosgeldin.setForeground(Color.BLACK);
        hosgeldin.setHorizontalAlignment(SwingConstants.CENTER);
        hosgeldin.setAlignmentX(Component.CENTER_ALIGNMENT);
        String mesaj = (dil.equals("English") ? "Welcome, " : "Hoş geldiniz, ") + kullaniciAdi + "!";

        new Timer(100, new ActionListener() {
            int i = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (i <= mesaj.length()) {
                    hosgeldin.setText(mesaj.substring(0, i));
                    i++;
                } else {
                    i = 0;
                    hosgeldin.setText("");
                }
            }
        }).start();

        anaPanel.add(hosgeldin);

        JLabel brailleLabel = new JLabel(dil.equals("English") ? "Please select a transaction." : "İşlem seçiniz.");
        brailleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        brailleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brailleLabel.setForeground(Color.GRAY);
        anaPanel.add(brailleLabel);

        // Butonlar
        String[] butonMetinleri = dil.equals("English")
                ? new String[]{"View Balance", "Deposit Money", "Withdraw Money", "Other Operations", "Exit"}
                : new String[]{"Bakiye Görüntüle", "Para Yatır", "Para Çek", "Diğer İşlemler", "Çıkış"};

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        gridPanel.setBackground(Color.WHITE);

        JButton[] butonlar = new JButton[5];
        for (int i = 0; i < 5; i++) {
            JButton btn = new JButton(butonMetinleri[i]);
            btn.setPreferredSize(new Dimension(180, 40));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(Color.ORANGE);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            buttonHoverEffect(btn);
            butonlar[i] = btn;
            if (i < 4) {
                gridPanel.add(btn);
            }
        }

        anaPanel.add(gridPanel);

        JPanel cikisPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cikisPanel.setBackground(Color.WHITE);
        cikisPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        cikisPanel.add(butonlar[4]);
        anaPanel.add(cikisPanel);

        // Buton İşlevleri
        butonlar[0].addActionListener(e -> gosterMesaj(dil.equals("English") ? "Balance: " : "Bakiye: ", bakiye));
        resetZamanlayici();
        double guncelBakiye = DosyaIslemleri.bakiyeOku(kullaniciAdi);
        this.bakiye = guncelBakiye;

        // Buton işlemlerinde bakiye güncellenir ve dosyaya yazılır
        butonlar[1].addActionListener(e -> {
            resetZamanlayici();
            double ek = islemAl("deposit");
            if (ek > 0) {
                this.bakiye += ek;
                DosyaIslemleri.bakiyeYaz(kullaniciAdi, bakiye);
                gosterMesaj(dil.equals("English") ? "Deposit successful. New balance: " : "Para yatırıldı. Yeni bakiye: ", bakiye);
            }
        });

        butonlar[2].addActionListener(e -> {
            resetZamanlayici();
            double cek = islemAl("withdraw");
            if (cek > 0 && cek <= bakiye) {
                this.bakiye -= cek;
                DosyaIslemleri.bakiyeYaz(kullaniciAdi, bakiye);
                gosterMesaj(dil.equals("English") ? "Withdrawal successful. New balance: " : "Para çekildi. Yeni bakiye: ", bakiye);
            } else {
                gosterMesaj(dil.equals("English") ? "Insufficient funds or invalid amount." : "Yetersiz bakiye veya geçersiz miktar.", bakiye);
            }
        });

        butonlar[3].addActionListener(e -> new DigerIslemlerPenceresi(dil, kullaniciAdi));

        butonlar[4].addActionListener(e -> {
            int secim = JOptionPane.showConfirmDialog(this,
                    dil.equals("English") ? "Are you sure?" : "Emin misiniz?",
                    dil.equals("English") ? "Confirm Exit" : "Çıkış Onayı",
                    JOptionPane.YES_NO_OPTION);
            if (secim == JOptionPane.YES_OPTION) {
                zamanlayici.shutdown();
                dispose();
                new TesekkurEkrani(dil);
            }
        });

        // Otomatik çıkış zamanlayıcısı
        zamanlayici.schedule(() -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        dil.equals("English") ? "Logged out due to inactivity." : "İşlem yapılmadığı için çıkış yapıldı.");
                dispose();
                new TesekkurEkrani(dil);
            });
        }, 5, TimeUnit.MINUTES);

        add(anaPanel);
        setVisible(true);
    }
    private void gosterMesaj(String msg, double bakiye) {
        Toolkit.getDefaultToolkit().beep();
        String bakiyeStr = String.format("%.2f TL", bakiye);
        JOptionPane.showMessageDialog(this, msg + bakiyeStr);
    

    }

    private double islemAl(String tur) {
        try {
            String giris = JOptionPane.showInputDialog(this, dil.equals("English") ? "Enter amount:" : "Miktarı girin:");
            return Double.parseDouble(giris);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, dil.equals("English") ? "Invalid input." : "Geçersiz giriş.");
            return 0;
        }
    }


    private void logla(String mesaj) {
        DosyaIslemleri.logYaz(kullaniciAdi, "[" + LocalDateTime.now() + "] " + mesaj);
    }

    private void resetZamanlayici() {
        zamanlayici.shutdownNow();
        zamanlayici = Executors.newSingleThreadScheduledExecutor();
        zamanlayici.schedule(() -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        dil.equals("English") ? "Logged out due to inactivity." : "İşlem yapılmadığı için çıkış yapıldı.");
                dispose();
                new TesekkurEkrani(dil);
            });
        }, 5, TimeUnit.MINUTES);
    }

    private void buttonHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 180, 0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.ORANGE);
            }
        });
    }
}

