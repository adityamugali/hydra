/*
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
package com.addthis.hydra.data.filter.bundle;

import com.addthis.bundle.core.Bundle;
import com.addthis.bundle.core.BundleField;
import com.addthis.bundle.value.ValueObject;
import com.addthis.codec.Codec;

/**
 * This {@link BundleFilter BundleFilter} <span class="hydra-summary">compares two fields in a bundle for equality</span>.
 * <p/>
 * <p>The output of this bundle filter returns true if the equality test is true
 * and false if the test fails.  The implementer may invert this behavior
 * by setting the not field to true</p>
 * <p/>
 * <p>Example1:</p>
 * <pre>
 *   {op:"equals", left:"FIELD_ONE", right:"FIELD_TWO"},
 * </pre>
 * <p/>
 * <p>Example2:</p>
 * <pre>
 *   {op:"equals", left:"FIELD_ONE", right:"FIELD_TWO", not:true},
 * </pre>
 *
 * @user-reference
 * @hydra-name equals
 */
public class BundleFilterEquals extends BundleFilter {

    /**
     * the left hand field value
     */
    @Codec.Set(codable = true, required = true)
    private String left;

    /**
     * The right hand field
     */
    @Codec.Set(codable = true, required = true)
    private String right;

    /**
     * inverts behavior of filter
     */
    @Codec.Set(codable = true)
    private boolean not;

    String[] fields;

    @Override
    public void initialize() {
        fields = new String[2];
        fields[0] = left;
        fields[1] = right;
    }

    @Override
    public boolean filterExec(Bundle bundle) {
        BundleField bound[] = getBindings(bundle, fields);
        ValueObject lv = bundle.getValue(bound[0]);
        ValueObject rv = bundle.getValue(bound[1]);
        if (lv == null && rv == null) {
            return !not;
        } else if (lv == null || rv == null) {
            return not;
        }
        ValueObject.TYPE lt = lv.getObjectType();
        ValueObject.TYPE rt = lv.getObjectType();
        if (lt != rt) {
            return not;
        } else if (lv.equals(rv)) {
            return !not;
        } else {
            return not;
        }
    }

}
