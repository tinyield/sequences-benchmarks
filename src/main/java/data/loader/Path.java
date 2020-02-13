package data.loader;

public class Path {

    private static final String RESOURCES_FOLDER = "resources";

    String getFile(String name) {
        return getClass().getClassLoader().getResource(name).getFile();
    }

    String getPath(String name) {
        return "" + name;
    }
}
