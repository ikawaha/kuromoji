/**
 * Copyright © 2010-2015 Atilika Inc. and contributors (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  A copy of the
 * License is distributed with this work in the LICENSE.md file.  You may
 * also obtain a copy of the License from
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atilika.kuromoji.fst;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FSTTest {

    @Test
    public void testFST() throws IOException {
        String inputValues[] = {
            "brats", "cat", "dog", "dogs", "rat",
        };

        int outputValues[] = {
            1, 3, 5, 7, 11
        };

        Builder builder = new Builder();
        builder.build(inputValues, outputValues);

        for (int i = 0; i < inputValues.length; i++) {
            assertEquals(outputValues[i], builder.transduce(inputValues[i]));
        }

        Compiler compiledFST = builder.getCompiler();
        FST fst = new FST(compiledFST.getBytes());

        assertEquals(1, fst.lookup("brats"));
        assertEquals(3, fst.lookup("cat"));
        assertEquals(5, fst.lookup("dog"));
        assertEquals(7, fst.lookup("dogs"));
        assertEquals(11, fst.lookup("rat"));
        assertEquals(-1, fst.lookup("rats")); // No match
    }

    @Test
    public void testCommonPrefixSearch() throws IOException {
        String inputValues[] = {
            "電気","電気通信","電気通信大学","電気通信大学大学院"
        };

        int outputValues[] = {
            1, 3, 5, 7
        };

        Builder builder = new Builder();
        builder.build(inputValues, outputValues);

        for (int i = 0; i < inputValues.length; i++) {
            assertEquals(outputValues[i], builder.transduce(inputValues[i]));
        }

        Compiler compiledFST = builder.getCompiler();
        FST fst = new FST(compiledFST.getBytes());

        List<Match> matches = fst.commonPrefixSearch("電気通信大学大学院は調布にあります");
        assertEquals(4, matches.size());
        assertEquals(1, matches.get(0).output);
        assertEquals(2, matches.get(0).position);
        assertEquals(3, matches.get(1).output);
        assertEquals(4, matches.get(1).position);
        assertEquals(5, matches.get(2).output);
        assertEquals(6, matches.get(2).position);
        assertEquals(7, matches.get(3).output);
        assertEquals(9, matches.get(3).position);
    }
}
