package com.example.plantquiz.Model

import org.json.JSONArray
import org.json.JSONObject

class ParsePlantUtility {
    fun parsePlantObjectFromJSONData():List<Plant>?{
        var allPlantObjects : ArrayList<Plant> = ArrayList()
        var downloadingObject = DownloadingObject()
        var topLevelPlantJSONData  = downloadingObject.downloadJSONDataFromLink("https://plantplaces.com/perl/mobile/flashcard.pl")
        var topLevelJSONObject : JSONObject = JSONObject(topLevelPlantJSONData)
        var plantObjectArray : JSONArray = topLevelJSONObject.getJSONArray("values")

        var index = 0
        while(index < plantObjectArray.length()){

            var plantObject = Plant()
            var jsonObject = plantObjectArray.getJSONObject(index)

            with(jsonObject){
                plantObject.common = getString("common")
                plantObject.cultivar = getString("cultivar")
                plantObject.description =getString("description")
                plantObject.genus = getString("genus")
                plantObject.pictureName = getString("picture_name")
                plantObject.species = getString("species")
                plantObject.difficulty = getInt("difficulty")
                plantObject.id = getInt("id")
            }
            allPlantObjects.add(plantObject)


            index++
        }

        return allPlantObjects

    }

}