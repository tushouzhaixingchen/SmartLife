package com.smartlife.library_common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartlife.library_common.core.UiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    // You can add common functionality here
    /*
    * 下划线前缀是Kotlin的命名约定，表示该属性或方法是私有的，只能在当前类中访问
    * _uiState是一个可变的StateFlow，用于存储UI状态
    * Any是Kotlin的根类型，表示所有类都继承自Any
    * ?表示空合并运算符，如果左边的值为null，则返回右边的值,即可空
    * uiState没有下划线，表示它是公共的，可以在其他类中访问
    * val代表这个属性是只读的，不能在其他类中修改，Value的缩写，表示值
    * var代表这个属性是可变的，可以在其他类中修改，Variable的缩写，表示变量
    * Kotlin底层的底层代码中StateFlow的value是val（只读的），但我们可以通过asStateFlow()方法将其转换为StateFlow（可读的）
    * MutableStateFlow的value是var（可变的），表示我们可以修改它的值
    * Kotlin的变量声明写法：val/var 变量名：类型=初始值
    * */
    private val _uiState = MutableStateFlow<UiState<Any?>>(UiState.Empty)
    val uiState: StateFlow<UiState<Any?>> = _uiState.asStateFlow()

    /*全局异常处理器，防止协程崩溃导致App闪退
    * _, throwable第一个参数是CoroutineContext，这里用不到，用_忽略，第二个是抛出异常
    * */

    private val exceptionHandler = CoroutineExceptionHandler{
        _, throwable->_uiState.value= UiState.Error(throwable.message ?:"未知错误")
    }
    /*
    * 统一的协程启动器
    * 所有子ViewModle都通过这个方法来启动协程
    * 注意：参数名不能叫 onError，否则会遮蔽下方成员函数 onError(String)，导致子类 lambda 里无法调用它
    * */
    protected fun launch(errorAction: (Throwable) -> Unit= { _uiState.value= UiState.Error(it.message ?:"未知错误") }, block: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            try {
                block()
            } catch (e: Exception) {
                errorAction(e)
            }
        }
    }

    /*更新UI状态成功*/
    protected fun <T> emitSuccess(data: T){
        _uiState.value=UiState.Success(data)
    }

    /*更新Ui状态为加载中*/
    protected fun onLoading(){
        _uiState.value= UiState.Loading
    }

    /*更新UI状态为错误*/
    protected fun onError(message: String){
        _uiState.value= UiState.Error(message)
    }

    // 👇 2. 新增一个重置状态的方法，供子类调用
    protected fun onResetState() {
        _uiState.value = UiState.Empty
    }
}