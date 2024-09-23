package com.example;

public class SandwichActivitiesImpl implements SandwichActivities {

    @Override
    public void gatherIngredients() {
        System.out.println("Gathering ingredients 02 07...");
    }

    @Override
    public void sliceBread() {
        System.out.println("Slicing bread...");
    }

    @Override
    public void addCondiments() {
        System.out.println("Adding condiments...");
    }

    @Override
    public void addIngredients() {
        System.out.println("Adding ingredients...");
    }

    @Override
    public void assembleSandwich() {
        System.out.println("Assembling sandwich...");
    }

    @Override
    public void cleanUp() {
        System.out.println("Cleaning up...");
    }
}
