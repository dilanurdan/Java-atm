package model;

import service.IslemYapilabilir;
import util.DosyaIslemleri;

public class Musteri extends Kullanici implements IslemYapilabilir {

    public Musteri(String adSoyad, String sifre, boolean bloke)
 {
        super(adSoyad, sifre, bloke);
        
    }
    
    @Override
    public void girisYap(String sifre) {
        if (this.sifre.equals(sifre)) {
            System.out.println("Giriş başarılı: " + adSoyad);
        } else {
            System.out.println("Şifre hatalı.");
        }
    }      
      
    @Override
    public boolean sifreDegistir(String kullaniciAdi, String eskiSifre, String yeniSifre) {
        if (this.sifre.equals(eskiSifre)) {
            boolean basarili = DosyaIslemleri.sifreDegistir(this.adSoyad, eskiSifre, yeniSifre);
            if (basarili) {
                this.sifre = yeniSifre;
                return true;
            } else {
                System.out.println("Dosyada şifre değiştirilemedi.");
                return false;
            }
        } else {
            System.out.println("Eski şifre yanlış.");
            return false;
        }
    }

    @Override
    public void bakiyeGoruntule() {
        System.out.println("Bakiyeniz: " + bakiye);
    }

    @Override
    public void paraYatir(double miktar) {
        if (miktar > 0) {
            bakiye += miktar;
            DosyaIslemleri.bakiyeYaz(adSoyad, bakiye); // Dosyayı güncelle
            System.out.println(miktar + " TL yatırıldı.");
        } else {
            System.out.println("Geçersiz miktar.");
        }
    }

    @Override
    public void paraCek(double miktar) {
        if (miktar > 0 && bakiye >= miktar) {
            bakiye -= miktar;
            DosyaIslemleri.bakiyeYaz(adSoyad, bakiye); // Dosyayı güncelle
            System.out.println(miktar + " TL çekildi.");
        } else {
            System.out.println("Yetersiz bakiye veya geçersiz miktar.");
        }
    }
}

