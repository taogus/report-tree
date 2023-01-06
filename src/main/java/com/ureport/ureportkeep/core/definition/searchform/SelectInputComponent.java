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
package com.ureport.ureportkeep.core.definition.searchform;


import com.ureport.ureportkeep.core.Utils;
import com.ureport.ureportkeep.core.build.Dataset;
import com.ureport.ureportkeep.core.exception.DatasetUndefinitionException;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年10月23日
 */
public class SelectInputComponent extends InputComponent {
    private boolean useDataset;
    private boolean useTree;
    private String dataset;
    private String labelField;
    private String valueField;
    private String treeJson;
    private List<Option> options;

    @Override
    String inputHtml(RenderContext context) {
        String name = getBindParameter();
        Object pvalue = context.getParameter(name) == null ? "" : context.getParameter(name);
        StringBuilder sb = new StringBuilder();
        if (useTree) {
            sb.append("<div id='" + context.buildComponentId(this) + "_tree'></div>");
        } else {
            sb.append("<select style=\"padding:3px;height:28px\" id='" + context.buildComponentId(this) + "' name='" + name + "' class='form-control'>");
        }
        if (useDataset && StringUtils.isNotBlank(dataset)) {
            Dataset ds = context.getDataset(dataset);
            if (ds == null) {
                throw new DatasetUndefinitionException(dataset);
            }
            for (Object obj : ds.getData()) {
                Object label = Utils.getProperty(obj, labelField);
                Object value = Utils.getProperty(obj, valueField);
                String selected = value.equals(pvalue) ? "selected" : "";
                sb.append("<option value='" + value + "' " + selected + ">" + label + "</option>");
            }
            if (pvalue.equals("")) {
                sb.append("<option value='' selected></option>");
            }
        } else if(!useTree) {
            for (Option option : options) {
                String value = option.getValue();
                String selected = value.equals(pvalue) ? "selected" : "";
                sb.append("<option value='" + value + "' " + selected + ">" + option.getLabel() + "</option>");
            }
            if (pvalue.equals("")) {
                sb.append("<option value='' selected></option>");
            }

        }
        if (!useTree) {
            sb.append("</select>");
        }
        return sb.toString();
    }

    @Override
    public String initJs(RenderContext context) {
        String name = getBindParameter();
        StringBuilder sb = new StringBuilder();
        if (useTree) {

        }

        sb.append("formElements.push(");
        sb.append("function(){");
        sb.append("if(''==='" + name + "'){");
        sb.append("alert('列表框未绑定查询参数名，不能进行查询操作!');");
        sb.append("throw '列表框未绑定查询参数名，不能进行查询操作!'");
        sb.append("}");
        sb.append("return {");
        sb.append("\"" + name + "\":");
        sb.append("$('#" + context.buildComponentId(this) + "').val()");
        sb.append("}");
        sb.append("}");
        sb.append(");");
        return sb.toString();
    }

    public boolean isUseTree() {
        return useTree;
    }

    public void setUseTree(boolean useTree) {
        this.useTree = useTree;
    }

    public String getTreeJson() {
        return treeJson;
    }

    public void setTreeJson(String treeJson) {
        this.treeJson = treeJson;
    }

    public boolean isUseDataset() {
        return useDataset;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public void setUseDataset(boolean useDataset) {
        this.useDataset = useDataset;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<Option> getOptions() {
        return options;
    }
}
