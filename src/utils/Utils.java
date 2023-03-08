package utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

// <editor-fold defaultstate="collapsed" desc="Leer enteros">
    private static Scanner scan = null;

    public static int LlegirInt() {
        int result;

        if (scan == null) {
            scan = new Scanner(System.in);
        }
        result = LlegirInt(scan);

        return result;
    }

    public static int LlegirInt(String missatge) {
        int result;

        if (scan == null) {
            scan = new Scanner(System.in);
        }
        result = LlegirInt(scan, missatge);

        return result;
    }

    public static int LlegirInt(Scanner scan) {
        return LlegirInt(scan, null);
    }

    public static int LlegirInt(int valorMin, int valorMax) {
        int result = 0;
        boolean correcte = false;
        if (valorMin > valorMax) {
            System.out.println("ERROR");
        } else {
            while (!correcte) {
                result = LlegirInt(scan);
                if (result < valorMin || result > valorMax) {
                    System.out.println("ERROR");
                } else {
                    correcte = true;
                }
            }
        }
        return result;
    }

    public static int LlegirInt(Scanner scan, String missatge, int valorMin, int valorMax) {
        int result = 0;
        boolean correcte = false;
        if (valorMin > valorMax) {
            System.out.println("ERROR");
        } else {
            while (!correcte) {
                result = LlegirInt(scan, missatge);
                if (result < valorMin || result > valorMax) {
                    System.out.println("ERROR");
                } else {
                    correcte = true;
                }
            }
        }
        return result;
    }

    public static int LlegirInt(Scanner scan, String missatge) {
        boolean dadesCorrectes;
        int result = 0;
        do {
            if (missatge != null) {
                System.out.print(missatge);
            }
            dadesCorrectes = scan.hasNextInt();
            if (dadesCorrectes) {
                result = scan.nextInt();
            } else if (scan.hasNext()) {
                System.out.println("ERROR");
                scan.next();
            }
        } while (!dadesCorrectes);

        return result;
    }

// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Metodos ordenacion">
    public static void bubbleSortVectInt(int[] vectInt) {
        boolean ordenat = false;
        int ultim = vectInt.length - 1;
        while (!ordenat) {
            ordenat = true;
            for (int i = 0; i < ultim; ++i) {
                if (vectInt[i] > vectInt[i + 1]) {
                    int aux = vectInt[i];
                    vectInt[i] = vectInt[i + 1];
                    vectInt[i + 1] = aux;
                    ordenat = false;
                }
            }
            --ultim;
        }
    }

    public static void countingSort(int[] vector) {
        //Vector auxiliar
        int[] aux = new int[vector.length + 1];

        
        int max = encontrarMasGrande(vector);

        
        int[] comptar = new int[max + 1];

        
        for (int i = 0; i < vector.length; ++i) {
            ++comptar[vector[i]];
        }

        
        for (int i = 1; i <= max; ++i) {
            comptar[i] += comptar[i - 1];
        }

        
        for (int i = vector.length - 1; i >= 0; --i) {
            aux[comptar[vector[i]] - 1] = vector[i];
            --comptar[vector[i]];
        }

        //Passem els números organitzats en l'auxiliar al vector original
        for (int i = 0; i < vector.length; ++i) {
            vector[i] = aux[i];
        }
    }

    public static void quickSort(int[] vector, int inici, int fi){
        if(inici < fi){
            int partIndex = pivot(vector, inici, fi);
            quickSort(vector, inici, partIndex-1);
            quickSort(vector, partIndex+1, fi);
        }
    }
    
    private static int pivot(int[] vector, int inici, int fi){
        int pivot = vector[fi];
        int i = (inici-1);
        
        for(int j = inici; j < fi; ++j){
            if(vector[j] <= pivot){
                ++i;
                
                int aux = vector[i];
                vector[i] =  vector[j];
                vector[j] = aux;
            }
        }
        
        int aux = vector[i+1];
        vector[i+1] = vector[fi];
        vector[fi] = aux;
        
        return i+1;
        
    }
    
    private static int encontrarMasGrande(int[] vector) {
        int max = vector[0];
        for (int i = 1; i < vector.length; ++i) {
            if (vector[i] > max) {
                max = vector[i];
            }
        }
        return max;
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Ficheros de texto">
    public static void LeerFichero(String nomFichero) {
        // Creamos el enlace con el fichero en el disco
        BufferedReader buf = AbrirFicheroLectura(nomFichero, true);

        String linea = LeerLinea(buf);
        while (linea != null) {
            System.out.println(linea);
            linea = LeerLinea(buf);
        }

        CerrarFichero(buf);

    }

    public static File AbrirFichero(String nomFichero, boolean crear) {
        File result = new File(nomFichero);

        if (!result.exists()) {
            if (crear) {
                try {
                    result.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
                    result = null;
                }
            } else {
                result = null;
            }
        }

        return result;
    }

    public static BufferedReader AbrirFicheroLectura(String nomFichero, boolean crear) {
        BufferedReader br = null;
        File f = AbrirFichero(nomFichero, crear);

        if (f != null) {
            // Declarar el reader para poder leer el fichero¡
            FileReader reader;
            try {
                reader = new FileReader(f);
                // Buffered reader para poder leer más comodamente
                br = new BufferedReader(reader);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return br;
    }

    public static PrintWriter AbrirFicheroEscritura(String nomFichero, boolean crear, boolean blnAnyadir) {
        PrintWriter pw = null;
        File f = AbrirFichero(nomFichero, crear);

        if (f != null) {
            // Declarar el writer para poder escribir en el fichero¡
            FileWriter writer;
            try {
                writer = new FileWriter(f, blnAnyadir);
                // PrintWriter para poder escribir más comodamente
                pw = new PrintWriter(writer);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return pw;
    }

    public static void CerrarFichero(BufferedReader br) {
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void CerrarFichero(PrintWriter pw) {
        pw.flush();
        pw.close();
    }

    public static String LeerLinea(BufferedReader br) {
        String linea = null;

        try {
            linea = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return linea;
    }

    public static void EscribirLinea(PrintWriter pw, String linea) {
        pw.println(linea);
    }

    public static void BorrarFichero(String filename) {
        File f = new File(filename);
        f.delete();
    }

    public static void RenombrarFichero(String filename_origen, String filename_final) {
        File f = new File(filename_origen);
        File f2 = new File(filename_final);
        f.renameTo(f2);
    }

    // </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Ficheros binarios">
    public static DataInputStream AbrirFicheroLecturaBinario(String nomFichero, boolean crear) {
        DataInputStream dis = null;
        File f = AbrirFichero(nomFichero, crear);

        if (f != null) {
            // Declarar el writer para poder escribir en el fichero¡
            FileInputStream reader;
            try {
                reader = new FileInputStream(f);
                // PrintWriter para poder escribir más comodamente
                dis = new DataInputStream(reader);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return dis;
    }

    public static DataOutputStream AbrirFicheroEscrituraBinario(String nomFichero, boolean crear, boolean blnAnyadir) {
        DataOutputStream dos = null;
        File f = AbrirFichero(nomFichero, crear);

        if (f != null) {
            // Declarar el writer para poder escribir en el fichero¡
            FileOutputStream writer;
            try {
                writer = new FileOutputStream(f, blnAnyadir);
                // PrintWriter para poder escribir más comodamente
                dos = new DataOutputStream(writer);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return dos;
    }

    public static void CerrarFicheroBinario(DataOutputStream dos) {
        try {
            dos.flush();
            dos.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void CerrarFicheroBinario(DataInputStream dis) {
        try {
            dis.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Ficheros de acceso aleatorio">
    public static RandomAccessFile AbrirAccesoDirecto(String nomFitxer, String mode) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(nomFitxer, mode);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return raf;
    }

    public static void moverPuntero(RandomAccessFile raf, long posicionMover) {
        try {
            raf.seek(posicionMover);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold>
}
