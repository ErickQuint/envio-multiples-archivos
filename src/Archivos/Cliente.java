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
import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class CArch {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Tamaño del buffer: ");
            String Buffer = br.readLine();
            System.out.printf("\n\n¿Activar algoritmo de Niggle?: ");
            String niggle = br.readLine();
            
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);

            int r = jf.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File[] files = jf.getSelectedFiles();

                Socket cl;
                DataOutputStream dos;
                DataInputStream dis;

                int tamBuffer = Integer.parseInt(Buffer);
                byte[] b = new byte[tamBuffer];

                String archivo, nombre;
                long tam, enviados;
                int porcentaje, n;

                for (File f : files) {
                    cl = new Socket("localhost", 7000);
                    
                    if(niggle.equals("si"))
                        cl.setTcpNoDelay(true);
                    else if(niggle.equals("no"))
                        cl.setTcpNoDelay(false);

                    archivo = f.getAbsolutePath();
                    nombre = f.getName();
                    tam = f.length();

                    dos = new DataOutputStream(cl.getOutputStream());
                    dis = new DataInputStream(new FileInputStream(archivo));

                    dos.writeInt(tamBuffer);
                    dos.flush();
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();

                    enviados = 0;
                    while (enviados < tam) {
                        n = dis.read(b);
                        dos.write(b, 0, n);
                        dos.flush();
                        enviados += n;
                        porcentaje = (int) (enviados * 100 / tam);

                        System.out.print("Enviado: " + porcentaje + "%\r");
                    }
                    System.out.print("\n\nArchivos enviados");

                    dis.close();
                    dos.close();
                    cl.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
