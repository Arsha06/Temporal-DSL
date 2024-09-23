package com.example;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import lombok.Getter;
import lombok.Setter;
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
        WorkflowData workflowData = loadWorkflowDefinition().getWorkflow();
        if (workflowData == null || workflowData.getSteps() == null) {
            throw new IllegalStateException("Failed to load workflow definition or steps are missing");
        }

        for (WorkflowStep step : workflowData.getSteps()) {
            executeStep(step);
        }
    }

    private WorkflowDefinition loadWorkflowDefinition() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("sandwich-workflow.yaml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Cannot find sandwich-workflow.yaml in the resources");
            }
            return yaml.loadAs(inputStream, WorkflowDefinition.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load workflow definition", e);
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
            case "sliceBreadNew":
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

    @Setter
    @Getter
    public static class WorkflowDefinition {
        private WorkflowData workflow;
    }

    @Setter
    @Getter
    public static class WorkflowData {
        private String name;
        private String version;
        private String taskQueue;
        private WorkflowStep[] steps;
    }

    @Setter
    @Getter
    public static class WorkflowStep {
        private String name;
        private String activity;
    }
}
