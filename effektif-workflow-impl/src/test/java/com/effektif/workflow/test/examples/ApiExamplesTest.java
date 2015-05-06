/*
 * Copyright 2014 Effektif GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.effektif.workflow.test.examples;

import java.util.List;

import org.junit.Test;

import com.effektif.workflow.api.Configuration;
import com.effektif.workflow.api.WorkflowEngine;
import com.effektif.workflow.api.activities.JavaServiceTask;
import com.effektif.workflow.api.deprecated.task.Task;
import com.effektif.workflow.api.deprecated.task.TaskQuery;
import com.effektif.workflow.api.deprecated.task.TaskService;
import com.effektif.workflow.api.model.TriggerInstance;
import com.effektif.workflow.api.model.WorkflowId;
import com.effektif.workflow.api.workflow.Workflow;
import com.effektif.workflow.api.workflowinstance.WorkflowInstance;
import com.effektif.workflow.impl.deprecated.json.JsonMapper;
import com.effektif.workflow.impl.memory.MemoryConfiguration;


/**
 * @author Tom Baeyens
 */
public class ApiExamplesTest {

  @Test
  public void testApiExample() {
    // Create the default (in-memory) workflow engine
    Configuration configuration = new MemoryConfiguration();
    WorkflowEngine workflowEngine = configuration.getWorkflowEngine();
    TaskService taskService = configuration.getTaskService();
    
    // Create a workflow
    Workflow workflow = new Workflow()
      .sourceWorkflowId("Release")
      .activity("Move open issues", new JavaServiceTask()
        .transitionToNext())
      .activity("Check continuous integration", new JavaServiceTask());
    
    // Deploy the workflow to the engine
    WorkflowId workflowId = workflowEngine
      .deployWorkflow(workflow)
      .checkNoErrorsAndNoWarnings()
      .getWorkflowId();

    // Start a new workflow instance
    WorkflowInstance workflowInstance = workflowEngine
      .start(new TriggerInstance()
        .workflowId(workflowId));
    
    List<Task> tasks = taskService.findTasks(new TaskQuery().open());

    // TODO Uncomment and make the test pass by finding the "Move open issues" JavaServiceTask
//    Task task = tasks.get(0);
//    assertEquals(1, tasks.size());
//    taskService.completeTask(task.getId());
    
    System.err.println(configuration.get(JsonMapper.class).writeToStringPretty(workflow));
  }
}
