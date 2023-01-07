/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.ureport.ureportkeep.core.parser.impl.searchform;

import com.ureport.ureportkeep.core.definition.searchform.LabelPosition;
import com.ureport.ureportkeep.core.definition.searchform.Option;
import com.ureport.ureportkeep.core.definition.searchform.SelectInputComponent;
import com.ureport.ureportkeep.core.exception.ReportParseException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年10月30日
 */
@Component
public class SelectInputParser implements FormParser<SelectInputComponent> {
	@Override
	public SelectInputComponent parse(Element element) {
		SelectInputComponent select=new SelectInputComponent();
		select.setBindParameter(element.attributeValue("bind-parameter"));
		select.setLabel(element.attributeValue("label"));
		select.setType(element.attributeValue("type"));
		select.setLabelPosition(LabelPosition.valueOf(element.attributeValue("label-position")));
		String useDataset=element.attributeValue("use-dataset");
		if(StringUtils.isNotBlank(useDataset)){
			select.setUseDataset(Boolean.valueOf(useDataset));
			select.setDataset(element.attributeValue("dataset"));
			select.setLabelField(element.attributeValue("label-field"));
			select.setValueField(element.attributeValue("value-field"));
		}
		String useTree = element.attributeValue("use-tree");
		if (StringUtils.isNotBlank(useTree)) {
			Object treeObj = element.elements().get(0);
			if(treeObj==null || !(treeObj instanceof Element)){
				return select;
			}
			Element treeElement = (Element) treeObj;
			if (!treeElement.getName().equals("tree-value")) {
				return select;
			}

			select.setUseTree(Boolean.valueOf(useTree));
			select.setTreeJson(treeElement.getText());
		}
		List<Option> options=new ArrayList<Option>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("option")){
				continue;
			}
			Option option=new Option();
			options.add(option);
			option.setLabel(ele.attributeValue("label"));
			option.setValue(ele.attributeValue("value"));
		}
		select.setOptions(options);
		return select;
	}


	private boolean getJSONType(String str) {
		boolean result = false;
		if (StringUtils.isNotBlank(str)) {
			str = str.trim();
			if (str.startsWith("[") && str.endsWith("]")) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return "input-select";
	}
}
