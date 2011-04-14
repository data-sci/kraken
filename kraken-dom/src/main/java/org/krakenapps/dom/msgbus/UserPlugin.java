/*
 * Copyright 2011 Future Systems, Inc.
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
 * limitations under the License.
 */
package org.krakenapps.dom.msgbus;

import java.util.Collection;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Requires;
import org.krakenapps.dom.api.OrganizationApi;
import org.krakenapps.dom.api.OrganizationUnitApi;
import org.krakenapps.dom.api.UserApi;
import org.krakenapps.dom.exception.OrganizationNotFoundException;
import org.krakenapps.dom.model.Organization;
import org.krakenapps.dom.model.OrganizationUnit;
import org.krakenapps.dom.model.User;
import org.krakenapps.msgbus.Marshaler;
import org.krakenapps.msgbus.Request;
import org.krakenapps.msgbus.Response;
import org.krakenapps.msgbus.handler.MsgbusMethod;
import org.krakenapps.msgbus.handler.MsgbusPermission;
import org.krakenapps.msgbus.handler.MsgbusPlugin;

@Component(name = "dom-user-plugin")
@MsgbusPlugin
public class UserPlugin {
	@Requires
	private OrganizationApi orgApi;

	@Requires
	private OrganizationUnitApi orgUnitApi;

	@Requires
	private UserApi userApi;

	@MsgbusMethod
	public void getUsers(Request req, Response resp) {
		Organization organization = orgApi.getOrganization(req.getOrgId());
		if (organization == null)
			throw new OrganizationNotFoundException(req.getOrgId());

		Collection<User> users = null;
		if (req.has("ou_id")) {
			int orgUnitId = req.getInteger("ou_id");
			OrganizationUnit ou = orgUnitApi.getOrganizationUnit(orgUnitId);
			boolean incChildren = req.has("inc_children") ? req.getBoolean("inc_children") : false;
			users = userApi.getUsers(ou, incChildren);
		} else
			users = userApi.getUsers(organization);

		resp.put("result", Marshaler.marshal(users));
	}

	@MsgbusMethod
	@MsgbusPermission(group = "dom.org", code = "manage")
	public void createUser(Request req, Response resp) {
		User user = toUser(req);
		userApi.createUser(user);
		resp.put("new_id", user.getId());
	}

	@MsgbusMethod
	@MsgbusPermission(group = "dom.org", code = "manage")
	public void updateUser(Request req, Response resp) {
		userApi.updateUser(toUser(req));
	}

	private User toUser(Request req) {
		User user = new User();
		if (req.has("id"))
			user.setId(req.getInteger("id"));
		
		user.setOrganization(orgApi.getOrganization(req.getOrgId()));
		if (req.get("org_unit_id") != null)
			user.setOrganizationUnit(orgUnitApi.getOrganizationUnit(req.getInteger("org_unit_id")));
			
		user.setName(req.getString("name"));
		user.setLoginName(req.getString("login_name"));
		user.setDescription(req.getString("description"));
		user.setTitle(req.getString("title"));
		user.setEmail(req.getString("email"));
		user.setPhone(req.getString("phone"));
		return user;
	}

	@MsgbusMethod
	@MsgbusPermission(group = "dom.org", code = "manage")
	public void removeUser(Request req, Response resp) {
		userApi.removeUser(req.getInteger("id"));
	}
}