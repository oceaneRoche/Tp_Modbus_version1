package fr.btsciel;

import classeModbusRTU.ClasseModbus;
import jssc.SerialPortException;

public class Main {
    static ClasseModbus modbus;
    public static void main(String[] args){
        System.out.println("le n° de l'esclave svp");
        modbus = new ClasseModbus(In.readByte());
        System.out.println("le com svp");
        try {
            modbus.connecEsclave(In.readString(),9600,8, 0, 1);
            System.out.println("t pour tension, f pour frequence, c pour courant");
            char rep = In.readChar();
            if (rep == 't'){
                System.out.println(modbus.lectureCoils(8192,2));
            } else if (rep == 'f') {
                System.out.println(modbus.lectureCoils(8224,2));
            } else{
                System.out.println(modbus.lectureCoils(8288,2));
            }
            modbus.fermerLiaisonSerie();
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }

    }
}