package goeurotest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by piskunov on 09/07/16.
 */

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Autowired
    private LocationsWriter writer;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        if(args.length == 0) {
            log.error("\nUsage: java -jar GoEuroTest.jar CITY_NAME");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = env.getProperty("endpoint") + args[0];
        List<Location> locations;

        try{
            locations = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Location>>() {}).getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }

        if(locations != null && locations.size() > 0) {
            writer.write(locations, env.getProperty("output"));
        } else {
            log.info("Empty result is returned by the service");
        }
    }
}
