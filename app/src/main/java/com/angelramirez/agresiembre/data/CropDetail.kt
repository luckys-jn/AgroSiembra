package com.angelramirez.agresiembre.data

data class StepItem(val stepNumber: Int, val title: String, val description: String)
data class CalendarItem(val stage: String, val days: String, val description: String)
data class RequirementItem(val title: String, val subtitle: String, val description: String)
data class PestItem(val name: String, val scientificName: String, val riskLevel: String, val description: String)

data class CropDetail(
    val id: String,
    val name: String,
    val scientificName: String,
    val cycleDays: String,
    val waterRequirement: String,
    val phRange: String,
    val steps: List<StepItem>,
    val calendar: List<CalendarItem>,
    val requirements: List<RequirementItem>,
    val pests: List<PestItem>
)