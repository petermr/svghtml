/**
 *    Copyright 2011 Peter Murray-Rust et. al.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.xmlcml.graphics.math;

import org.xmlcml.xml.XMLConstants;

import nu.xom.XPathContext;

public interface MathMLConstants extends XMLConstants {

	/** standard namespace for SVG
	 * 
	 */
	public static final String MATHML_NS = "http://www.w3.org/1998/Math/MathML";

    /** XPathContext for MATHML.
     */
    XPathContext SVG_XPATH = new XPathContext("mathml", MATHML_NS);
    
}