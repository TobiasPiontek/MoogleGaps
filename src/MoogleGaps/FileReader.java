package MoogleGaps;

import crosby.binary.osmosis.OsmosisReader;

import org.openstreetmap.osmosis.core.container.v0_6.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class FileReader {
    public static void readPBSFile(String relativeFilePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("Test");
        OsmosisReader reader = new OsmosisReader(inputStream);

    }

}
