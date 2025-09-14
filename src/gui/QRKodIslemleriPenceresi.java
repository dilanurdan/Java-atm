package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRKodIslemleriPenceresi extends JFrame {
    private String kullaniciAdi;
    private String dil;

    public QRKodIslemleriPenceresi(String dil, String kullaniciAdi) {
        this.dil = dil;
        this.kullaniciAdi = kullaniciAdi;

        setTitle(dil.equals("English") ? "QR Code Operations" : "QR Kod İşlemleri");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);

        Font butonFont = new Font("Arial", Font.BOLD, 14);
        Color butonRenk = new Color(255, 165, 0); // Turuncu

        // --- QR Kod Oluştur butonu ---
        JButton qrOlusturBtn = new JButton(dil.equals("English") ? "Create QR Code" : "QR Kod Oluştur");
        JButton qrSecBtn = new JButton(dil.equals("English") ? "Select QR Code" : "QR Kod Seç");

        for (JButton btn : new JButton[]{qrOlusturBtn, qrSecBtn}) {
            btn.setFont(butonFont);
            btn.setFocusPainted(false);
            btn.setBackground(butonRenk);
            btn.setForeground(Color.BLACK);
            btn.setPreferredSize(new Dimension(180, 45));
            btn.setToolTipText(dil.equals("English") ? "Click to proceed" : "Tıklayarak devam edin");

            // Hover efekti
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(butonRenk.brighter());
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(butonRenk);
                }
            });
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(qrOlusturBtn, gbc);

        gbc.gridy = 1;
        add(qrSecBtn, gbc);

        // QR Kod Oluştur buton aksiyonu
        qrOlusturBtn.addActionListener(e -> {
            String transaction = JOptionPane.showInputDialog(this,
                dil.equals("English") ? "Enter transaction type (transfer/havale/eft):" : "İşlem türünü giriniz (transfer/havale/eft):");
            if (transaction == null || transaction.isEmpty()) {
				return;
			}

            String amountStr = JOptionPane.showInputDialog(this,
                dil.equals("English") ? "Enter amount:" : "Tutarı giriniz:");
            if (amountStr == null || amountStr.isEmpty()) {
				return;
			}

            String account = JOptionPane.showInputDialog(this,
                dil.equals("English") ? "Enter account number (optional):" : "Hesap numarasını giriniz (isteğe bağlı):");
            if (account == null) {
				account = "";
			}

            String qrContent = String.format("kullaniciAdi=%s;transaction=%s;amount=%s;account=%s;",
                kullaniciAdi, transaction.trim().toLowerCase(), amountStr.trim(), account.trim());

            try {
                // QR kod oluştur, sadece göster (kaydetme)
                BufferedImage qrImage = createQRCode(qrContent, 300, 300, "");

                // Küçük pencere ile QR kodu göster
                ImageIcon icon = new ImageIcon(qrImage);
                JLabel label = new JLabel(icon);

                JDialog dialog = new JDialog(this, dil.equals("English") ? "QR Preview" : "QR Önizleme", true);
                dialog.setLayout(new BorderLayout());
                dialog.add(label, BorderLayout.CENTER);

                JButton saveButton = new JButton(dil.equals("English") ? "Save" : "Kaydet");
                dialog.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(ev -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setSelectedFile(new File("qrcode.png"));
                    if (chooser.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                        try {
                            createQRCode(qrContent, 300, 300, chooser.getSelectedFile().getAbsolutePath());
                            JOptionPane.showMessageDialog(this, dil.equals("English") ? "Saved!" : "Kaydedildi!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Hata oluştu!");
                        }
                    }
                });

                dialog.pack();
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, dil.equals("English") ? "QR code creation failed!" : "QR kod oluşturulamadı!");
            }
        });

        qrSecBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(dil.equals("English") ? "Select QR Code Image" : "QR Kod Resmi Seç");
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedImage bufferedImage = ImageIO.read(selectedFile);
                    if (bufferedImage == null) {
                        JOptionPane.showMessageDialog(this, dil.equals("English") ? "Invalid image file." : "Geçersiz resim dosyası.");
                        return;
                    }

                    // ZXing kullanarak QR kodu oku
                    com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource(bufferedImage);
                    com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap(new com.google.zxing.common.HybridBinarizer(source));

                    com.google.zxing.Result qrResult = new com.google.zxing.MultiFormatReader().decode(bitmap);

                    String qrText = qrResult.getText();

                    // Sonucu göster
                    JOptionPane.showMessageDialog(this, (dil.equals("English") ? "QR Code Content:\n" : "QR Kod İçeriği:\n") + qrText);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, dil.equals("English") ? "Failed to read QR code." : "QR kod okunamadı.");
                }
            }
        });

        
        
    }

    private BufferedImage createQRCode(String text, int width, int height, String filePath) throws WriterException, java.io.IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        if (filePath != null && !filePath.isEmpty()) {
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        }
        return qrImage;
    }
}
