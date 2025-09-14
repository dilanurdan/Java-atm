package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DosyaYardimcisi {

    public static HashMap<String, String> kullanicilariOku(String dosyaAdi) {
        HashMap<String, String> kullanicilar = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length >= 2) {
                    kullanicilar.put(parcalar[0].trim(), parcalar[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okunurken hata oluştu: " + e.getMessage());
        }
        return kullanicilar;
    }

    public static boolean kullaniciDogrula(String girilenAd, String girilenSifre) {
        HashMap<String, String> kullanicilar = kullanicilariOku("kullanicilar.txt");

        // Loglama
        System.out.println("Giriş yapan kullanıcı adı: " + girilenAd);
        System.out.println("Dosyadaki kullanıcılar: " + kullanicilar);

        // Kullanıcı adı ve şifre doğrulama
        boolean dogrulama = kullanicilar.containsKey(girilenAd) && kullanicilar.get(girilenAd).equals(girilenSifre);
        System.out.println("Doğrulama sonucu: " + dogrulama);

        return dogrulama;
    }
    public static boolean kullaniciBlokeMi(String kullaniciAdi) {
        try (BufferedReader reader = new BufferedReader(new FileReader("kullanicilar.txt"))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length == 3) {
                    String ad = parcalar[0].trim();
                    boolean bloke = Boolean.parseBoolean(parcalar[2].trim());
                    if (ad.equals(kullaniciAdi)) {
                        return bloke;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Bloke kontrolünde hata: " + e.getMessage());
        }
        return false; // Varsayılan olarak bloke değil
    }

    public static void blokeKaldir(String kullaniciAdi) {
        List<String> satirlar = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("kullanicilar.txt"))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length == 3 && parcalar[0].trim().equals(kullaniciAdi)) {
                    // bloke kaldır
                    satir = parcalar[0].trim() + "," + parcalar[1].trim() + ",false";
                }
                satirlar.add(satir);
            }
        } catch (IOException e) {
            System.out.println("Bloke kaldırma sırasında okuma hatası: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("kullanicilar.txt"))) {
            for (String s : satirlar) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Bloke kaldırma sırasında yazma hatası: " + e.getMessage());
        }
    }
    public static void blokeEkle(String kullaniciAdi) {
        List<String> satirlar = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("kullanicilar.txt"))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length == 3 && parcalar[0].trim().equals(kullaniciAdi)) {
                    // Bloke et
                    satir = parcalar[0].trim() + "," + parcalar[1].trim() + ",true";
                }
                satirlar.add(satir);
            }
        } catch (IOException e) {
            System.out.println("Bloke etme sırasında okuma hatası: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("kullanicilar.txt"))) {
            for (String s : satirlar) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Bloke etme sırasında yazma hatası: " + e.getMessage());
        }
    }
    public static void islemKaydiYaz(String kullaniciAdi, String islem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            String tarihSaat = java.time.LocalDateTime.now().toString();
            writer.write(kullaniciAdi + " - " + islem + " - " + tarihSaat);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("İşlem kaydı yazılamadı: " + e.getMessage());
        }
    }

}


