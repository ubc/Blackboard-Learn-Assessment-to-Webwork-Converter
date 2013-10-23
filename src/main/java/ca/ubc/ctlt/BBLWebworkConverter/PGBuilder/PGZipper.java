package ca.ubc.ctlt.BBLWebworkConverter.PGBuilder;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by compass on 13-10-21.
 */
public class PGZipper {
    private List<PGProblem> problems = new ArrayList<PGProblem>();
    private static final String path = "local/";

    public void addProblem(PGProblem pgProblem) {
        this.problems.add(pgProblem);
    }

    public void pack(String packFileName) {
        System.out.println("Creating WeBWork archive file: " + packFileName + ".tgz");
        tar(packFileName);
    }

    public void tar(String packFileName) {
        try {
            FileOutputStream fos = new FileOutputStream(packFileName + ".tgz");

            GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(fos);

            //create ZipOutputStream to write to the zip file
            ArchiveOutputStream os = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.TAR, gzOut);

            String directory = path + packFileName + "/";
            os.putArchiveEntry(new TarArchiveEntry(path));
            os.putArchiveEntry(new TarArchiveEntry(directory));

            String defFile = createDefFile(packFileName);
            // WeBWork likes the name of def file start with "set"
            TarArchiveEntry ze = new TarArchiveEntry("set" + packFileName + ".def");
            ze.setSize(defFile.getBytes().length);
            os.putArchiveEntry(ze);
            os.write(defFile.getBytes());
            os.closeArchiveEntry();

            for (PGProblem problem : problems) {

                //add a new Zip Entry to the ZipOutputStream
                ze = new TarArchiveEntry(directory + problem.getTitle().replace(" ", "_") + ".pg");
                ze.setSize(problem.toString().getBytes().length);
                os.putArchiveEntry(ze);
                os.write(problem.toString().getBytes());

                //Close the zip entry to write to zip file
                os.closeArchiveEntry();
            }
            //Close resources
            os.close();
            gzOut.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }

    public String createDefFile(String zipFileName) {
        String directory = path + zipFileName + "/";
        StringBuffer buffer = new StringBuffer();

        buffer.append("openDate          = 09/06/2012 at 03:28pm PDT\n" +
                "dueDate           = 09/20/2012 at 03:28pm PDT\n" +
                "answerDate        = 09/20/2012 at 03:28pm PDT\n" +
                "paperHeaderFile   = defaultHeader\n" +
                "screenHeaderFile  = defaultHeader\n" +
                "problemList       = \n");

        for (PGProblem problem : problems) {
            buffer.append(directory + problem.getTitle().replace(" ", "_") + ".pg, 1, -1" + PGProblem.LF);
        }

        return buffer.toString();
    }
}
