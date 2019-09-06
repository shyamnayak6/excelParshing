package com.data.parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.data.parse.CLI;
import com.data.parse.CLIOptions;

public class AppMain {
	


	  final static Logger logger = Logger.getLogger(AppMain.class);
	private static String destinationCSV;
	
	public static void main(String[] args) {

		Properties log4jProperties = new Properties();
		log4jProperties.setProperty("log4j.rootLogger", "ERROR, myConsoleAppender");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender", "org.apache.log4j.ConsoleAppender");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender.layout", "org.apache.log4j.PatternLayout");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender.layout.ConversionPattern", "%-5p %c %x - %m%n");
		PropertyConfigurator.configure(log4jProperties);
//		 CommandLineParser parser = new DefaultParser();
//		 String sourceFilePath = parser.parse(arg0, arg1)
		AppMain main = new AppMain();
		
		
		

		try {
	
			//main.readSheet(file1);
			CLI cli = new CLIOptions(args).parse();
			String sourceFilePath = cli.getSourceFilePath();
			destinationCSV= cli.getOutputCSVDirectory();
			main.readSheet(new File(sourceFilePath));
		} catch (Exception e) {
			
			logger.error(e.getLocalizedMessage());
			e.getStackTrace();
		}


	}

	private String headerName;
	private TreeSet<String> columnData1;
	private CSVPrinter csvPrinter;
	private Iterator<String> iterrate;
	private TreeSet<String> columnDataList;

	// get file from classpath, resources folder
	private File getFileFromResources(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}

	}


	private void readSheet(File file1) throws Exception {

		XSSFWorkbook xSSFWorkbook = null;
		String cellValue = null;
		try {
			// File file1 = new File("E:\\Eclipse
			// latest\\parse\\src\\main\\resources\\test_data_distribution_sheet.xlsx");
			File file = new File(
				"\\\\catl0fs02\\dept\\QA\\Current\\2019\\Care\\ICE\\ICE 5.0\\02 Requirement Gathering\\Test Data Requirement\\delete\\TestData_Mapping Sheet_V3.0_Shyam.xlsx");

			xSSFWorkbook = new XSSFWorkbook(file);
		} catch (InvalidFormatException e) {

			logger.error(e.getLocalizedMessage());
		} catch (IOException e) {

			logger.error(e.getLocalizedMessage());
		}
		int sheetCount = xSSFWorkbook.getNumberOfSheets();
		for (int i = 0; i < sheetCount; i++) {
			Map<String, TreeSet<String>> storeColumnData = new HashMap<String, TreeSet<String>>();
			XSSFSheet sheet = xSSFWorkbook.getSheetAt(i);

			int lastRowNum = sheet.getLastRowNum();

			for (int j = 3; j <= lastRowNum; j++) {

				XSSFRow row0 = sheet.getRow(0);
				XSSFRow row1 = sheet.getRow(1);
				XSSFRow row2 = sheet.getRow(2);

				XSSFRow row = sheet.getRow(j);
				int lastCellNum = row.getLastCellNum();
				for (int k = 0; k < lastCellNum; k++) {
					headerName = getCellValueBasedOnType(row0.getCell(k)) + "_"
							+ getCellValueBasedOnType(row1.getCell(k)) + "_" + getCellValueBasedOnType(row2.getCell(k));
					cellValue = null;
					logger.error("CSV file names"+headerName);
					XSSFCell cell = row.getCell(k);
					if (null != cell) {

						cellValue = getCellValueBasedOnType(cell);

					}

					if (storeColumnData.containsKey(headerName)) {
						columnData1 = storeColumnData.get(headerName);
						if (null != cellValue)
							columnData1.add(cellValue);
					} else {
						storeColumnData.put(headerName, new TreeSet());
						columnData1 = storeColumnData.get(headerName);
						if (null != cellValue)
							columnData1.add(cellValue);

					}

				}

			}

			writeDataToCSV(storeColumnData);

			//System.out.println(storeColumnData);

		}

		try {
			xSSFWorkbook.close();
		} catch (IOException e) {

			logger.error(e.getLocalizedMessage());
		}

	}

	private String getCellValueBasedOnType(XSSFCell cell) throws Exception {
		final int type = cell.getCellType().getCode();
		if (type == cell.getCellType().BLANK.getCode()) {
			return null;
		} else if (type == cell.getCellType().STRING.getCode()) {
			return cell.getStringCellValue();
		} else if (type == cell.getCellType().BOOLEAN.getCode()) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (type == cell.getCellType().NUMERIC.getCode()) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return String.valueOf(cell.getDateCellValue());
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}

		} else if (type == cell.getCellType()._NONE.getCode()) {
			return null;
		} else {
			throw new IllegalArgumentException("Invalid cell type " + cell.getCellType());
		}
		// return cellValue;
	}

	private void writeDataToCSV(Map<String, TreeSet<String>> storeColumnData) {

		Set<String> keys = storeColumnData.keySet();
		for (String key : keys) {
			columnDataList = storeColumnData.get(key);
			// FileWriter out = new FileWriter(key+".csv");
			BufferedWriter writer;
			try {
//				writer = Files.newBufferedWriter(Paths.get(
//						"\\\\catl0fs02\\dept\\QA\\Current\\2019\\Care\\ICE\\ICE 5.0\\02 Requirement Gathering\\Test Data Requirement\\delete\\csv\\"
//								+ key + ".csv"));
				
				writer = Files.newBufferedWriter(Paths.get(destinationCSV+ key + ".csv"));
				csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(key.split("_")[1]));
				iterrate = columnDataList.iterator();
				while (iterrate.hasNext()) {
					csvPrinter.printRecord(iterrate.next());
				}
				csvPrinter.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getLocalizedMessage());
			}

		}

		/*
		 * Iterator<String> keys =storeColumnData.keySet().iterator();
		 * while(keys.hasNext()) { TreeSet<String> columnData=
		 * storeColumnData.get(keys.next());
		 */
	}

}
