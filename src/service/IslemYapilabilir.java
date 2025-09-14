package service;

public interface IslemYapilabilir {
    void bakiyeGoruntule();
    void paraYatir(double miktar);
    void paraCek(double miktar);
    boolean sifreDegistir(String kullaniciAdi, String eskiSifre, String yeniSifre);
}
