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
package com.effektif.workflow.impl.script;

import com.effektif.workflow.api.workflow.Expression;
import com.effektif.workflow.impl.WorkflowParser;
import com.effektif.workflow.impl.configuration.Brewable;
import com.effektif.workflow.impl.configuration.Brewery;
import com.effektif.workflow.impl.data.DataTypeService;
import com.effektif.workflow.impl.workflowinstance.ScopeInstanceImpl;


/**
 * @author Tom Baeyens
 */
public class ExpressionServiceImpl implements ExpressionService, Brewable {
  
  ScriptService scriptService;
  DataTypeService dataTypeService;
  
  @Override
  public void brew(Brewery brewery) {
    this.scriptService = brewery.get(ScriptService.class);
    this.dataTypeService = brewery.get(DataTypeService.class);
  }

  public ScriptImpl compile(Expression expression, WorkflowParser parser) {
    if (expression==null) {
      return null;
    }
    ScriptImpl scriptImpl = scriptService.compile(expression, parser);
    scriptImpl.readOnly = true;
    scriptImpl.expectedResultType = dataTypeService.createDataType(expression.getType());
    return scriptImpl;
  }

  public Object execute(Object compiledscript, ScopeInstanceImpl scopeInstance) {
    return scriptService.evaluate(scopeInstance, (ScriptImpl)compiledscript);
  }
}