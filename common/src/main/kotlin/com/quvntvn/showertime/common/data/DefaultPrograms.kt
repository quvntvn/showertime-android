package com.quvntvn.showertime.common.data

object DefaultPrograms {

    val programs = listOf(
        RoutineProgram(
            id = "essentiel",
            name = "🚿 Essentiel (~5 min)",
            steps = listOf(
                RoutineStep("Pré-rinçage", 45),
                RoutineStep("Savonnage Corps", 90),
                RoutineStep("Rinçage n°1", 90)
            )
        ),
        RoutineProgram(
            id = "quotidien",
            name = "🧼 Quotidien (~7 min)",
            steps = listOf(
                RoutineStep("Pré-rinçage", 45),
                RoutineStep("Savonnage Corps", 90),
                RoutineStep("Shampoing", 75),
                RoutineStep("Rinçage n°1", 120)
            )
        ),
        RoutineProgram(
            id = "complet",
            name = "✨ Complet (~9 min)",
            steps = listOf(
                RoutineStep("Pré-rinçage", 60),
                RoutineStep("Savonnage Corps", 90),
                RoutineStep("Shampoing", 75),
                RoutineStep("Rinçage n°1", 60),
                RoutineStep("Soin / Après-shampoing", 45),
                RoutineStep("Rinçage Final", 120)
            )
        ),
        RoutineProgram(
            id = "express",
            name = "⚡ Express (~3 min)",
            steps = listOf(
                RoutineStep("Pré-rinçage", 30),
                RoutineStep("Savonnage Corps", 60),
                RoutineStep("Rinçage", 60)
            )
        )
    )
}
