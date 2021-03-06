/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.VirtualEntity;
import com.haulmont.cuba.core.sys.jpql.tree.QueryNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryVariableContext {
    private Map<String, Entity> entityVariableName2entity = new HashMap<>();
    private QueryNode node;
    private List<QueryVariableContext> children = new ArrayList<>();
    private Entity entity;
    private boolean propagateVariablesUpstairs = true;
    private QueryVariableContext parent = null;

    public QueryVariableContext(DomainModel model, QueryNode node) {
        if (model == null)
            throw new NullPointerException("No model passed");
        if (node == null)
            throw new NullPointerException("No node passed");

        this.node = node;
        this.entity = new VirtualEntity();
    }

    public boolean isPropagateVariablesUpstairs() {
        return propagateVariablesUpstairs;
    }

    public void setPropagateVariablesUp(boolean propateVariablesUpdatairs) {
        this.propagateVariablesUpstairs = propateVariablesUpdatairs;
    }

    public Entity getEntityByVariableName(String entityVariableName) {
        Entity result = entityVariableName2entity.get(entityVariableName);
        if (result != null) {
            return result;
        }

        return parent == null ? null : parent.getEntityByVariableName(entityVariableName);
    }

    /**
     * Internal method to register entity variables found in query
     *
     * @param variableName - found entity variable name
     * @param entity
     */
    public void addEntityVariable(String variableName, Entity entity) {
        if (variableName == null) {
            throw new NullPointerException("No entity variable name passed");
        }
        if (entity == null) {
            throw new NullPointerException("No entity passed");
        }
        if (entityVariableName2entity.containsKey(variableName))
            throw new IllegalArgumentException("Trying to rebing variable [" + variableName + "]");
        
        entityVariableName2entity.put(variableName, entity);
    }

    public QueryVariableContext getContextByCaretPosition(int caretPosition) {
        if (!node.contains(caretPosition)) {
            return null;
        }

        for (QueryVariableContext child : children) {
            QueryVariableContext childResult = child.getContextByCaretPosition(caretPosition);
            if (childResult != null)
                return childResult;
        }
        return this;

    }

    public void addChild(QueryVariableContext child) {
        if (child == null) {
            throw new NullPointerException("No child passed");
        }
        if (child.getParent() != null) {
            throw new IllegalArgumentException("Child has parent already");
        }
        child.setParent(this);
        children.add(child);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    private QueryVariableContext getParent() {
        return parent;
    }

    private void setParent(QueryVariableContext parent) {
        this.parent = parent;
    }

    public String getVariableNameByEntity(String entityName) {
        if (entityName == null)
            throw new NullPointerException("No entity name passed");

        for (Map.Entry<String, Entity> entry : entityVariableName2entity.entrySet()) {
            if (entityName.equals(entry.getValue().getName())) {
                return entry.getKey();
            }
        }

        return parent == null ? null : parent.getVariableNameByEntity(entityName);
    }
}