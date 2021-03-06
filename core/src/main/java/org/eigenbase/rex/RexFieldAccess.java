/*
// Licensed to Julian Hyde under one or more contributor license
// agreements. See the NOTICE file distributed with this work for
// additional information regarding copyright ownership.
//
// Julian Hyde licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except in
// compliance with the License. You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/
package org.eigenbase.rex;

import org.eigenbase.reltype.*;
import org.eigenbase.sql.SqlKind;

/**
 * Access to a field of a row-expression.
 *
 * <p>You might expect to use a <code>RexFieldAccess</code> to access columns of
 * relational tables, for example, the expression <code>emp.empno</code> in the
 * query
 *
 * <blockquote>
 * <pre>SELECT emp.empno FROM emp</pre>
 * </blockquote>
 *
 * but there is a specialized expression {@link RexInputRef} for this purpose.
 * So in practice, <code>RexFieldAccess</code> is usually used to access fields
 * of correlating variabless, for example the expression <code>emp.deptno</code>
 * in
 *
 * <blockquote>
 * <pre>SELECT ename
 * FROM dept
 * WHERE EXISTS (
 *     SELECT NULL
 *     FROM emp
 *     WHERE emp.deptno = dept.deptno
 *     AND gender = 'F')</pre>
 * </blockquote>
 *
 * @author jhyde
 * @version $Id$
 * @since Nov 24, 2003
 */
public class RexFieldAccess
    extends RexNode
{
    //~ Instance fields --------------------------------------------------------

    private RexNode expr;
    private final RelDataTypeField field;

    //~ Constructors -----------------------------------------------------------

    RexFieldAccess(
        RexNode expr,
        RelDataTypeField field)
    {
        this.expr = expr;
        this.field = field;
        computeDigest();
    }

    //~ Methods ----------------------------------------------------------------

    public RelDataTypeField getField()
    {
        return field;
    }

    public RelDataType getType()
    {
        return field.getType();
    }

    public RexFieldAccess clone()
    {
        return new RexFieldAccess(expr, field);
    }

    public SqlKind getKind()
    {
        return SqlKind.FIELD_ACCESS;
    }

    public <R> R accept(RexVisitor<R> visitor)
    {
        return visitor.visitFieldAccess(this);
    }

    /**
     * Returns the expression whose field is being accessed.
     */
    public RexNode getReferenceExpr()
    {
        return expr;
    }

    public void setReferenceExpr(RexNode expr)
    {
        this.expr = expr;
    }

    /**
     * Returns the name of the field.
     */
    public String getName()
    {
        return field.getName();
    }

    public String toString()
    {
        return computeDigest();
    }

    private String computeDigest()
    {
        return (this.digest = expr + "." + field.getName());
    }
}

// End RexFieldAccess.java
