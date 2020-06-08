package MoogleGaps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CLInterface {

    /***
     * @param directory filepath to search
     * @param  fileType the datatype to list
     * @return a String with the File to be loaded
     */
    public static String getFilename(String fileType, String directory) {

        System.out.println("The following Files are available: ");
        File dir = new File(directory);
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(fileType);
            }
        });

        int indexCounter = 1;
        for (File pbfFile : files) {
            System.out.println("[" + indexCounter++ + "]: " + pbfFile.getName());
        }
        int select;
        String pathValue;
        while(true) {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.print("Enter file to load: ");
                select = scan.nextInt();
                scan.close();
                return files[select-1].getPath();

            } catch (InputMismatchException e) {
                System.err.println("Only numbers as input are allowed!");
                continue;
            }
        }
    }

    public static void generateNavigationRoute(){

    }

}
