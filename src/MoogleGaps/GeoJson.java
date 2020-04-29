package MoogleGaps;

public class GeoJson {

    // print GeoJSON of nodeIds
    public static void printGeoJson() {
        System.out.println("{");
        System.out.println("  \"type\": \"FeatureCollection\",");
        System.out.print("  \"features\": [");
        for (int i = 0; i < FileReader.nodeIds.size(); i++) {
            System.out.println("{");
            System.out.println("      \"type\": \"Feature\",");
            System.out.println("      \"geometry\": {");
            System.out.println("        \"type\": \"Point\",");
            System.out.println("        \"coordinates\": [" + FileReader.longitudes[i] + ", " + FileReader.latitudes[i] + "]");
            System.out.println("      },");
            System.out.println("      \"properties\": {");
            System.out.println("        \"prop0\": \"value0\"");
            System.out.println("      }");
            if(i != 99) {
                System.out.print("    }, ");
            } else {
                System.out.println("  }]");
                System.out.println("}");
            }
        }
    }
}
