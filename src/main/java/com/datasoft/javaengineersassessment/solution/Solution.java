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
		getInput();
		System.out.println("Goodbye :)");
	
	}


	/**
	 * This method will get user input
	 */
	private void getInput() {
		int numberOfTestCase = Integer.parseInt(Objects.requireNonNull(IO.readLine()));

//		Reading user input for all test cases
		for (int i=0; i<numberOfTestCase; i++) {
			String[] temp = IO.readLine().split(" ");
			int numberOfTables = Integer.parseInt(temp[0]);
			int numberOfJsonData = Integer.parseInt(temp[1]);
			temp = IO.readLine().split(" ");
			ArrayList<String> tableNames = new ArrayList<>();
			if (numberOfTables == temp.length) {
				tableNames.addAll(Arrays.asList(temp));
			}

//			Storing JSON objects in an arraylist
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

//			Showing output
			showOutput(tableNames, jsonObjects, i+1);

//			Reading empty line after each test case except the last one
			if (i+1 < numberOfTestCase) {
				IO.readLine();
			}

//			Clearing memory
			tableNames.clear();
			jsonObjects.clear();
		}
	}


	/**
	 * This method will show output in required structure
	 *
	 * @param tableNames
	 * @param jsonObjects
	 */
	private void showOutput(ArrayList<String> tableNames, ArrayList<JsonObject> jsonObjects, int testCaseNo) {
		System.out.println("------------- Output ------------------");
		System.out.println("Table names: "+tableNames);
		System.out.println("Json Objects: "+jsonObjects);
		System.out.println("Json Objects size: "+jsonObjects.size());
		for (int i=0; i<jsonObjects.size(); i++) {
			System.out.println("Json Object No. "+i+" size: "+jsonObjects.get(i).size());
			System.out.println("Json Object No. "+i+" key set: "+jsonObjects.get(i).keySet());
			System.out.println("Json Object No. "+i+" entry set: "+jsonObjects.get(i).entrySet());
		}
		System.out.println("------------- Output Ended ------------------");
		System.out.println("\n\n\n");

//		Printing test case number
		System.out.println("\nTest# "+testCaseNo);

//		Printing table names and data
		for (String tableName : tableNames) {
			System.out.println(tableName);

//			Printing 1st column name which is id
			System.out.print("id");

//			Retrieving all the column names form json object
			Object[] maxKeys = jsonObjects.get(getMaxSize(jsonObjects)).keySet().toArray();

//			Checking number of tables for the case
			if (tableNames.size() == 1) {
//				Printing all the column names
				for (Object maxKey : maxKeys) {
					System.out.print(" " + maxKey);
				}

//				Printing all the rows of json data
//				Checking if output is required in which order and printing according to that
				if (tableName.contains("desc")) {
					for (int i=jsonObjects.size()-1; i>=0; i--) {
						System.out.print("\n"+(i+1));
						for (Object maxKey: maxKeys) {
							System.out.print(" " + String.valueOf(jsonObjects.get(i).get(String.valueOf(maxKey)))
									.replace("\"", ""));
						}
					}
				} else {
					for (int i=0; i<jsonObjects.size(); i++) {
						System.out.print("\n"+(i+1));
						for (Object maxKey: maxKeys) {
							System.out.print(" " + String.valueOf(jsonObjects.get(i).get(String.valueOf(maxKey)))
									.replace("\"", ""));
						}
					}
				}
			} else {

			}
			System.out.println();
		}

		System.out.println("\n\n\n");
	}


	/**
	 * This method will return index number of
	 * largest json object from an ArrayList
	 *
	 * @param jsonObjects
	 * @return
	 */
	private int getMaxSize(ArrayList<JsonObject> jsonObjects) {
		int maxIndex = 0;
		for (int i=0; i<jsonObjects.size(); i++) {
			if (jsonObjects.get(i).size() > jsonObjects.get(maxIndex).size()) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}
}
