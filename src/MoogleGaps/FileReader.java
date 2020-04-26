package MoogleGaps;

import crosby.binary.osmosis.OsmosisReader;
import org.openstreetmap.osmosis.core.Osmosis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class FileReader {
    public static void readPBSFile(String relativeFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(relativeFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

        OsmosisReader reader = new OsmosisReader(inputStream);
        MyOsmReader mapData = new MyOsmReader();
        reader.setSink(mapData);
        reader.run();
        for (int i = 0; i < mapData.wayIds.size(); i++) {
            System.out.println(mapData.wayIds.get(i));
        }
    }

}
