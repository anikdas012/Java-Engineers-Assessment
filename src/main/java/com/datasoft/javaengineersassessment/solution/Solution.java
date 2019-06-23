package com.datasoft.javaengineersassessment.solution;

import com.datasoft.javaengineersassessment.utils.IO;

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
		}
	}
}
