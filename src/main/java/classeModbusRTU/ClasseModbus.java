package classeModbusRTU;

import jssc.SerialPortException;
import liaisonSerie.LiaisonSerie;

public class ClasseModbus extends LiaisonSerie {
    byte numeroEsclave = 0;
    Crc16 crc16 = new Crc16();
    public byte [] resultatValeur = null;
    public ClasseModbus() {
    }

    public ClasseModbus(byte numeroEsclave) {
        this.numeroEsclave = numeroEsclave;
    }

    public byte [] intDeuxBytes (int nombre){
        byte [] nbDeuxOctets = new byte[2];
        nbDeuxOctets[0] = (byte) ((nombre & 0xFF00) >> 8);
        nbDeuxOctets[1] = (byte) (nombre & 0xFF);
        return nbDeuxOctets;
    }
    public void connecEsclave(String port, int vitesse, int donnee, int parite, int stop) throws SerialPortException {
        super.initCom(port);
        super.configurerParametres(vitesse, donnee, parite, stop);
    }
    public float lectureCoils(int registre, int bloc){
        byte [] adresse = intDeuxBytes(registre);
        byte msbAdresse = adresse[0];
        byte lsbAdresse = adresse[1];

        byte [] longueur = intDeuxBytes(bloc);
        byte msbLongueur = longueur[0];
        byte lsbLongueur = longueur[1];

        byte [] tabSansCrc16 = {numeroEsclave, (byte)(0x03), msbAdresse ,lsbAdresse, msbLongueur, lsbLongueur};
        byte [] tabCrc16 = intDeuxBytes(crc16.calculCrc16(tabSansCrc16));
        byte msbCrc16 = tabCrc16[0];
        byte lsbCrc16 = tabCrc16[1];
        byte [] tabAvecCrc16 = {numeroEsclave, (byte)(0x03), msbAdresse ,lsbAdresse, msbLongueur, lsbLongueur, lsbCrc16, msbCrc16};
        super.ecrire(tabAvecCrc16);

        resultatValeur = lireTrame(9);
        byte [] tableauValeur = {resultatValeur[0],resultatValeur[1],resultatValeur[2],resultatValeur[3],resultatValeur[4],resultatValeur[5],resultatValeur[6]};
        byte [] resultatCrc = intDeuxBytes(crc16.calculCrc16(tableauValeur));
        if (resultatValeur[7] == resultatCrc[1] && resultatCrc[0] == resultatValeur[8]){
            byte [] reception ={resultatValeur[3],resultatValeur[4],resultatValeur[5],resultatValeur[6]};
            return BigEndian.fromArray(reception);
        }
        return 0;
    }
    public void fermerLiaisonSerie(){
        super.fermerPort();
    }
}
