package com.github.tiniyield.sequences.benchmarks.operations.data;

import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.CaseFormat;

public class WorkflowGenerator {

    private static final String COLLECTION_SIZE_SHORT="cs";
    private static final String COLLECTION_SIZE="collection_size";
    private static final String BRANCH="branch";
    private static final String CLASS="class";
    private static final HashMap<String, String> COLLECTION_SIZES = new HashMap<>();

    static  {
        COLLECTION_SIZES.put("10", "10");
        COLLECTION_SIZES.put("1000", "1K");
        COLLECTION_SIZES.put("100000", "100K");
    }

    public static void generateCollectionWorkflow(String className, String branch){
        COLLECTION_SIZES.keySet().forEach(collectionSize -> {
            createCollectionWorkflow(className, collectionSize,  getCollectionTemplateMap(collectionSize, className, branch));
        });

    }

    private static void createCollectionWorkflow(String className,
                                                 String collectionSize,
                                                 HashMap<String, String> scopes) {
        try {
            Writer writer = new FileWriter(getFileName(className, collectionSize), false);
            MustacheFactory mf = new DefaultMustacheFactory();
            InputStream input = WorkflowGenerator.class.getClassLoader()
                                                       .getResourceAsStream("collection-benchmark-template.yml");
            Mustache mustache = mf.compile(new InputStreamReader(input), "template");
            mustache.execute(writer, scopes);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileName(String className, String collectionSize) {
        String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className).replace("_", "-").replace("-benchmark", "");
        return "./.github/workflows/" + name + "-" + COLLECTION_SIZES.get(collectionSize) + ".yml";
    }

    private static HashMap<String, String> getCollectionTemplateMap(String collectionSize, String name, String branch) {
        HashMap<String, String> scopes = new HashMap<>();
        scopes.put(COLLECTION_SIZE_SHORT, COLLECTION_SIZES.get(collectionSize));
        scopes.put(COLLECTION_SIZE, collectionSize);
        scopes.put(BRANCH, branch);
        scopes.put(CLASS, name);
        return scopes;
    }
}
