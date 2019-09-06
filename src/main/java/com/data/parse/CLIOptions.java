package com.data.parse;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CLIOptions {
	private static final Logger log = Logger.getLogger(CLIOptions.class.getName());
	private String[] args = null;
	private Options options = new Options();

	public CLIOptions(String[] args) {
		this.args = args;
		options.addOption("h", "help", false, "Show help commands");
		options.addOption("s", "sourceFilePath", true, "Set input excel file path to parsed ");
		options.addOption("d", "outputCSVDirectory", true, "Set output CSV directory path to generate CSV files ");
				
	}

	public CLI parse() {
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		CLI cli = new CLI();
		
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("s")) {
				
				cli.setSourceFilePath(cmd.getOptionValue("s"));
				log.log(Level.INFO, "Using CLI Argument -source file path : " + cmd.getOptionValue("s"));				
			}
			else {
				log.log(Level.ERROR, "Missing s (SourceFilePath) option");
				help();
			}
			if (cmd.hasOption("d")) {
				cli.setOutputCSVDirectory(cmd.getOptionValue("d"));
				log.log(Level.INFO, "Using CLI Argument - destination path: " + cmd.getOptionValue("d"));				
			}
			else {
				log.log(Level.ERROR, "Missing d (Destination) option");
				help();
			}
			
		} catch (ParseException e) {
			log.log(Level.ERROR, "Failed to parse comand line properties", e);
			help();
		}
		return cli;
	}

	private void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("Main", options);
		System.exit(0);
	}
}
