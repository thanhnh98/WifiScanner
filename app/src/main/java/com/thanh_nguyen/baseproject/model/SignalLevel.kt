package com.thanh_nguyen.baseproject.model

sealed class SignalLevel(val level: Int){
    class BestSignal(level: Int): SignalLevel(level)
    class GoodSignal(level: Int): SignalLevel(level)
    class LowSignal(level: Int): SignalLevel(level)
    class WeakSignal(level: Int): SignalLevel(level)
    class TooLowSignal(level: Int): SignalLevel(level)
}
