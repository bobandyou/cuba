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

package com.haulmont.cuba.gui.app.security.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

@com.haulmont.chile.core.annotations.MetaClass(name = "sec$AttributeTarget")
@SystemLevel
public class AttributeTarget extends AbstractInstance
        implements Entity<String> {

    @MetaProperty(mandatory = true)
    private String id;

    @MetaProperty(mandatory = true)
    private AttributePermissionVariant permissionVariant = AttributePermissionVariant.NOTSET;

    public AttributeTarget(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public MetaClass getMetaClass() {
        return AppBeans.get(Metadata.class).getSession().getClass(getClass());
    }

    public AttributePermissionVariant getPermissionVariant() {
        return permissionVariant;
    }

    public void setPermissionVariant(AttributePermissionVariant permissionVariant) {
        this.permissionVariant = permissionVariant;
    }
}