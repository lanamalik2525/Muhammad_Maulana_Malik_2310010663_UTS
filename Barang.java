package model;

/**
 * Class Barang: model data untuk Aplikasi Inventaris Barang
 * Simple POJO dengan atribut kode, nama, jumlah
 */
public class Barang {
    private String kode;
    private String nama;
    private int jumlah;

    public Barang(String kode, String nama, int jumlah) {
        this.kode = kode;
        this.nama = nama;
        this.jumlah = jumlah;
    }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    @Override
    public String toString() {
        return kode + ", " + nama + ", " + jumlah;
    }
}
