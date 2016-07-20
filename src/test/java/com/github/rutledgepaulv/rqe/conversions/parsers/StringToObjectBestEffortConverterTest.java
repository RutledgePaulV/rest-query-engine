/*
 *  com.github.rutledgepaulv.rqe.conversions.parsers.StringToObjectBestEffortConverterTest
 *  *
 *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *
 *  * This software may be modified and distributed under the terms
 *  * of the MIT license.  See the LICENSE file for details.
 *
 */

package com.github.rutledgepaulv.rqe.conversions.parsers;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

public class StringToObjectBestEffortConverterTest {

    private static final StringToObjectBestEffortConverter it = new StringToObjectBestEffortConverter();

    @Test
    public void itConvertsBooleans() throws Exception {
        assertEquals(true, it.convert("true"));
        assertEquals(false, it.convert("false"));
    }

    @Test
    public void itConvertsLongs() throws Exception {
        assertEquals(1L, it.convert("1"));
        assertEquals(9999L, it.convert("9999"));
        assertEquals(-30L, it.convert("-30"));
    }

    @Test
    public void itConvertsDoubles() throws Exception {
        assertEquals(30.5, it.convert("30.5"));
        assertEquals(3.1415926, it.convert("3.1415926"));
        assertEquals(-9993333.25980, it.convert("-9993333.25980"));
    }

    @Test
    public void itConvertsDatesInIsoFormat() throws Exception {
        Instant expected1 = Instant.EPOCH.plus(35, ChronoUnit.HOURS).plus(3, ChronoUnit.DAYS);
        Instant expected2 = Instant.EPOCH.plus(35, ChronoUnit.MINUTES).plus(3, ChronoUnit.HALF_DAYS);
        Instant now = Instant.now();
        assertEquals(expected1, it.convert("1970-01-05T11:00:00Z"));
        assertEquals(expected2, it.convert("1970-01-02T12:35:00Z"));
        assertEquals(now, it.convert(now.toString()));
    }

    @Test
    public void ifItCantConvertToSomethingElseItJustReturnsTheString() throws Exception {
        assertEquals("badgers", it.convert("badgers"));
        assertEquals("chevron", it.convert("chevron"));
        assertEquals("buggy", it.convert("buggy"));
    }

}
