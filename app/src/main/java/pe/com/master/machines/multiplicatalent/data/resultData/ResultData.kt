package pe.com.master.machines.multiplicatalent.data.resultData

sealed class ResultData<out T> {

    object First : ResultData<Nothing>()

    object Loading : ResultData<Nothing>()

    object Empty : ResultData<Nothing>()

    data class Success<out R>(val data: R) : ResultData<R>()

    data class Error(val throwable: Throwable) : ResultData<Nothing>()

}
