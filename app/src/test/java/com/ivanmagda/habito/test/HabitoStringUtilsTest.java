package com.ivanmagda.habito.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.junit.*;
import org.mockito.runners.MockitoJUnitRunner;
import com.ivanmagda.habito.utils.HabitoStringUtils;

/**
 * Created by Bence on 2017. 05. 04..
 */
@RunWith(MockitoJUnitRunner.class)

public class HabitoStringUtilsTest {

        @Test
        public void stringUtilTest() {
            String s1 = HabitoStringUtils.capitalized("bob");
            assertEquals("Bob",s1);

            String s2 = HabitoStringUtils.capitalized("Bob");
            assertEquals("Bob",s2);
        }

}