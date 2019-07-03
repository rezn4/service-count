package android.rezndm.servicecount

class MainPresenterImpl(private val mainView: MainView,
                        private val repo: Repository): MainPresenter {

    override fun updateCountData(count: Int) {
        mainView.showCount(count)
    }

    override fun loadCountData() {
        val count = repo.getCount()
        mainView.showCount(count)
    }

    override fun loadDateData() {
        val date = repo.getDate()
        mainView.showDate(date)
    }
}