package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DosyaIslemleri {

    // Bakiye dosyasından kullanıcının bakiyesini oku
    public static double bakiyeOku(String kullaniciAdi) {
        try (BufferedReader okuyucu = new BufferedReader(new FileReader("bakiye.txt"))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                String[] parcala = satir.split("=");
                if (parcala.length >= 2 && parcala[0].trim().equals(kullaniciAdi)) {
                    return Double.parseDouble(parcala[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Bakiye okuma hatası: " + e.getMessage());
        }
        return 0.0;
    }

    // Bakiye dosyasına bakiye yaz
    public static void bakiyeYaz(String kullaniciAdi, double bakiye) {
        File dosya = new File("bakiye.txt");
        List<String> satirlar = new ArrayList<>();

        try (BufferedReader okuyucu = new BufferedReader(new FileReader(dosya))) {
            String satir;
            boolean bulundu = false;
            while ((satir = okuyucu.readLine()) != null) {
                String[] parcala = satir.split("=");
                if (parcala.length >= 2 && parcala[0].trim().equals(kullaniciAdi)) {
                    satirlar.add(kullaniciAdi + "," + bakiye);
                    bulundu = true;
                } else {
                    satirlar.add(satir);
                }
            }
            if (!bulundu) {
                satirlar.add(kullaniciAdi + "," + bakiye);
            }
        } catch (IOException e) {
            System.out.println("Bakiye okuma hatası: " + e.getMessage());
        }

        try (BufferedWriter yazici = new BufferedWriter(new FileWriter(dosya))) {
            for (String satir : satirlar) {
                yazici.write(satir);
                yazici.newLine();
            }
        } catch (IOException e) {
            System.out.println("Bakiye yazma hatası: " + e.getMessage());
        }
    }

    // Log kaydı yaz
    public static void logYaz(String kullanici, String mesaj) {
        File dosya = new File("log.txt");
        try (BufferedWriter yazici = new BufferedWriter(new FileWriter(dosya, true))) {
            yazici.write("[" + kullanici + "] " + mesaj);
            yazici.newLine();
        } catch (IOException e) {
            System.out.println("Log yazma hatası: " + e.getMessage());
        }
    }


    // Logları oku
    public static List<String> logOku() {
        List<String> loglar = new ArrayList<>();
        try (BufferedReader okuyucu = new BufferedReader(new FileReader("log.txt"))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                loglar.add(satir);
            }
        } catch (IOException e) {
            System.out.println("Log okuma hatası: " + e.getMessage());
        }
        return loglar;
    }

    // Bloke kaldırma (örnek)
    public static boolean blokeKaldir(String kullaniciAdi) {
        // Gerçek uygulamada bloke listesi kontrol edilir ve kaldırılır
    	
    	logYaz(kullaniciAdi, kullaniciAdi + " kullanıcısının blokesini kaldırma işlemi yapıldı.");

        return true;
    }

    // Kullanıcı var mı kontrolü
    public static boolean kullaniciVarMi(String kullaniciAdi) {
        try (BufferedReader okuyucu = new BufferedReader(new FileReader("kullanicilar.txt"))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                String[] parcala = satir.split(",");
                if (parcala.length >= 1 && parcala[0].trim().equals(kullaniciAdi)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Kullanıcı kontrol hatası: " + e.getMessage());
        }
        return false;
    }

    // Şifre değiştirme örneği
    public static boolean sifreDegistir(String kullaniciAdi, String eskiSifre, String yeniSifre) {
        File dosya = new File("kullanicilar.txt");
        List<String> satirlar = new ArrayList<>();
        boolean degisti = false;

        try (BufferedReader okuyucu = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                String[] parcala = satir.split(",");
                if (parcala.length >= 2 && parcala[0].trim().equals(kullaniciAdi) && parcala[1].trim().equals(eskiSifre)) {
                    satirlar.add(kullaniciAdi + "," + yeniSifre);
                    degisti = true;
                } else {
                    satirlar.add(satir);
                }
            }
        } catch (IOException e) {
            System.out.println("Şifre okuma hatası: " + e.getMessage());
            return false;
        }

        if (degisti) {
            try (BufferedWriter yazici = new BufferedWriter(new FileWriter(dosya))) {
                for (String s : satirlar) {
                    yazici.write(s);
                    yazici.newLine();
                }
            } catch (IOException e) {
                System.out.println("Şifre yazma hatası: " + e.getMessage());
                return false;
            }
        }
        return degisti;
    }

    // Para transfer işlemi (örnek)
    public static boolean transferYap(String gonderen, String alici, double miktar) {
        if (!kullaniciVarMi(gonderen) || !kullaniciVarMi(alici)) {
            return false;
        }
        double gonderenBakiye = bakiyeOku(gonderen);
        double aliciBakiye = bakiyeOku(alici);

        if (gonderenBakiye < miktar) {
            return false; // Yetersiz bakiye
        }

        bakiyeYaz(gonderen, gonderenBakiye - miktar);
        bakiyeYaz(alici, aliciBakiye + miktar);

        logYaz(alici, gonderen + " kullanıcısından " + alici + " kullanıcısına " + miktar + " TL transfer yapıldı.");


        return true;
    }
    public static void bakiyeGuncelle(String adSoyad, double bakiye) {
        bakiyeYaz(adSoyad, bakiye);
    }

}


