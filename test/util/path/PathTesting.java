package util.path;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;

public class PathTesting {

    public static void main(String[] args) {
        PathList pathList = new PathList(new PathPoint(10,10 , Color.BLACK));
        pathList.add(new PathPoint(20, 20, Color.BLACK));
        pathList.add(new PathPoint(10, 20, Color.BLACK));
        try {
            String pathToFile = pathList.writeToSerializationFile();
            ObjectInputStream objectInput = new ObjectInputStream(PathTesting.class.getClassLoader().getResourceAsStream(pathToFile));
            PathList pathListRecovered = (PathList) objectInput.readObject();
            System.out.println(pathListRecovered.next().getX());
        } catch (ClassNotFoundException | IOException e) {
            // do nothing
            e.printStackTrace();
        }
    }
}
