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
                System.out.println("longitude: ");
                Scanner scanner = new Scanner(System.in);
                scanner.next();
                scanner.close();
                //longitudeStart= scanner.nextDouble();
                longitudeStart = -107;

                System.out.print("latitude: ");

                //latitudeStart = scanner.nextDouble();
                latitudeStart = -75;

                int source = GridGraph.findVertex(longitudeStart,latitudeStart);
                System.out.println("Node locked in to: ");
                System.out.println("Source longitude: " + GridGraph.colToLongitude(GridGraph.idToCol(source)) +
                        " Source latitude: " + GridGraph.rowToLatitude(GridGraph.idToRow(source)));
                System.out.print("Enter Destination node");
                System.out.print("longitude: ");

                //longitudeDest = scanner.nextDouble();
                longitudeDest = -55;

                System.out.print("latitude: ");

                //latitudeDest = scanner.nextDouble();
                latitudeDest = -70;

                int target = GridGraph.findVertex(longitudeDest,latitudeDest);
                System.out.println("Node locked in to: ");
                System.out.println("Source longitude: " + GridGraph.colToLongitude(GridGraph.idToCol(target)) +
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

                GeoJson.printPolyline(longitudes, latitudes);

                System.out.println("Printing out Route: ");


                scanner.close();
                System.exit(0);


            } catch (InputMismatchException e) {
                System.err.println("Only coordinates input are allowed!");
                continue;
            }
        }


    }

}
