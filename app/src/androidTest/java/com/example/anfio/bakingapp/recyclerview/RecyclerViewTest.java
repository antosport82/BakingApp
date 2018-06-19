package com.example.anfio.bakingapp.recyclerview;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public RecyclerViewTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private CountingIdlingResource mIdlingResource;
    private IdlingRegistry mIdlingRegistry;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingRegistry.register(mIdlingResource);
    }

    @Test
    public void testItemClick() {
        onView(withRecyclerView(R.id.rv_recipes).atPosition(1)).perform(click());
        onView(withId(R.id.ingredients_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.steps_main_fragment)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.tv_step_text)).check(matches(isDisplayed()));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }
}