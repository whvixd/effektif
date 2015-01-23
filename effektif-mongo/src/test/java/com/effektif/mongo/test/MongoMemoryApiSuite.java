/* Copyright (c) 2014, Effektif GmbH.
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
 * limitations under the License. */
package com.effektif.mongo.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.effektif.mongo.MongoMemoryWorkflowEngineConfiguration;
import com.effektif.workflow.api.TaskService;
import com.effektif.workflow.api.WorkflowEngine;
import com.effektif.workflow.impl.configuration.DefaultConfiguration;
import com.effektif.workflow.test.WorkflowTest;
import com.effektif.workflow.test.api.CallTest;
import com.effektif.workflow.test.api.EmbeddedSuprocessTest;
import com.effektif.workflow.test.api.ExclusiveGatewayTest;
import com.effektif.workflow.test.api.MultiInstanceTest;
import com.effektif.workflow.test.api.MultipleStartActivitiesTest;
import com.effektif.workflow.test.api.ParallelGatewayTest;
import com.effektif.workflow.test.api.ScriptTest;
import com.effektif.workflow.test.api.SequentialExecutionTest;
import com.effektif.workflow.test.api.TaskTest;


@SuiteClasses({    
  CallTest.class,
  EmbeddedSuprocessTest.class,
  ExclusiveGatewayTest.class,
  MultiInstanceTest.class,
  MultipleStartActivitiesTest.class,
  ParallelGatewayTest.class,
  ScriptTest.class,
  SequentialExecutionTest.class,
  TaskTest.class
})
@RunWith(Suite.class)
public class MongoMemoryApiSuite {
  
  private static final Logger log = LoggerFactory.getLogger(MongoMemoryApiSuite.class);
  
  static DefaultConfiguration originalConfiguration;
  static WorkflowEngine originalWorkflowEngine;
  static TaskService originalTaskService;

  @BeforeClass
  public static void switchToSerializingWorkflowEngine() {
    log.debug("Switching to mongo workflow engine");
    originalConfiguration = WorkflowTest.cachedConfiguration;
    
    DefaultConfiguration configuration = createMongoMemoryWorkflowEngineConfiguration();
    WorkflowTest.cachedConfiguration = configuration;
  }

  public static DefaultConfiguration createMongoMemoryWorkflowEngineConfiguration() {
    return new MongoMemoryWorkflowEngineConfiguration()
      .prettyPrint()
      .synchronous()
      .initialize();
  }

  @AfterClass
  public static void switchBackToOriginalWorkflowEngine() {
    log.debug("Switching back to original workflow engine");
    WorkflowTest.cachedConfiguration = originalConfiguration;
  }
}