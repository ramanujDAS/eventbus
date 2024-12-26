package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//Singleton
public class ConfigReader {
    String filePath = "config.yml";

    private ConfigReader(){
        readConfigFile(filePath);
    }

    private static ConfigReader instance = null;

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }
    public String getValue(String key) {
        return readConfigFile(filePath).get(key);
    }
    private  Map<String, String> readConfigFile(String configFile) {
        final Map<String, String> config = new HashMap<>();
        try {
            File file = new File(configFile);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(":");
                if (parts.length != 2) {
                    throw new RuntimeException("Invalid config file format");
                }

                String key = parts[0].trim();
                String value = parts[1].trim();
                config.put(key, value);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Config yml file not found: " + configFile);
        }

        return config;
    }
}
