public class HyperLogLog {
    private final int p;          // Kova indeksini belirlemek için kullanılacak bit sayısı
    private final int m;          // Kova sayısı (2^p)
    private final double alphaMM; // Harmonik ortalama için alfa sabiti * m^2
    private final byte[] registers; // Kovalar (Buckets)

    // Yapıcı Metot
    public HyperLogLog(int p) {
        if (p < 4 || p > 16) {
            throw new IllegalArgumentException("p değeri 4 ile 16 arasında olmalıdır.");
        }
        this.p = p;
        this.m = 1 << p; // 2^p
        this.registers = new byte[m];
        this.alphaMM = calculateAlphaMM(p, m);
    }

    // Alfa sabitinin hesaplanması
    private double calculateAlphaMM(int p, int m) {
        switch (p) {
            case 4: return 0.673 * m * m;
            case 5: return 0.697 * m * m;
            case 6: return 0.709 * m * m;
            default: return (0.7213 / (1 + 1.079 / m)) * m * m;
        }
    }

    // Veri Ekleme (Kovalama ve Ardışık Sıfır Sayısı)
    public void add(String item) {
        // Not: Burada MurmurHash3 gibi iyi dağılım yapan 32-bit bir hash fonksiyonu kullanılmalıdır.
        // Bu örnek için basit bir hash çağrısı varsayıyoruz (Uygulamada kendi Hash fonksiyonunuzu yazmalısınız).
        int hash = MurmurHash3.hash32(item); 
        
        // İlk p biti kova indeksi olarak al
        int bucketIndex = hash >>> (32 - p);
        
        // Geriye kalan bitlerdeki ardışık sıfırları bulmak için p bit kaydır
        // p=0 iken sonsuz döngü olmaması için 1 biti ekliyoruz.
        int w = (hash << p) | (1 << (p - 1)); 
        int rank = Integer.numberOfLeadingZeros(w) + 1;
        
        // İlgili kovadaki en büyük rank değerini tut
        registers[bucketIndex] = (byte) Math.max(registers[bucketIndex], rank);
    }

    // Tahmin Hesaplama (Harmonik Ortalama ve Düzeltmeler)
    public long count() {
        double estimate = 0;
        for (int i = 0; i < m; i++) {
            estimate += Math.pow(2, -registers[i]);
        }
        estimate = alphaMM / estimate;

        // Küçük Veri Seti Düzeltmesi (Linear Counting)
        if (estimate <= 2.5 * m) {
            int emptyRegisters = 0;
            for (int i = 0; i < m; i++) {
                if (registers[i] == 0) emptyRegisters++;
            }
            if (emptyRegisters > 0) {
                estimate = m * Math.log((double) m / emptyRegisters);
            }
        } 
        // Büyük Veri Seti Düzeltmesi (32-bit hash çakışmaları için)
        else if (estimate > (1L << 32) / 30.0) {
            estimate = - (1L << 32) * Math.log(1 - estimate / (1L << 32));
        }

        return (long) estimate;
    }

    // İki HLL Yapısını Birleştirme (Merge)
    public void merge(HyperLogLog other) {
        if (this.p != other.p) {
            throw new IllegalArgumentException("Birleştirilecek yapıların p değerleri aynı olmalıdır.");
        }
        for (int i = 0; i < m; i++) {
            this.registers[i] = (byte) Math.max(this.registers[i], other.registers[i]);
        }
    }
}   