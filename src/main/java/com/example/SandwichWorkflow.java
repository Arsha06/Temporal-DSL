package com.example;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SandwichWorkflow {
    @WorkflowMethod
    void makeSandwich();
}
