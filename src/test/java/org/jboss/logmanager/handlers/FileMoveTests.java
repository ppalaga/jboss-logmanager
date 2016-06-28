/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
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

package org.jboss.logmanager.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 *
 */
public class FileMoveTests extends AbstractHandlerTest {

    @Test
    public void testMoveTargetExists() throws IOException {
        assertMove(true, false);

    }

    @Test
    public void testMoveTargetNotExists() throws IOException {
        assertMove(false, false);
    }

    @Test
    public void testMoveTargetExistsBytewise() throws IOException {
        assertMove(true, true);

    }

    @Test
    public void testMoveTargetNotExistsBytewise() throws IOException {
        assertMove(false, true);
    }

    private static void assertMove(boolean f2Exists, boolean bytewise) throws IOException {
        File f1 = new File(BASE_LOG_DIR, "f1.txt");
        File f2 = new File(BASE_LOG_DIR, "f2.txt");

        Assert.assertFalse(f1.getAbsolutePath() + " should not exist before the test", f1.exists());
        Assert.assertFalse(f2.getAbsolutePath() + " should not exist before the test", f2.exists());
        write(f1, "f1");
        if (f2Exists) {
            write(f2, "f2");
        }

        FileMove.move(f1, f2, bytewise);

        Assert.assertFalse(f1.getAbsolutePath() + " should not exist after FileMove.move()", f1.exists());
        Assert.assertTrue(f2.getAbsolutePath() + " should exist after FileMove.move()", f2.exists());

        Assert.assertEquals("f1", read(f2));
    }

    private static void write(File out, String content) throws IOException {
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(out);
            o.write(content.getBytes("utf-8"));
        } finally {
            if (o != null) {
                o.close();
            }
        }
    }

    private static String read(File in) throws IOException {
        Reader r = null;
        try {
            r = new InputStreamReader(new FileInputStream(in), "utf-8");
            int c;
            StringBuilder result = new StringBuilder();
            while ((c = r.read()) > 0) {
                result.append((char) c);
            }
            return result.toString();
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

}
