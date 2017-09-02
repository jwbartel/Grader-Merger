package framework.merging.logging.loggers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import mergingTools.utils.MergingEnvironment;

import org.apache.commons.io.FileUtils;

import framework.merging.logging.MergerLogger;
import framework.merging.navigation.GraderResultFolder;
import framework.merging.rules.ResultTypes;

public class CsvMerger extends MergerLogger {

	private void writeResults(File outputCsvFile, Map<String, Double> csvResults) {

		System.out.println("\tMerging CSVs...");
		List<String> csvLines;
		try {
			csvLines = FileUtils.readLines(outputCsvFile);
		} catch (Exception e) {
			System.out.println("Error loading csv results from " + outputCsvFile);
			return;
		}

		try {
			FileWriter writer = new FileWriter(outputCsvFile);
			Iterator<String> lineIter = csvLines.iterator();
			writer.write(lineIter.next() + "\n");
			writer.write(lineIter.next() + "\n");
			writer.write(lineIter.next() + "\n");

			while (lineIter.hasNext()) {
				String line = lineIter.next();
				line = correctCsvEntrySpacing(line);

				String key = "";
				Double outputVal = null;

				String[] split = line.split(",");
				if (split.length>=5 &&!isBlank(split[4])) { //Is there a score present?
					outputVal = Double.valueOf(split[4]);
				}
				for (int i = 0; i < 4; i++) {
					if (key.length() > 0) {
						key += ",";
					}
					key += split[i];

				}

				Double inputVal = csvResults.get(key);
				Double mergedVal = (Double) MergingEnvironment.get().getMergingRules()
						.getMergedVal(ResultTypes.FINAL_SCORE, outputVal, inputVal);

				writer.write(key + ",");
				if (mergedVal != null) {
					writer.write("" + mergedVal);
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing merged results.");
		}
		System.out.println("\tDone.");
	}

	private String correctCsvEntrySpacing(String entry) {
		String[] split = entry.split(",");
		String correctedEntry = "";
		for (int i = 0; i < split.length; i++) {
			if (i > 0) {
				correctedEntry += ",";
			}
			if (i == 4) {
				correctedEntry += "   ";
			}
			correctedEntry += split[i].trim();
		}
		return correctedEntry;
	}

	private Map<String, Double> loadResults(File csvFile) throws FileNotFoundException {
		Map<String, Double> csvResults = new TreeMap<String, Double>();
		Scanner scanner = new Scanner(csvFile);
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			line = correctCsvEntrySpacing(line);

			String[] split = line.split(",");
			if (split.length <= 4 || isBlank(split[4])) {
				continue;
			}

			String key = split[0]+","+split[1]+","+split[2]+","+split[3];
			Double value = Double.parseDouble(split[4]);
			csvResults.put(key, value);
		}
		scanner.close();
		return csvResults;
	}

	@Override
	public void mergeResults(GraderResultFolder resultFolder, File outputFolder) {
		File csvFile = resultFolder.getBulkDownloadFolder().getGrades();
		File outputCsvFile = findLocationInMerge(outputFolder, resultFolder, csvFile);

		if (!outputCsvFile.exists()) {
			try {
				if (!outputCsvFile.getParentFile().exists()) {
					outputCsvFile.getParentFile().mkdirs();
				}
				FileUtils.copyFile(csvFile, outputCsvFile);
			} catch (IOException e) {
				System.out.println("Error creating csv file: " + outputCsvFile);
			}
			// return;
		}

		Map<String, Double> csvResults;
		try {
			csvResults = loadResults(csvFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error loading csv results from " + csvFile);
			return;
		}

		writeResults(outputCsvFile, csvResults);

	}
	  public static boolean isBlank(String str) {
	      int strLen;
	      if (str == null || (strLen = str.length()) == 0) {
	          return true;
	      }
	      for (int i = 0; i < strLen; i++) {
	          if ((Character.isWhitespace(str.charAt(i)) == false)) {
	              return false;
	          }
	      }
	      return true;
	  }

}
