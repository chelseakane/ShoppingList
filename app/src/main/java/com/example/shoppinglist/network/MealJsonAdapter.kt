package com.example.shoppinglist.network

import com.example.shoppinglist.models.Meal
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class MealJsonAdapter: JsonAdapter<Meal>() {
    private val options: JsonReader.Options = JsonReader.Options.of(
        "idMeal",
        "strMeal",
        "strCategory",
        "strInstructions",
        "strIngredient1",
        "strIngredient2",
        "strIngredient3",
        "strIngredient4",
        "strIngredient5",
        "strIngredient6",
        "strIngredient7",
        "strIngredient8",
        "strIngredient9",
        "strIngredient10",
        "strIngredient11",
        "strIngredient12",
        "strIngredient13",
        "strIngredient14",
        "strIngredient15",
        "strIngredient16",
        "strIngredient17",
        "strIngredient18",
        "strIngredient19",
        "strIngredient20"
    )

    override fun fromJson(reader: JsonReader): Meal {
        var id: String? = null
        var name: String? = null
        var category: String? = null
        var instructions: String? = null
        val ingredients = mutableListOf<String>()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> id = reader.nextString()
                1 -> name = reader.nextString()
                2 -> category = reader.nextString()
                3 -> instructions = reader.nextString()
                in 4..23 -> {
                    val ingredient = reader.nextString()
                    if (ingredient.isNotEmpty()) {
                        ingredients.add(ingredient)
                    }
                }
                -1 -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return Meal(id ?: "", name ?: "", category ?: "", instructions ?: "", ingredients)
    }

    override fun toJson(writer: JsonWriter, value: Meal?) {
        writer.beginObject()
        writer.name("idMeal").value(value?.id)
        writer.name("strMeal").value(value?.name)
        writer.name("strCategory").value(value?.category)
        writer.name("strInstructions").value(value?.instructions)
        value?.let {
            for (i in value.ingredients.indices) {
                writer.name("strIngredient${i + 1}").value(value.ingredients[i])
            }
            writer.endObject()
        }
    }

}