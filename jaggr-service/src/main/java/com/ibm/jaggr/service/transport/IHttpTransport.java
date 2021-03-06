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

package com.ibm.jaggr.service.transport;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.ibm.jaggr.service.IExtensionInitializer;
import com.ibm.jaggr.service.cachekeygenerator.ICacheKeyGenerator;
import com.ibm.jaggr.service.resource.IResource;
import com.ibm.jaggr.service.resource.IResourceFactory;

/**
 * Instances of IHttpTransport are responsible for parsing the HTTP request to
 * extract request information such as module list, feature lists and other
 * request parameters, and then 'decorating' the request with the processed
 * values as defined types in request attributes.
 * <p>
 * This interface also provides the AMD loader extension JavaScript used to
 * format and send the requests to the aggregator.
 */
public interface IHttpTransport extends IExtensionInitializer {

	/**
	 * Name of the request attribute specifying the ordered
	 * <code>Collection&lt;String&gt;</code> specifying the list of modules
	 * requested.
	 * <p>
	 * The collection's toString() method must return a string representation of
	 * the list that can be used to uniquely identify the order and items in the
	 * list for use as a cache key. Implementors may choose to provide a
	 * condensed presentation for the sake of efficiency, however,
	 * non-displayable characters should be avoided.
	 */
	public static final String REQUESTEDMODULES_REQATTRNAME = IHttpTransport.class
			.getName() + ".REQUESTEDMODULELIST"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying the <code>Feature</code> object
	 * for the feature set specified in the request
	 */
	public static final String FEATUREMAP_REQATTRNAME = IHttpTransport.class
			.getName() + ".FEATUREMAP"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying the {@link OptimizationLevel}
	 * specifying the requested optimization level for module builds. Not all
	 * module builders may support all (or any) levels.
	 */
	public static final String OPTIMIZATIONLEVEL_REQATTRNAME = IHttpTransport.class
			.getName() + ".OptimizationLevel"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying the Boolean flag indicating if
	 * dependency lists in require() calls should be expanded to include nested
	 * dependencies.
	 */
	public static final String EXPANDREQUIRELISTS_REQATTRNAME = IHttpTransport.class
			.getName() + ".ExpandRequireLists"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying the Boolean flag indicating if
	 * the aggregator should export the name of the requested module in the
	 * define() functions of anonymous modules.
	 */
	public static final String EXPORTMODULENAMES_REQATTRNAME = IHttpTransport.class
			.getName() + ".ExportModuleNames"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying a {@code Collection;&lt;String&gt;}
	 * containing set of requested locales.  How the requested locales are determined
	 * is implementation specific.
	 */
	public static final String REQUESTEDLOCALES_REQATTRNAME = IHttpTransport.class
			.getName() + ".RequestedLocales"; //$NON-NLS-1$
	/**
	 * Name of the request attribute specifying the Boolean flag indicating if
	 * debug logging output about require list expansion should be displayed in
	 * the browser console
	 */
	public static final String EXPANDREQLOGGING_REQATTRNAME = IHttpTransport.class
			.getName() + ".ExpandReqLogging"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying the Boolean flag indicating if
	 * the response should be annotated with comments indicating the names of
	 * the module source files.
	 */
	public static final String SHOWFILENAMES_REQATTRNAME = IHttpTransport.class
			.getName() + ".ShowFilenames"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying the Boolean flag indicating if
	 * the responses should not be cached. This flag affects caching of
	 * responses on the client as well as caching of module and layer builds on
	 * the server.
	 */
	public static final String NOCACHE_REQATTRNAME = IHttpTransport.class
			.getName() + ".NoCache"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying a comma delimited list of
	 * modules that, together with their expanded dependencies, will be included
	 * in the response in addition to the list of modules specified by
	 * {@link #REQUESTEDMODULES_REQATTRNAME}.
	 * <p>
	 * This feature is typically used to load a bootstrap layer of modules using
	 * the same request that is used to load the loader and the loader config.
	 * It is not used for aggregator generated requests.
	 * <p>
	 * Note: The module id(s) specified using this query arg may not specify a
	 * loader plugin, and any expanded dependencies that specify a loader plugin
	 * are not included in the response. The only exception to this is the has!
	 * plugin, which will be resolved by the aggregator in expanded dependencies
	 * as long as the specified feature is defined in the request.
	 */
	public static final String REQUIRED_REQATTRNAME = IHttpTransport.class
			.getName() + ".Required"; //$NON-NLS-1$

	/**
	 * Name of the request attribute specifying the config var name used to
	 * configure the loader on the client.  The default value is "require". 
	 * This parameter may be specified if a different var name is used to 
	 * configure the loader
	 * <p>
	 * This information is used by the javascript module builder to locate
	 * the loader config {@code deps} property in order to expand the 
	 * modules specified by that property to include nested dependencies.
	 * <p>
	 * This request attribute is not required to be present if no config var 
	 * name was specified in the request.
	 */
	public static final String CONFIGVARNAME_REQATTRNAME = IHttpTransport.class
			.getName() + ".ConfigVarName"; //$NON-NLS-1$
	
	/**
	 * Supported optimization levels. Module builders are not required to
	 * support all, or any, of these.
	 */
	public enum OptimizationLevel {
		NONE, WHITESPACE, SIMPLE, ADVANCED
	};

	/**
	 * Called to parse the HTTP request and decorate the request with the
	 * request attributes defined in this interface.
	 * 
	 * @param request
	 *            The HTTP request
	 * @throws IOException
	 */
	public void decorateRequest(HttpServletRequest request) throws IOException;

	/**
	 * Called by aggregator extensions to contribute JavaScript that will be
	 * included in the loader extension JavaScript module that is loaded by the
	 * client.
	 * <p>
	 * The loader extension JavaScript is loaded before the AMD loader. Code
	 * contributed to the loader extension JavaScript may modify or augment the
	 * loader config, such as adding paths or aliases, etc.
	 * <p>
	 * In addition, this interface defines the property
	 * <code>urlProcessors</code> which is in scope when the JavaScript
	 * contributed by this method is run. This property is an array of functions
	 * that are each called just before a request is sent to the aggregator. The
	 * function takes a single parameter which is the aggregator URL and it
	 * returns the same or updated URL. Extensions wishing to contribute query
	 * args to aggregator URLs, or otherwise modify the URL, may do so by adding
	 * a url processor function to this array in the JavaScript contribution as
	 * shown in the following example:
	 * <p>
	 * <code>urlProcessors.push(function(url) {return url+'&foo=bar'});</code>
	 * <p>
	 * The mechanism by which the loader extension JavaScript is delivered to 
	 * the client is outside the scope of this interface.  A typical implementation
	 * will register an {@link IResourceFactory} which returns an {@link IResource}
	 * object that will deliver the loader extension JavaScript when the AMD module
	 * which has been mapped to the resource URI for the loader extension JavaScript
	 * is requested.  
	 * 
	 * @param contribution
	 *            The JavaScript being contributed.
	 */
	public void contributeLoaderExtensionJavaScript(String contribution);

	/**
	 * Enum defining the constants for the various layer contribution types.
	 */
	public enum LayerContributionType {

		/**
		 * Before anything has been written to the response.
		 */
		BEGIN_RESPONSE,

		/**
		 * Before any modules build outputs have been written to the response
		 */
		BEGIN_MODULES,

		/**
		 * Before the first module build is written to the response.
		 */
		BEFORE_FIRST_MODULE,

		/**
		 * Before subsequent module builds are written to the response. The
		 * distinction between the first and subsequent module builds is made to
		 * facilitate the placing of commas between list items.
		 */
		BEFORE_SUBSEQUENT_MODULE,

		/**
		 * After the module build as been written to the response.
		 */
		AFTER_MODULE,

		/**
		 * After all module builds have beeen written to the respnose
		 */
		END_MODULES,

		/**
		 * Before any required module builds (the module(s) specified by the
		 * {@code required} request parameter, plus any of its expanded
		 * dependencies) have been written to the response.
		 */
		BEGIN_REQUIRED_MODULES,

		/**
		 * Before the first required module is written to the response.
		 */
		BEFORE_FIRST_REQUIRED_MODULE,

		/**
		 * Before subsequent required module builds are written to the response.
		 */
		BEFORE_SUBSEQUENT_REQUIRED_MODULE,

		/**
		 * After a required module build has been written to the response. The
		 * distinction between the first and subsequent module builds is made to
		 * facilitate the placing of commas between list items.
		 */
		AFTER_REQUIRED_MODULE,

		/**
		 * After a required module build has been written to the response.
		 */
		END_REQUIRED_MODULES,

		/**
		 * After all normal and required modules have been written to the
		 * response.
		 */
		END_RESPONSE
	}

	/**
	 * Returns a string value that will be added to the layer in the location
	 * specified by {@code type}, or null if the transport has no contribution
	 * to make. This method is provided to allow the transport to inject
	 * scaffolding JavaScript that may be required by the AMD loader.
	 * 
	 * @param request
	 *            The request object
	 * @param type
	 *            The layer contribution type
	 * @param mid
	 *            The module id. This parameter is specified for the following
	 *            {@code type} values:
	 *            <ul>
	 *            <li>{@link LayerContributionType#BEFORE_FIRST_MODULE}</li>
	 *            <li>{@link LayerContributionType#BEFORE_SUBSEQUENT_MODULE}</li>
	 *            <li>{@link LayerContributionType#AFTER_MODULE}</li>
	 *            <li>{@link LayerContributionType#BEFORE_FIRST_REQUIRED_MODULE}
	 *            </li>
	 *            <li>
	 *            {@link LayerContributionType#BEFORE_SUBSEQUENT_REQUIRED_MODULE}
	 *            </li>
	 *            <li>{@link LayerContributionType#AFTER_REQUIRED_MODULE}</li>
	 *            </ul>
	 *            For the following values of {@code type}, {@code mid} specifies
	 *            the comma separated list of required modules specified in the
	 *            request.
	 *            <ul>
	 *            <li>{@link LayerContributionType#BEGIN_REQUIRED_MODULES}</li>
	 *            <li>{@link LayerContributionType#END_REQUIRED_MODULES}</li>
	 *            </ul>
	 *            For all other values of {@code type}, {@code mid} is null.
	 * @return A string value that is to be added to the layer in the location
	 *         specified by {@code type}.
	 */
	public String getLayerContribution(HttpServletRequest request,
			LayerContributionType type, String mid);

	/**
	 * Returns a cache key generator for the JavaScript contained in the
	 * loader extension JavaScript and output by 
	 * {@link #getLayerContribution(HttpServletRequest, LayerContributionType, String)}
	 * . If the output JavaScript is invariant with regard to the request for
	 * the same set of modules, then this function may return null.
	 * 
	 * @return The cache key generator for the JavaScript output by this
	 *         transport.
	 */
	public ICacheKeyGenerator[] getCacheKeyGenerators();
}
