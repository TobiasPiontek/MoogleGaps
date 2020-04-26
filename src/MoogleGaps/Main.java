package MoogleGaps;




public class Main {

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();

        FileReader.readPBSFile("./OSMMapData/antarctica-latest.osm.pbf");
    }
}
