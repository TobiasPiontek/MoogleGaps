package MoogleGaps;

import com.sun.prism.shader.AlphaOne_LinearGradient_AlphaTest_Loader;

public class GeoJson {

    // print GeoJSON of nodeIds
    public static void printGeoJson() {
        System.out.println("{");
        System.out.println("  \"type\": \"FeatureCollection\",");
        System.out.println("  \"features\": [");
        int elementsToPrint = 100;
        for (int i = 0; i < elementsToPrint; i++) {       // used before: FileReader.nodeIds.size()
            System.out.println(getGeoJsonElement(6 , FileReader.longitudes[i], FileReader.latitudes[i]));

            if(i != elementsToPrint-1) {
                System.out.print("}, ");
            } else {
                System.out.println("}]");
                System.out.println("}");
            }
        }
    }



    /**
     * @param moveCharsIn   the static amount of Chars to move in
     * @param longitude     longitude coordinate of node to display
     * @param latitude      latitude coordinate of node to display
     * @return              a GeoJson representation of the coordinates
     */
    private static String getGeoJsonElement(int moveCharsIn, double longitude, double latitude){
        String indention= "";
        for(int i=0; i<moveCharsIn; i++){
            indention.concat(" ");
        }
        String Element =    "{\n" +
                            indention + "\"type\": \"Feature\",\n" +
                            indention + "\"geometry\": {\n" +
                            indention + "  \"type\": \"Point\",\n" +
                            indention + "  \"coordinates\": [" + longitude + ", " + latitude + "]\n" +
                            indention + "},\n" +
                            indention + "\"properties\": {\n" +
                            indention + "\"prop0\": \"value0\"\n" +
                            indention + "}\n";
        return Element;
    }
}
