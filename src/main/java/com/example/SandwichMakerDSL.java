package com.example;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import org.yaml.snakeyaml.Yaml;

public class SandwichMakerDSL implements SandwichWorkflow {

    private final SandwichActivities activities;

    public SandwichMakerDSL() {
        this.activities = Workflow.newActivityStub(SandwichActivities.class,
                ActivityOptions.newBuilder()
                        .setScheduleToCloseTimeout(Duration.ofSeconds(2))
                        .build());
    }

    @Override
    public void makeSandwich() {
        WorkflowDefinition workflow = loadWorkflowDefinition();
        if (workflow == null || workflow.getSteps() == null) {
            throw new IllegalStateException("Failed to load workflow definition or steps are missing");
        }

        for (WorkflowStep step : workflow.getSteps()) {
            executeStep(step);
        }
    }

    private WorkflowDefinition loadWorkflowDefinition() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("dsl/sandwich-workflow.yaml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Cannot find sandwich-workflow.yaml");
            }
            return yaml.loadAs(inputStream, WorkflowDefinition.class);
        } catch (IOException e) {
            throw Workflow.wrap(new RuntimeException("Failed to load workflow definition", e));
        }
    }

    private void executeStep(WorkflowStep step) {
        if (step == null || step.getActivity() == null) {
            throw new IllegalArgumentException("Invalid workflow step");
        }

        switch (step.getActivity()) {
            case "gatherIngredients":
                activities.gatherIngredients();
                break;
            case "sliceBread":
                activities.sliceBread();
                break;
            case "addCondiments":
                activities.addCondiments();
                break;
            case "addIngredients":
                activities.addIngredients();
                break;
            case "assembleSandwich":
                activities.assembleSandwich();
                break;
            case "cleanUp":
                activities.cleanUp();
                break;
            default:
                throw new IllegalArgumentException("Unknown activity: " + step.getActivity());
        }
    }

    public static class WorkflowDefinition {
        private String name;
        private String version;
        private String taskQueue;
        private WorkflowStep[] steps;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTaskQueue() {
            return taskQueue;
        }

        public void setTaskQueue(String taskQueue) {
            this.taskQueue = taskQueue;
        }

        public WorkflowStep[] getSteps() {
            return steps;
        }

        public void setSteps(WorkflowStep[] steps) {
            this.steps = steps;
        }
    }

    public static class WorkflowStep {
        private String name;
        private String activity;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }
    }
}
