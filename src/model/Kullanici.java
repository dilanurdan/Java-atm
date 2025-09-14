package model;

import util.DosyaIslemleri;

public abstract class Kullanici {
    protected String adSoyad;
    protected String sifre;
    protected double bakiye;
    private boolean bloke;

    public Kullanici(String adSoyad, String sifre, boolean bloke) {
        this.adSoyad = adSoyad;
        this.sifre = sifre;
        this.bakiye = DosyaIslemleri.bakiyeOku(adSoyad);
        this.bloke = bloke;
    }

    public abstract void girisYap(String sifre);
    
    public abstract boolean sifreDegistir(String kullaniciAdi, String eskiSifre, String yeniSifre);

    public String getAdSoyad() {
        return adSoyad;
    }

    public String getSifre() {
        return sifre;
    }
    
    public void setSifre(String yeniSifre) {
        this.sifre = yeniSifre;
    }
    
    public void bakiyeGuncelle(double yeniBakiye) {
        this.bakiye = yeniBakiye;
        DosyaIslemleri.bakiyeYaz(this.adSoyad, yeniBakiye);
    }

        
        public double getBakiye() {
            return bakiye;
        }
        
        public boolean isBloke() {
            return bloke;
        }
        
        public void setBloke(boolean bloke) {
            this.bloke = bloke;
        }
        
        @Override
        public String toString() {
            return adSoyad + "," + sifre + "," + bloke;
        }
}