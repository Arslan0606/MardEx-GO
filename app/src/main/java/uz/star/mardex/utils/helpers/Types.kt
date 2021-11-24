package uz.star.mardex.utils.helpers

typealias SingleBlock <T> = (T) -> Unit
typealias DoubleBlock <T, E> = (T, E) -> Unit
typealias TrialBlock <T, E, F> = (T, E, F) -> Unit
typealias EmptyBlock = () -> Unit
