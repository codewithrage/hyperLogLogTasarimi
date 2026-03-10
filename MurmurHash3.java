public class MurmurHash3 {
    public static int hash32(String data) {
        byte[] bytes = data.getBytes();
        int c1 = 0xcc9e2d51;
        int c2 = 0x1b873593;
        int h1 = 0; // Başlangıç seed değeri
        int length = bytes.length;
        int i = 0;

        // 4 bytelık blokların işlenmesi
        while (i + 4 <= length) {
            int k1 = (bytes[i] & 0xFF) | 
                     ((bytes[i + 1] & 0xFF) << 8) | 
                     ((bytes[i + 2] & 0xFF) << 16) | 
                     ((bytes[i + 3] & 0xFF) << 24);

            k1 *= c1;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= c2;

            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;
            i += 4;
        }

        // Kuyruk (Tail) kısmının işlenmesi (EKSİK OLAN VE HATAYA YOL AÇAN KISIM BURASIYDI)
        int k1 = 0;
        int tailLength = length - i;
        if (tailLength == 3) {
            k1 ^= (bytes[i + 2] & 0xFF) << 16;
        }
        if (tailLength >= 2) {
            k1 ^= (bytes[i + 1] & 0xFF) << 8;
        }
        if (tailLength >= 1) {
            k1 ^= (bytes[i] & 0xFF);
            k1 *= c1;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= c2;
            h1 ^= k1;
        }

        // Finalizasyon (Avalanche effect)
        h1 ^= length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        return h1;
    }
}