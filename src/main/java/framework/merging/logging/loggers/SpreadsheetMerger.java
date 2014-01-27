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

	private Set<String> mergedOnyens = new HashSet<String>();
	private Set<String> unmergedOnyens = new HashSet<String>();
	private ArrayList<String> featuresAndRestrictions = new ArrayList<String>();

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

	/*
	 * Export user id data to output row that does not change with merging
	 */
	private void outputUserIdData(XSSFRow outputRow, XSSFRow inputRow) {
		outputRow.createCell(0).setCellValue(inputRow.getCell(0).getStringCellValue());
		outputRow.createCell(1).setCellValue(inputRow.getCell(1).getStringCellValue());
		outputRow.createCell(2).setCellValue(inputRow.getCell(2).getStringCellValue());
		outputRow.createCell(3).setCellValue(inputRow.getCell(3).getStringCellValue());
	}

	/*
	 * Used for the case when score data has nothing to merge with
	 */
	private void outputScoreData(XSSFRow outputRow, XSSFRow inputRow,
			ArrayList<String> featuresAndRestrictions, int rowNum) {
		outputRow.createCell(4).setCellValue(inputRow.getCell(4).getNumericCellValue());
		outputRow.createCell(5).setCellValue(inputRow.getCell(5).getNumericCellValue());
		outputRow.createCell(6).setCellFormula("E" + (rowNum + 1) + "*F" + (rowNum + 1));
		for (int i = 0; i < featuresAndRestrictions.size(); i++) {
			outputRow.createCell(i + 7).setCellValue(inputRow.getCell(i + 7).getNumericCellValue());
		}
	}

	private void mergeScoreData(XSSFRow outputRow, XSSFRow inputRow1, XSSFRow inputRow2,
			ArrayList<String> featuresAndRestrictions, int rowNum) {

		// Output the raw score
		Double rawVal1 = inputRow1.getCell(4).getNumericCellValue();
		Double rawVal2 = inputRow2.getCell(4).getNumericCellValue();
		Double outputRaw = (Double) MergingEnvironment.get().getMergingRules()
				.getMergedVal(ResultTypes.RAW_SCORE, rawVal1, rawVal2);
		outputRow.createCell(4).setCellValue(outputRaw);

		// Output the late or early penalty
		Double penaltyVal1 = inputRow1.getCell(5).getNumericCellValue();
		Double penaltyVal2 = inputRow2.getCell(5).getNumericCellValue();
		Double outputPenalty = (Double) MergingEnvironment.get().getMergingRules()
				.getMergedVal(ResultTypes.LATE_OR_EARLY_PENALTY, penaltyVal1, penaltyVal2);
		outputRow.createCell(5).setCellValue(outputPenalty);

		// Output the formulat for final score
		outputRow.createCell(6).setCellFormula("E" + (rowNum + 1) + "*F" + (rowNum + 1));

		// Output all features and restrictions
		for (int i = 0; i < featuresAndRestrictions.size(); i++) {

			String featureOrRestrictionName = featuresAndRestrictions.get(i);
			Double val1 = inputRow1.getCell(i + 7).getNumericCellValue();
			Double val2 = inputRow2.getCell(i + 7).getNumericCellValue();
			Double outputVal = (Double) MergingEnvironment
					.get()
					.getMergingRules()
					.getMergedVal(new ResultTypes.NamedResultType(featureOrRestrictionName), val1,
							val2);
			outputRow.createCell(i + 7).setCellValue(outputVal);
		}
	}

	/*
	 * Export all rows that already exist in the previously merged sheet
	 * (mergedSheet) to the output sheet (outputSheet)
	 * 
	 * If these rows also exist in the non-merged sheet (resultSheet) merge
	 * those into the output as well
	 * 
	 * returns the next row number in the output sheet
	 */
	private int outputAlreadyMergedRows(XSSFSheet outputSheet, XSSFSheet mergedSheet,
			XSSFSheet unmergedSheet, ArrayList<String> featuresAndRestrictions) {
		int rowNum;
		for (rowNum = 1; rowNum <= mergedSheet.getLastRowNum(); rowNum++) {

			XSSFRow outputRow = outputSheet.createRow(rowNum);

			// Write user data that does not change with merge
			XSSFRow mergedRow = mergedSheet.getRow(rowNum);
			outputUserIdData(outputRow, mergedRow);

			String onyen = mergedRow.getCell(0).getStringCellValue().toLowerCase();
			if (onyen != null && onyen.length() > 0) {

				// Mark this onyen as seen before
				mergedOnyens.add(onyen);

				// Find the row from non-merged results with this same onyen
				XSSFRow nonMergedRow = findRow(onyen, unmergedSheet, unmergedOnyens);
				if (nonMergedRow != null) {
					mergeScoreData(outputRow, mergedRow, nonMergedRow, featuresAndRestrictions,
							rowNum);

				} else {
					// No row from non-merged results, so we only write already
					// merged results to the output
					outputScoreData(outputRow, mergedRow, featuresAndRestrictions, rowNum);
				}
			}
		}
		return rowNum;
	}

	/*
	 * Export to output all rows that were in the unmerged sheet but not in the
	 * merged sheet
	 */
	private void outputMissingNonMergedRows(XSSFSheet outputSheet, XSSFSheet unmergedSheet,
			int rowNum) {
		for (String onyen : unmergedOnyens) {
			XSSFRow resultRow = findRow(onyen, unmergedSheet, new HashSet<String>());
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

	/*
	 * Merge all rows from the previously merged workbook and the unmerged
	 * workbook
	 */
	private void mergeRows(XSSFWorkbook outputWorkbook, XSSFWorkbook unmergedWorkbook,
			XSSFWorkbook mergedWorkbook) {

		mergedOnyens = new HashSet<String>();
		unmergedOnyens = new HashSet<String>();

		XSSFSheet outputSheet = outputWorkbook.getSheetAt(0);
		XSSFSheet unmergedSheet = unmergedWorkbook.getSheetAt(0);
		XSSFSheet mergedSheet = mergedWorkbook.getSheetAt(0);

		int rowNum = outputAlreadyMergedRows(outputSheet, mergedSheet, unmergedSheet,
				featuresAndRestrictions);

		// Output any rows from the non-merged sheet that have not already been
		// merged
		unmergedOnyens.removeAll(mergedOnyens);
		outputMissingNonMergedRows(outputSheet, unmergedSheet, rowNum);
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

		featuresAndRestrictions = writeHeaders(outputWorkbook, mergedWorkbook);

		mergeRows(outputWorkbook, resultWorkbook, mergedWorkbook);

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
