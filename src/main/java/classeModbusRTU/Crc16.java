package classeModbusRTU;

public class Crc16 {
    public Crc16() {
    }
    public int calculCrc16(byte[] octets) {
        int crc = 0xFF;
        for (int p = 0; p < octets.length; p++) {
            crc ^= (octets[p] & 0xFF);
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc = crc >> 1;
                }
            }
        }
        return crc;
    }
}
