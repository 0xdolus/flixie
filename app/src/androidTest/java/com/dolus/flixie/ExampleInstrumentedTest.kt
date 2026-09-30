package com.dolus.flixie

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewbinding.ViewBinding
import com.dolus.flixie.databinding.BottomResultviewPreviewBinding
import com.dolus.flixie.databinding.FragmentHomeBinding
import com.dolus.flixie.databinding.FragmentHomeTvBinding
import com.dolus.flixie.databinding.FragmentLibraryBinding
import com.dolus.flixie.databinding.FragmentLibraryTvBinding
import com.dolus.flixie.databinding.FragmentPlayerBinding
import com.dolus.flixie.databinding.FragmentPlayerTvBinding
import com.dolus.flixie.databinding.FragmentResultBinding
import com.dolus.flixie.databinding.FragmentResultTvBinding
import com.dolus.flixie.databinding.FragmentSearchBinding
import com.dolus.flixie.databinding.FragmentSearchTvBinding
import com.dolus.flixie.databinding.HomeResultGridBinding
import com.dolus.flixie.databinding.HomepageParentBinding
import com.dolus.flixie.databinding.HomepageParentEmulatorBinding
import com.dolus.flixie.databinding.HomepageParentTvBinding
import com.dolus.flixie.databinding.PlayerCustomLayoutBinding
import com.dolus.flixie.databinding.PlayerCustomLayoutTvBinding
import com.dolus.flixie.databinding.RepositoryItemBinding
import com.dolus.flixie.databinding.RepositoryItemTvBinding
import com.dolus.flixie.databinding.SearchResultGridBinding
import com.dolus.flixie.databinding.SearchResultGridExpandedBinding
import com.dolus.flixie.databinding.TrailerCustomLayoutBinding
import com.dolus.flixie.utils.SubtitleHelper
import com.dolus.flixie.utils.TestingUtils
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestApplication : Activity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private fun getAllProviders(): Array<MainAPI> {
        println("Providers: ${APIHolder.allProviders.size}")
        return APIHolder.allProviders.toTypedArray() //.filter { !it.usesWebView }
    }

    @Throws
    private inline fun <reified T : ViewBinding> testAllLayouts(
        activity: Activity,
       vararg layouts: Int
    ) {

        val bind = T::class.java.methods.first { it.name == "bind" }
        val inflater = LayoutInflater.from(activity)
        for (layout in layouts) {
            val root = inflater.inflate(layout, null, false)
            bind.invoke(null, root)
        }
    }

    @Test
    @Throws
    fun layoutTest() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity: MainActivity ->
                // FragmentHomeHeadBinding and FragmentHomeHeadTvBinding CANT be the same
                //testAllLayouts<FragmentHomeHeadBinding>(activity, R.layout.fragment_home_head, R.layout.fragment_home_head_tv)
                //testAllLayouts<FragmentHomeHeadTvBinding>(activity, R.layout.fragment_home_head, R.layout.fragment_home_head_tv)

                // main cant be tested
               // testAllLayouts<ActivityMainTvBinding>(activity,R.layout.activity_main, R.layout.activity_main_tv)
               // testAllLayouts<ActivityMainBinding>(activity,R.layout.activity_main, R.layout.activity_main_tv)
                //testAllLayouts<ActivityMainBinding>(activity, R.layout.activity_main_tv)

                testAllLayouts<BottomResultviewPreviewBinding>(activity, R.layout.bottom_resultview_preview,R.layout.bottom_resultview_preview_tv)

                testAllLayouts<FragmentPlayerBinding>(activity, R.layout.fragment_player,R.layout.fragment_player_tv)
                testAllLayouts<FragmentPlayerTvBinding>(activity, R.layout.fragment_player,R.layout.fragment_player_tv)

               // testAllLayouts<FragmentResultBinding>(activity, R.layout.fragment_result,R.layout.fragment_result_tv)
               // testAllLayouts<FragmentResultTvBinding>(activity, R.layout.fragment_result,R.layout.fragment_result_tv)

                testAllLayouts<PlayerCustomLayoutBinding>(activity, R.layout.player_custom_layout,R.layout.player_custom_layout_tv, R.layout.trailer_custom_layout)
                testAllLayouts<PlayerCustomLayoutTvBinding>(activity, R.layout.player_custom_layout,R.layout.player_custom_layout_tv, R.layout.trailer_custom_layout)
                testAllLayouts<TrailerCustomLayoutBinding>(activity, R.layout.player_custom_layout,R.layout.player_custom_layout_tv, R.layout.trailer_custom_layout)

                testAllLayouts<RepositoryItemBinding>(activity, R.layout.repository_item_tv, R.layout.repository_item)
                testAllLayouts<RepositoryItemTvBinding>(activity, R.layout.repository_item_tv, R.layout.repository_item)

                testAllLayouts<RepositoryItemBinding>(activity, R.layout.repository_item_tv, R.layout.repository_item)
                testAllLayouts<RepositoryItemTvBinding>(activity, R.layout.repository_item_tv, R.layout.repository_item)

                testAllLayouts<FragmentHomeBinding>(activity, R.layout.fragment_home_tv, R.layout.fragment_home)
                testAllLayouts<FragmentHomeTvBinding>(activity, R.layout.fragment_home_tv, R.layout.fragment_home)

                testAllLayouts<FragmentSearchBinding>(activity, R.layout.fragment_search_tv, R.layout.fragment_search)
                testAllLayouts<FragmentSearchTvBinding>(activity, R.layout.fragment_search_tv, R.layout.fragment_search)

                testAllLayouts<HomeResultGridBinding>(activity, R.layout.home_result_grid_expanded, R.layout.home_result_grid)
                //testAllLayouts<HomeResultGridExpandedBinding>(activity, R.layout.home_result_grid_expanded, R.layout.home_result_grid) ??? fails ???

                testAllLayouts<SearchResultGridExpandedBinding>(activity, R.layout.search_result_grid, R.layout.search_result_grid_expanded)
                testAllLayouts<SearchResultGridBinding>(activity, R.layout.search_result_grid, R.layout.search_result_grid_expanded)


               // testAllLayouts<HomeScrollViewBinding>(activity, R.layout.home_scroll_view, R.layout.home_scroll_view_tv)
               // testAllLayouts<HomeScrollViewTvBinding>(activity, R.layout.home_scroll_view, R.layout.home_scroll_view_tv)

                testAllLayouts<HomepageParentTvBinding>(activity, R.layout.homepage_parent_tv, R.layout.homepage_parent_emulator, R.layout.homepage_parent)
                testAllLayouts<HomepageParentEmulatorBinding>(activity, R.layout.homepage_parent_tv, R.layout.homepage_parent_emulator, R.layout.homepage_parent)
                testAllLayouts<HomepageParentBinding>(activity, R.layout.homepage_parent_tv, R.layout.homepage_parent_emulator, R.layout.homepage_parent)

                testAllLayouts<FragmentLibraryTvBinding>(activity, R.layout.fragment_library_tv, R.layout.fragment_library)
                testAllLayouts<FragmentLibraryBinding>(activity, R.layout.fragment_library_tv, R.layout.fragment_library)
            }
        }
    }

    @Test
    @Throws(AssertionError::class)
    fun providerCorrectData() {
        val langTagsIETF = SubtitleHelper.languages.map { it.IETF_tag }
        Assert.assertFalse("IETFTagNames does not contain any languages", langTagsIETF.isNullOrEmpty())
        for (api in getAllProviders()) {
            Assert.assertTrue("Api does not contain a mainUrl", api.mainUrl != "NONE")
            Assert.assertTrue("Api does not contain a name", api.name != "NONE")
            Assert.assertTrue(
                "Api ${api.name} does not contain a valid language code",
                langTagsIETF.contains(api.lang)
            )
            Assert.assertTrue(
                "Api ${api.name} does not contain any supported types",
                api.supportedTypes.isNotEmpty()
            )
        }
        println("Done providerCorrectData")
    }

    @Test
    fun providerCorrectHomepage() {
        runBlocking {
            getAllProviders().toList().amap { api ->
                TestingUtils.testHomepage(api, TestingUtils.Logger())
            }
        }
        println("Done providerCorrectHomepage")
    }

    @Test
    fun testAllProvidersCorrect() {
        runBlocking {
            TestingUtils.getDeferredProviderTests(
                this,
                getAllProviders(),
            ) { _, _ -> }
        }
    }
}
