package be.hogent.jochensnextdinner.utils

/**
 * Sealed class representing a resource with a status.
 * It can be either Success, Error, or Loading.
 *
 * @param T The type of the data.
 * @property data The data of the resource. It can be null.
 * @property message The message of the resource. It can be null.
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    /**
     * Class representing a successful resource.
     *
     * @param T The type of the data.
     * @property data The data of the resource. It cannot be null.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Class representing an erroneous resource.
     *
     * @param T The type of the data.
     * @property message The error message of the resource. It cannot be null.
     * @property data The data of the resource. It can be null.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Class representing a loading resource.
     *
     * @param T The type of the data.
     */
    class Loading<T> : Resource<T>(null)
}