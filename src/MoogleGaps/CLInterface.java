package MoogleGaps;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        while(true) {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.print("Enter file to load: ");
                select = scan.nextInt();
                return files[select-1].getPath();

            } catch (InputMismatchException e) {
                System.err.println("Only numbers as input are allowed!");
                continue;
            }
        }
    }

    public static void generateNavigationRoute(){
        while(true) {
            try {
                System.out.println(new Timestamp(System.currentTimeMillis()));
                double longitudeSource;
                double latitudeSource;
                double longitudeTarget;
                double latitudeTarget;
                System.out.println("Enter source node");
                System.out.print("    longitude: ");
                Scanner scanner = new Scanner(System.in);
                longitudeSource = scanner.nextDouble();
                System.out.print("    latitude: ");
                latitudeSource = scanner.nextDouble();
                int source = GridGraph.findVertex(longitudeSource,latitudeSource);
                System.out.println("Node locked in to (" + GridGraph.colToLongitude(GridGraph.idToCol(source)) +
                        ", " + GridGraph.rowToLatitude(GridGraph.idToRow(source)) + ")");
                System.out.println("Enter target node");
                System.out.print("    longitude: ");
                longitudeTarget = scanner.nextDouble();
                System.out.print("    latitude: ");
                latitudeTarget = scanner.nextDouble();
                int target = GridGraph.findVertex(longitudeTarget,latitudeTarget);
                System.out.println("Node locked in to (" + GridGraph.colToLongitude(GridGraph.idToCol(target)) +
                        ", " + GridGraph.rowToLatitude(GridGraph.idToRow(target)) + ")");
                Navigation.dijkstra(source, target);
                ArrayList<Integer> way = Navigation.getWay(source, target);

                int row;
                int col;
                double longitude;
                double latitude;
                double[] longitudes = new double[way.size()];
                double[] latitudes = new double[way.size()];;
                for (int i = 0; i < way.size(); i++) {
                    row = GridGraph.idToRow(way.get(i));
                    col = GridGraph.idToCol(way.get(i));
                    longitude = GridGraph.colToLongitude(col);
                    latitude = GridGraph.rowToLatitude(row);
                    longitudes[i] = longitude;
                    latitudes[i] = latitude;
                }

                System.out.println("Printing out Route: ");
                System.out.println();
                GeoJson.printPolyline(longitudes, latitudes);
                System.out.println();

            } catch (InputMismatchException e) {
                System.err.println("Only coordinate inputs are allowed!");
                continue;
            }
        }


    }

}
