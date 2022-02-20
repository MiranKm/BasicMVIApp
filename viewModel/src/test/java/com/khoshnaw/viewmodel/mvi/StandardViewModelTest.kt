package com.khoshnaw.viewmodel.mvi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.khoshnaw.controller.standard.StandardController
import com.khoshnaw.usecase.movie.base.InputPort
import com.khoshnaw.usecase.movie.base.OutputPort
import com.khoshnaw.viewmodel.standard.StandardViewModel
import com.khoshnaw.viewmodel.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class StandardViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesDispatcherRule = CoroutineTestRule()

    @MockK
    lateinit var controller1: DummyController1

    @MockK
    lateinit var controller2: DummyController2

    @MockK
    lateinit var controller3: DummyController3

    @MockK
    lateinit var controller4: DummyController4

    @MockK
    lateinit var controller5: DummyController5

    @InjectMockKs
    lateinit var viewModel: DummyViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `on init register output port to all controllers`() = runTest(StandardTestDispatcher()) {
        coVerify(exactly = 1) {
            controller1.registerOutputPort(outputPort = viewModel)
            controller2.registerOutputPort(outputPort = viewModel)
            controller3.registerOutputPort(outputPort = viewModel)
            controller4.registerOutputPort(outputPort = viewModel)
            controller5.registerOutputPort(outputPort = viewModel)
        }
    }

    @Test
    fun `on init consume flow`() = runTest(StandardTestDispatcher()) {
        viewModel.intents.send(DummyIntent.DummyAction)

        coVerify(exactly = 1) { controller1.doSomeThing() }
    }


    companion object {

        interface DummyInputPort : InputPort<DummyOutputPort>
        interface DummyOutputPort : OutputPort
        class DummyController1(
            private val inputPort: DummyInputPort
        ) : StandardController<DummyOutputPort>() {
            fun doSomeThing() = Unit
        }

        class DummyController2 : StandardController<DummyOutputPort>()

        class DummyController3 : StandardController<DummyOutputPort>()

        class DummyController4 : StandardController<DummyOutputPort>()

        class DummyController5 : StandardController<DummyOutputPort>()

        sealed class DummyIntent : MVIIntent {
            object DummyAction : DummyIntent()
        }

        sealed class DummyState : MVIState

        @Suppress("unused")
        class DummyViewModel(
            private val controller1: DummyController1,
            private val controller2: DummyController2,
            private val controller3: DummyController3,
            private val controller4: DummyController4,
            private val controller5: DummyController5,
        ) : StandardViewModel<DummyState, DummyIntent>(),
            DummyOutputPort {

            init {
                init()
            }

            public override suspend fun handleIntent(intent: DummyIntent) = when (intent) {
                DummyIntent.DummyAction -> controller1.doSomeThing()
            }
        }
    }
}
