package facerecognition.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;

public class WriteToHDFS {
	
	public static void addFile(org.apache.hadoop.fs.FileSystem fileSystem, String source, Path dest) throws IOException {
		
        //FileSystem fileSystem = FileSystem.get(conf);

        // Get the filename out of the file path
        String filename = source.substring(source.lastIndexOf('/') + 1,
            source.length());

        // Create the destination path including the filename.
       
       dest=dest.suffix(filename);
        

        // System.out.println("Adding file to " + destination);

        // Check if the file already exists
        Path path = dest;
        if (fileSystem.exists(path)) {
            System.out.println("File " + dest + " already exists");
            return;
        }

        // Create a new file and write data to it.
        FSDataOutputStream out = fileSystem.create(path);
        InputStream in = new BufferedInputStream(new FileInputStream(
            new File(source)));

        byte[] b = new byte[1024];
        int numBytes = 0;
        while ((numBytes = in.read(b)) > 0) {
            out.write(b, 0, numBytes);
        }

        // Close all the file descripters
        in.close();
        out.close();
        
    }


}
