package service;

import util.DosyaIslemleri;

public class MusteriIslemleri implements IslemYapilabilir {
    private String kullaniciAdi;
    private double bakiye;

    public double bakiyeGetir(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
        this.bakiye = DosyaIslemleri.bakiyeOku(kullaniciAdi);
        return bakiye;
    }

    public double bakiyeGetir() {
        return bakiye;
    }

    @Override
    public void bakiyeGoruntule() {
        System.out.println("Mevcut Bakiye: " + bakiye + " TL");
    }

    @Override
    public void paraYatir(double miktar) {
        bakiye += miktar;
        System.out.println(miktar + " TL yatırıldı. Yeni bakiye: " + bakiye);
    }

    @Override
    public void paraCek(double miktar) {
        if (miktar <= bakiye) {
            bakiye -= miktar;
            System.out.println(miktar + " TL çekildi. Yeni bakiye: " + bakiye);
        } else {
            System.out.println("Yetersiz bakiye!");
        }
    }

    public void bakiyeGuncelle(String kullaniciAdi, double yeniBakiye) {
        this.bakiye = yeniBakiye;
        DosyaIslemleri.bakiyeYaz(kullaniciAdi, yeniBakiye);
    }

    // 3 parametreli şifre değiştirme metodu
    @Override
    public boolean sifreDegistir(String kullaniciAdi, String eskiSifre, String yeniSifre) {
        // Dosya işlemi kullanarak şifre değiştirme
        boolean degistirildi = DosyaIslemleri.sifreDegistir(kullaniciAdi, eskiSifre, yeniSifre);
        return degistirildi;
    }
}

