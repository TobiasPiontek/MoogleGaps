package MoogleGaps;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        String filepath = CLInterface.getFilename(".pbf", "./OSMMapData");
        int gridGraphSize = CLInterface.enterGridGraphResolution();
        FileReader.readPbfFile(filepath);
        Polygons.createPolygons();
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Generating grid graph...");
        GridGraph.generate(gridGraphSize);

        //For testing purposes
        //GeoJson.printGridGraph()
        //CLInterface.generateNavigationRoute();


        //Start of the Webserver
        System.out.println("Starting webserver...");
        InetSocketAddress Adresse = new InetSocketAddress(8004);
        HttpServer server = HttpServer.create(Adresse, 0);
        server.createContext("/MoogleGaps", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Webserver online!");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "uninitialisiert";
            String global = "";

            if (t.getRequestMethod().equals("GET")) {
                System.out.println("GET wurde gesendet!");
                System.out.println("global ist: " + global);
                response = "Es wurde ein GET request abgefragt!";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

            if (t.getRequestMethod().equals("POST")) {
                System.out.println("Post wurde gesendet!");
                String input = "";

                // Block to read Post reqest
                int x = t.getRequestBody().read();
                while (x != -1) {
                    input = input + (char) x;
                    x = t.getRequestBody().read();
                }

                System.out.println("[Debug]: " + input);
                String[] split = input.split(";");

                //Procedure to set the startnode
                if (split[0].equals("SetStartnode")) {
                    String[] latlng = split[1].split(",");
                    double startLatitude = Double.parseDouble(latlng[0]);
                    double startLongitude = Double.parseDouble(latlng[1]);
                    startLongitude = ((((startLongitude + 180) % 360) + 360) % 360) - 180;
                    System.out.println("[Debug] Latitude= " + startLatitude);
                    System.out.println("[Debug] Longitude= " + startLongitude);
                    int nodeID = GridGraph.findVertex(startLongitude, startLatitude);
                    nodeID = GridGraph.findVertexInWater(nodeID);
                    response = GridGraph.idToLatitude(nodeID) + "," + GridGraph.idToLongitude(nodeID);
                }

                //Procedure to set the endnode
                if (split[0].equals("SetEndnode")) {
                    String[] latlng = split[1].split(",");
                    double endLatitude = Double.parseDouble(latlng[0]);
                    double endLongitude = Double.parseDouble(latlng[1]);
                    endLongitude = ((((endLongitude + 180) % 360) + 360) % 360) - 180;
                    System.out.println("[Debug] Latitude= " + endLatitude);
                    System.out.println("[Debug] Longitude= " + endLongitude);
                    int nodeID = GridGraph.findVertex(endLongitude, endLatitude);
                    nodeID = GridGraph.findVertexInWater(nodeID);
                    response = GridGraph.idToLatitude(nodeID) + "," + GridGraph.idToLongitude(nodeID);
                }

                if (split[0].equals("calculateRoute")) {
                    String[] startNode = split[1].split(",");
                    String[] endNode = split[2].split(",");
                    double startLatitude = Double.parseDouble(startNode[0]);
                    double startLongitude = Double.parseDouble(startNode[1]);
                    double endLatitude = Double.parseDouble(endNode[0]);
                    double endLongitude = Double.parseDouble(endNode[1]);
                    int sourceId = GridGraph.findVertex(startLongitude, startLatitude);
                    int targetId = GridGraph.findVertex(endLongitude, endLatitude);
                    ArrayList<Integer> route = Navigation.dijkstra(sourceId, targetId);
                    response = GeoJson.generateRoute(GridGraph.idToLongitude(route), GridGraph.idToLatitude(route));
                    System.out.println(response);
                }

                t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
