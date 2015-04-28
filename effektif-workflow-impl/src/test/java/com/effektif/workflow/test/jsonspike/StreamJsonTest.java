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
package com.effektif.workflow.test.jsonspike;

import org.junit.BeforeClass;

import com.effektif.workflow.test.jsonspike.json.JsonStreamMapper;


/**
 * @author Tom Baeyens
 */
public class StreamJsonTest extends AbstractMapperTest {

  static JsonStreamMapper jsonStreamMapper = null;
  
  @BeforeClass
  public static void initialize() {
    jsonStreamMapper = new JsonStreamMapper();
    jsonStreamMapper.pretty();
  }

  @Override
  public <T> T serialize(T o) {
    String jsonString = jsonStreamMapper.write(o);
    System.out.println(jsonString);
    return jsonStreamMapper.readString(jsonString, o.getClass());
  }
}
