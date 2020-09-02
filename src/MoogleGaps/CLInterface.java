package MoogleGaps;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
        File[] files = dir.listFiles(new FilenameFilter() {
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
        while (true) {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.print("Enter file to load: ");
                select = scan.nextInt();
                return files[select - 1].getPath();

            } catch (InputMismatchException e) {
                System.err.println("Only numbers as input are allowed!");
                continue;
            }
        }
    }


    public static int enterGridGraphResolution() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of nodes for grid graph: ");
        return scanner.nextInt();
    }

    public static boolean generateNewGridGraph() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Generate new grid graph [0] or use existing one[1]? ");
        int input = scanner.nextInt();
        if (input == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBarSize = 100;
        int remainPercent = ((100 * remain) / total);
        char defaultChar = ' ';
        String icon = "░";
        String bare = new String(new char[maxBarSize]).replace('\0', defaultChar) + "|";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("|");
        for (int i = 0; i < remainPercent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainPercent, bare.length());
        String toBeWritten = "\r" + bareDone + bareRemain + " " + remainPercent + "%";
        try {
            System.out.write(toBeWritten.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (remain == total) {
            System.out.print("\n");
        }
    }
}
