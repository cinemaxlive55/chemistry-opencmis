/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.chemistry.opencmis.commons.enums;

/**
 * Base Object Type Ids Enum.
 * 
 * @author <a href="mailto:fmueller@opentext.com">Florian M&uuml;ller</a>
 * 
 */
public enum BaseTypeId {

    CMIS_DOCUMENT("cmis:document"), //
    CMIS_FOLDER("cmis:folder"), //
    CMIS_RELATIONSHIP("cmis:relationship"), //
    CMIS_POLICY("cmis:policy");

    private final String value;

    BaseTypeId(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BaseTypeId fromValue(String v) {
        for (BaseTypeId c : BaseTypeId.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
