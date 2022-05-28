package assignment3;

//@name Vicki Young
//@date/version 2022.05.04
//CS245 Assignment3: CryptoPortfolio

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CryptoPortfolio {
    public static void main(String[] args) {
        String filename, source;
        //args[0] = source, args[1] = csv filename
        //if no csv file provided, default file name "instruments.csv" is used
        if (args.length < 2) {
            filename = "instrument.csv";
        } else {
            filename = args[1];
        }
        //if no source provided, default source "btc" is used
        if (args.length < 1) {
            source = "btc";
        } else {
            source = args[0];
        }

        //Do not modify the code in the try/catch block below.
        try {
            HashMap<String, Double> counts = aggregateCounts(filename);
            Graph g = new Graph(counts);
            Map<String, Double> flows = g.findFlows(source);
            System.out.println(g.sortFlows(flows));
        } catch (FileNotFoundException e) {
            System.out.println(filename + " does not exist");
            System.exit(1);
        }
    }

    /**
     * aggregateCounts opens the csv file using the filename parameter and aggregate
     * trade (transaction) counts over the same cryptocurrency Base Asset and Quote Asset pair.
     * @param filename represents the csv file name
     * @return HashMap<String, Double> list of "Base Asset, Quote Asset" and total trade counts.
     * @throws FileNotFoundException if the file does not exist.
     */
    private static HashMap<String, Double> aggregateCounts(String filename) throws FileNotFoundException {
        HashMap<String, Double> counts = new HashMap<>();
        int baseAsset = 0;
        int quoteAsset = 0;
        int tradesCount = 0;
        //read filename line by line
        FileInputStream is = new FileInputStream(filename);
        try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8.name())) {
            boolean firstLine = true;
            //for each line in file, split contents by comma into an array of strings
            while (sc.hasNextLine()) {
                String[] arrOfStr = sc.nextLine().split(",");
                //if first line in file, get indexes for base asset, quote asset, and trades count
                if (firstLine) {
                    //loop through first line string array to get indexes
                    for (int i = 0;i < arrOfStr.length; i++) {
                        if (arrOfStr[i].equals("Base Asset")) {
                            baseAsset = i;
                        }
                        if (arrOfStr[i].equals("Quote Asset")) {
                            quoteAsset = i;
                        }
                        if (arrOfStr[i].equals("Trades Count")) {
                            tradesCount = i;
                        }
                    }
                    //make firstLine false to indicate first line has been read
                    firstLine = false;
                    continue;
                }
                //using indexes from first line in file, get key and total, and add key-value pair to hashmap
                String key = arrOfStr[baseAsset] + ", " + arrOfStr[quoteAsset];
                Double total = Double.parseDouble(arrOfStr[tradesCount]);
                //if key already exists in hashmap, remove + get its trade count, add it to new trades count total, and add key-value to hashmap again
                if (counts.containsKey(key)) {
                    total += counts.remove(key);
                }
                //add key and total transaction count to counts
                counts.put(key, total);
            }
        }
        //return counts
        return counts;
    }
}
