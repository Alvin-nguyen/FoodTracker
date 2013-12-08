package com.alvinnguyen.foodtracker.object;

public class FoodItem {
	
	private String name;
	private int calories;
	private Double protein;
	private Double fat;
	private Double carbs;
	private Double fiber;
	private Double suger;
	private int calcium;
	private Double iron;
	private int sodium;
	private Double vit_c;
	private int vit_a;
	private int chol;
	private String add_date;
	private String meal;
	
	public FoodItem(String name, int calories, Double protein, Double fat, Double carbs, Double fiber, Double suger, 
			int cal, Double iron, int sodium, Double vit_c, int vit_a, int chol) {
		this.name = name;
		this.calories = calories;
		this.protein = protein;
		this.fat = fat;
		this.carbs = carbs;
		this.fiber = fiber;
		this.suger = suger;
		this.calcium = cal;
		this.iron = iron;
		this.sodium = sodium;
		this.vit_c = vit_c;
		this.vit_a = vit_a;
		this.chol = chol;
	}
	
	public FoodItem(String name, int calories, Double protein, Double fat, Double carbs, Double fiber, Double suger, 
			int cal, Double iron, int sodium, Double vit_c, int vit_a, int chol, String add_date) {
		this.name = name;
		this.calories = calories;
		this.protein = protein;
		this.fat = fat;
		this.carbs = carbs;
		this.fiber = fiber;
		this.suger = suger;
		this.calcium = cal;
		this.iron = iron;
		this.sodium = sodium;
		this.vit_c = vit_c;
		this.vit_a = vit_a;
		this.chol = chol;
		this.add_date = add_date;
		
	}
	
	public FoodItem(String name, int calories, Double protein, Double fat, Double carbs, Double fiber, Double suger, 
			int cal, Double iron, int sodium, Double vit_c, int vit_a, int chol, String add_date, String meal) {
		this.name = name;
		this.calories = calories;
		this.protein = protein;
		this.fat = fat;
		this.carbs = carbs;
		this.fiber = fiber;
		this.suger = suger;
		this.calcium = cal;
		this.iron = iron;
		this.sodium = sodium;
		this.vit_c = vit_c;
		this.vit_a = vit_a;
		this.chol = chol;
		this.add_date = add_date;
		this.meal = meal;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Double getProtein() {
		return this.protein;
	}
	
	public Double getCarbs() {
		return this.carbs;
	}	
	
	public Double getFiber() {
		return this.fiber;
	}
	
	public Double getSuger() {
		return this.suger;
	}
	
	public int getCalcium() {
		return this.calcium;
	}
	
	public Double getIron() {
		return this.iron;
	}
	
	public int getSodium() {
		return this.sodium;
	}
	
	public int getVitA() {
		return this.vit_a;
	}
	
	public Double getVitC() {
		return this.vit_c;
	}
	
	public int getChol() {
		return this.chol;
	}
	
	public String getDate() {
		return this.add_date;
	}
	
	public Double getFat() {
		return this.fat;
	}
	
	public int getCalories() {
		return this.calories;
	}
	
	public String toString() {
		return this.name;
	}
	
	public String getMeal() {
		return this.meal;
	}
}
