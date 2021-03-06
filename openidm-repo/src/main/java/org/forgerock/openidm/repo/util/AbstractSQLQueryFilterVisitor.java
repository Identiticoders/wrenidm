/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2015 ForgeRock AS. All rights reserved.
 * Portions Copyright 2018 Wren Security.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://forgerock.org/license/CDDLv1.0.html
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at http://forgerock.org/license/CDDLv1.0.html
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 */
package org.forgerock.openidm.repo.util;

import java.util.List;

import org.forgerock.json.JsonPointer;
import org.forgerock.util.query.QueryFilter;
import org.forgerock.util.query.QueryFilterVisitor;

/**
 * An abstract {@link QueryFilterVisitor} to produce SQL via an
 * {@link SQLRenderer}.
 * <p>
 * Includes implementation patterns for the following standard operators:
 * <ul>
 *   <li>&gt;=</li>
 *   <li>&gt;</li>
 *   <li>=</li>
 *   <li>&lt;</li>
 *   <li>&lt;=</li>
 * </ul>
 * <p>
 * Along with the following implementations for {@link QueryFilter}'s
 * <ul>
 *     <li>contains : field LIKE '%value%'</li>
 *     <li>startsWith : field LIKE 'value%'</li>
 * </ul>
 * <p>
 * This implementation does not support extended-match.
 * <p>
 * The implementer is responsible for implementing the following:
 * <ul>
 *     <li>{@link #visitValueAssertion(Object, String, org.forgerock.json.JsonPointer, Object)}
 *     which handles the value assertions - x operand y for the standard
 *     operands.</li>
 *     <li>{@link #visitPresentFilter(Object, org.forgerock.json.JsonPointer)}
 *     as "field present" can vary by database implementation (though typically
 *     "field IS NOT NULL" is chosen).</li>
 *     <li>{@link #visitBooleanLiteralFilter(Object, boolean)} to express a
 *     boolean true or false in whatever SQL dialect is being used.</li>
 *     <li>{@link #visitNotFilter(Object, QueryFilter)} to negate the
 *     QueryFilter parameter.</li>
 *     <li>{@link #visitAndFilter(Object, List)} to
 *     dictate how the composite function AND behaves.</li>
 *     <li>{@link #visitOrFilter(Object, List)} to
 *     dictate how the composite function OR behaves.</li>
 * </ul>
 *
 * @param <R> the subclass of SQLRenderer the visitor returns
 * @param <P> the parameter type passed in the visit methods
 */
public abstract class AbstractSQLQueryFilterVisitor<R extends SQLRenderer<?>, P>
implements QueryFilterVisitor<R, P, JsonPointer> {
    /**
     * A templating method that will generate the actual value assertion.
     * <p>
     * Example:
     * <pre>{@code
     *     ?_queryFilter=email+eq+"someone@example.com"
     * }</pre>
     * is an QueryFilter stating the value assertion "email" equals "someone@example.com".  The correct SQL for that
     * may vary depending on database variant and schema definition.  This method will be invoked as
     * <pre>{@code
     *     return visitValueAssertion(parameters, "=", JsonPointer(/email), "someone@example.com");
     * }</pre>
     * A possible implementation for the above example may be
     * <pre>{@code
     *     return getDatabaseColumnFor("email") + "=" + ":email";
     * }</pre>
     * The parameters argument is implementation-dependent as a way to store placeholder mapping throughout the query-filter visiting.
     *
     * @param parameters storage of parameter-substitutions for the value of the assertion
     * @param operand the operand used to compare
     * @param field the object field as a JsonPointer - implementations need to map this to an appropriate database column
     * @param valueAssertion the value in the assertion
     * @return a query expression or clause
     */
    public abstract R visitValueAssertion(P parameters, String operand, JsonPointer field, Object valueAssertion);

    /*
     * {@inheritDoc}
     */
    @Override
    public abstract R visitPresentFilter(P parameters, JsonPointer field);

    /*
     * {@inheritDoc}
     */
    @Override
    public abstract R visitBooleanLiteralFilter(P parameters, boolean value);

    /*
     * {@inheritDoc}
     */
    @Override
    public abstract R visitNotFilter(P parameters, QueryFilter<JsonPointer> subFilter);

    /*
     * {@inheritDoc}
     */
    @Override
    public abstract R visitAndFilter(P parameters, List<QueryFilter<JsonPointer>> subFilters);

    /*
     * {@inheritDoc}
     */
    @Override
    public abstract R visitOrFilter(P parameters, List<QueryFilter<JsonPointer>> subFilters);

    @Override
    public R visitContainsFilter(P parameters, JsonPointer field, Object valueAssertion) {
        return visitValueAssertion(parameters, "LIKE", field, "%" + valueAssertion + "%");
    }

    @Override
    public R visitEqualsFilter(P parameters, JsonPointer field, Object valueAssertion) {
        return visitValueAssertion(parameters, "=", field, valueAssertion);
    }

    @Override
    public R visitExtendedMatchFilter(P parameters, JsonPointer field, String operator, Object valueAssertion) {
        throw new UnsupportedOperationException("Extended match filter not supported on this endpoint");
    }

    @Override
    public R visitGreaterThanFilter(P parameters, JsonPointer field, Object valueAssertion) {
        return visitValueAssertion(parameters, ">", field, valueAssertion);
    }

    @Override
    public R visitGreaterThanOrEqualToFilter(P parameters, JsonPointer field, Object valueAssertion) {
        return visitValueAssertion(parameters, ">=", field, valueAssertion);
    }

    @Override
    public R visitLessThanFilter(P parameters, JsonPointer field, Object valueAssertion) {
        return visitValueAssertion(parameters, "<", field, valueAssertion);
    }

    @Override
    public R visitLessThanOrEqualToFilter(P parameters, JsonPointer field, Object valueAssertion) {
        return visitValueAssertion(parameters, "<=", field, valueAssertion);
    }


    @Override
    public R visitStartsWithFilter(P parameters, JsonPointer field, Object valueAssertion) {
        return visitValueAssertion(parameters, "LIKE", field, valueAssertion + "%");
    }
}
