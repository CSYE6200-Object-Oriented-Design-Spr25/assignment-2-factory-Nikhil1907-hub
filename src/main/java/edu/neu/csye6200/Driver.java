package edu.neu.csye6200;

/**
 * 
 * @author Ruchika Sharm
 * 
 */

public class Driver {
	public static void main(String[] args) {
		System.out.println("============Main Execution Start===================\n\n");
		Store store = new Store();
		store.loadItems();
		store.demo();
		System.out.println("\n\n============Main Execution End===================");
	}

}
