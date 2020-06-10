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
                System.out.println("Please start by entering longitude of the start:");
                System.out.println(new Timestamp(System.currentTimeMillis()));
                double longitudeStart;
                double latitudeStart;
                double longitudeDest;
                double latitudeDest;
                System.out.println("Start of Route calculation: ");
                System.out.println("Enter Start node: ");
                System.out.print("longitude: ");
                Scanner scanner = new Scanner(System.in);
                longitudeStart = scanner.nextDouble();
                System.out.print("latitude: ");
                latitudeStart = scanner.nextDouble();
                int source = GridGraph.findVertex(longitudeStart,latitudeStart);
                System.out.println("Node locked in to:  Source longitude: " + GridGraph.colToLongitude(GridGraph.idToCol(source)) +
                        " Source latitude: " + GridGraph.rowToLatitude(GridGraph.idToRow(source)));
                System.out.println("Enter Destination node");
                System.out.print("longitude: ");
                longitudeDest = scanner.nextDouble();
                System.out.print("latitude: ");
                latitudeDest = scanner.nextDouble();
                int target = GridGraph.findVertex(longitudeDest,latitudeDest);
                System.out.println("Node locked in to:  Source longitude: " + GridGraph.colToLongitude(GridGraph.idToCol(target)) +
                        " Source latitude: " + GridGraph.rowToLatitude(GridGraph.idToRow(target)));
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
                GeoJson.printPolyline(longitudes, latitudes);



            } catch (InputMismatchException e) {
                System.err.println("Only coordinates input are allowed!");
                continue;
            }
        }


    }

}
