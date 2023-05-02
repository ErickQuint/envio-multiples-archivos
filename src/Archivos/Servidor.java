/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sarch;

/**
 *
 * @author erick
 */
import java.net.*;
import java.io.*;

public class SArch {

    public static void main(String[] args) {
        try {
            // Creamos el socket
            ServerSocket s = new ServerSocket(7000);
            // Iniciamos el ciclo infinito del servidor
            for (;;) {
                // Esperamos una conexión 
                //System.out.println("Conexión establecida desde/"+"127.0.0.1"+":"+"7000");
                Socket cl = s.accept();
                //System.out.println("Conexión establecida desde "+cl.getInetAddress()+":"+cl.getPort());
                
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                
                int tamBuffer = dis.readInt();
                byte[] b = new byte[tamBuffer];
                
                String nombre = dis.readUTF();
                System.out.println("Recibimos el archivo:"+nombre);
                
                long tam = dis.readLong();

                DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                
                long recibidos = 0;
                int n,porcentaje;
                
                while (recibidos < tam) {
                    n = dis.read(b);
                    dos.write(b, 0, n);
                    dos.flush();
                    recibidos += n;
                    porcentaje = (int) (recibidos * 100 / tam);
                    System.out.print("Recibido: " + porcentaje + "%\r");
                }
                System.out.print("\n\nArchivo recibido\n\n");
                dos.close();
                dis.close();
                cl.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
