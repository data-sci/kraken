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
package org.krakenapps.dom.api;

import java.util.Map;

public interface PushApi {
	void subscribe(int organizationId, int sessionId, int processId, String callback);
	
	void subscribe(int organizationId, int sessionId, int processId, String callback, Map<String, Object> options);

	void unsubscribe(int organizationId, int sessionId, int processId, String callback);
	
	void addInterceptor(String callback, PushInterceptor interceptor);
	
	void removeInterceptor(String callback);

	void push(int organizationId, String callback, Map<String, Object> m);

	void sessionClosed(int organizationId, int sessionId);
}