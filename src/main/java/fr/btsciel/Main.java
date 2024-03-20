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
            System.out.println(modbus.lectureCoils(8192,2));
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }

    }
}