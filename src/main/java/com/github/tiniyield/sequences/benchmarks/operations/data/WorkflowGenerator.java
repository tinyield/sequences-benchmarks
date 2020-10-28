package com.github.tiniyield.sequences.benchmarks.operations.data;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.tiniyield.sequences.benchmarks.all.match.AllMatchBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryClassBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryIntegerBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryRandomStringBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryStringBenchmark;
import com.github.tiniyield.sequences.benchmarks.find.FindClassBenchmark;
import com.github.tiniyield.sequences.benchmarks.find.FindFixedIndexBenchmark;
import com.github.tiniyield.sequences.benchmarks.find.FindIntegerBenchmark;
import com.github.tiniyield.sequences.benchmarks.find.FindStringBenchmark;
import com.github.tiniyield.sequences.benchmarks.first.FindFirstInBeginningBenchmark;
import com.github.tiniyield.sequences.benchmarks.first.FindFirstInEndBenchmark;
import com.github.tiniyield.sequences.benchmarks.first.FindFirstInMiddleBenchmark;
import com.github.tiniyield.sequences.benchmarks.flatmap.FlatMapAndReduceBenchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;

public class WorkflowGenerator {

    private static final String BRANCH = "branch";
    private static final String CLASS = "class";
    private static final String NAME = "name";

    public static void generateWorkflows() {
        // all.match
        WorkflowGenerator.generateCollectionWorkflow("All_Match", "all-match", AllMatchBenchmark.class.getSimpleName());

        // every
        WorkflowGenerator.generateCollectionWorkflow("Every[Random_String]", "every/random-string", EveryRandomStringBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("Every[Integer]", "every/integer", EveryIntegerBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("Every[Class]", "every/class", EveryClassBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("Every[String]", "every/string", EveryStringBenchmark.class.getSimpleName());

        // find
        WorkflowGenerator.generateCollectionWorkflow("Find[Class]", "find/class", FindClassBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("Find[Integer]", "find/integer", FindIntegerBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("Find[String]", "find/string", FindStringBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("Find[Fixed_Index]", "find/fixed-index", FindFixedIndexBenchmark.class.getSimpleName());

        // first
        WorkflowGenerator.generateCollectionWorkflow("First[Beginning]", "first/beginning", FindFirstInBeginningBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("First[Middle]", "first/middle", FindFirstInMiddleBenchmark.class.getSimpleName());
        WorkflowGenerator.generateCollectionWorkflow("First[End]", "first/end", FindFirstInEndBenchmark.class.getSimpleName());

        // FlatMap and Reduce
        WorkflowGenerator.generateCollectionWorkflow("Flatmap_Reduce", "flatmap-reduce", FlatMapAndReduceBenchmark.class.getSimpleName());
    }


    public static void generateCollectionWorkflow(String benchmarkName,
                                                  String branch,
                                                  String className) {
        createCollectionWorkflow(benchmarkName, getCollectionTemplateMap(className, benchmarkName, branch));
    }

    private static void createCollectionWorkflow(String benchmarkName,
                                                 HashMap<String, String> scopes) {
        try {
            Writer writer = new FileWriter(getFileName(benchmarkName), false);
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

    private static String getFileName(String benchName) {
        return "./.github/workflows/" + benchName.toLowerCase().replace(' ', '-').replace('_', '-') + ".yml";
    }

    private static HashMap<String, String> getCollectionTemplateMap(String className, String benchName, String branch) {
        HashMap<String, String> scopes = new HashMap<>();
        scopes.put(BRANCH, branch);
        scopes.put(CLASS, className);
        scopes.put(NAME, benchName);
        return scopes;
    }
}
