package android.rezndm.servicecount

interface MainPresenter {
    fun loadCountData()
    fun loadDateData()
    fun updateCountData(count: Int)
}