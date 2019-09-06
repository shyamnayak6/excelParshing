package com.data.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CLI {
	private String SourceFilePath;
	public String getSourceFilePath() {
		return SourceFilePath;
	}
	public void setSourceFilePath(String sourceFilePath) {
		SourceFilePath = sourceFilePath;
	}
	public String getOutputCSVDirectory() {
		return OutputCSVDirectory;
	}
	public void setOutputCSVDirectory(String outputCSVDirectory) {
		OutputCSVDirectory = outputCSVDirectory;
	}
	private String OutputCSVDirectory;

}
