package com.datasoft.javaengineersassessment.solution;

import com.datasoft.javaengineersassessment.utils.IO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Solution implements Runnable{


	/**
	 * Application entry to your solution
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		System.out.println("All set ...");
		System.out.println("Goodbye :)");
	
	}


	/**
	 * This method will get user input
	 */
	private void getInput() {
		int numberOfTestCase = Integer.parseInt(Objects.requireNonNull(IO.readLine()));
		for (int i=0; i<numberOfTestCase; i++) {
			String[] temp = IO.readLine().split(" ");
			int numberOfTables = Integer.parseInt(temp[0]);
			int numberOfJsonData = Integer.parseInt(temp[1]);
			temp = IO.readLine().split(" ");
			ArrayList<String> tableNames = new ArrayList<>();
			if (numberOfTables == temp.length) {
				tableNames.addAll(Arrays.asList(temp));
			}
			ArrayList<JsonObject> jsonObjects = new ArrayList<>();
			for (int j = 0; j < numberOfJsonData; j++) {
				StringBuilder jobj = new StringBuilder();
				int startingBrace = 0;
				int endingBrace = 0;
				while (true) {
					String z = IO.readLine();
					if (z != null) {
						if (z.equals("{")) {
							startingBrace++;
						}
						if (z.equals("}")) {
							endingBrace++;
						}
						jobj.append(z);
						if (startingBrace == endingBrace) {
							break;
						}
					}
				}
				jsonObjects.add(new JsonParser().parse(jobj.toString()).getAsJsonObject());
			}
			showOutput(tableNames, jsonObjects);
		}
	}


	private void showOutput(ArrayList<String> tableNames, ArrayList<JsonObject> jsonObjects) {
	}
}
