# excelParshing

This utility is designed to create CSV files from excel worksheet. This is particularly developed for JMeter CSV test data generation using excel distribution sheet where first 3 rows are defined as header( script name, test data type and load generator machine). 

•	Test Data Generation (From distribution excel to Multiple CSV Files)
1.	CSV name<scriptanme_testdataType_machineName.CSV> will be generated based on the first 3 rows of distribution sheet header containing script name, data type and LG machine name.
2.	The 1st row of CSV file will be test data type.
3.	It reads multiple sheets of distribution sheet file.
4.	Executable jar with -s <source> and -d<destination> parameter where source is  distribution sheet path and destination is directory path for CSV files 
