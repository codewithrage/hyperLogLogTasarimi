public class Main {
    public static void main(String[] args) {
        System.out.println("HyperLogLog (HLL) Algoritması Testi Başlıyor...\n");

        // p = 14 seçiyoruz (16384 kova, %0.81 hata payı)
        int p = 14;
        HyperLogLog hll1 = new HyperLogLog(p);
        int exactCount1 = 1_000_000;

        System.out.println("1. HLL yapısına " + exactCount1 + " adet benzersiz eleman ekleniyor...");
        for (int i = 0; i < exactCount1; i++) {
            hll1.add("veri_A_" + i);
        }

        long estimate1 = hll1.count();
        System.out.println("Gerçek Sayı: " + exactCount1);
        System.out.println("HLL Tahmini: " + estimate1);
        System.out.println("Hata Oranı: %" + String.format("%.2f", Math.abs(exactCount1 - estimate1) * 100.0 / exactCount1) + "\n");

        // Merge (Birleştirme) İşlemi Testi
        HyperLogLog hll2 = new HyperLogLog(p);
        int exactCount2 = 500_000;
        
        System.out.println("2. HLL yapısına " + exactCount2 + " adet farklı benzersiz eleman ekleniyor...");
        for (int i = 0; i < exactCount2; i++) {
            hll2.add("veri_B_" + i);
        }

        System.out.println("İki HLL yapısı birleştiriliyor (Merge)...");
        hll1.merge(hll2);
        
        long mergedEstimate = hll1.count();
        int totalExactCount = exactCount1 + exactCount2;
        
        System.out.println("Toplam Gerçek Sayı: " + totalExactCount);
        System.out.println("Birleştirilmiş HLL Tahmini: " + mergedEstimate);
        System.out.println("Yeni Hata Oranı: %" + String.format("%.2f", Math.abs(totalExactCount - mergedEstimate) * 100.0 / totalExactCount));
    }
}