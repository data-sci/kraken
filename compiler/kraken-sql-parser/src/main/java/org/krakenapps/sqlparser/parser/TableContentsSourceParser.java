/*
 * Copyright 2010 NCHOVY
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
package org.krakenapps.sqlparser.parser;

import java.util.ArrayList;
import java.util.List;

import org.krakenapps.bnf.Binding;
import org.krakenapps.bnf.Parser;
import org.krakenapps.sqlparser.ast.TableElement;

/**
 * table_contents_source ::= table_element_list | OF user_defined_type [
 * subtable_clause ] [ table_element_list ]
 * 
 * @author xeraph
 * 
 */
public class TableContentsSourceParser implements Parser {

	/**
	 * Support table_element_list only
	 */
	@Override
	public Object parse(Binding b) {
		// contents_source
		// +table_element_list seq
		// ++( table_element_list_inner )
		Binding innerRoot = b.getChildren()[1];

		List<TableElement> elements = new ArrayList<TableElement>();
		TableElementListParser.parse(innerRoot, elements);
		return elements;
	}

}
