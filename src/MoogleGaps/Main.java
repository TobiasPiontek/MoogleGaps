package MoogleGaps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileReader.readPBSFile(getFilename(".pbf", "./OSMMapData"));
    }

    /***
     * @param directory filepath to search
     * @param  filetype the datatype to list
     * @return a String with the File to be loaded
     */
    public static String getFilename(String filetype, String directory) {

        System.out.println("The following Files are available: ");
        File dir = new File(directory);
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(filetype);
            }
        });

        int indexCounter = 1;
        for (File pbfFile : files) {
            System.out.println("[" + indexCounter++ + "]: " + pbfFile.getName());
        }
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter file to load: ");
        int select = scan.nextInt();
        scan.close();
        return files[select-1].getPath();
    }
}
