package com.neu.prattle;

import com.neu.prattle.GroupModelTest;
import com.neu.prattle.GroupServiceImplTest;
import com.neu.prattle.UserModelTest;
import com.neu.prattle.UserServiceImplTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({GroupModelTest.class, GroupServiceImplTest.class, UserModelTest.class,
        UserServiceImplTest.class})
public class TestSuite {
}
