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
package com.effektif.workflow.test.implementation;

import static org.junit.Assert.*;

import org.junit.Test;

import com.effektif.workflow.api.model.TriggerInstance;
import com.effektif.workflow.api.model.UserId;
import com.effektif.workflow.api.types.TextType;
import com.effektif.workflow.api.types.Type;
import com.effektif.workflow.api.types.UserIdType;
import com.effektif.workflow.api.workflow.Condition;
import com.effektif.workflow.api.workflow.Workflow;
import com.effektif.workflow.impl.WorkflowEngineImpl;
import com.effektif.workflow.impl.WorkflowParser;
import com.effektif.workflow.impl.identity.IdentityService;
import com.effektif.workflow.impl.identity.User;
import com.effektif.workflow.impl.script.CompiledCondition;
import com.effektif.workflow.impl.script.ConditionService;
import com.effektif.workflow.impl.workflowinstance.WorkflowInstanceImpl;
import com.effektif.workflow.test.WorkflowTest;


/**
 * @author Tom Baeyens
 */
public class ConditionsTest extends WorkflowTest {

  @Test
  public void testConditionTextEquals() {
    assertTrue(evaluate(TextType.INSTANCE, "v", "hello", "v == 'hello'"));
    assertFalse(evaluate(TextType.INSTANCE, "v", "hello", "v == 'by'"));
    assertFalse(evaluate(TextType.INSTANCE, "v", null, "v == 'hello'"));
  }

  @Test
  public void testConditionTextContains() {
    assertTrue(evaluate(TextType.INSTANCE, "v", "hello", "contains(v, 'ell')"));
    assertFalse(evaluate(TextType.INSTANCE, "v", "hello", "contains(v, 'by')"));
    assertFalse(evaluate(TextType.INSTANCE, "v", null, "contains(v,'hello')"));
  }

  @Test
  public void testConditionUserEquals() {
    User johndoe = new User()
      .id("johndoe")
      .fullName("John Doe")
      .email("johndoe@localhost");
  
    configuration.get(IdentityService.class)
      .createUser(johndoe);
    
    UserId johndoeId = johndoe.getId();

    assertTrue(evaluate(UserIdType.INSTANCE, "v", johndoeId, "v.id == 'johndoe'"));
    assertFalse(evaluate(UserIdType.INSTANCE, "v", johndoeId, "v.id == 'superman'"));
    assertFalse(evaluate(UserIdType.INSTANCE, "v", null, "v.id == 'johndoe'"));
  }

  public boolean evaluate(Type type, String variableId, Object value, String expression) {
    Workflow workflow = new Workflow()
      .variable(variableId, type);
    
    deploy(workflow);
    
    TriggerInstance triggerInstance = new TriggerInstance()
      .data(variableId, value)
      .workflowId(workflow.getId());
    
    WorkflowEngineImpl workflowEngineImpl = (WorkflowEngineImpl) workflowEngine;
    WorkflowInstanceImpl workflowInstance = workflowEngineImpl.startInitialize(triggerInstance);
  
    ConditionService conditionService = configuration.get(ConditionService.class);
    Condition condition = new Condition()
      .expression(expression);
    CompiledCondition compiledCondition = conditionService.compile(condition, new WorkflowParser(configuration));
    return compiledCondition.evaluate(workflowInstance);
  }
}
