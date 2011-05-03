/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]".
 *
 * Copyright © 2011 ForgeRock AS. All rights reserved.
 */

package org.forgerock.openidm.recon.impl;

import java.util.Map;

/**
 * The correlator runs a configured script i.e. the correlation rule that evaluates
 * whether the {@code sourceObject} and {@targetObject} match. A {@code scope} object
 * is handed off to the script consists of:
 * </p>
 * <p/>
 * Pseudo json:
 * <code>
 * {
 * "sourceObject" : { properties },
 * "targetObject" : { properties },
 * "policy" : {},
 * }
 * </code>
 * <p/>
 * The Correlator expects a return value of either True or False from the script. True
 * indicating correlation and False otherwise.
 */
public class Correlator {

    /**
     * Determine if the two objects correlate, by calling out to a script configured
     * in the {@link org.forgerock.openidm.recon.impl.ReconciliationPolicyEntry}.
     *
     * @param sourceObject to compare
     * @param targetObject to compare
     * @return boolean indicating correlation, true if they match, false otherwise
     */
    public Boolean correlates(Map<String, Object> sourceObject, Map<String, Object> targetObject)
            throws ReconciliationException { //throw an exception if one is generated by the script
        return false;
    }

}
