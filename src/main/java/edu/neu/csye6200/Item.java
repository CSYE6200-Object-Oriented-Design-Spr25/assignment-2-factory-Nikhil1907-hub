package edu.neu.csye6200;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

abstract class Item {
	protected int id;
	protected String name;
	protected double price;

	public Item(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	@Override
	public abstract String toString();

	protected static String[] parseCSV(String csv) {
		return csv.split(",");
	}
	public static class FoodItem extends Item {
		public FoodItem(String csv) {
			super(Integer.parseInt(parseCSV(csv)[0]), parseCSV(csv)[1], Double.parseDouble(parseCSV(csv)[2]));
		}

		@Override
		public String toString() {
			return "FoodItem: ID=" + id + ", Name=" + name + ", Price=" + price;
		}
	}

	public static class ElectronicItem extends Item {
		public ElectronicItem(String csv) {
			super(Integer.parseInt(parseCSV(csv)[0]), parseCSV(csv)[1], Double.parseDouble(parseCSV(csv)[2]));
		}

		@Override
		public String toString() {
			return "ElectronicItem: ID=" + id + ", Name=" + name + ", Price=" + price;
		}
	}

	public static class ServiceItem extends Item {
		public ServiceItem(String csv) {
			super(Integer.parseInt(parseCSV(csv)[0]), parseCSV(csv)[1], Double.parseDouble(parseCSV(csv)[2]));
		}

		@Override
		public String toString() {
			return "ServiceItem: ID=" + id + ", Name=" + name + ", Price=" + price;
		}
	}
}

interface ItemFactory {
	Item createItem(String csv);
}

class FoodItemFactory implements ItemFactory {
	public Item createItem(String csv) {
		return new Item.FoodItem(csv);
	}
}

class ElectronicItemFactory implements ItemFactory {
	private static ElectronicItemFactory instance;
	private ElectronicItemFactory() {
	}
	public static synchronized ElectronicItemFactory getInstance() {
		if (instance == null) {
			instance = new ElectronicItemFactory();
		}
		return instance;
	}

	public Item createItem(String csv) {
		return new Item.ElectronicItem(csv);
	}
}

class ServiceItemFactory implements ItemFactory {
	private static final ServiceItemFactory instance = new ServiceItemFactory();
	private ServiceItemFactory() {
	}
	public static ServiceItemFactory getInstance() {
		return instance;
	}

	public Item createItem(String csv) {
		return new Item.ServiceItem(csv);
	}
}

class FileUtil {
	public static List<String> readLines(String fileName) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + fileName);
		}
		return lines;
	}
}

abstract class AbstractStore {
	protected List<Item> items = new ArrayList<>();
	public abstract void loadItems();

	public void displayItems() {
		for (Item item : items) {
			System.out.println(item);
		}
	}

	public void sortById() {
		items.sort(Comparator.comparingInt(item -> item.id));
	}

	public void sortByName() {
		items.sort(Comparator.comparing(item -> item.name));
	}

	public void sortByPrice() {
		items.sort(Comparator.comparingDouble(item -> item.price));
	}
}

class Store extends AbstractStore {
	public void loadItems() {
		FoodItemFactory foodFactory = new FoodItemFactory();
		ElectronicItemFactory electronicFactory = ElectronicItemFactory.getInstance();
		ServiceItemFactory serviceFactory = ServiceItemFactory.getInstance();

		for (String csv : FileUtil.readLines("./src/FoodItemCSV.txt")) {
			items.add(foodFactory.createItem(csv));
		}
		for (String csv : FileUtil.readLines("./src/ElectronicItemCSV.txt")) {
			items.add(electronicFactory.createItem(csv));
		}
		for (String csv : FileUtil.readLines("./src/ServiceItemCSV.txt")) {
			items.add(serviceFactory.createItem(csv));
		}
	}

	public void demo() {
		System.out.println("Items before sorting:");
		displayItems();

		System.out.println("\nSorted by ID:");
		sortById();
		displayItems();

		System.out.println("\nSorted by Name:");
		sortByName();
		displayItems();

		System.out.println("\nSorted by Price:");
		sortByPrice();
		displayItems();
	}
}
