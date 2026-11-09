package com.angelramirez.agresiembre.data

object CropRepository {

    private val cropDetailsMap = mapOf(
        // 1. MAÍZ CRIOLLO AMARILLO
        "1" to CropDetail(
            id = "1",
            name = "Maíz Criollo Amarillo",
            scientificName = "Zea mays var. indurata",
            cycleDays = "110-120 d",
            waterRequirement = "Moderado",
            phRange = "5.8 - 7.0",
            steps = listOf(
                StepItem(1, "Pre-siembra y Surcado", "Apertura de surco a 25 cm de profundidad. Aplicar abonado de fondo antes de tapar."),
                StepItem(2, "Espaciamiento de Plantación", "25 cm entre plantas en hilera / 80 cm entre surcos continuos."),
                StepItem(3, "Profundidad de Semilla", "Depositar de 3 a 5 cm. En suelos secos o arenosos asegurar 5 cm."),
                StepItem(4, "Riego de Asentamiento", "Riego abundante inmediato tras la siembra sin encharcar. Mantener humedad los primeros 8 días.")
            ),
            calendar = listOf(
                CalendarItem("Germinación y Emergencia", "Día 5 - 7", "Aparición de plúnula y primera hoja verdadera."),
                CalendarItem("Crecimiento Vegetativo", "Día 25 - 50", "Desarrollo foliar acelerado, aporcado preventivo y fijación de raíces."),
                CalendarItem("Floración y Espigado", "Día 60 - 75", "Emisión de polen y formación del jilote. Momento de mayor demanda hídrica."),
                CalendarItem("Maduración y Cosecha", "Día 110 - 120", "Grano lechoso a duro. Caída de hojas secas y corte en punto óptimo.")
            ),
            requirements = listOf(
                RequirementItem("Exposición Solar", "Directa (+6 horas diarias)", "Planta tipo C4 de alta eficiencia fotosintética. La sombra reduce el rendimiento."),
                RequirementItem("Tolerancia Hídrica", "Media (Punto crítico: Floración)", "Soporta estrés moderado en plántula, pero la falta de agua en floración causa aborto de granos."),
                RequirementItem("Nutrientes Clave (N-P-K)", "Nitrógeno alto + Fósforo/Potasio", "Nitrógeno primordial en etapa vegetativa. Fósforo y Potasio para llenado de grano.")
            ),
            pests = listOf(
                PestItem("Gusano Cogollero", "Spodoptera frugiperda", "Riesgo Alto", "Barrena el cogollo tierno dejando aserrín y perforaciones. Inspeccionar envés de hojas semanalmente."),
                PestItem("Barrenador del Tallo", "Diatraea lineolata", "Riesgo Medio", "Penetra los entrenudos causando acame (caída de plantas por viento)."),
                PestItem("Mancha de Asfalto", "Phyllachora maydis", "Hongo Foliar", "Puntos negros elevados sobre las hojas que necrosan el tejido bajo alta humedad.")
            )
        ),

        // 2. FRIJOL ARBUSTIVO ROJO
        "2" to CropDetail(
            id = "2",
            name = "Frijol Arbustivo Rojo",
            scientificName = "Phaseolus vulgaris",
            cycleDays = "70-75 d",
            waterRequirement = "Baja-Media",
            phRange = "6.0 - 7.5",
            steps = listOf(
                StepItem(1, "Preparación del Cama", "Descompactar los primeros 15-20 cm de suelo para facilitar la nodulación radicular."),
                StepItem(2, "Siembra Directa", "Colocar 2 semillas por golpe cada 15 cm a una profundidad de 2 a 3 cm."),
                StepItem(3, "Deshierbe Inicial", "Mantener limpio el cultivo durante los primeros 20 días para evitar competencia de luz."),
                StepItem(4, "Aporcado Ligero", "Cubrir ligeramente la base del tallo al día 25 para fortalecer raíces adventicias.")
            ),
            calendar = listOf(
                CalendarItem("Emergencia", "Día 4 - 6", "Despliegue de cotiledones y hojas unifoliadas."),
                CalendarItem("Floración", "Día 30 - 35", "Aparición de flores racemosas. Evitar riego por aspersión directo."),
                CalendarItem("Llenado de Vainas", "Día 45 - 60", "Acumulación de proteínas y almidón en los granos."),
                CalendarItem("Cosecha Seca", "Día 70 - 75", "Vainas amarillas y quebradizas. Cosecha manual matutina.")
            ),
            requirements = listOf(
                RequirementItem("Fijación Biológica", "Simbiosis con Rhizobium", "Capaz de fijar hasta 40kg de Nitrógeno por hectárea directamente al suelo."),
                RequirementItem("Sensibilidad al Encharcamiento", "Muy Alta", "Suelos con mal drenaje provocan pudrición radicular (Fusarium)."),
                RequirementItem("Luz Solar", "Plena exposición", "Requiere entre 5 y 8 horas diarias de radiación solar directa.")
            ),
            pests = listOf(
                PestItem("Mosca Blanca", "Bemisia tabaci", "Riesgo Alto", "Transmite el virus del mosaico dorado. Monitorear con trampas amarillas."),
                PestItem("Gorgojo del Frijol", "Acanthoscelides obtectus", "Riesgo Medio", "Daña los granos almacenados y en etapa final de secado en campo.")
            )
        ),

        // 3. CALABAZA / ZAPALLO
        "3" to CropDetail(
            id = "3",
            name = "Calabaza / Zapallo",
            scientificName = "Cucurbita pepo",
            cycleDays = "90-100 d",
            waterRequirement = "Media-Alta",
            phRange = "5.5 - 7.0",
            steps = listOf(
                StepItem(1, "Preparación de Casillas", "Cavar hoyos de 30x30 cm enriquecidos con materia orgánica o compost maduro."),
                StepItem(2, "Siembra de Golpes", "Sembrar 3 semillas por casilla, raleando a las 2 plantas más fuertes."),
                StepItem(3, "Guiado de Guías", "Dirigir el crecimiento rastrero para cubrir el suelo de forma uniforme."),
                StepItem(4, "Protección de Fruto", "Colocar pajote o cama seca bajo los frutos para evitar contacto directo con tierra húmeda.")
            ),
            calendar = listOf(
                CalendarItem("Brote Vegetativo", "Día 7 - 10", "Desarrollo rápido de guías y hojas anchas sombreadoras."),
                CalendarItem("Floración Sexuada", "Día 40 - 50", "Aparición de flores masculinas y femeninas. Requiere polinizadores."),
                CalendarItem("Desarrollo de Fruto", "Día 60 - 85", "Engorde acelerado y endurecimiento de la corteza."),
                CalendarItem("Cosecha", "Día 90 - 100", "Pedúnculo seco y corteza dura al tacto.")
            ),
            requirements = listOf(
                RequirementItem("Cobertura Viva", "Excelente retenedor de humedad", "Sus hojas anchas reducen la evaporación del suelo hasta en un 50%."),
                RequirementItem("Nutrición Orgánica", "Demanda alta de Humus", "Requiere abundancia de potasio y materia orgánica en el hoyo de siembra.")
            ),
            pests = listOf(
                PestItem("Cenicilla o Oídio", "Erysiphe cichoracearum", "Hongo Foliar", "Polvo blanco sobre hojas por exceso de humedad o mala ventilación."),
                PestItem("Gusano del Fruto", "Diaphania hyalinata", "Riesgo Medio", "Perfora la cáscara del fruto maduro. Controlar con bacillus thuringiensis.")
            )
        ),

        // 4. TOMATE CHONTO
        "4" to CropDetail(
            id = "4",
            name = "Tomate Chonto",
            scientificName = "Solanum lycopersicum",
            cycleDays = "105-115 d",
            waterRequirement = "Alta y Regular",
            phRange = "6.0 - 6.8",
            steps = listOf(
                StepItem(1, "Trasplante de Semillero", "Trasplantar cuando la plántula tenga 4 hojas verdaderas (aprox 20-25 días)."),
                StepItem(2, "Instalación de Tutor", "Colocar varas o hilos para sostener el peso de los ramos florales."),
                StepItem(3, "Deshije o Poda", "Eliminar brotes axilares (mamones) para concentrar la energía en el fruto principal."),
                StepItem(4, "Riego Goteo", "Riego constante en la base sin mojar el follaje para prevenir hongos.")
            ),
            calendar = listOf(
                CalendarItem("Establecimiento", "Día 1 - 15", "Enraizamiento post-trasplante en campo definitivo."),
                CalendarItem("Floración y Cuajado", "Día 35 - 50", "Aparición de los primeros racimos florales."),
                CalendarItem("Engorde de Fruto", "Día 60 - 90", "Crecimiento del tomate de verde a maduración fisiológica."),
                CalendarItem("Cosecha Escalonada", "Día 105 - 120", "Recolección manual semanal según viraje de color.")
            ),
            requirements = listOf(
                RequirementItem("Calcio Filiar", "Prevención de Pudrición Apical", "Demanda alta de Calcio para evitar manchas negras en el culo del tomate."),
                RequirementItem("Riego Uniforme", "Evitar rajado de fruto", "Riegos irregulares provocan que la piel del fruto se rompa.")
            ),
            pests = listOf(
                PestItem("Tuta Absoluta", "Tuta absoluta", "Riesgo Muy Alto", "Minador de hojas y perforador de frutos. Usar trampas de feromonas."),
                PestItem("Gota o Tizón Tardío", "Phytophthora infestans", "Hongo Crítico", "Manchas acuosas en hojas que queman la planta en pocos días.")
            )
        ),

        // 5. ASOCIACIÓN POLICULTIVO MILPA (MILPA TRADICIONAL)
        "milpa" to CropDetail(
            id = "milpa",
            name = "Plan Policultivo: La Milpa",
            scientificName = "Sinergia: Maíz + Frijol + Calabaza",
            cycleDays = "120 d (Completo)",
            waterRequirement = "Optimizado",
            phRange = "6.0 - 7.0",
            steps = listOf(
                StepItem(1, "Siembra del Maíz (Tutor)", "Sembrar el maíz primero al día 0 para que gane altura y fuerza."),
                StepItem(2, "Siembra del Frijol (Fijador)", "Sembrar el frijol al pie del maíz a los 15-20 días para que trepe por su tallo."),
                StepItem(3, "Siembra de Calabaza (Cobertura)", "Sembrar la calabaza entre los surcos a los 25 días para tapar el suelo."),
                StepItem(4, "Manejo del Gremio", "No requiere deshierbe agresivo; la calabaza ahoga las malezas espontáneas.")
            ),
            calendar = listOf(
                CalendarItem("Fase 1: Tutorado", "Día 1 - 20", "Crecimiento exclusivo del maíz para soporte."),
                CalendarItem("Fase 2: Trepado y Fijación", "Día 20 - 60", "El frijol se enreda en el maíz y aporta Nitrógeno."),
                CalendarItem("Fase 3: Sombra Protectora", "Día 30 - 90", "La calabaza cubre la totalidad del suelo conservando humedad."),
                CalendarItem("Fase 4: Cosecha Múltiple", "Día 90 - 120", "Cosecha progresiva de calabaza, frijol y finalmente maíz.")
            ),
            requirements = listOf(
                RequirementItem("Sinergia Agroecológica", "Eficiencia Land Equivalent Ratio (LER > 1.4)", "Produce 40% más alimento en el mismo espacio que los monocultivos."),
                RequirementItem("Equilibrio Nutricional", "Autosuficiencia de Nitrógeno", "El frijol alimenta al maíz y la calabaza protege el ecosistema del suelo.")
            ),
            pests = listOf(
                PestItem("Control Biológico Natural", "Biodiversidad de Insectos", "Baja Incidencia", "La diversidad de plantas confunde a las plagas y atrae polinizadores y depredadores naturales.")
            )
        )
    )

    fun getCropDetail(id: String): CropDetail {
        return cropDetailsMap[id] ?: cropDetailsMap["1"]!!
    }
}