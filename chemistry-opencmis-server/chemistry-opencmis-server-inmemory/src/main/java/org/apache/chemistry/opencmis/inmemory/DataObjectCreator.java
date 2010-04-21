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
package org.apache.chemistry.opencmis.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.chemistry.opencmis.commons.api.Ace;
import org.apache.chemistry.opencmis.commons.api.Acl;
import org.apache.chemistry.opencmis.commons.api.AllowableActions;
import org.apache.chemistry.opencmis.commons.api.ChangeEventInfo;
import org.apache.chemistry.opencmis.commons.api.ObjectData;
import org.apache.chemistry.opencmis.commons.api.PolicyIdList;
import org.apache.chemistry.opencmis.commons.api.RenditionData;
import org.apache.chemistry.opencmis.commons.api.server.CallContext;
import org.apache.chemistry.opencmis.commons.enums.Action;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AccessControlListImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AllowableActionsImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ChangeEventInfoDataImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PolicyIdListImpl;
import org.apache.chemistry.opencmis.inmemory.server.RuntimeContext;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.Content;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.Folder;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.ObjectStore;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.StoredObject;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.Version;
import org.apache.chemistry.opencmis.inmemory.storedobj.api.VersionedDocument;

/**
 * @author Jens A collection of utility functions to fill the data objects used
 *         as return values for the service object calls
 */
public class DataObjectCreator {

	public static AllowableActions fillAllowableActions(ObjectStore objStore, StoredObject so, String user) {

		boolean isFolder = so instanceof Folder;
		boolean isDocument = so instanceof Content;
		boolean isCheckedOut = false;
		boolean canCheckOut = false;
		boolean canCheckIn = false;
		boolean isVersioned = so instanceof Version || so instanceof VersionedDocument;
		boolean hasContent = so instanceof Content && ((Content) so).hasContent();

		if (so instanceof Version) {
			isCheckedOut = ((Version) so).isPwc();
			canCheckIn = isCheckedOut && ((Version) so).getParentDocument().getCheckedOutBy().equals(user);
		} else if (so instanceof VersionedDocument) {
			isCheckedOut = ((VersionedDocument) so).isCheckedOut();
			canCheckOut = !((VersionedDocument) so).isCheckedOut();
			canCheckIn = isCheckedOut && ((VersionedDocument) so).getCheckedOutBy().equals(user);
		}

		AllowableActionsImpl allowableActions = new AllowableActionsImpl();
		Set<Action> set = allowableActions.getAllowableActions();

		set.add(Action.CAN_DELETE_OBJECT);
		set.add(Action.CAN_UPDATE_PROPERTIES);

		if (isFolder || isDocument) {
			set.add(Action.CAN_GET_PROPERTIES);
			if (!so.equals(objStore.getRootFolder())) {
				set.add(Action.CAN_GET_OBJECT_PARENTS);
			}
			set.add(Action.CAN_MOVE_OBJECT);
		}

		if (isFolder) {
			if (!so.equals(objStore.getRootFolder())) {
				set.add(Action.CAN_GET_FOLDER_PARENT);
			}
			set.add(Action.CAN_GET_FOLDER_TREE);
			set.add(Action.CAN_GET_DESCENDANTS);

			set.add(Action.CAN_ADD_OBJECT_TO_FOLDER);
			set.add(Action.CAN_REMOVE_OBJECT_FROM_FOLDER);
			set.add(Action.CAN_CREATE_DOCUMENT);
			set.add(Action.CAN_CREATE_FOLDER);
			set.add(Action.CAN_GET_CHILDREN);
			set.add(Action.CAN_DELETE_TREE);
		}

		if (hasContent) {
			set.add(Action.CAN_DELETE_CONTENT_STREAM);
			set.add(Action.CAN_GET_CONTENT_STREAM);
		}

		if (isVersioned) {
			if (canCheckOut) {
				set.add(Action.CAN_CHECK_OUT);
			}
			if (isCheckedOut) {
				set.add(Action.CAN_CANCEL_CHECK_OUT);
			}
			if (canCheckIn) {
				set.add(Action.CAN_CHECK_IN);
			}
			set.add(Action.CAN_GET_ALL_VERSIONS);
		}

		if (isDocument) {
			if (!isVersioned || canCheckIn) {
				set.add(Action.CAN_SET_CONTENT_STREAM);
			}
		}

		allowableActions.setAllowableActions(set);
		return allowableActions;
	}

	public static Acl fillACL(StoredObject so) {
		AccessControlListImpl acl = new AccessControlListImpl();
		List<Ace> aces = new ArrayList<Ace>();
		// TODO to be completed if ACLs are implemented
		acl.setAces(aces);
		return acl;
	}

	public static PolicyIdList fillPolicyIds(StoredObject so) {
		// TODO: to be completed if policies are implemented
		PolicyIdListImpl polIds = new PolicyIdListImpl();
		// polIds.setPolicyIds(...);
		return polIds;
	}

	public static List<ObjectData> fillRelationships(IncludeRelationships includeRelationships, StoredObject so) {
		// TODO: to be completed if relationships are implemented
		List<ObjectData> relationships = new ArrayList<ObjectData>();
		return relationships;
	}

	public static List<RenditionData> fillRenditions(StoredObject so) {
		// TODO: to be completed if renditions are implemented
		List<RenditionData> renditions = new ArrayList<RenditionData>();
		return renditions;
	}

	public static ChangeEventInfo fillChangeEventInfo(StoredObject so) {
		// TODO: to be completed if change information is implemented
		ChangeEventInfo changeEventInfo = new ChangeEventInfoDataImpl();
		return changeEventInfo;
	}
}
