package com.example;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SandwichActivities {

    @ActivityMethod
    void gatherIngredients();

    @ActivityMethod
    void sliceBread();

    @ActivityMethod
    void addCondiments();

    @ActivityMethod
    void addIngredients();

    @ActivityMethod
    void assembleSandwich();

    @ActivityMethod
    void cleanUp();
}
