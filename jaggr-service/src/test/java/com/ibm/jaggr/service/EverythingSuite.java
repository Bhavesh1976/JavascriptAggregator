/*
 * (C) Copyright 2012, IBM Corporation
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
 */

package com.ibm.jaggr.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ibm.jaggr.service.impl.config.ConfigTests;
import com.ibm.jaggr.service.impl.deps.DepTreeNodeTests;
import com.ibm.jaggr.service.impl.deps.DepUtilsTest;
import com.ibm.jaggr.service.impl.deps.DependenciesTest;
import com.ibm.jaggr.service.impl.deps.HasNodeTests;
import com.ibm.jaggr.service.impl.layer.LayerTest;
import com.ibm.jaggr.service.impl.modulebuilder.css.CSSModuleBuilderTest;
import com.ibm.jaggr.service.impl.modulebuilder.i18n.I18nModuleBuilderTest;
import com.ibm.jaggr.service.impl.modulebuilder.javascript.ExportModuleNameCompilerPassTest;
import com.ibm.jaggr.service.impl.modulebuilder.javascript.HasFilteringCompilerPassTest;
import com.ibm.jaggr.service.impl.modulebuilder.javascript.JsModuleContentProviderTest;
import com.ibm.jaggr.service.impl.modulebuilder.javascript.RequireExpansionCompilerPassTest;
import com.ibm.jaggr.service.impl.modulebuilder.text.TxtModuleContentProviderTest;
import com.ibm.jaggr.service.impl.transport.DojoHttpTransportTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = { 
	ConfigTests.class,
	DependenciesTest.class,
	DepTreeNodeTests.class,
	DepUtilsTest.class,
	HasNodeTests.class,
	LayerTest.class,
	CSSModuleBuilderTest.class,
	I18nModuleBuilderTest.class,
	ExportModuleNameCompilerPassTest.class,
	HasFilteringCompilerPassTest.class,
	JsModuleContentProviderTest.class,
	TxtModuleContentProviderTest.class,
	RequireExpansionCompilerPassTest.class,
	DojoHttpTransportTest.class
})
public class EverythingSuite {

}
