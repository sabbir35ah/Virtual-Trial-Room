package com.example.data

import com.example.R
import com.example.model.Garment
import com.example.model.GarmentCategory
import com.example.model.GarmentDimensions
import com.example.model.SizeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object CatalogRepository {

    private val defaultGarments = listOf(
        Garment(
            id = "g_tee_sage",
            title = "Codixel Washed Sage Cotton Tee",
            titleBn = "কোডিক্সেল ওয়াশড সেজ কটন টি-শার্ট",
            category = GarmentCategory.T_SHIRT,
            brand = "Codixel Originals",
            priceBdt = 650,
            fabric = "100% Combed Cotton (180 GSM)",
            fabricBn = "১০০% মার্সারাইজড কটন (১৮০ জিএসএম)",
            localDrawableRes = R.drawable.img_garment_tee,
            sizeChart = mapOf(
                SizeOption.S to GarmentDimensions(chestInches = 36.5f, lengthInches = 26.5f, shoulderInches = 16.5f, waistInches = 35.0f),
                SizeOption.M to GarmentDimensions(chestInches = 38.5f, lengthInches = 27.5f, shoulderInches = 17.5f, waistInches = 37.0f),
                SizeOption.L to GarmentDimensions(chestInches = 41.0f, lengthInches = 28.5f, shoulderInches = 18.5f, waistInches = 39.5f),
                SizeOption.XL to GarmentDimensions(chestInches = 43.5f, lengthInches = 29.5f, shoulderInches = 19.5f, waistInches = 42.0f),
                SizeOption.XXL to GarmentDimensions(chestInches = 46.0f, lengthInches = 30.5f, shoulderInches = 20.5f, waistInches = 44.5f)
            ),
            sellerPageName = "Urban Dhaka Threads",
            isFCommercePilotItem = true
        ),
        Garment(
            id = "g_panjabi_navy",
            title = "South Asian Embroidered Cotton Panjabi",
            titleBn = "সাউথ এশিয়ান এমব্রয়ডারি কটন পাঞ্জাবি",
            category = GarmentCategory.PANJABI,
            brand = "TraditionCraft BD",
            priceBdt = 1850,
            fabric = "Pure Handloom Slub Cotton",
            fabricBn = "খাঁটি তাঁতের স্ল্যাব কটন",
            localDrawableRes = R.drawable.img_garment_panjabi,
            sizeChart = mapOf(
                SizeOption.S to GarmentDimensions(chestInches = 39.0f, lengthInches = 38.0f, shoulderInches = 17.0f, waistInches = 38.0f),
                SizeOption.M to GarmentDimensions(chestInches = 41.0f, lengthInches = 40.0f, shoulderInches = 18.0f, waistInches = 40.0f),
                SizeOption.L to GarmentDimensions(chestInches = 43.0f, lengthInches = 42.0f, shoulderInches = 19.0f, waistInches = 42.0f),
                SizeOption.XL to GarmentDimensions(chestInches = 45.0f, lengthInches = 44.0f, shoulderInches = 20.0f, waistInches = 44.0f),
                SizeOption.XXL to GarmentDimensions(chestInches = 47.0f, lengthInches = 45.0f, shoulderInches = 21.0f, waistInches = 46.0f)
            ),
            sellerPageName = "Zahra Boutique (F-Commerce)",
            isFCommercePilotItem = true
        ),
        Garment(
            id = "g_shirt_oxford",
            title = "Slim-Fit Oxford Button Down Shirt",
            titleBn = "স্লিম-ফিট অক্সফোর্ড বাটন ডাউন শার্ট",
            category = GarmentCategory.SHIRT,
            brand = "Dapper Dhaka",
            priceBdt = 1450,
            fabric = "Yarn-dyed Oxford Weave",
            fabricBn = "ইয়ার্ন-ডাইড প্রিমিয়াম অক্সফোর্ড",
            localDrawableRes = null,
            sizeChart = mapOf(
                SizeOption.S to GarmentDimensions(chestInches = 37.0f, lengthInches = 28.0f, shoulderInches = 16.5f, waistInches = 34.0f),
                SizeOption.M to GarmentDimensions(chestInches = 39.5f, lengthInches = 29.0f, shoulderInches = 17.5f, waistInches = 36.5f),
                SizeOption.L to GarmentDimensions(chestInches = 42.0f, lengthInches = 30.0f, shoulderInches = 18.5f, waistInches = 39.0f),
                SizeOption.XL to GarmentDimensions(chestInches = 44.5f, lengthInches = 31.0f, shoulderInches = 19.5f, waistInches = 41.5f),
                SizeOption.XXL to GarmentDimensions(chestInches = 47.0f, lengthInches = 31.5f, shoulderInches = 20.5f, waistInches = 44.0f)
            ),
            sellerPageName = "Gentlemen's Wardrobe BD",
            isFCommercePilotItem = true
        )
    )

    private val _garments = MutableStateFlow<List<Garment>>(defaultGarments)
    val garments: StateFlow<List<Garment>> = _garments.asStateFlow()

    fun addCustomGarment(
        title: String,
        category: GarmentCategory,
        brand: String,
        priceBdt: Int,
        fabric: String,
        customUri: String?,
        sizeChart: Map<SizeOption, GarmentDimensions>,
        sellerPageName: String
    ): Garment {
        val newGarment = Garment(
            id = "custom_" + UUID.randomUUID().toString().take(8),
            title = title,
            titleBn = title,
            category = category,
            brand = brand,
            priceBdt = priceBdt,
            fabric = fabric,
            fabricBn = fabric,
            localDrawableRes = null,
            customImageUri = customUri,
            sizeChart = sizeChart,
            sellerPageName = sellerPageName,
            isFCommercePilotItem = true
        )
        _garments.value = listOf(newGarment) + _garments.value
        return newGarment
    }

    fun getGarmentById(id: String): Garment? {
        return _garments.value.find { it.id == id }
    }
}
