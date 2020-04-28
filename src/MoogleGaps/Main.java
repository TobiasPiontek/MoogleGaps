package MoogleGaps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));

    }
}
