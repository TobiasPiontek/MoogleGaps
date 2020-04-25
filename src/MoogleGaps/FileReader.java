package MoogleGaps;


import crosby.binary.osmosis.OsmosisReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileReader {
    public void readPBSFile(String relativeFilePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("Test");
        OsmosisReader reader = new OsmosisReader(inputStream);
    }

}
