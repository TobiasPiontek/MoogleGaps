package MoogleGaps;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
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
        server.createContext("/MoogleGaps", new MyHandler()); // haengt verschiedene URLS ein
        server.createContext("/index", new HtmlHandler());
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

                // einlesen, des POST requests
                int x = t.getRequestBody().read();
                while (x != -1) {
                    input = input + (char) x;
                    x = t.getRequestBody().read();
                }
                // ende einlesen des POST requests

                System.out.println("[Debug]: " + input);
                //Format der erhaltenen Nachricht: SetStartnode;lat,lng oder SetEndnode;lat,lng oder RouteRechnen;lat1,lng1;lat2,lng2
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
                    //response = calculation.WegKoordinatenAusgeben(ID1, ID2);
                    System.out.println(response);
                    //response = "{\"type\": \"LineString\",\"coordinates\":[[8.1, 53.1],[8.1, 51.6],[8.1, 50.1]]}";
                }


                t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        }

    }

    static class HtmlHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String root = "/home/markus/Documents/Uni/MoogleGaps/JavaScriptPlusMore";
            URI uri = t.getRequestURI();
            System.out.println("looking for: " + root + uri.getPath());
            String path = uri.getPath();
            File file = new File(root + path).getCanonicalFile();

            if (!file.isFile()) {
                // Object does not exist or is not a file: reject with 404 error.
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Object exists and is a file: accept with response code 200.
                String mime = "text/html";
                if (path.substring(path.length() - 3).equals(".js")) mime = "application/javascript";
                if (path.substring(path.length() - 3).equals("css")) mime = "text/css";

                Headers h = t.getResponseHeaders();
                h.set("Content-Type", mime);
                t.sendResponseHeaders(200, 0);

                OutputStream os = t.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                final byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                fs.close();
                os.close();
            }
        }
    }

}
