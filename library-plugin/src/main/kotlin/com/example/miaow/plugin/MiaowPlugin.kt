package com.example.miaow.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.example.miaow.plugin.asm.ScanClassVisitorFactory
import com.example.miaow.plugin.asm.TimeClassVisitorFactory
import com.example.miaow.plugin.asm.TraceClassVisitorFactory
import com.example.miaow.plugin.bean.ScanBean
import com.example.miaow.plugin.bean.TimeBean
import com.example.miaow.plugin.bean.TraceBean
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.Opcodes

class MiaowPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.transformClassesWith(
                TimeClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                it.listOfTimes.set(
                    listOf(
//                        TimeBean( //具体到方法名称
//                            "com/example/fragment/project/activity/MainActivity",
//                            "onCreate",
//                            "(Landroid/os/Bundle;)V"
//                        ),
//                        TimeBean( //以包名和执行时间为条件
//                            "com/example/fragment/library/base",
//                            time = 50L
//                        ),
                        TimeBean( //以包名和执行时间为条件
                            "freemusic/download/musicplayer/mp3player",
                            time = 1L
                        ),
                        TimeBean( //以包名和执行时间为条件
                            "musicplayer/musicapps/music",
                            time = 1L
                        ),
//                        TimeBean( //以包名和执行时间为条件
//                            "freemusic/download/musicplayer/mp3player/sort",
//                            time = 0L
//                        )
                    )
                )
            }
            variant.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }

}