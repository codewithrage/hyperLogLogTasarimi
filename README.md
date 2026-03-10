# Büyük Veri Analitiği: HyperLogLog (HLL) Algoritması

Bu proje, "Cardinality Estimation" (Küme Büyüklüğü Tahmini) problemini çözmek amacıyla **HyperLogLog (HLL)** olasılıksal veri yapısının Java dilinde sıfırdan tasarlanmasını ve teorik analizini içermektedir.

## 🤖 Agentic Kodlama Metodolojisi
Bu projenin geliştirilme sürecinde **Gemini** dil modeli kullanılarak "Agentic Kodlama" yaklaşımı benimsenmiştir. Algoritma tek bir blok halinde yazdırılmamış; iteratif promptlar aracılığıyla adım adım inşa edilmiştir. 
Özellikle `MurmurHash3` fonksiyonunun entegrasyonu sırasında karşılaşılan hash çakışması (collision) problemi, fonksiyonun "kuyruk (tail)" işleme mantığının modele analiz ettirilip koda eklenmesiyle çözülmüş ve başlangıçtaki yüksek hata oranı %0.68 seviyelerine indirilmiştir.

## 🚀 Projenin Amacı ve Özellikleri
Geleneksel yöntemlerle milyarlarca veri içindeki benzersiz eleman sayısını bulmak O(N) bellek gerektirirken, HyperLogLog bu işlemi hash fonksiyonları ve olasılık kuramı kullanarak O(1) (sabit) bellek karmaşıklığı ile yapar. Bu projede:
- 32-bit MurmurHash3 (Tam donanımlı, tail destekli) kullanılarak homojen veri dağılımı sağlanmıştır.
- Veri seti p=14 bit kullanılarak m = 16384 adet alt kümeye (kovaya) ayrılmıştır.
- Her kova için ardışık sıfır sayıları (rank) takip edilmiş ve harmonik ortalama ile tahmin yapılmıştır.
- Küçük ve büyük veri setleri için gerekli düzeltme faktörleri koda entegre edilmiştir.
- Algoritmanın iki farklı HLL yapısını veri kaybı olmadan birleştirebilme (Merge) özelliği (`Math.max` kullanılarak) gerçeklenmiştir.

## 📁 Proje Yapısı
- `HyperLogLog.java`: HLL algoritmasının çekirdek sınıfı (add, count, merge metotları).
- `MurmurHash3.java`: Veriyi 32-bit integer değerlere çeviren yüksek performanslı hash algoritması.
- `Main.java`: Algoritmanın 1.000.000 veri ve birleştirme (merge) senaryolarıyla test edildiği çalıştırıcı sınıf.

## 💻 Nasıl Çalıştırılır?
Projeyi kendi lokal ortamınızda çalıştırmak için terminal üzerinden aşağıdaki adımları izleyebilirsiniz (Sürüm uyuşmazlığını önlemek için Java 8 formatında derlenmesi önerilir):

1. Depoyu klonlayın ve proje dizinine gidin.
2. Java dosyalarını derleyin:
    javac --release 8 *.java
3. Test senaryosunu çalıştırın:
    java Main

## 📊 Teorik Analiz ve Hata Sınırları
HLL algoritmasında bağıl standart hata, kova sayısının (m) karekökü ile ters orantılıdır: 1.04 / sqrt(m)

Projede optimum denge için p = 14 seçilmiştir. Bu seçim ve test sonuçları:
- **Kova Sayısı (m):** 16384
- **Tahmini Bellek Kullanımı:** ~16 KB
- **Teorik Bağıl Hata Sınırı:** %0.81
- **Uygulamalı Test Hata Oranı:** %0.68 (1 Milyon veride)
- **Birleştirilmiş (Merged) Test Hata Oranı:** %0.69 (1.5 Milyon veride)

Kova sayısını artırmak hata payını düşürse de bellek maliyetini doğrusal olarak artırmaktadır. Yapılan teorik analizler p=14 değerinin hafıza ve isabet oranı açısından en ideal ödünleşimi (trade-off) sunduğunu göstermektedir.

## 🔗 İlgili Bağlantılar
- **Sunum ve Analiz Dokümanı (PDF):** [Buraya PDF dosyanızın repo içindeki linkini ekleyin]
- **Proje Anlatım Videosu:** [Buraya Unlisted YouTube linkini ekleyin]
