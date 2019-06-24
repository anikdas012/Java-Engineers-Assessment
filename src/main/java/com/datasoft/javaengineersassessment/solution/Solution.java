package com.datasoft.javaengineersassessment.solution;

import com.datasoft.javaengineersassessment.utils.IO;
import com.google.gson.JsonArray;
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
//		Printing test case number
		System.out.println("\nTest# "+testCaseNo);

//		Printing table names and data
		for (String tableName : tableNames) {
			System.out.println(tableName.split("\\(")[0]);

//			Printing 1st column name which is id
			System.out.print("id");

//			Retrieving all the column names form json object
			int maxKeyIndex = getMaxSize(jsonObjects);
			Object[] maxKeys = jsonObjects.get(maxKeyIndex).keySet().toArray();

//			Checking number of tables for the case
			if (tableNames.size() == 1) {
//				Produce output of single table test case
				printSingleTableData(tableName, maxKeys, jsonObjects);
			} else {
//				Printing column names of the table if table name is not in the column names
				if (!Arrays.asList(maxKeys).contains(tableName.split("\\(")[0])) {
//					Show output of outer object table incase of multiple tables
					printOuterObjectData(maxKeys, tableName, jsonObjects);
				} else {
//					Printing column names for the table who's data is in the nested json object
					if (jsonObjects.get(maxKeyIndex).get(tableName.split("\\(")[0]).isJsonObject()) {
//						Retrieving the key who's value is json object
						String key = tableName.split("\\(")[0];

//						Getting the maximum key list for all the objects
						JsonObject jsonObject = jsonObjects.get(maxKeyIndex).get(key).getAsJsonObject();
						maxKeys = jsonObject.keySet().toArray();

//						Printing keys as column name
						for (Object maxKey : maxKeys) {
							System.out.print(" " + maxKey);
						}

//						Printing all data rows in required order
						if (tableName.contains("desc")) {
							for (int i=jsonObjects.size()-1; i>=0; i--) {
								System.out.print("\n"+(i+1));
//								Getting inner object
								jsonObject = jsonObjects.get(i).get(key).getAsJsonObject();
								for (Object maxKey : maxKeys) {
//									Printing values of inner object
									System.out.print(" " + String.valueOf(jsonObject.get(String.valueOf(maxKey)))
											.replace("\"", ""));
								}
							}
						} else {
							for (int i=0; i<jsonObjects.size(); i++) {
								System.out.print("\n"+(i+1));
//								Getting inner object
								jsonObject = jsonObjects.get(i).get(key).getAsJsonObject();
								for (Object maxKey : maxKeys) {
//									Printing values of inner object
									System.out.print(" " + String.valueOf(jsonObject.get(String.valueOf(maxKey)))
											.replace("\"", ""));
								}
							}
						}

//						Adding empty line between tables of same test case
						System.out.println();
					} else {
//						Retrieving the key who's value is array
						String key = tableName.split("\\(")[0];

//						Printing column name for array data
						System.out.print(" " + tableNames.get(0).split("\\(")[0] + " "+ key);

//						Getting total number of entries for all objects
						int itemCount = 0;
						for (JsonObject object : jsonObjects) {
							itemCount += object.get(key).getAsJsonArray().size();
						}

//						Printing all data rows in required order
						if (tableName.contains("desc")) {
//							Setting maximum id to maximum number of entries
							int ids = itemCount;

//							Iterating through all json objects
							for (int i=jsonObjects.size()-1; i>=0; i--) {
//								Getting the actual array
								JsonArray jsonArray = jsonObjects.get(i).get(key).getAsJsonArray();
//								Iterating through all entries of the array
								for (int j=jsonArray.size()-1; j>=0; j--) {
//									Printing the actual data (id, user ref and array value)
									System.out.print("\n" + ids-- + " " + (i+1) +" " + String.valueOf(jsonArray.get(j))
											.replace("\"", ""));
								}
							}
						} else {
//							Setting initial id to 1
							int ids = 1;

//							Iterating through all json objects
							for (int i=0; i<jsonObjects.size(); i++) {
//								Getting the actual array
								JsonArray jsonArray = jsonObjects.get(i).get(key).getAsJsonArray();
//								Iterating through all entries of the array
								for (int j=0; j<jsonArray.size(); j++) {
//									Printing the actual data (id, user ref and array value)
									System.out.print("\n" + ids++ + " " + (i+1) + " " + String.valueOf(jsonArray.get(j))
											.replace("\"", ""));
								}
							}
						}
					}
				}
			}
//			Adding line break after each table
			System.out.println();
		}
	}


	/**
	 * This method will print data of outer object if
	 * test case has more than one table
	 *
	 * @param maxKeys
	 * @param tableName
	 * @param jsonObjects
	 */
	private void printOuterObjectData(Object[] maxKeys, String tableName, ArrayList<JsonObject> jsonObjects) {
		for (Object maxKey : maxKeys) {
//						if the vale of the column is not an array then printing that column name
			if ((!jsonObjects.get(maxKeyIndex).get(String.valueOf(maxKey)).isJsonArray())) {
				System.out.print(" " + maxKey);
			}
		}

//					Printing row data of the table according to required order
		if (tableName.contains("desc")) {
			for (int i=jsonObjects.size()-1; i>=0; i--) {
				System.out.print("\n"+(i+1));
				for (Object maxKey : maxKeys) {
					if ((!jsonObjects.get(i).get(String.valueOf(maxKey)).isJsonObject()) &&
							(!jsonObjects.get(i).get(String.valueOf(maxKey)).isJsonArray())) {
//									Printing value only if it is not object or array
						System.out.print(" " + String.valueOf(jsonObjects.get(i).get(String.valueOf(maxKey)))
								.replace("\"", ""));
					} else if (jsonObjects.get(i).get(String.valueOf(maxKey)).isJsonObject()) {
//									Printing reference of parent object if the value is an object
						System.out.print(" " + (i+1));
					}
				}
			}
		} else {
			for (int i=0; i<jsonObjects.size(); i++) {
				System.out.print("\n"+(i+1));
				for (Object maxKey : maxKeys) {
					if ((!jsonObjects.get(i).get(String.valueOf(maxKey)).isJsonObject()) &&
							(!jsonObjects.get(i).get(String.valueOf(maxKey)).isJsonArray())) {
//									Printing value only if it is not object or array
						System.out.print(" " + String.valueOf(jsonObjects.get(i).get(String.valueOf(maxKey)))
								.replace("\"", ""));
					} else if (jsonObjects.get(i).get(String.valueOf(maxKey)).isJsonObject()) {
//									Printing reference of parent object if the value is an object
						System.out.print(" " + (i+1));
					}
				}
			}
		}

//					Adding a empty line between tables of same test case
		System.out.println();
	}


	/**
	 * This method will print data of test case which
	 * has only one table
	 *
	 * @param tableName
	 * @param maxKeys
	 * @param jsonObjects
	 */
	private void printSingleTableData(String tableName, Object[] maxKeys, ArrayList<JsonObject> jsonObjects) {
//		Printing all the column names
		for (Object maxKey : maxKeys) {
			System.out.print(" " + maxKey);
		}

//		Printing all the rows of json data
//		Checking if output is required in which order and printing according to that
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
