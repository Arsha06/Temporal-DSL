package com.example;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class SandwichWorkflowStarter {

    public static void main(String[] args) {
        // Create a gRPC stubs wrapper that talks to the local docker instance of the Temporal server.
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();

        // WorkflowClient can be used to start, signal, query, cancel, and terminate workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Worker factory is used to create workers that poll specific Task Queues.
        WorkerFactory factory = WorkerFactory.newInstance(client);

        // Define the task queue name.
        String taskQueue = "SANDWICH_TASK_QUEUE";

        // Create a worker that listens on a task queue and hosts both workflow and activity implementations.
        Worker worker = factory.newWorker(taskQueue);

        // Register the workflow implementation with the worker.
        worker.registerWorkflowImplementationTypes(SandwichMakerDSL.class);

        // Register the activity implementation with the worker.
        worker.registerActivitiesImplementations(new SandwichActivitiesImpl());

        // Start all workers registered for a specific task queue.
        factory.start();

        // WorkflowOptions specify workflow execution parameters, including task queue.
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .build();

        // Create the workflow client stub.
        SandwichWorkflow workflow = client.newWorkflowStub(SandwichWorkflow.class, options);

        // Execute the workflow asynchronously.
        WorkflowClient.start(workflow::makeSandwich);

        System.out.println("Workflow started successfully!");

        // No need to close the client
    }
}
