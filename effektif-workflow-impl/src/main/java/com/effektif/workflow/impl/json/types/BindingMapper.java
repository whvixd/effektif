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
package com.effektif.workflow.impl.json.types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.effektif.workflow.api.types.DataType;
import com.effektif.workflow.api.workflow.Binding;
import com.effektif.workflow.impl.json.JsonReader;
import com.effektif.workflow.impl.json.JsonWriter;
import com.effektif.workflow.impl.util.Reflection;


/**
 * Maps a {@link String} to a JSON string field for serialisation and deserialisation.
 *
 * @author Tom Baeyens
 */
public class BindingMapper extends BeanMapper<Binding> {
  
  public BindingMapper(Class clazz, Type type) {
    super(clazz, (type instanceof ParameterizedType ? type : null));
  }

  @Override
  public void write(Binding typeValue, JsonWriter jsonWriter) {
    DataType dataType = typeValue.getDataType();
    Object value = typeValue.getValue();
    if (dataType==null && value!=null) {
      dataType = mappings.getTypeByValue(value);
      typeValue.setDataType(dataType);
    }
    super.write(typeValue, jsonWriter);
  }

  @Override
  public Binding read(Object jsonValue, JsonReader jsonReader) {
    Binding binding = super.read(jsonValue, jsonReader);
    if (! (type instanceof ParameterizedType)) {
      DataType dataType = binding.getDataType();
      Object jsonVariableValue = binding.getValue();
      if (jsonVariableValue!=null && dataType!=null) {
        Object objectVariableValue = jsonReader.readObject(jsonVariableValue, dataType.getValueType());
        binding.setValue(objectVariableValue);
      }
    }
    return binding;
  }

  @Override
  public String toString() {
    if (type!=null) {
      return "BindingMapper<"+Reflection.getTypeArg(type, 0)+">";
    }
    return "BindingMapper";
  }
}
