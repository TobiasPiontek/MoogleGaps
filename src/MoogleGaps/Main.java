package MoogleGaps;

public class Main {

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));

        GeoJson.printGeoJson();
    }
}
