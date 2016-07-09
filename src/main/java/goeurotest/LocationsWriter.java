package goeurotest;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * Created by piskunov on 09/07/16.
 */

@Component
public class LocationsWriter {

    private static final Logger log = LoggerFactory.getLogger(LocationsWriter.class);
    private final static String[] header = {"_id", "name", "type", "latitude", "longitude"};

    public void write(List<Location> locations, String fileName)  {

        try(FileOutputStream fos = new FileOutputStream(fileName);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                CSVWriter writer = new CSVWriter(osw)) {
            writer.writeNext(header);
            for(Location location: locations) {
                String[] row = new String[5];
                row[0] = String.valueOf(location.getId());
                row[1] = location.getName();
                row[2] = location.getType();

                Location.Position pos = location.getPosition();
                if(pos != null) {
                    row[3] = String.valueOf(pos.getLatitude());
                    row[4] = String.valueOf(pos.getLongitude());
                }
                writer.writeNext(row);
            }
        } catch (Exception e) {
            log.error("Unable to save " + fileName + ": " + e.getMessage(), e);
            return;
        }
        log.info(fileName + " saved.");
    }
}
