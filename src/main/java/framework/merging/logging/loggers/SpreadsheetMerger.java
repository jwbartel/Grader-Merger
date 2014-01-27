package framework.merging.logging.loggers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import mergingTools.utils.MergingEnvironment;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import framework.merging.logging.MergerLogger;
import framework.merging.navigation.GraderResultFolder;
import framework.merging.rules.ResultTypes;

public class SpreadsheetMerger extends MergerLogger {

	/*
	 * Writes the header from input into the output workbook
	 * 
	 * returns an ArrayList of Feature and Restriction names
	 */
	private ArrayList<String> writeHeaders(XSSFWorkbook output, XSSFWorkbook input) {

		ArrayList<String> featuresAndRestrictions = new ArrayList<String>();

		XSSFSheet sheet = output.createSheet();
		XSSFRow outputRow = sheet.createRow(0);
		outputRow.createCell(0).setCellValue("Display ID");
		outputRow.createCell(1).setCellValue("ID");
		outputRow.createCell(2).setCellValue("Last Name");
		outputRow.createCell(3).setCellValue("First Name");
		outputRow.createCell(4).setCellValue("Raw score");
		outputRow.createCell(5).setCellValue("Early/Late modifier");
		outputRow.createCell(6).setCellValue("Final score");

		XSSFRow inputRow = input.getSheetAt(0).getRow(0);
		for (int columnCount = 7; columnCount < inputRow.getLastCellNum(); columnCount++) {
			String label = inputRow.getCell(columnCount).getStringCellValue();
			outputRow.createCell(columnCount).setCellValue(label);
			featuresAndRestrictions.add(label);
		}
		return featuresAndRestrictions;
	}

	private void mergeRows(XSSFWorkbook outputWorkbook, XSSFWorkbook resultWorkbook,
			XSSFWorkbook mergedWorkbook, ArrayList<String> featuresAndRestrictions) {

		Set<String> mergedOnyens = new HashSet<String>();
		Set<String> resultOnyens = new HashSet<String>();

		XSSFSheet outputSheet = outputWorkbook.getSheetAt(0);
		XSSFSheet resultSheet = resultWorkbook.getSheetAt(0);
		XSSFSheet mergedSheet = mergedWorkbook.getSheetAt(0);

		int rowNum;
		for (rowNum = 1; rowNum <= mergedSheet.getLastRowNum(); rowNum++) {

			XSSFRow outputRow = outputSheet.createRow(rowNum);

			// Write user data that does not change with merge
			XSSFRow mergedRow = mergedSheet.getRow(rowNum);
			outputRow.createCell(0).setCellValue(mergedRow.getCell(0).getStringCellValue());
			outputRow.createCell(1).setCellValue(mergedRow.getCell(1).getStringCellValue());
			outputRow.createCell(2).setCellValue(mergedRow.getCell(2).getStringCellValue());
			outputRow.createCell(3).setCellValue(mergedRow.getCell(3).getStringCellValue());

			String onyen = mergedRow.getCell(0).getStringCellValue().toLowerCase();
			if (onyen != null && onyen.length() > 0) {

				if (onyen.equals("amyzhang") || onyen.equals("lindzh")) {
					System.out.println("reached");
				}

				// Mark this onyen as seen before
				mergedOnyens.add(onyen);

				// Find the row from non-merged results with this same onyen
				XSSFRow resultRow = findRow(onyen, resultSheet, resultOnyens);
				if (resultRow != null) {
					// Merge the non-merged results with already merged one

					// Output the raw score
					Double mergedRaw = mergedRow.getCell(4).getNumericCellValue();
					Double resultRaw = resultRow.getCell(4).getNumericCellValue();
					Double outputRaw = (Double) MergingEnvironment.get().getMergingRules()
							.getMergedVal(ResultTypes.RAW_SCORE, mergedRaw, resultRaw);
					outputRow.createCell(4).setCellValue(outputRaw);

					// Output the late or early penalty
					Double mergedLate = mergedRow.getCell(5).getNumericCellValue();
					Double resultLate = resultRow.getCell(5).getNumericCellValue();
					Double outputLate = (Double) MergingEnvironment
							.get()
							.getMergingRules()
							.getMergedVal(ResultTypes.LATE_OR_EARLY_PENALTY, mergedLate, resultLate);
					outputRow.createCell(5).setCellValue(outputLate);

					// Output the formulat for final score
					outputRow.createCell(6)
							.setCellFormula("E" + (rowNum + 1) + "*F" + (rowNum + 1));

					// Output all features and restrictions
					for (int i = 0; i < featuresAndRestrictions.size(); i++) {

						String featureOrRestrictionName = featuresAndRestrictions.get(i);
						Double mergedVal = mergedRow.getCell(i + 7).getNumericCellValue();
						Double resultVal = resultRow.getCell(i + 7).getNumericCellValue();
						Double outputVal = (Double) MergingEnvironment
								.get()
								.getMergingRules()
								.getMergedVal(
										new ResultTypes.NamedResultType(featureOrRestrictionName),
										mergedVal, resultVal);
						outputRow.createCell(i + 7).setCellValue(outputVal);
					}

				} else {
					// No row from non-merged results, so we only write already
					// merged results to the output
					outputRow.createCell(4)
							.setCellValue(mergedRow.getCell(4).getNumericCellValue());
					outputRow.createCell(5)
							.setCellValue(mergedRow.getCell(5).getNumericCellValue());
					outputRow.createCell(6)
							.setCellFormula("E" + (rowNum + 1) + "*F" + (rowNum + 1));
					for (int i = 0; i < featuresAndRestrictions.size(); i++) {
						outputRow.createCell(i + 7).setCellValue(
								mergedRow.getCell(i + 7).getNumericCellValue());
					}
				}
			}
		}

		// Output any rows from the non-merged sheet that have not already been
		// merged
		resultOnyens.removeAll(mergedOnyens);
		for (String onyen : resultOnyens) {
			XSSFRow resultRow = findRow(onyen, resultSheet, new HashSet<String>());
			if (resultRow != null) {
				XSSFRow outputRow = outputSheet.createRow(rowNum);
				outputRow.createCell(4).setCellValue(resultRow.getCell(4).getNumericCellValue());
				outputRow.createCell(5).setCellValue(resultRow.getCell(5).getNumericCellValue());
				outputRow.createCell(6).setCellFormula("E" + (rowNum + 1) + "*F" + (rowNum + 1));
				for (int j = 0; j < featuresAndRestrictions.size(); j++) {
					outputRow.createCell(j + 7).setCellValue(
							resultRow.getCell(j + 7).getNumericCellValue());
				}
				rowNum++;
			}
		}
	}

	private XSSFRow findRow(String onyen, XSSFSheet sheet, Set<String> foundOnyens) {
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			String foundOnyen = row.getCell(0).getStringCellValue().toLowerCase();
			if (foundOnyen != null && foundOnyen.length() > 0) {
				foundOnyens.add(foundOnyen);
			}
			if (foundOnyen.equals(onyen)) {
				return row;
			}
		}
		return null;
	}

	private void mergeSpreadsheets(File spreadsheetResults, File mergedSpreadsheet)
			throws IOException {
		System.out.println("\tMerging Spreadsheets...");
		XSSFWorkbook outputWorkbook = new XSSFWorkbook();

		XSSFWorkbook resultWorkbook = new XSSFWorkbook(new FileInputStream(spreadsheetResults));
		XSSFWorkbook mergedWorkbook = new XSSFWorkbook(new FileInputStream(mergedSpreadsheet));

		ArrayList<String> featuresAndRestrictions = writeHeaders(outputWorkbook, mergedWorkbook);

		mergeRows(outputWorkbook, resultWorkbook, mergedWorkbook, featuresAndRestrictions);

		outputWorkbook.write(new FileOutputStream(mergedSpreadsheet));
		System.out.println("\tdone.");
	}

	@Override
	public void mergeResults(GraderResultFolder resultFolder, File mergeFolder) {
		File spreadsheetFile = new File(resultFolder.getFolder(), "grades.xlsx");
		File mergedSpreadsheetFile = findLocationInMerge(mergeFolder, resultFolder, spreadsheetFile);

		if (spreadsheetFile.exists()) {

			if (!mergedSpreadsheetFile.exists()) {
				try {
					FileUtils.copyFile(spreadsheetFile, mergedSpreadsheetFile, true);
				} catch (IOException e) {
					System.out.println("Error copying spreadsheet " + spreadsheetFile);
				}
			} else {
				try {
					mergeSpreadsheets(spreadsheetFile, mergedSpreadsheetFile);
				} catch (IOException e) {
					System.out.println("Error merging values from " + spreadsheetFile);
				}
			}

		}
	}

}
